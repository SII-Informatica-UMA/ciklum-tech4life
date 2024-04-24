package TECH4LIFE.entidadesJPA.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Entity
public class Mensaje {

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




