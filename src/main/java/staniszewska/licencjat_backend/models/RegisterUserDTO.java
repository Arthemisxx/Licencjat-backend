package staniszewska.licencjat_backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterUserDTO {
    private String email;

    private String password;

    private String firstName;

    private String lastName;

}
