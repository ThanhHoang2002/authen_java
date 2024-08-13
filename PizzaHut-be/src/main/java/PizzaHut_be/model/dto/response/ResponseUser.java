package PizzaHut_be.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Id;
import PizzaHut_be.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUser {
    @Id
    private long clientId;

    private String name;

    private String email;

    private Date birthday;

    private String gender;

    private String phone;

    private String avatar;

    private long point;
}
