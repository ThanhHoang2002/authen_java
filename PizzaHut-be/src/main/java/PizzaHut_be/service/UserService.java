package PizzaHut_be.service;

import PizzaHut_be.config.security.UserDetailImpl;
import PizzaHut_be.dao.UserDao;
import PizzaHut_be.dao.repository.UserModelRepository;
import PizzaHut_be.model.builder.ResponseBuilder;
import PizzaHut_be.model.dto.ResponseDto;
import PizzaHut_be.model.dto.request.LoginOtpRequest;
import PizzaHut_be.model.dto.request.LoginRequest;
import PizzaHut_be.model.dto.request.RegisterRequest;
import PizzaHut_be.model.dto.response.*;
import PizzaHut_be.model.entity.Client;
import PizzaHut_be.model.enums.OtpDestinationEnum;
import PizzaHut_be.model.enums.OtpRequestTypeEnum;
import PizzaHut_be.model.enums.StatusCodeEnum;
import PizzaHut_be.model.mapper.CommonMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final OtpService otpService;
    private final LanguageService languageService;
    private final UserModelRepository repository;
    private final UserDao userDao;
    private final JwtService jwtService;
    private final CommonMapper mapper;
    private final UserModelRepository userModelRepository;

    public Client getUserInfoFromContext() {
        try {
            UserDetailImpl userDetails = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            log.info("User details by authentication: " + userDetails);

            return userDetails.getUserModel();
        } catch (Exception e) {
            log.error("Get user from context failed by: ", e.getMessage());

            return null;
        }
    }


    /***
     * Check if username is valid, and send OTP
     * @param loginOtpRequest
     * @return
     */
    public ResponseEntity<ResponseDto<LoginOtpResponse>> requestRegister(LoginOtpRequest loginOtpRequest) {

        String email = loginOtpRequest.getEmail();
        Client userModel = userDao.findOneUserModel(email);
        LoginOtpResponse loginOtpResponse = new LoginOtpResponse();
        loginOtpResponse.setHasSentOtp(true);

        if (userModel != null) {
            return ResponseBuilder.badRequestResponse("Account has been exits", StatusCodeEnum.LOGIN0201);
        } else {
            return getCheckResponseByEmail(email, loginOtpResponse);
        }

    }


    private ResponseEntity<ResponseDto<LoginOtpResponse>> getCheckResponseByEmail(String username,
                                                                                  LoginOtpResponse loginOtpResponse) {

        boolean isSentOtp = otpService.sendOtp(
                null,
                null,
                LocaleContextHolder.getLocale(),
                username,
                OtpDestinationEnum.EMAIL,
                OtpRequestTypeEnum.LOGIN
        );

        if (isSentOtp) {
            return ResponseBuilder.okResponse(
                    languageService.getMessage("otp.sent.successfully"),
                    loginOtpResponse,
                    StatusCodeEnum.OTP1001);
        }

        return ResponseBuilder.badRequestResponse(
                languageService.getMessage("otp.sent.failed"),
                StatusCodeEnum.OTP0001);
    }

    /***
     * Login by username/email/password
     * @param loginRequest
     * @return
     */
    public ResponseEntity<ResponseDto<LoginResponse>> login(LoginRequest loginRequest) {
        UserResponse userResponse = getLoginUserResponse(loginRequest);

        if (userResponse.getUserModel() == null) {
            return userResponse.getResponse();
        } else {
            Client userModel = userResponse.getUserModel();
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setAccessToken(jwtService.generateJwtTokenByUserId(userModel.getClientId()+""));
            loginResponse.setUserInfo(mapper.map(userModel, ResponseUser.class));

            return ResponseBuilder.okResponse(
                    languageService.getMessage("login.main.success"),
                    loginResponse,
                    StatusCodeEnum.LOGIN1204);
        }
    }

    public UserResponse getLoginUserResponse(LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String passwordOrOtp = loginRequest.getPassword();
        Client userModel = userModelRepository.findByEmail(username).orElse(null);
        if (userModel != null && userModel.getPassword().equals(passwordOrOtp)) {
            UserResponse otpLoginResponse = new UserResponse<>();
            otpLoginResponse.setUserModel(userModel);
            otpLoginResponse.setNewUser(false);
            if (otpLoginResponse != null) {
                return otpLoginResponse;
            }
        }
        return new UserResponse(null, ResponseBuilder.badRequestResponse(
                "Login failed",
                StatusCodeEnum.LOGIN0209), false);
    }

//    public UserResponse getRegisterUserResponse(LoginRequest loginRequest) {
//        String username = loginRequest.getUsername();
//        String passwordOrOtp = loginRequest.getPassword();
//        log.info(loginRequest.getUsername());
//        Client userModel = userDao.findOneUserModel(username);
//        log.info(userModel.getEmail(), userModel);
//
//        if (passwordOrOtp != null) {
//            UserResponse otpLoginResponse = handleOtpLogin(userModel, username, passwordOrOtp);
//            if (otpLoginResponse != null) {
//                return otpLoginResponse;
//            }
//        }
//        return new UserResponse(null, ResponseBuilder.badRequestResponse(
//                languageService.getMessage("login.password.or.otp.invalid"),
//                StatusCodeEnum.LOGIN0209), false);
//    }

    private UserResponse handleOtpLogin(Client userModel, String username, String passwordOrOtp) {
        if (otpService.verifyOtp(username, OtpRequestTypeEnum.LOGIN, passwordOrOtp)) {
            UserResponse userResponse = new UserResponse();
            userResponse.setUserModel(userModel);
            return userResponse;
        }
        return null;
    }

    public ResponseEntity<ResponseDto<RegisterResponse>> register(RegisterRequest registerRequest) {
        String email= registerRequest.getEmail();
        Client userModel = userModelRepository.findByEmail(email).orElse(null);
        String passwordOrOtp = registerRequest.getPasswordOrOtp();


        if (userModel != null) {
            return ResponseBuilder.badRequestResponse("Username is present!",
                    StatusCodeEnum.LOGIN0201);
        } else {

            if (otpService.verifyOtp(registerRequest.getEmail(), OtpRequestTypeEnum.LOGIN, passwordOrOtp)) {
                Client newUserModel = mapper.map(registerRequest, Client.class);
                newUserModel.setPoint((long) 0);
                userModelRepository.save(newUserModel);
                RegisterResponse registerResponse = new RegisterResponse();
                registerResponse = mapper.map(registerRequest, RegisterResponse.class);

                return ResponseBuilder.okResponse(
                        "Register successfully",
                        registerResponse,
                        StatusCodeEnum.LOGIN1204);
            }else{
                log.info("otp in put:{}", passwordOrOtp);

                return ResponseBuilder.badRequestResponse("Verify OTP fail", StatusCodeEnum.OTP0001);
            }

        }

    }
}
