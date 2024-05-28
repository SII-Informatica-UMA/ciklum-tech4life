package TECH4LIFE.entidadesJPA.servicios;

import TECH4LIFE.entidadesJPA.dtos.MensajeDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Destinatario;
import TECH4LIFE.entidadesJPA.entities.Mensaje;
import TECH4LIFE.entidadesJPA.excepciones.MensajeNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.UsuarioNoAutorizado;
import TECH4LIFE.entidadesJPA.repositories.CentroRepository;
import TECH4LIFE.entidadesJPA.repositories.DestinatarioRepository;
import TECH4LIFE.entidadesJPA.repositories.GerenteRepository;
import TECH4LIFE.entidadesJPA.repositories.MensajeRepository;
import TECH4LIFE.entidadesJPA.security.JwtUtil;
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
     
    private final JwtUtil jwtUtil;
    @Autowired
    private MensajeRepository mensajeRepo;
    private CentroRepository centroRepo;
    private DestinatarioRepository destinatarioRepo;
    private GerenteRepository gerenteRepo;

    public LogicaMensaje(JwtUtil jwtUtil,GerenteRepository gerenteRepo,DestinatarioRepository destinatarioRepo,
    CentroRepository centroRepo,MensajeRepository mensajeRepo){
        this.jwtUtil=jwtUtil;
        this.gerenteRepo=gerenteRepo;
        this.destinatarioRepo=destinatarioRepo;
        this.centroRepo=centroRepo;
        this.mensajeRepo=mensajeRepo;
    }
//-----------------------------------------------------------------------------------------------
    //Get todos los mensajes de un centro

    public List<Mensaje> getMensajesByCentro(Integer centro,String authHeader) throws MensajeNoExistente, UsuarioNoAutorizado {

         // SEGURIDAD:
         String token = jwtUtil.extractToken(authHeader);
         String idUsuario = jwtUtil.getUsernameFromToken(token);
 
         if (!( gerenteRepo.FindGerenteByidUsuario(Integer.parseInt(idUsuario))!=null)) {
             throw new UsuarioNoAutorizado();
         }

        List<Mensaje> mensajesCentro = mensajeRepo.bandejaTodos(centro);

        if(mensajesCentro.isEmpty()) throw new MensajeNoExistente();
        return mensajesCentro;
    }

//----------------------------------------------------------------------------------------
    //Get un mensaje por su idMensaje

    public Mensaje getMensajeById(Integer idMensaje,String authHeader) throws MensajeNoExistente, UsuarioNoAutorizado{
        Optional<Mensaje> mensaje = mensajeRepo.findById(idMensaje);
        // SEGURIDAD:
        String token = jwtUtil.extractToken(authHeader);
        String idUsuario = jwtUtil.getUsernameFromToken(token);
  
          if (!( gerenteRepo.FindGerenteByidUsuario(Integer.parseInt(idUsuario))!=null)) {
              throw new UsuarioNoAutorizado();
          }
          
        if(mensaje.isEmpty()) throw new MensajeNoExistente();

        return mensaje.get();
    }

//----------------------------------------------------------------------------------------
    //Post un nuevo mensaje

    public Mensaje postMensaje(Mensaje mensajeCrear,String authHeader) throws UsuarioNoAutorizado{
          // SEGURIDAD:
          String token = jwtUtil.extractToken(authHeader);
          String idUsuario = jwtUtil.getUsernameFromToken(token);
  
          if (!( gerenteRepo.FindGerenteByidUsuario(Integer.parseInt(idUsuario))!=null)) {
              throw new UsuarioNoAutorizado();
          }
        Mensaje mensajeCreado = mensajeRepo.save(mensajeCrear);
        return mensajeCreado;
    }

//----------------------------------------------------------------------------------------
    //Delete un mensaje por su idMensaje

    public void deleteMensaje(Integer idMensaje,String authHeader) throws MensajeNoExistente, UsuarioNoAutorizado {
        Optional<Mensaje> mensaje = mensajeRepo.findById(idMensaje);
          // SEGURIDAD:
          String token = jwtUtil.extractToken(authHeader);
          String idUsuario = jwtUtil.getUsernameFromToken(token);
  
          if (!( gerenteRepo.FindGerenteByidUsuario(Integer.parseInt(idUsuario))!=null)) {
              throw new UsuarioNoAutorizado();
          }

          
        if(mensaje.isEmpty()) throw new MensajeNoExistente();

        mensajeRepo.deleteById(idMensaje);
    }
}

