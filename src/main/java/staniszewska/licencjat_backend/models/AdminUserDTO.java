package staniszewska.licencjat_backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long reportsCount;
}
