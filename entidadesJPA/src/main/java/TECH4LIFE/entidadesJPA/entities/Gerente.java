package TECH4LIFE.entidadesJPA.entities;
import jakarta.persistence.*;


import java.util.Objects;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
@Entity
public class Gerente {
    @Id @GeneratedValue
    private Integer id ;
    private Integer idUsuario ;
    private String empresa ;
    @OneToOne
    private Centro centro;

}
