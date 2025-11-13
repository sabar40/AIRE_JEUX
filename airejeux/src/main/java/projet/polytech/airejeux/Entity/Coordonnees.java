package projet.polytech.airejeux.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "coordonnees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordonnees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String latitude;
    @Column(nullable = false)
    private String longitude;
}