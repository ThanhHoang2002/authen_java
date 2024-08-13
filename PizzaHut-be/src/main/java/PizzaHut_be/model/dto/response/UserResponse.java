package PizzaHut_be.model.dto.response;

import PizzaHut_be.model.dto.ResponseDto;
import PizzaHut_be.model.entity.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse<T> {
    private Client userModel;
    private ResponseEntity<ResponseDto<T>> response;
    private boolean isNewUser;
}
