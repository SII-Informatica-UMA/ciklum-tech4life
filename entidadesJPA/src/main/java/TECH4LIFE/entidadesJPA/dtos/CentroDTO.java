package TECH4LIFE.entidadesJPA.dtos;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class CentroDTO {

    private Integer idCentro ;
    private String nombre ;
    private String direccion ;
}
