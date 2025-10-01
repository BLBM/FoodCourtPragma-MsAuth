package co.com.foodcourt.model.user;
import co.com.foodcourt.model.rol.Rol;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private Long userId;
    private String firstName;
    private String lastName;
    private Long documentId;
    private String phone;
    private LocalDate birthDate;
    private String email;
    private String password;
    private Rol role;

}
