package TECH4LIFE.entidadesJPA.entities;

import java.util.Objects;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
public class Centro {
    @Id @GeneratedValue
    private Integer idCentro ;
    private String nombre ;
    private String direccion ;
    @OneToOne (mappedBy = "centro")
    private Gerente gerente;


}
