package staniszewska.licencjat_backend.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import staniszewska.licencjat_backend.models.ReportDTO;
import staniszewska.licencjat_backend.services.ReportService;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    private final Logger logger = LogManager.getLogger(ReportController.class);

    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReports(){
        List<ReportDTO> result = reportService.getAllReports();
        if(result.isEmpty()){
            logger.info("No tasks found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            result.forEach(e -> logger.info(e.getDescription()));
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReport(@PathVariable Long id){
        ReportDTO result = reportService.getReportById(id);
        if(result == null){
            logger.info("Task not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

//    @PostMapping
//    public ResponseEntity<Void> createReport(@RequestBody ReportDTO report){
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeReport(@PathVariable Long id){

        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PutMapping
//    public ResponseEntity<Void> updateReport(@RequestBody ReportDTO report){
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }




}
