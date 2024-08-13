package PizzaHut_be.model.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterResponse {
    private String firstName;

    private String lastName;

    private String password;

    private String username;

    private String email;

    private LocalDate birthday;

    private String address;

    private String locate;

    private String gender;

    private String phone;

    private String description;

    private String avatar;
}
