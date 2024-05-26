package TECH4LIFE.entidadesJPA.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
public class Destinatario {
    @Id @GeneratedValue
    private Integer id ;
    private TipoDestinatario tipo;
}

// ¿Destinatario es un @Entity?
