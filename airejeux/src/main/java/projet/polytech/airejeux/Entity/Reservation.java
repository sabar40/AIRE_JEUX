package projet.polytech.airejeux.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * @brief Entity represent a reservation/booking in the system
 * 
 *        This entity maps to the 'reservation' table in the database and represents
 *        a user's booking for a specific playground equipment at a
 *        specific date and time slot.
 */
@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * @brief Reference to the user who made the reservation
   * 
   *        Many reservations can belong to one user (Many-to-One relationship)
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "utilisateur_id", nullable = false)
  private Utilisateur utilisateur;

  /**
   * @brief Reference to the playground equipment being reserved
   * 
   *        Many reservations can be made for one equipment (Many-to-One relationship)
   *        Important : Jeux entity will be created by another team member
   */
  @Column(name = "jeux_id", nullable = false)
  private Long jeuxId;

  /**
   * @brief Date of the reservation
   */
  @Column(name = "booking_date", nullable = false)
  private LocalDate bookingDate;

  /**
   * @brief Start time of the reservation
   */
  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  /**
   * @brief End time of the reservation
   */
  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  /**
   * @brief Number of spots/slots reserved
   * 
   *        Allows users to reserve multiple slots if the equipment has capacity
   */
  @Column(name = "quantity", nullable = false)
  private Integer quantity = 1;

  /**
   * @brief Current status of the reservation
   * 
   *        Possible values: CONFIRMED, CANCELLED, COMPLETED
   */
  @Column(name = "status", length = 20)
  private String status = "CONFIRMED";

  /**
   * @brief Additional notes or comments for the reservation
   */
  @Column(name = "notes", columnDefinition = "TEXT")
  private String notes;

  /**
   * @brief Timestamp when the reservation was created
   */
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  /**
   * @brief Timestamp when the reservation was last updated
   */
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  /**
   * @brief Legacy reservation field from original table
   * 
   *        Kept for backward compatibility with existing data
   */
  @Column(name = "reservation")
  private Integer reservation;

  /**
   * @brief Automatically set creation timestamp before persisting
   */
  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  /**
   * @brief Automatically update timestamp before updating
   */
  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
