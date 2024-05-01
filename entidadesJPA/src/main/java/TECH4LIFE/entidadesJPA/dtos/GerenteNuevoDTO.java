package TECH4LIFE.entidadesJPA.dtos;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class GerenteNuevoDTO {
    private Integer idUsuario ;
    private String empresa ;
}
