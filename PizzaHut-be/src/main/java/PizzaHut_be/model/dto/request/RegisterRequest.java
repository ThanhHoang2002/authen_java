package PizzaHut_be.model.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class RegisterRequest {
    private String name;

    private String gender;

    private String password;

    private Date birthday;

    private String phone;

    private String rePassword;

    private String email;

    private String passwordOrOtp;
}
