package staniszewska.licencjat_backend.entities;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String password;

    @Nullable
    private String firstName;

    @Nullable
    private String lastName;

    private String role;

    private boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;







}
