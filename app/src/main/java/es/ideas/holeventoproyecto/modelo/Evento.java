package es.ideas.holeventoproyecto.modelo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;


public class Evento {
    private long idEvento;
    private String idUsuario;
    private String idProvincia;
    private String direccion;
    private String nombreUsuario;
    private String contenido;
    private String fechaPublicacion;
    private String fechaEvento;
    private String imagen;
    private int plazasTotales;


    public Evento(Long idEvento, String idUsuario, String idProvincia,String direccion, String contenido, String fechaEvento, String imagen,
                  int plazasTotales, String fechaPublicacion) {
        this.idEvento = idEvento;
        Log.i("Datos", "IDEVENTO: "+idEvento);
        this.idUsuario = idUsuario;
        this.idProvincia = idProvincia;
        this.direccion = direccion;
        this.contenido = contenido;
        this.fechaEvento = fechaEvento;
        this.imagen = imagen;
        this.plazasTotales = plazasTotales;
        this.fechaPublicacion = fechaPublicacion;
    }

    public long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(long idEvento) {
        this.idEvento = idEvento;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(String idProvincia) {
        this.idProvincia = idProvincia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(String fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getPlazasTotales() {
        return plazasTotales;
    }

    public void setPlazasTotales(int plazasTotales) {
        this.plazasTotales = plazasTotales;
    }

}
