package TECH4LIFE.entidadesJPA.dtos;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    @OneToMany(cascade = CascadeType.PERSIST)
    private Set<DestinatarioDTO> destinatarios ;
    @OneToMany(cascade = CascadeType.PERSIST)
    private Set<DestinatarioDTO> copia ;
    @OneToMany(cascade = CascadeType.PERSIST)
    private Set<DestinatarioDTO> copiaOculta ;
    @OneToOne(cascade = CascadeType.PERSIST)
    private DestinatarioDTO remitente ;
    private String contenido;
}
