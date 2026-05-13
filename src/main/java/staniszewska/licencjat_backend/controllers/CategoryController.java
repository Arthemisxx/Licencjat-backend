package staniszewska.licencjat_backend.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import staniszewska.licencjat_backend.entities.UserEntity;
import staniszewska.licencjat_backend.models.CategoryDTO;
import staniszewska.licencjat_backend.services.CategoryService;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final Logger logger = LogManager.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> result = categoryService.getAllCategories();
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        if (currentUser == null || !Objects.equals(currentUser.getRole(), "ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            boolean deleted = categoryService.deleteCategory(id);

            if (deleted) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping
    public ResponseEntity<Void> addNewCategory(@RequestBody String categoryName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        if (currentUser == null || !Objects.equals(currentUser.getRole(), "ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String cleanName = categoryName.replace("\"", "");
        Long newId = categoryService.addCategory(cleanName);
        if(newId != null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

}
