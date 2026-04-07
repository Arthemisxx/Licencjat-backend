package staniszewska.licencjat_backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import staniszewska.licencjat_backend.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDetailsDTO {
    private Long id;

    private String description;

    private Double latitude;

    private Double longitude;

    private String address;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    //Category
    private Long categoryId;

    private String categoryName;

    private String categoryIconKey;

    private String categoryColorHex;

    private List<String> imageUrls = new ArrayList<>();

    private Boolean isWatched;


    public ReportDetailsDTO(Long id, String description, Double latitude, Double longitude, String address, String status, LocalDateTime createdAt, LocalDateTime updatedAt, Long categoryId, String categoryName, String categoryIconKey, String categoryColorHex) {
        this.id = id;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryIconKey = categoryIconKey;
        this.categoryColorHex = categoryColorHex;
    }

}
