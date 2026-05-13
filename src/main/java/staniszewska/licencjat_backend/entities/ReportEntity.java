package staniszewska.licencjat_backend.entities;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "reports")
public class ReportEntity {

    @Nullable
    @Column(name = "admin_note")
    private String adminNote;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Nullable
    private UserEntity author;

    @Nullable
    @Column(name = "guest_email")
    private String guestEmail;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    private String description;

    private Double latitude;

    private Double longitude;

    @Nullable
    private String address;


    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;





}
