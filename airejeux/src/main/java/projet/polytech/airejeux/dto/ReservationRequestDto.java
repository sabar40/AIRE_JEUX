package projet.polytech.airejeux.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservationRequestDto { // Le nom de la classe doit correspondre au nom du fichier
    private Long jeuxId;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer quantity;
    private String notes;
}