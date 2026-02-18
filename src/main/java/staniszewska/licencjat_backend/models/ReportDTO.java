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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private Long id;

    private Long authorId;

    private String guestEmail;

    private Long categoryId;

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
