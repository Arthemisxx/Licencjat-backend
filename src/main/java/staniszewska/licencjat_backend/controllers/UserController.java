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
            return new ResponseEntity<>(userReports, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/me/watched")
    public ResponseEntity<List<ReportDetailsDTO>> getAllUserWatched() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        if (currentUser != null) {
            List<ReportDetailsDTO> userReports = reportService.getReportsWatchedByUserId(currentUser.getId());
            return new ResponseEntity<>(userReports, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/me")
    public ResponseEntity<UserUpdatedDetailsDTO> updateUserDetails(@RequestBody UserUpdatedDetailsDTO user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            UserUpdatedDetailsDTO updatedUser = userService.updateUserDetails(currentUser, user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().contains("already exists")) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            throw e;
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

        if (!userRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<AdminUserReportDetailsDTO> reports = reportService.getUserReportsMini(id);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        logger.error("Unexpected error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }
}
