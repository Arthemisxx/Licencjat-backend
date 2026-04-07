package staniszewska.licencjat_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import staniszewska.licencjat_backend.entities.CategoryEntity;
import staniszewska.licencjat_backend.entities.ImageEntity;
import staniszewska.licencjat_backend.entities.ReportEntity;
import staniszewska.licencjat_backend.entities.UserEntity;
import staniszewska.licencjat_backend.models.CreateReportDTO;
import staniszewska.licencjat_backend.models.ReportDTO;
import staniszewska.licencjat_backend.models.ReportDetailsDTO;
import staniszewska.licencjat_backend.repositories.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SupabaseStorageService supabaseStorageService;
    private final ImageRepository imageRepository;
    private final WatchRepository watchRepository;


    public List<ReportDTO> getAllReports() {
        List<ReportDTO> reports = reportRepository.findAllReportDTO();
        if (reports.isEmpty()) {
            return new ArrayList<>();
        }

        return reports;
    }

    public List<ReportDTO> getFilteredReports(List<Long> categoryIds) {
        List<ReportDTO> reports = reportRepository.findAllByCategoryIdIn(categoryIds);
        if (reports.isEmpty()) {
            return new ArrayList<>();
        }
        return reports;
    }

    public ReportDetailsDTO getReportById(Long id) {

        ReportDetailsDTO reportDetails = reportRepository.getReportDetailsDTOById(id);
        if (reportDetails == null) {
            return null;
        }

        List<String> imageUrls = imageRepository.findImageUrlsByReportId(id);
        reportDetails.setImageUrls(imageUrls);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            UserEntity currentUser = (UserEntity) authentication.getPrincipal();
            Boolean isWatched = watchRepository.isReportWatchedByUser(currentUser.getId(), id);
            reportDetails.setIsWatched(isWatched);
        } else {
            reportDetails.setIsWatched(false);
        }

        return reportDetails;

    }

    public List<ReportDetailsDTO> getReportsByUserId(Long id) {
        List<ReportDetailsDTO> reports = reportRepository.getReportDetailsDTOByUserId(id);

        reports.forEach(r -> {
            List<String> imageUrls = imageRepository.findImageUrlsByReportId(r.getId());
            r.setImageUrls(imageUrls);

            Boolean isWatched = watchRepository.isReportWatchedByUser(id, r.getId());
            r.setIsWatched(isWatched);
        });

        return reports;
    }

    public List<ReportDetailsDTO> getReportsWatchedByUserId(Long id) {
        List<ReportDetailsDTO> reports = reportRepository.getReportDetailsDTOWatchedByUser(id);

        reports.forEach(r -> {
            List<String> imageUrls = imageRepository.findImageUrlsByReportId(r.getId());
            r.setImageUrls(imageUrls);

            Boolean isWatched = watchRepository.isReportWatchedByUser(id, r.getId());
            r.setIsWatched(isWatched);
        });

        return reports;
    }

    public Long createReport(CreateReportDTO report) {
        UserEntity user = null;
        if (report.getAuthorId() != null) {
            user = userRepository.findById(report.getAuthorId()).orElse(null);
        }

        CategoryEntity category = categoryRepository.findById(report.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("Błędna kategoria!"));

        ReportEntity newReport = ReportEntity.builder()
                .author(user)
                .guestEmail(report.getGuestEmail())
                .category(category)
                .description(report.getDescription())
                .latitude(report.getLatitude())
                .longitude(report.getLongitude())
                .address(report.getAddress())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status("NOWE")
                .build();

        return reportRepository.save(newReport).getId();
    }

    public void saveImagesForReport(Long reportId, List<MultipartFile> images) {
        ReportEntity report = reportRepository.getById(reportId);

        for (MultipartFile file : images) {
            if (file.isEmpty()) continue;

            try {
                String publicUrl = supabaseStorageService.uploadFile(file);

                ImageEntity imageEntity = new ImageEntity();
                imageEntity.setReport(report);
                imageEntity.setUrl(publicUrl);
                imageEntity.setUploadedAt(LocalDateTime.now());

                imageRepository.save(imageEntity);

            } catch (Exception e) {
                throw new RuntimeException("Nie udało się zapisać zdjęcia do raportu", e);
            }
        }
    }
}
