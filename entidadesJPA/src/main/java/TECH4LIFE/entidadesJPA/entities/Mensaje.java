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
    @Id @GeneratedValue
    private Integer idMensaje ;
    private String asunto ;
    @OneToMany(mappedBy = "Destinatario")
    private List<Destinatario> destinatarios;
    @OneToMany(mappedBy = "Destinatario")
    private List<Destinatario> copia;
    @OneToMany(mappedBy = "Destinatario")
    private List<Destinatario> copiaOculta;
    private Destinatario remitente;
    private String contenido ;
}




