package staniszewska.licencjat_backend.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminUpdateDTO {
    private String status;
    private String note;
}
