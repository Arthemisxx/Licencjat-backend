package staniszewska.licencjat_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import staniszewska.licencjat_backend.entities.ReportEntity;
import staniszewska.licencjat_backend.models.ReportDTO;
import staniszewska.licencjat_backend.models.ReportDetailsDTO;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    List<ReportEntity> findAll();

    @Query(value = "select new staniszewska.licencjat_backend.models.ReportDTO(r.id, c.id, c.name, c.iconKey, c.colorHex, r.latitude, r.longitude)"
            + " from ReportEntity r JOIN r.category c"
            + " where r.id = :id ")
    ReportDTO getReportDTOById(@Param("id") Long id);

    @Query(value = "select new staniszewska.licencjat_backend.models.ReportDetailsDTO(r.id, r.description, r.latitude, r.longitude, r.address, r.status, r.createdAt, r.updatedAt, c.id, c.name, c.iconKey, c.colorHex)"
            + " from ReportEntity r JOIN r.category c"
            + " where r.id = :id ")
    ReportDetailsDTO getReportDetailsDTOById(@Param("id") Long id);

    @Query(value = "select new staniszewska.licencjat_backend.models.ReportDetailsDTO(r.id, r.description, r.latitude, r.longitude, r.address, r.status, r.createdAt, r.updatedAt, c.id, c.name, c.iconKey, c.colorHex)"
            + " from ReportEntity r JOIN r.category c"
            + " where r.author.id = :id ")
    List<ReportDetailsDTO> getReportDetailsDTOByUserId(@Param("id") Long id);

    @Query("SELECT new staniszewska.licencjat_backend.models.ReportDetailsDTO(" +
            "r.id, r.description, r.latitude, r.longitude, r.address, r.status, r.createdAt, r.updatedAt, " +
            "c.id, c.name, c.iconKey, c.colorHex) " +
            "FROM WatchEntity w " +
            "JOIN w.id.report r " +
            "JOIN r.category c " +
            "WHERE w.id.user.id = :id")
    List<ReportDetailsDTO> getReportDetailsDTOWatchedByUser(@Param("id") Long id);

    @Query(value = "SELECT new staniszewska.licencjat_backend.models.ReportDTO(r.id, c.id, c.name, c.iconKey, c.colorHex, r.latitude, r.longitude)"
            + " from ReportEntity r JOIN r.category c")
    List<ReportDTO> findAllReportDTO();


    @Query(value = "select new staniszewska.licencjat_backend.models.ReportDTO(r.id, c.id, c.name, c.iconKey, c.colorHex, r.latitude, r.longitude)"
            + " from ReportEntity r JOIN r.category c"
            + " where c.id IN :categoryIds")
    List<ReportDTO> findAllByCategoryIdIn(@Param("categoryIds") List<Long> categoryIds);


    ReportEntity getById(Long reportId);
}
