package staniszewska.licencjat_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import staniszewska.licencjat_backend.entities.CategoryEntity;
import staniszewska.licencjat_backend.mappers.CategoryMapper;
import staniszewska.licencjat_backend.models.CategoryDTO;
import staniszewska.licencjat_backend.repositories.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDTO> getAllCategories(){
        List<CategoryEntity> categories =  categoryRepository.findAll();
        if(categories.isEmpty()){
            return new ArrayList<>();
        }

        return categories.stream()
                .map(categoryMapper::toDTO)
                .toList();
    }


}
