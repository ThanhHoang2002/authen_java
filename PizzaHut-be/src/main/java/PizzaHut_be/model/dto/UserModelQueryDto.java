package PizzaHut_be.model.dto;

import PizzaHut_be.model.enums.AccountStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModelQueryDto {
    private String id;
    private AccountStatusEnum status;
}
