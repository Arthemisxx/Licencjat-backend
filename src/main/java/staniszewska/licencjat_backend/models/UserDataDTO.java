package staniszewska.licencjat_backend.models;

import com.nimbusds.jose.shaded.gson.internal.bind.JsonTreeReader;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDataDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
