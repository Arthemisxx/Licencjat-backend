package staniszewska.licencjat_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import staniszewska.licencjat_backend.entities.UserEntity;
import staniszewska.licencjat_backend.models.ReportDetailsDTO;
import staniszewska.licencjat_backend.models.UserDataDTO;
import staniszewska.licencjat_backend.models.UserUpdatedDetailsDTO;
import staniszewska.licencjat_backend.repositories.UserRepository;
import staniszewska.licencjat_backend.services.ReportService;

import java.util.List;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final Logger logger = LogManager.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final ReportService reportService;


    @GetMapping("/me")
    public ResponseEntity<UserDataDTO> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        UserDataDTO user = new UserDataDTO();
        user.setId(currentUser.getId());
        user.setRole(currentUser.getRole());
        user.setEmail(currentUser.getEmail());
        user.setFirstName(currentUser.getFirstName());
        user.setLastName(currentUser.getLastName());

        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    @GetMapping("/me/reports")
    public ResponseEntity<List<ReportDetailsDTO>> getAllUserReports() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        if (currentUser != null) {
            List<ReportDetailsDTO> userReports = reportService.getReportsByUserId(currentUser.getId());


            if(userReports != null && !userReports.isEmpty()){
                return new ResponseEntity<>(userReports, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/me/watched")
    public ResponseEntity<List<ReportDetailsDTO>> getAllUserWatched() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        if (currentUser != null) {
            List<ReportDetailsDTO> userReports = reportService.getReportsWatchedByUserId(currentUser.getId());
            if(userReports != null && !userReports.isEmpty()){
                return new ResponseEntity<>(userReports, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/me")
    public ResponseEntity<UserUpdatedDetailsDTO> updateUserDetails(@RequestBody UserUpdatedDetailsDTO user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        UserUpdatedDetailsDTO updatedUser = new UserUpdatedDetailsDTO();

        if (currentUser != null) {
            if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
                currentUser.setFirstName(user.getFirstName());
                updatedUser.setFirstName(user.getFirstName());
            }

            if (user.getLastName() != null && !user.getLastName().isEmpty()) {
                currentUser.setLastName(user.getLastName());
                updatedUser.setLastName(user.getLastName());
            }

            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                currentUser.setEmail(user.getEmail());
                updatedUser.setEmail(user.getEmail());
            }

            logger.info(updatedUser);

            userRepository.save(currentUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
