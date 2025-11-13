package projet.polytech.airejeux.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationResponseDto { // Le nom de la classe est correct
    private Long id;
    private Long utilisateurId;
    private String utilisateurUsername; 
    private Long jeuxId;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer quantity;
    private String status;
    private String notes;
}