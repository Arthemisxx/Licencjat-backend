package staniszewska.licencjat_backend.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import staniszewska.licencjat_backend.entities.ReportEntity;
import staniszewska.licencjat_backend.models.ReportDTO;

import java.util.List;

@Repository
public interface ReportRepository extends CrudRepository<ReportEntity, Long> {
    List<ReportEntity> findAll();

    @Query(value = "select new staniszewska.licencjat_backend.models.ReportDTO(r.id, r.author.id, r.guestEmail, c.id, c.name, c.iconKey, c.colorHex, r.description, r.latitude, r.longitude, r.address, r.status, r.createdAt, r.updatedAt)"
            + " from ReportEntity r JOIN r.category c"
            + " where r.id = ?1 ")
    ReportDTO getReportDTOById(Long id);

    @Query(value = "SELECT new staniszewska.licencjat_backend.models.ReportDTO(r.id, r.author.id, r.guestEmail, c.id, c.name, c.iconKey, c.colorHex, r.description, r.latitude, r.longitude, r.address, r.status, r.createdAt, r.updatedAt)"
            + " from ReportEntity r JOIN r.category c")
    List<ReportDTO> findAllReportDTO();

}
