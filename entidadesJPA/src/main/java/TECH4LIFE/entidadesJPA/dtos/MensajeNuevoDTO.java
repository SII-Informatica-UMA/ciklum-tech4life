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
public class MensajeNuevoDTO {
    private String asunto ;
    private Set<DestinatarioDTO> destinatarios ;
    private Set<DestinatarioDTO> copia ;
    private Set<DestinatarioDTO> copiaOculta ;
    private DestinatarioDTO remitente ;
    private String contenido;
}
