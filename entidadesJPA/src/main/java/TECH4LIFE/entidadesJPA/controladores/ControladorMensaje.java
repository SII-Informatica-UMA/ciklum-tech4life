package TECH4LIFE.entidadesJPA.controladores;

import TECH4LIFE.entidadesJPA.dtos.MensajeDTO;
import TECH4LIFE.entidadesJPA.dtos.MensajeNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Mensaje;
import TECH4LIFE.entidadesJPA.excepciones.MensajeNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.PeticionNoValida;
import TECH4LIFE.entidadesJPA.excepciones.UsuarioNoAutorizado;
import TECH4LIFE.entidadesJPA.servicios.LogicaMensaje;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/mensaje")
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
    @GetMapping("/{centro}")    //capturo el objeto centro.
    // DUDA: En PathVariable hay que poner (name="idMensaje")??
    public ResponseEntity<List<MensajeDTO>> listaDeMensajes(@PathVariable Centro centro){   //guardo en la variable centro el objeto Centro
        try{
            //CODE 200: Devuelve la lista de mensajes de cierto centro
            List<MensajeDTO> listaMensajes = servicio.getMensajesByCentro(centro);
            return ResponseEntity.ok(listaMensajes);
        } catch(UsuarioNoAutorizado e){
            //CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch(MensajeNoExistente e){
            //CODE 404: El mensaje no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    /*
    DEL MÉTODO ANTERIOR FALTA RESOLVER LA DUDA EN GETMENSAJESBYCENTRO
     */
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
    @PostMapping("/{centro}")
    public ResponseEntity<MensajeDTO> crearMensaje(@PathVariable Centro centro,
                                                   @RequestBody MensajeDTO mensajeDTO,
                                                   UriComponentsBuilder uriBuilder) {

        Mensaje mensaje = Mapper.toMensajeDTO(Mensaje.class);   --revisar
        mensaje.setIdMensaje(null);
        mensaje.setRemitente(idCentro);

        Integer idMensaje = servicio.crearMensaje(mensaje);

        URI uri = uriBuilder
                .path("/centros/{idCentro}/mensajes/{idMensaje}")
                .buildAndExpand(idCentro, idMensaje)
                .toUri();

        return ResponseEntity.created(uri).body(Mapper(mensaje, MensajeDTO.class));
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
            Mensaje mensajeEliminado = servicio.eliminarMensaje(idMensaje);
            return ResponseEntity.ok(mensajeEliminado);
        } catch(UsuarioNoAutorizado e){
            //CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch(MensajeNoExistente e){
            //CODE 404: El mensaje no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}