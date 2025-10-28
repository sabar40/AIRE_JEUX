package projet.polytech.airejeux.dto;


public class UserDTO {
    private String username;
    private String password;
    private String nom;
    private String prenom;
    private String mail;
    private String role;

    // 🧱 Constructeurs
    public UserDTO() {}

    public UserDTO(String username, String password, String nom, String prenom, String mail, String role) {
        this.username = username;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.role = role;
    }

    // 🧩 Getters et Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
