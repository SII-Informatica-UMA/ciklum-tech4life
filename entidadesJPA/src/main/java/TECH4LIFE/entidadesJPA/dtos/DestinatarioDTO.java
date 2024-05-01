package TECH4LIFE.entidadesJPA.dtos;
import TECH4LIFE.entidadesJPA.entities.TipoDestinatario;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class DestinatarioDTO {
    private Integer id ;
    private TipoDestinatario tipo;
}
