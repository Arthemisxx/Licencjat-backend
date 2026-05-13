package staniszewska.licencjat_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import staniszewska.licencjat_backend.entities.UserEntity;
import staniszewska.licencjat_backend.models.*;
import staniszewska.licencjat_backend.repositories.UserRepository;
import staniszewska.licencjat_backend.services.ReportService;
import staniszewska.licencjat_backend.services.UserService;

import java.util.List;
import java.util.Objects;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final Logger logger = LogManager.getLogger(UserController.class);
    private final UserRepository userRepository;
    private final ReportService reportService;
    private final UserService userService;


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

    @GetMapping("/admin")
    public ResponseEntity<Page<AdminUserDTO>> getAdminUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String dir) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        if (currentUser == null || !Objects.equals(currentUser.getRole(), "ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        Page<AdminUserDTO> users = userService.getAdminUsers(page, size, search, sort, dir);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/admin/{id}/reports")
    public ResponseEntity<List<AdminUserReportDetailsDTO>> getUserReports(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        if (currentUser == null || !Objects.equals(currentUser.getRole(), "ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<AdminUserReportDetailsDTO> reports = reportService.getUserReportsMini(id);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }







}
