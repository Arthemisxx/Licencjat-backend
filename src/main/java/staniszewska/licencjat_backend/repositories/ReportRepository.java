package staniszewska.licencjat_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import staniszewska.licencjat_backend.entities.ReportEntity;
import staniszewska.licencjat_backend.models.*;

import java.util.List;
import java.util.Optional;

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
    List<ReportDetailsDTO> getReportDetailsDTO(@Param("id") Long id);

    @Query("SELECT new staniszewska.licencjat_backend.models.AdminReportDetailsDTO(" +
            "r.adminNote, r.id, r.description, r.latitude, r.longitude, r.address, " +
            "c.id, c.name, r.createdAt, r.updatedAt, r.status, u.id," +
            "CASE WHEN u.id IS NOT NULL THEN CONCAT(u.firstName, ' ', u.lastName) END, " +
            "CASE WHEN u.id IS NOT NULL THEN u.email ELSE r.guestEmail END)" +
            "FROM ReportEntity r " +
            "JOIN r.category c " +
            "LEFT JOIN r.author u " +
            "WHERE r.id = :id")
    AdminReportDetailsDTO getAdminReportDetailsDTO(@Param("id") Long id);



    @Query(value = "SELECT new staniszewska.licencjat_backend.models.ReportDTO(r.id, c.id, c.name, c.iconKey, c.colorHex, r.latitude, r.longitude)"
            + " from ReportEntity r JOIN r.category c")
    List<ReportDTO> findAllReportDTO();


    @Query(value = "select new staniszewska.licencjat_backend.models.ReportDTO(r.id, c.id, c.name, c.iconKey, c.colorHex, r.latitude, r.longitude)"
            + " from ReportEntity r JOIN r.category c"
            + " where c.id IN :categoryIds")
    List<ReportDTO> findAllByCategoryIdIn(@Param("categoryIds") List<Long> categoryIds);




    @Query("""
    SELECT new staniszewska.licencjat_backend.models.AdminReportDTO(
        r.id,
        r.description,
        c.id,
        c.name,
        r.createdAt,
        r.updatedAt,
        r.status,
        a.id,
        CASE WHEN a.id IS NOT NULL 
             THEN CONCAT(a.firstName, ' ', a.lastName) 
             ELSE r.guestEmail 
        END
    )
    FROM ReportEntity r
    LEFT JOIN r.category c
    LEFT JOIN r.author a
    WHERE :searchTerm IS NULL OR :searchTerm = '' OR (
        CAST(r.id AS string) LIKE CONCAT('%', :searchTerm, '%') OR
        LOWER(r.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
        LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
        LOWER(CONCAT(a.firstName, ' ', a.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR
        LOWER(r.guestEmail) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
    )
    """)
    Page<AdminReportDTO> findReportsForAdminPanel(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT new staniszewska.licencjat_backend.models.AdminUserReportDetailsDTO(" +
            "r.id, r.description, c.name, r.status, r.createdAt) " +
            "FROM ReportEntity r " +
            "JOIN r.category c " +
            "WHERE r.author.id = :userId " +
            "ORDER BY r.createdAt DESC")
    List<AdminUserReportDetailsDTO> getUserReportsMini(@Param("userId") Long userId);


    ReportEntity getById(Long reportId);

    boolean existsByCategoryId(Long categoryId);

    void deleteById(Long id);

    boolean existsById(Long id);

    Optional<ReportEntity> findById(Long id);
}
