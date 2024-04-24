package TECH4LIFE.entidadesJPA.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
public class Mensaje {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mensaje mensaje = (Mensaje) o;
        return Objects.equals(idMensaje, mensaje.idMensaje) && Objects.equals(asunto, mensaje.asunto) && Objects.equals(destinatarios, mensaje.destinatarios) && Objects.equals(copia, mensaje.copia) && Objects.equals(copiaOculta, mensaje.copiaOculta) && Objects.equals(remitente, mensaje.remitente) && Objects.equals(contenido, mensaje.contenido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMensaje, asunto, destinatarios, copia, copiaOculta, remitente, contenido);
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "idMensaje=" + idMensaje +
                ", asunto='" + asunto + '\'' +
                ", destinatarios=" + destinatarios +
                ", copia=" + copia +
                ", copiaOculta=" + copiaOculta +
                ", remitente=" + remitente +
                ", contenido='" + contenido + '\'' +
                '}';
    }

    @Embeddable // This class can be separate
    public static class Destinatario {
        private Integer id ;
        private enum tipo {
            CENTRO, ENTRENADOR, CLIENTE
        } ;
    }

    @Id @GeneratedValue
    private Integer idMensaje ;
    private String asunto ;
    @ElementCollection
    private Set<Destinatario> destinatarios;
    @ElementCollection
    private Set<Destinatario> copia;
    @ElementCollection
    private Set<Destinatario> copiaOculta;
    private Destinatario remitente;
    private String contenido;
}




