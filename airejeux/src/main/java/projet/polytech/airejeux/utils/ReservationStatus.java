package projet.polytech.airejeux.utils;

/**
 * Centralise les constantes de statut pour les réservations.
 * Cela évite les fautes de frappe dans le code (ex: "PENDING" vs "Pending").
 */
public class ReservationStatus {
    public static final String PENDING = "PENDING";     // En attente de validation
    public static final String APPROVED = "APPROVED";   // Validé par l'admin
    public static final String REJECTED = "REJECTED";   // Rejeté par l'admin
    public static final String CANCELLED = "CANCELLED"; // Annulé par l'utilisateur
}