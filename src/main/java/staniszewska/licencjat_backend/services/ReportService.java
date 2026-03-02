package staniszewska.licencjat_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import staniszewska.licencjat_backend.entities.ReportEntity;
import staniszewska.licencjat_backend.mappers.ReportMapper;
import staniszewska.licencjat_backend.models.ReportDTO;
import staniszewska.licencjat_backend.repositories.ReportRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    public List<ReportDTO> getAllReports(){
        List<ReportDTO> reports = reportRepository.findAllReportDTO();
        if(reports.isEmpty()){
            return new ArrayList<>();
        }

        return reports;
    }

    public ReportDTO getReportById(Long id) {
        return reportRepository.getReportDTOById(id);
       }
}
