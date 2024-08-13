package PizzaHut_be.model.enums;

public enum StatusCodeEnum {
    //OTP
    OTP1001("OTP1001"), // Sent OTP successfully by email
    OTP0001("OTP0001"), // Sent OTP failed by email
    //LOGIN SUCCESS
    LOGIN1201("LOGIN1201"), // Login Google Sso successfully with create new user
    LOGIN1206("LOGIN1206"), // Login Google successfully with user exist
    LOGIN1204("LOGIN1204"), // Login successfully by verify password
    LOGIN0209("LOGIN0209"), // Wrong password or otp when login
    LOGIN0404("LOGIN0404"), // User not exists
    //TOKEN
    TOKEN1000("TOKEN1000"), // Get refresh token success
    TOKEN0000("TOKEN0000"), // Get refresh token failed
    //LOGIN FAIL
    LOGIN0201("LOGIN0201"), // Login Google Sso failed by verify Google authorization code
    LOGIN0213("LOGIN0213"), // Login Google Sso failed when create new user by Google user
    LOGIN0228("LOGIN0228"); // Login Google Sso failed by update avatar user failed
    public final String value;

    StatusCodeEnum(String i) {
        value = i;
    }
}
