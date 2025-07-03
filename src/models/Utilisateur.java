package models;

public class Utilisateur {
    private int id;
    private String username;
    private String password;
    private String role;
    private int idPersonne;
    private boolean estActif;

    // Constructeurs
    public Utilisateur() {}

    public Utilisateur(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.estActif = true;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getIdPersonne() {
        return idPersonne;
    }

    public void setIdPersonne(int idPersonne) {
        this.idPersonne = idPersonne;
    }

    public boolean isEstActif() {
        return estActif;
    }

    public void setEstActif(boolean estActif) {
        this.estActif = estActif;
    }
}