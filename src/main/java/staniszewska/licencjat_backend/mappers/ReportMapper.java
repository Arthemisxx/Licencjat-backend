package staniszewska.licencjat_backend.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import staniszewska.licencjat_backend.entities.ReportEntity;
import staniszewska.licencjat_backend.models.ReportDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReportMapper {
    public ReportDTO toDTO(ReportEntity report) {
        Long id = report.getId();
        Long authorId;
        if(report.getAuthor().getId() != null){
           authorId = report.getAuthor().getId();
        }else {
            authorId = null;
        }
        String guestEmail = report.getGuestEmail();
        Long categoryId = report.getCategory().getId();
        String description = report.getDescription();
        BigDecimal latitude = report.getLatitude();
        BigDecimal longitude = report.getLongitude();
        String address = report.getAddress();
        String status = report.getStatus();
        LocalDateTime createdAt = report.getCreatedAt();
        LocalDateTime updatedAt = report.getUpdatedAt();

        return new ReportDTO(id, authorId, guestEmail, categoryId, description, latitude, longitude, address, status, createdAt, updatedAt);
    }
}
