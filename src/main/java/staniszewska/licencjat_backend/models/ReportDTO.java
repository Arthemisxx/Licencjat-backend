package staniszewska.licencjat_backend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import staniszewska.licencjat_backend.entities.CategoryEntity;
import staniszewska.licencjat_backend.entities.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//@AllArgsConstructor
//class DTO{
//    private Long id;
//    private Long categoryId;
//}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@IdClass(DTO.class)
public class ReportDTO {
//    @Id
    private Long id;

    private Long authorId;

    private String guestEmail;

    //Category
//    @Id
    private Long categoryId;

    private String categoryName;

    private String categoryIconKey;

    private String categoryColorHex;
    //

    private String description;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String address;

    private String status;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime updatedAt;
}
