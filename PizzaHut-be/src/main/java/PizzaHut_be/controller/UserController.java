package PizzaHut_be.controller;

import PizzaHut_be.model.dto.ResponseDto;
import PizzaHut_be.model.dto.request.LoginOtpRequest;
import PizzaHut_be.model.dto.request.LoginRequest;
import PizzaHut_be.model.dto.request.LoginSocialRequest;
import PizzaHut_be.model.dto.request.RegisterRequest;
import PizzaHut_be.model.dto.response.LoginOtpResponse;
import PizzaHut_be.model.dto.response.LoginResponse;
import PizzaHut_be.model.dto.response.RegisterResponse;
import PizzaHut_be.service.GoogleService;
import PizzaHut_be.service.LanguageService;
import PizzaHut_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.CustomLog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CustomLog
@RestController
@RequestMapping("/client")
@AllArgsConstructor
public class UserController {

    private final GoogleService googleService;

    private final UserService userService;

    private final LanguageService languageService;

    @Operation(summary = "Login sso by google",
            description = "Api login by google account",
            tags = {"auth"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "LOGIN1201", description = "Login Google Sso successfully with create new user", content = {@Content(examples = @ExampleObject(value = """
                    {
                        "success": true,
                        "message": "Login Google Sso successfully with create new user.",
                        "data": {
                           "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDA3MDEyMzU4NCIsImlhdCI6MTcxNTMxNDIwNSwiZXhwIjoxNzE2NTIzODA1fQ.HemoUsztf_OGgM-KEeDIU1388MkX4n7S1hzpeY49eXCAwFL_8IF0KenQpk5mqsKsb0RZVDXb_cru3fuyuET0Xg",
                               "userInfo": {
                                 "id": "00070123584",
                                 "username": "SaThit34330",
                                 "firstName": "Sa",
                                 "lastName": "Thit",
                                 "avatar": "https://lh3.googleusercontent.com/a/ACg8ocIlFZzHGLmVu28xBLWDWvbSGDqidU2PqjGbEewL1AlK=s96-c",
                                 "banner": null,
                                 "dateOfBirth": null,
                                 "gender": "FEMALE",
                                 "publicId": null,
                                 "language": null,
                                 "email": [
                                   {
                                     "email": "sathit11042004@gmail.com",
                                     "primary": true
                                   }
                                 ],
                                 "phoneNumber": [
                                   {
                                     "phoneNumber": "8562092679246",
                                     "primary": true
                                   }
                                 ],
                                 "province": null,
                                 "country": null,
                                 "provinceName": null,
                                 "countryName": null,
                                 "hasPassword": false
                        },
                        },
                        "statusCode": "LOGIN1201"
                    }                        
                    """))}),
            @ApiResponse(responseCode = "LOGIN1206", description = "Login Google successfully with user exist", content = {@Content(examples = @ExampleObject(value = """
                    {
                        "success": true,
                        "message": "Login Google successfully with user exist",
                        "data": {
                          "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDA3MDEyMzU4NCIsImlhdCI6MTcxNTMxNDIwNSwiZXhwIjoxNzE2NTIzODA1fQ.HemoUsztf_OGgM-KEeDIU1388MkX4n7S1hzpeY49eXCAwFL_8IF0KenQpk5mqsKsb0RZVDXb_cru3fuyuET0Xg",
                               "userInfo": {
                                 "id": "00070123584",
                                 "username": "SaThit34330",
                                 "firstName": "Sa",
                                 "lastName": "Thit",
                                 "avatar": "https://lh3.googleusercontent.com/a/ACg8ocIlFZzHGLmVu28xBLWDWvbSGDqidU2PqjGbEewL1AlK=s96-c",
                                 "banner": null,
                                 "dateOfBirth": null,
                                 "gender": "FEMALE",
                                 "publicId": null,
                                 "language": null,
                                 "email": [
                                   {
                                     "email": "sathit11042004@gmail.com",
                                     "primary": true
                                   }
                                 ],
                                 "phoneNumber": [
                                   {
                                     "phoneNumber": "8562092679246",
                                     "primary": true
                                   }
                                 ],
                                 "province": null,
                                 "country": null,
                                 "provinceName": null,
                                 "countryName": null,
                                 "hasPassword": false
                        },
                        },
                        "statusCode": "LOGIN1206"                        
                    }
                    """))}),
            @ApiResponse(responseCode = "LOGIN0201", description = "Login Google Sso failed by verify Google authorization code", content = {@Content(examples = @ExampleObject(value = """
                    {
                        "success": false,
                        "message": "Login Google Sso failed by verify Google authorization code",
                        "data": null,
                        "statusCode": "LOGIN0201"                       
                    }
                    """))}),
            @ApiResponse(responseCode = "LOGIN0213", description = "Login Google Sso failed when create new user by google user", content = {@Content(examples = @ExampleObject(value = """
                    {
                        "success": false,
                        "message": "Login Google Sso failed when create new user by google user",
                        "data": null,
                        "statusCode": "LOGIN0213"                       
                    }
                    """))})
    })
    @PostMapping("/google")
    public ResponseEntity<ResponseDto<Object>> loginGoogleSSO(@RequestBody LoginSocialRequest loginSocialRequest) {
        return googleService.loginGoogleSSO(loginSocialRequest);
    }

    @PostMapping("/request-register")
    public ResponseEntity<ResponseDto<LoginOtpResponse>> requestLogin(@Valid @RequestBody LoginOtpRequest loginOtpRequest) {
        return userService.requestRegister(loginOtpRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<RegisterResponse>> login(@Valid @RequestBody RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }


}
