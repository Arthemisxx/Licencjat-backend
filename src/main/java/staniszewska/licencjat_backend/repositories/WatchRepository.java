package staniszewska.licencjat_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import staniszewska.licencjat_backend.entities.WatchEntity;
import staniszewska.licencjat_backend.entities.WatchId;

@Repository
public interface WatchRepository extends JpaRepository<WatchEntity, WatchId> {
    @Query("SELECT COUNT(w) > 0 FROM WatchEntity w WHERE w.id.user.id = :userId AND w.id.report.id = :reportId")
    boolean isReportWatchedByUser(@Param("userId") Long userId, @Param("reportId") Long reportId);

    @Modifying
    @Query("DELETE FROM WatchEntity w WHERE w.id.user.id = :userId AND w.id.report.id = :reportId")
    int deleteByIds(@Param("userId") Long userId, @Param("reportId") Long reportId);
}
