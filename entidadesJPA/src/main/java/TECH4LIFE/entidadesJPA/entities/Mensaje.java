package TECH4LIFE.entidadesJPA.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List ;
import java.util.Set;

@Setter
@Getter
@Entity
public class Mensaje {

    /* Duda: El conjunto de destinatarios se modela así??
        Con el @Embeddable y el @ElementCollection
    * */

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
}




