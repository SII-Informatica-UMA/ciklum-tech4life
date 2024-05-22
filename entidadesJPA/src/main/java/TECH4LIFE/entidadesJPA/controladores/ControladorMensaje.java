package TECH4LIFE.entidadesJPA.controladores;

import TECH4LIFE.entidadesJPA.dtos.MensajeDTO;
import TECH4LIFE.entidadesJPA.dtos.MensajeNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Mensaje;
import TECH4LIFE.entidadesJPA.repositories.CentroRepository;
import TECH4LIFE.entidadesJPA.repositories.MensajeRepository;
import TECH4LIFE.entidadesJPA.repositories.DestinatarioRepository;
import TECH4LIFE.entidadesJPA.excepciones.MensajeNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.UsuarioNoAutorizado;
import TECH4LIFE.entidadesJPA.servicios.LogicaMensaje;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/mensaje/centro")
//cuando llegue una solicitud con esta ruta, se ejecutará algún procedimiento de aquí
public class ControladorMensaje {
    private LogicaMensaje servicio;

    //CONSTRUCTOR DE LA CLASE CONTROLADORMENSAJE
    public ControladorMensaje(LogicaMensaje servicioMensaje){

        this.servicio = servicioMensaje;
    }

//------------------------------------------------------------------------------------------
/*
    GETS
 */
    //OBTENGO TODOS LOS MENSAJES DE UN CENTRO DADO EL CENTRO
    @GetMapping //capturo el objeto centro.
    public ResponseEntity<List<MensajeDTO>> listaDeMensajes(@RequestParam (value="centro", required = true) Integer centro){   //guardo en la variable centro el objeto Centro
        try{
            //CODE 200: Devuelve la lista de mensajes de cierto centro
            List<MensajeDTO> listaMensajes = servicio.getMensajesByCentro();
            return ResponseEntity.ok(listaMensajes);
        } catch(UsuarioNoAutorizado e){
            //CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch(MensajeNoExistente e){
            //CODE 404: El mensaje no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
//------------------------------------------------------------------------------------------


    //OBTENGO UN MENSAJE CONCRETO POR EL IDMENSAJE
    @GetMapping("/{idMensaje}")
    public ResponseEntity<MensajeDTO> getMensajeById(@PathVariable(name="idMensaje") Integer idMensaje) {
        try{
            //CODE 200: Devuelve la lista de mensajes de cierto centro
            MensajeDTO mensaje = Mapper.toMensajeDTO(servicio.getMensajeById(idMensaje));
            return ResponseEntity.ok(mensaje);
        } catch(UsuarioNoAutorizado e){
            //CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch(MensajeNoExistente e){
            //CODE 404: El mensaje no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
//------------------------------------------------------------------------------------------
/*
    POST
 */
    @PostMapping("/centro")
    public ResponseEntity<MensajeDTO> crearMensaje(@PathVariable (name="centro") Centro centro, @RequestBody MensajeNuevoDTO mensajeNuevoDTO, UriComponentsBuilder builder) {
        try{
            //CODE 201: Se crea el mensaje y lo devuelve
            Mensaje mensaje  = servicio.postMensaje(Mapper.toMensaje(mensajeNuevoDTO));
            URI uri = builder
                    .path(String.format("/%d/%d", mensaje.getIdMensaje(), centro.getIdCentro()))
                    .build()
                    .toUri();
            return ResponseEntity.created(uri).body(Mapper.toMensajeDTO(mensaje));
        } catch(UsuarioNoAutorizado e){
            //CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch(MensajeNoExistente e){
            //CODE 404: El mensaje no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
//------------------------------------------------------------------------------------------
/*
    DELETE
 */
    //Eliminamos un mensaje por su idMensaje.
    @DeleteMapping("{idMensaje}")
    public ResponseEntity<?> eliminarMensaje(@PathVariable(name="idMensaje") Integer idMensaje) {
        try{
            //CODE 200: Devuelve la lista de mensajes de cierto centro
            servicio.deleteMensaje(idMensaje);
            return ResponseEntity.ok().build();
        } catch(UsuarioNoAutorizado e){
            //CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch(MensajeNoExistente e){
            //CODE 404: El mensaje no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}