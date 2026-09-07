package staniszewska.licencjat_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import staniszewska.licencjat_backend.entities.CategoryEntity;
import staniszewska.licencjat_backend.models.CategoryDTO;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {
    List<CategoryEntity> findAll();

    @Query(value = "select new staniszewska.licencjat_backend.models.CategoryDTO(c.id, c.name, c.iconKey)"
            + " from CategoryEntity c")
    List<CategoryDTO> getAllCategories();

    Optional<CategoryEntity> findById(Long id);
}
