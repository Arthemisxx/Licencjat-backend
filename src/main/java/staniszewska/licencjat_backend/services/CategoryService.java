package staniszewska.licencjat_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import staniszewska.licencjat_backend.entities.CategoryEntity;
import staniszewska.licencjat_backend.models.CategoryDTO;
import staniszewska.licencjat_backend.repositories.CategoryRepository;
import staniszewska.licencjat_backend.repositories.ReportRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ReportRepository reportRepository;

    public List<CategoryDTO> getAllCategories(){
        List<CategoryDTO> categories =  categoryRepository.getAllCategories();
        if(categories.isEmpty()){
            return new ArrayList<>();
        }
        return categories;
    }

    public Long addCategory(String categoryName){
        CategoryEntity newCategory = CategoryEntity.builder()
                .name(categoryName)
                .iconKey("other.svg")
                .colorHex(null)
                .build();

        return categoryRepository.save(newCategory).getId();
    }

    public boolean deleteCategory(Long id){
        if (!categoryRepository.existsById(id)) {
            return false;
        }

        if (reportRepository.existsByCategoryId(id)) {
            throw new IllegalStateException("Nie można usunąć kategorii, ponieważ ma przypisane zgłoszenia.");
        }

        categoryRepository.deleteById(id);
        return true;
    }
}
