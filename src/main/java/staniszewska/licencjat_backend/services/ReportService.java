package staniszewska.licencjat_backend.services;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import staniszewska.licencjat_backend.entities.CategoryEntity;
import staniszewska.licencjat_backend.entities.ImageEntity;
import staniszewska.licencjat_backend.entities.ReportEntity;
import staniszewska.licencjat_backend.entities.UserEntity;
import staniszewska.licencjat_backend.models.*;
import staniszewska.licencjat_backend.repositories.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<AdminUserReportDetailsDTO> getUserReportsMini(Long userId) {
        return reportRepository.getUserReportsMini(userId);
    }

    public List<ReportDTO> getFilteredReports(List<Long> categoryIds) {
        List<ReportDTO> reports = reportRepository.findAllByCategoryIdIn(categoryIds);
        if (reports.isEmpty()) {
            return new ArrayList<>();
        }
        return reports;
    }

    @Transactional
    public boolean deleteReport(Long id) {
        if (!reportRepository.existsById(id)) {
            return false;
        }

        List<ImageEntity> images = imageRepository.findAllByReportId(id);
        for (ImageEntity image : images) {
            supabaseStorageService.deleteFileFromUrl(image.getUrl());
        }
        imageRepository.deleteAllByReportId(id);
        watchRepository.deleteAllByReportId(id);
        reportRepository.deleteById(id);

        return true;
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


    public AdminReportDetailsDTO getAdminReportById(Long id) {
        AdminReportDetailsDTO reportDetails = reportRepository.getAdminReportDetailsDTO(id);
        if (reportDetails == null) {
            return null;
        }

        List<String> imageUrls = imageRepository.findImageUrlsByReportId(id);
        reportDetails.setImageUrls(imageUrls);

        Integer count = watchRepository.countWatchersByReportId(id);
        reportDetails.setWatchedBy(count);

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

    public Page<AdminReportDTO> searchAdminReports(String search, Pageable pageable) {



        String safeSearch = (search != null && search.trim().isEmpty()) ? null : search;
        return reportRepository.findReportsForAdminPanel(safeSearch, pageable);

    }

    public List<ReportDetailsDTO> getReportsWatchedByUserId(Long id) {
        List<ReportDetailsDTO> reports = reportRepository.getReportDetailsDTO(id);

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

    public boolean updateReportStatusAndNote(Long id, String newStatus, String newNote) {
        Optional<ReportEntity> reportOptional = reportRepository.findById(id);

        if (reportOptional.isPresent()) {
            ReportEntity report = reportOptional.get();

            if(newStatus != null){
                report.setStatus(newStatus);
                report.setUpdatedAt(LocalDateTime.now());
            }

            if(newNote != null){
                report.setAdminNote(newNote);
            }

            reportRepository.save(report);
            return true;
        }

        return false;
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
