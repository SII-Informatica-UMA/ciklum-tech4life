package TECH4LIFE.entidadesJPA.dtos;
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
    private enum tipo {
        CENTRO, ENTRENADOR, CLIENTE
    } ;
}
