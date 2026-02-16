package staniszewska.licencjat_backend.entities;

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
@Table(name = "report_reactions")
public class ReactionEntity {
    @EmbeddedId
    private ReactionId id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
