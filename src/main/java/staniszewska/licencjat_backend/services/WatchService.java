package staniszewska.licencjat_backend.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import staniszewska.licencjat_backend.controllers.ReportController;
import staniszewska.licencjat_backend.entities.ReportEntity;
import staniszewska.licencjat_backend.entities.UserEntity;
import staniszewska.licencjat_backend.entities.WatchEntity;
import staniszewska.licencjat_backend.entities.WatchId;
import staniszewska.licencjat_backend.repositories.ReportRepository;
import staniszewska.licencjat_backend.repositories.UserRepository;
import staniszewska.licencjat_backend.repositories.WatchRepository;

import java.beans.Transient;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WatchService {
    private final WatchRepository watchRepository;
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public void toggleWatch(Long userId, Long reportId){
        int deletedRows = watchRepository.deleteByIds(userId, reportId);

        if (deletedRows == 0) {
            UserEntity user = userRepository.getReferenceById(userId);
            ReportEntity report = reportRepository.getReferenceById(reportId);
            WatchId watchId = new WatchId(user, report);
            WatchEntity newWatch = new WatchEntity(watchId, LocalDateTime.now());
            watchRepository.save(newWatch);
        }
    }
}
