package staniszewska.licencjat_backend.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import staniszewska.licencjat_backend.entities.ReportEntity;

import java.util.List;

@Repository
public interface ReportRepository extends CrudRepository<ReportEntity, Long> {
    List<ReportEntity> findAll();

}
