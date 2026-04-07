package staniszewska.licencjat_backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReportDTO {
    private Long authorId;
    private Long categoryId;
    private String description;
    private Double latitude;
    private Double longitude;
    private String address;
    private String guestEmail;
}
