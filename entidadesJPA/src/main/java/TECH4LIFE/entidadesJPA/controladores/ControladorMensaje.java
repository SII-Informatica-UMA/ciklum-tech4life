package TECH4LIFE.entidadesJPA.controladores;

import TECH4LIFE.entidadesJPA.dtos.MensajeDTO;
import TECH4LIFE.entidadesJPA.dtos.MensajeNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Mensaje;
import TECH4LIFE.entidadesJPA.excepciones.MensajeNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.UsuarioNoAutorizado;
import TECH4LIFE.entidadesJPA.servicios.LogicaCentro;
import TECH4LIFE.entidadesJPA.servicios.LogicaMensaje;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mensaje/centro")
//cuando llegue una solicitud con esta ruta, se ejecutará algún procedimiento de aquí
public class ControladorMensaje {
    private LogicaMensaje servicioM;
    private LogicaCentro servicioC;

    //CONSTRUCTOR DE LA CLASE CONTROLADORMENSAJE
    public ControladorMensaje(LogicaMensaje servicioMensaje, LogicaCentro servicioCentro){

        this.servicioM = servicioMensaje;
        this.servicioC = servicioCentro;
    }

//------------------------------------------------------------------------------------------
/*
    GETS
 */
    //OBTENGO TODOS LOS MENSAJES DE UN CENTRO DADO EL CENTRO
    @GetMapping()    //capturo el objeto centro.
    public ResponseEntity<List<MensajeDTO>> listaDeMensajes(@RequestParam(value = "centro", required = false) Centro centro){   //guardo en la variable centro el objeto Centro
        try{
            //CODE 200: Devuelve la lista de mensajes de cierto centro
            List<MensajeDTO> listaMensajes =
                    servicioM.getMensajesByCentro(centro).stream().map(Mapper::toMensajeDTO)
                    .collect(Collectors.toList());
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
            MensajeDTO mensaje = Mapper.toMensajeDTO(servicioM.getMensajeById(idMensaje));
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
    @PostMapping
    public ResponseEntity<MensajeDTO> crearMensaje(@RequestParam(value = "centro", required = false) Centro centro, @RequestBody MensajeNuevoDTO mensajeNuevoDTO, UriComponentsBuilder builder) {
        try{
            //CODE 201: Se crea el mensaje y lo devuelve
            Mensaje mensaje  = servicioM.postMensaje(Mapper.toMensaje(mensajeNuevoDTO));
            URI uri = builder
                    .path(String.format("/%d", centro.getIdCentro()))
                    .path(String.format("/%d", mensaje.getIdMensaje()))
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
            servicioM.deleteMensaje(idMensaje);
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