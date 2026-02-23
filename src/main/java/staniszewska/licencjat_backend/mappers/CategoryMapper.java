package staniszewska.licencjat_backend.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import staniszewska.licencjat_backend.entities.CategoryEntity;
import staniszewska.licencjat_backend.models.CategoryDTO;

@Component
@RequiredArgsConstructor
public class CategoryMapper {
    public CategoryDTO toDTO(CategoryEntity category) {
       Long id = category.getId();
       String name = category.getName();
       String iconKey = category.getIconKey();
       String colorHex = category.getColorHex();

       return new CategoryDTO(id, name,iconKey, colorHex);
    }


}
