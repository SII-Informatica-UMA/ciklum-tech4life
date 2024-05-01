package TECH4LIFE.entidadesJPA.dtos;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class GerenteDTO {
    private Integer id ;
    private Integer idUsuario ;
    private String empresa ;
}
