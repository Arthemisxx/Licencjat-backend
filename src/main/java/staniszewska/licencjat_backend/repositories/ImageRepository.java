package staniszewska.licencjat_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import staniszewska.licencjat_backend.entities.ImageEntity;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    @Query("SELECT i.url FROM ImageEntity i WHERE i.report.id = :reportId")
    List<String> findImageUrlsByReportId(@Param("reportId") Long reportId);
    void deleteAllByReportId(Long reportId);
    List<ImageEntity> findAllByReportId(Long id);


}
