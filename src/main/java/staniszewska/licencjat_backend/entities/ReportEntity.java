package staniszewska.licencjat_backend.entities;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reports")
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Nullable
    private UserEntity author;

    @Nullable
    @Column(name = "quest_email")
    private String guestEmail;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    private String description;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @Nullable
    private String address;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;










}
