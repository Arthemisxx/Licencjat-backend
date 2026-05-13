package staniszewska.licencjat_backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import staniszewska.licencjat_backend.entities.UserEntity;
import staniszewska.licencjat_backend.models.AdminUserDTO;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(Long id);

    @Query(value = "SELECT new staniszewska.licencjat_backend.models.AdminUserDTO(" +
            "u.id, u.firstName, u.lastName, u.email, COUNT(r.id)) " +
            "FROM UserEntity u " +
            "LEFT JOIN ReportEntity r ON r.author = u " +
            "WHERE (:search IS NULL OR :search = '' " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "GROUP BY u.id, u.firstName, u.lastName, u.email",
            countQuery = "SELECT COUNT(u.id) FROM UserEntity u " +
                    "WHERE (:search IS NULL OR :search = '' " +
                    "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
                    "OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<AdminUserDTO> getAdminUsers(@Param("search") String search, Pageable pageable);

}
