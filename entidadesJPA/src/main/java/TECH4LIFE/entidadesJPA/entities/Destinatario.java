package TECH4LIFE.entidadesJPA.entities;

import jakarta.persistence.Entity;
import lombok.*;

@ToString
@EqualsAndHashCode
@Setter
@Getter
@Entity
@Builder

public class Destinatario {
    private Integer id ;
    private TipoDestinatario tipo;
}
