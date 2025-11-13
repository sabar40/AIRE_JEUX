package projet.polytech.airejeux.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "jeux")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jeux {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private int quantite;
    private String description;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "coordonnees_id", referencedColumnName = "id", nullable = false)
    private Coordonnees coordonnees;
}