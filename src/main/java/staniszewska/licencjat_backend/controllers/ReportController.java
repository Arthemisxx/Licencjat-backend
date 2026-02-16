package staniszewska.licencjat_backend.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    @GetMapping
    public ResponseEntity<Void> getAllReports(){

        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> getReport(@PathVariable Long id){

        return null;
    }




}
