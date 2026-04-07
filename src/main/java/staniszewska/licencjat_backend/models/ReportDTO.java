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

    //Category
    private Long categoryId;

    private String categoryName;

    private String categoryIconKey;

    private String categoryColorHex;
    //

    private Double latitude;

    private Double longitude;

}
