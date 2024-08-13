package PizzaHut_be.service;

import PizzaHut_be.dao.UserDao;
import PizzaHut_be.dao.repository.UserModelRepository;
import PizzaHut_be.model.builder.ResponseBuilder;
import PizzaHut_be.model.constant.TypeUploadConstant;
import PizzaHut_be.model.dto.GoogleUserDto;
import PizzaHut_be.model.dto.ResponseDto;
import PizzaHut_be.model.dto.request.LoginSocialRequest;
import PizzaHut_be.model.dto.response.LoginResponse;
import PizzaHut_be.model.dto.response.ResponseUser;
import PizzaHut_be.model.dto.response.UserResponse;
import PizzaHut_be.model.entity.Client;
import PizzaHut_be.model.enums.StatusCodeEnum;
import PizzaHut_be.model.mapper.CommonMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@CustomLog
@RequiredArgsConstructor
public class GoogleService {
    private final UserModelRepository userModelRepository;
    private final CommonMapper mapper;
    private final LanguageService languageService;
    private final FileService fileService;
    private final UserDao userDao;
    private final JwtService jwtService;

    @Value("${google.token.path}")
    private String googleTokenPath;

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.secret}")
    private String googleClientSecret;

    @Value("${google.redirect.uri}")
    private String googleRedirectUri;

    /***
     * Login sso with Google
     * @param loginSocialRequest
     * @return
     */
    public ResponseEntity<ResponseDto<Object>> loginGoogleSSO(LoginSocialRequest loginSocialRequest) {
        UserResponse userResponse = getGoogleUserInfoResponse(loginSocialRequest);
        Client userModel = userResponse.getUserModel();

        if (userModel == null) {
            return userResponse.getResponse();
        }
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(jwtService.generateJwtTokenByUserId(userResponse.getUserModel().getClientId()+""));
        loginResponse.setUserInfo(mapper.map(userResponse.getUserModel(), ResponseUser.class));
        if (userResponse.isNewUser()) {
            return ResponseBuilder.okResponse(
                    languageService.getMessage("login.social.google.success"),
                    loginResponse,
                    StatusCodeEnum.LOGIN1201);
        }

        return ResponseBuilder.okResponse(
                languageService.getMessage("login.social.google.success"),
                loginResponse,
                StatusCodeEnum.LOGIN1206);
    }

    public UserResponse getGoogleUserInfoResponse(LoginSocialRequest loginSocialRequest) {
        GoogleTokenResponse googleTokenResponse = null;

        //Verify Google authorization code
        try {
            googleTokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    new JacksonFactory(),
                    googleTokenPath,
                    googleClientId,
                    googleClientSecret,
                    loginSocialRequest.getCode(),
                    googleRedirectUri)
                    .execute();
        } catch (Exception e) {
            log.error("Verify authorization code of Google failed" + e.getMessage(), e);
        }

        if (googleTokenResponse != null) {
            String idTokenString = googleTokenResponse.getIdToken();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            // Verify the Google ID token
            GoogleIdToken idToken = null;
            try {
                idToken = verifier.verify(idTokenString);

                log.info("Google id token : {}", idToken);
            } catch (GeneralSecurityException | IOException e) {
                log.error("Verify id token of Google failed" + e.getMessage(), e);
            }

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String id = payload.getSubject();
                String email = payload.getEmail();

                Client userModel = new Client();
                userModel = userModelRepository.findById(id).orElse(null);

                if (userModel == null) {
                    GoogleUserDto googleUserDto = GoogleUserDto
                            .builder()
                            .id(id)
                            .email(email)
                            .name((String) payload.get("name"))
                            .familyName((String) payload.get("family_name"))
                            .givenName((String) payload.get("given_name"))
                            .picture((String) payload.get("picture"))
                            .locale((String) payload.get("locale"))
                            .address((String) payload.get("address"))
                            .gender((String) payload.get("gender"))
                            .birthday((String) payload.get("birthday"))
                            .phoneNumber((String) payload.get("phone_number"))
                            .build();

                    Client newUser = mapper.map(googleUserDto, Client.class);
                    try {
                        userModelRepository.save(newUser);
                    } catch (Exception e) {
                        log.error("Create user by google user failed " + e.getMessage());

                        return new UserResponse(null, ResponseBuilder.badRequestResponse(
                                languageService.getMessage("login.social.google.failed"),
                                StatusCodeEnum.LOGIN0213), false);
                    }

                    //upload avatar get from google image url to minio server
                    String avatarUploadUrl = fileService.uploadImageFileFromImageUrl(
                            newUser.getAvatar(),
                            newUser.getClientId()+"",
                            TypeUploadConstant.AVATAR
                    );

                    newUser.setAvatar(avatarUploadUrl);
                    try {
                        userDao.update(newUser);
                    } catch (Exception e) {
                        log.error("Update user avatar failed " + e.getMessage(), e);

                        return new UserResponse(null, ResponseBuilder.badRequestResponse(
                                languageService.getMessage("update.user.avatar.failed"),
                                StatusCodeEnum.LOGIN0228), false);
                    }

                    log.info("create Google user info : {}", newUser);

                    UserResponse userResponse = new UserResponse<>();
                    userResponse.setUserModel(newUser);
                    userResponse.setNewUser(true);
                    return userResponse;
                }
                UserResponse userResponse = new UserResponse<>();
                userResponse.setUserModel(userModel);

                return userResponse;
            }
        }
        return new UserResponse(null, ResponseBuilder.badRequestResponse(
                languageService.getMessage("login.social.google.failed"),
                StatusCodeEnum.LOGIN0201), false);
    }
}
