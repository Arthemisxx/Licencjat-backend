package staniszewska.licencjat_backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserReportDetailsDTO {
    private Long id;
    private String description;
    private String categoryName;
    private String status;
    private LocalDateTime createdAt;
}
