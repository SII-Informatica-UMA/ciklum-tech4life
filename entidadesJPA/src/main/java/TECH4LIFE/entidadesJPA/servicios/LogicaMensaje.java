package TECH4LIFE.entidadesJPA.servicios;

import TECH4LIFE.entidadesJPA.dtos.MensajeDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Destinatario;
import TECH4LIFE.entidadesJPA.entities.Mensaje;
import TECH4LIFE.entidadesJPA.excepciones.MensajeNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.UsuarioNoAutorizado;
import TECH4LIFE.entidadesJPA.repositories.MensajeRepository;
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

//-----------------------------------------------------------------------------------------------
    //Get todos los mensajes de un centro
    @RolesAllowed("Gerente")
    public List<MensajeDTO> getMensajesByCentro(Centro centro) throws MensajeNoExistente, UsuarioNoAutorizado {
       //necesito pasar de centro a remitente


        /*

        DUDA: como creo un destinatario a partir de un objeto de tipo Centro?

         */
        Destinatario desCentro = new Destinatario();

        List<MensajeDTO> bandejaEntrada = mensajeRepo.bandejaEntrada(desCentro);
        List<MensajeDTO> bandejaSalida = mensajeRepo.bandejaSalida(desCentro);
        List<MensajeDTO> listaMensajes = new ArrayList<>();
        listaMensajes.addAll(bandejaEntrada);
        listaMensajes.addAll(bandejaSalida);

        if(listaMensajes.isEmpty()) throw new MensajeNoExistente();
        return listaMensajes;
    }

//----------------------------------------------------------------------------------------
    //Get un mensaje por su idMensaje
    @RolesAllowed("Gerente")
    public Mensaje getMensajeById(Integer idMensaje) throws MensajeNoExistente, UsuarioNoAutorizado{
        Optional<Mensaje> mensaje = mensajeRepo.findById(idMensaje);

        if(mensaje.isEmpty()) throw new MensajeNoExistente();

        return mensaje.get();
    }

//----------------------------------------------------------------------------------------
    //Post un nuevo mensaje
    @RolesAllowed("Gerente")
    public Mensaje postMensaje(Mensaje mensajeCrear) throws MensajeNoExistente, UsuarioNoAutorizado{
        if(mensajeCrear == null) throw new MensajeNoExistente();

        return mensajeRepo.save(mensajeCrear);
    }

//----------------------------------------------------------------------------------------
    //Delete un mensaje por su idMensaje
    @RolesAllowed("Gerente")
    public void deleteMensaje(Integer idMensaje) throws MensajeNoExistente, UsuarioNoAutorizado {
        Optional<Mensaje> mensaje = mensajeRepo.findById(idMensaje);

        if(mensaje.isEmpty()) throw new MensajeNoExistente();

        mensajeRepo.deleteById(idMensaje);
    }
}

