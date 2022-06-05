package es.ideas.holeventoproyecto.modelo;

import static es.ideas.holeventoproyecto.utils.utils.generateEncode;

public class UsuarioNormal {
    private String email;
    private String idUsuario;
    private String nombreUsuario;
    private String password;

    public UsuarioNormal(String email, String idUsuario, String nombreUsuario, String password) {
        this.email = email;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.password = generateEncode(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
