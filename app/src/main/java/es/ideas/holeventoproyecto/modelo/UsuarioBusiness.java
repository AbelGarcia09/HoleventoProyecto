package es.ideas.holeventoproyecto.modelo;

import static es.ideas.holeventoproyecto.utils.utils.generateEncode;

public class UsuarioBusiness {

    private String email;
    private String idUsuario;
    private String nombreUsuario;
    private String password;
    private String provincia;
    private String telefono;

    public UsuarioBusiness(String email, String idUsuario, String nombreUsuario, String password,
                           String provincia, String telefono) {
        this.email = email;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.password = generateEncode(password);
        this.provincia = provincia;
        this.telefono = telefono;
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

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
