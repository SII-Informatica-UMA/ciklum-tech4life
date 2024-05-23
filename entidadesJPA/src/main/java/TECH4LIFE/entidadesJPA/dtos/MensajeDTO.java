package TECH4LIFE.entidadesJPA.dtos;

import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class MensajeDTO {

    private Integer idMensaje ;
    private String asunto ;
    private Set<DestinatarioDTO> destinatarios ;
    private Set<DestinatarioDTO> copia ;
    private Set<DestinatarioDTO> copiaOculta ; // lista vacía
    private DestinatarioDTO remitente ;
    private String contenido;
}
