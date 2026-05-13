package staniszewska.licencjat_backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminReportDetailsDTO {
    private Long id;

    private String description;

    private Double latitude;

    private Double longitude;

    private String address;

    private Long categoryId;

    private String categoryName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String status;

    private Long authorId;

    private String authorName;

    private String authorEmail;

    private String adminNote;

    private List<String> imageUrls = new ArrayList<>();

    private Integer watchedBy;

    public AdminReportDetailsDTO(String adminNote, Long id, String description, Double latitude, Double longitude, String address, Long categoryId, String categoryName, LocalDateTime createdAt, LocalDateTime updatedAt, String status, Long authorId, String authorName, String authorEmail) {
        this.id = id;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.adminNote = adminNote;
    }
}
