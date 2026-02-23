package staniszewska.licencjat_backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import staniszewska.licencjat_backend.entities.CategoryEntity;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity,Long> {
    List<CategoryEntity> findAll();
}
