package TECH4LIFE.entidadesJPA.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

import lombok.*;
import org.springframework.boot.context.properties.bind.Name;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
public class Mensaje {

    @Id
    @GeneratedValue
    private Integer idMensaje ;
    private String asunto ;
    @OneToMany
    private Set<Destinatario> destinatarios;
    @OneToMany
    private Set<Destinatario> copia;
    @OneToMany
    private Set<Destinatario> copiaOculta;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "remitente_fk", nullable = false)
    private Destinatario remitente;
    private String contenido;
}




