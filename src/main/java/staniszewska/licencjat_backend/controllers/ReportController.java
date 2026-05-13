package staniszewska.licencjat_backend.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import staniszewska.licencjat_backend.entities.UserEntity;
import staniszewska.licencjat_backend.models.*;
import staniszewska.licencjat_backend.repositories.ReportRepository;
import staniszewska.licencjat_backend.services.ReportService;

import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import staniszewska.licencjat_backend.services.WatchService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    private final Logger logger = LogManager.getLogger(ReportController.class);
    private final WatchService watchService;
    private final ReportRepository reportRepository;

    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReportsWithFilter(@RequestParam(required = false) List<Long> categoryIds ){
        List<ReportDTO> result;
        if(categoryIds == null || categoryIds.isEmpty()){
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }

        result = reportService.getFilteredReports(categoryIds);
        if(result.isEmpty()){
            logger.info("No tasks found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
//            result.forEach(e -> logger.info(e.getId()));
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }



    @GetMapping("admin")
    public ResponseEntity<Page<AdminReportDTO>> getAdminReports(@RequestParam(required = false) String search, Pageable pageable){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        if(currentUser != null && !Objects.equals(currentUser.getRole(), "ADMIN")){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Page<AdminReportDTO> page = reportService.searchAdminReports(search, pageable);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("admin/{id}")
    public ResponseEntity<AdminReportDetailsDTO> getAdminReportDetails(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        if(currentUser != null && !Objects.equals(currentUser.getRole(), "ADMIN")){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        AdminReportDetailsDTO result = reportService.getAdminReportById(id);
        if(result == null){
            logger.info("Task not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

//
//    @GetMapping
//    public ResponseEntity<List<ReportDTO>> getAllReports(){
//        List<ReportDTO> result = reportService.getAllReports();
//        if(result.isEmpty()){
//            logger.info("No tasks found!");
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }else{
//            result.forEach(e -> logger.info(e.getId()));
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        }
//    }



    @GetMapping("/{id}")
    public ResponseEntity<ReportDetailsDTO> getReport(@PathVariable Long id){
        ReportDetailsDTO result = reportService.getReportById(id);
        if(result == null){
            logger.info("Task not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @PostMapping("/{id}/watch")
    public ResponseEntity<ReportDetailsDTO> watchReport(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        logger.info("Weszło");

        if(currentUser == null){
            logger.info("Brak usera");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        watchService.toggleWatch(currentUser.getId(), id);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createReport(@RequestPart("reportData") CreateReportDTO report, @RequestPart(value = "images", required = false)List<MultipartFile> images){
        Long createdReportId = reportService.createReport(report);

        if(images != null && !images.isEmpty()) {
            reportService.saveImagesForReport(createdReportId, images);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestBody AdminUpdateDTO updated){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        if (currentUser == null || !Objects.equals(currentUser.getRole(), "ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String cleanStatus = updated.getStatus();
        String note = updated.getNote();


        boolean updatedData = reportService.updateReportStatusAndNote(id, cleanStatus, note);

        if (updatedData) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeReport(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        if (currentUser == null || !Objects.equals(currentUser.getRole(), "ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        boolean isDeleted = reportService.deleteReport(id);

        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }






}
