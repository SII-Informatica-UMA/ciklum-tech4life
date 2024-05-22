package TECH4LIFE.entidadesJPA.servicios;

import TECH4LIFE.entidadesJPA.controladores.Mapper;
import TECH4LIFE.entidadesJPA.dtos.DestinatarioDTO;
import TECH4LIFE.entidadesJPA.dtos.MensajeDTO;
import TECH4LIFE.entidadesJPA.dtos.MensajeNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Destinatario;
import TECH4LIFE.entidadesJPA.entities.Mensaje;
import TECH4LIFE.entidadesJPA.excepciones.MensajeNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.PeticionNoValida;
import TECH4LIFE.entidadesJPA.excepciones.UsuarioNoAutorizado;
import TECH4LIFE.entidadesJPA.repositories.CentroRepository;
import TECH4LIFE.entidadesJPA.repositories.MensajeRepository;
import TECH4LIFE.entidadesJPA.repositories.DestinatarioRepository;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class LogicaMensaje {

    @Autowired
    private MensajeRepository mensajeRepo;
    private CentroRepository centroRepo;
    private DestinatarioRepository destinatarioRepo;

//-----------------------------------------------------------------------------------------------
    //Get todos los mensajes de un centro
    public List<Mensaje> getMensajesByCentro(Long idCentro) throws MensajeNoExistente, UsuarioNoAutorizado {
        List<Mensaje> mensajes;

        mensajes = mensajeRepo.bandejaTodos(idCentro);

            if(mensajes.isEmpty()) throw new MensajeNoExistente();

        return mensajes;
    }

//----------------------------------------------------------------------------------------
    //Get un mensaje por su idMensaje
    public Mensaje getMensajeById(Integer idMensaje) throws MensajeNoExistente, UsuarioNoAutorizado{
        Optional<Mensaje> mensaje = mensajeRepo.findById(idMensaje);

        if(mensaje.isEmpty()) throw new MensajeNoExistente();

        return mensaje.get();
    }

//----------------------------------------------------------------------------------------
    //Post un nuevo mensaje
    public Mensaje postMensaje(MensajeNuevoDTO mensajeCrear, Integer idCentro) throws MensajeNoExistente, UsuarioNoAutorizado{
        //if(mensajeCrear == null) throw new MensajeNoExistente();

        Destinatario remite = destinatarioRepo.findById(idCentro).get();
        DestinatarioDTO remitente = Mapper.toDestinatarioDTO(remite);
        mensajeCrear.setRemitente(remitente);

        Mensaje mensajeCreado = mensajeRepo.save(Mapper.toMensaje(mensajeCrear));
        return mensajeCreado;
    }

//----------------------------------------------------------------------------------------
    //Delete un mensaje por su idMensaje
    public void deleteMensaje(Integer idMensaje) throws MensajeNoExistente, UsuarioNoAutorizado {
        Optional<Mensaje> mensaje = mensajeRepo.findById(idMensaje);

        if(mensaje.isEmpty()) throw new MensajeNoExistente();

        mensajeRepo.deleteById(idMensaje);
    }
}

