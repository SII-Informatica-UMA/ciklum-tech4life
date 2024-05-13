package TECH4LIFE.entidadesJPA.controladores;

import TECH4LIFE.entidadesJPA.dtos.CentroDTO;
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

@RestController
@RequestMapping("/mensaje/centro")
//cuando llegue una solicitud con esta ruta, se ejecutará algún procedimiento de aquí
public class ControladorMensaje {
    private LogicaMensaje servicioMensaje;
    private LogicaCentro servicioCentro;

    //CONSTRUCTOR DE LA CLASE CONTROLADORMENSAJE
    public ControladorMensaje(LogicaMensaje servicioM, LogicaCentro servicioC){

        this.servicioMensaje = servicioM;
        this.servicioCentro = servicioC;
    }

    //se ha cambiado el constructor añadiento servicioCentro

//------------------------------------------------------------------------------------------
/*
    GETS
 */
    //OBTENGO TODOS LOS MENSAJES DE UN CENTRO DADO EL CENTRO
   /*
    @GetMapping
    public ResponseEntity<List<MensajeDTO>> listaDeMensajes(@PathVariable (name="centro") Centro centro){   //guardo en la variable centro el objeto Centro
        try{
            //CODE 200: Devuelve la lista de mensajes de cierto centro
            List<MensajeDTO> listaMensajes = servicioMensaje.getMensajesByCentro(centro).stream().map(Mapper::toMensajeDTO).toList();
            return ResponseEntity.ok(listaMensajes);
        } catch(UsuarioNoAutorizado e){
            //CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch(MensajeNoExistente e){
            //CODE 404: El mensaje no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }*/
    /*@GetMapping
    public ResponseEntity<List<MensajeDTO>> listaDeMensajes(@RequestBody CentroDTO centroDTO, UriComponentsBuilder builder) { // Recibo el ID del centro desde la query string
        Centro centro = servicioCentro.getCentroById(centroId); // Busco el centro por ID
        if (centro == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Si no se encuentra el centro, retorno 404
        }

        try {
            // CODE 200: Devuelve la lista de mensajes de cierto centro
            List<MensajeDTO> listaMensajes = servicioMensaje.getMensajesByCentro(centro).stream()
                    .map(Mapper::toMensajeDTO)
                    .toList();
                return ResponseEntity.ok(listaMensajes);
        } catch (UsuarioNoAutorizado e) {
            // CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (MensajeNoExistente e) {
            // CODE 404: El mensaje no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

     */
public ResponseEntity<List<MensajeDTO>> listaDeMensajes(@RequestBody Centro centro) { // Recibo el ID del centro desde la query string
    if (centro == null) {
        return ResponseEntity.notFound().build(); // Devuelve un 404 si no se encuentra el centro
    }
    try {
        // CODE 200: Devuelve la lista de mensajes de cierto centro
        List<MensajeDTO> listaMensajes = servicioMensaje.getMensajesByCentro(centro);
        return ResponseEntity.ok(listaMensajes);
    } catch (UsuarioNoAutorizado e) {
        // CODE 403: Acceso no autorizado
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    } catch (MensajeNoExistente e) {
        // CODE 404: El mensaje no existe
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}



//------------------------------------------------------------------------------------------
/*
como tengo en cuenta el idCentro que le tengo que pasar? sino como sé el cetro???
 */

    //OBTENGO UN MENSAJE CONCRETO POR EL IDMENSAJE
    @GetMapping("/{idMensaje}")
    public ResponseEntity<MensajeDTO> getMensajeById(@PathVariable(name="idMensaje") Integer idMensaje) {
        try{
            //CODE 200: Devuelve la lista de mensajes de cierto centro
            MensajeDTO mensaje = Mapper.toMensajeDTO(servicioMensaje.getMensajeById(idMensaje));
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
    public ResponseEntity<MensajeDTO> crearMensaje(@RequestBody MensajeNuevoDTO mensajeNuevoDTO, UriComponentsBuilder builder) {
        try{
            //CODE 201: Se crea el mensaje y lo devuelve
            /*Mensaje mensaje  = servicioMensaje.postMensaje(Mapper.toMensaje(mensajeNuevoDTO));
            URI uri = builder

                    .path(String.format("/%d", mensaje.getIdMensaje()))
                    .path(String.format("/%d", centro.getIdCentro()))
                    .build()
                    .toUri();
            return ResponseEntity.created(uri).body(Mapper.toMensajeDTO(mensaje));*/
            Mensaje mensaje = servicioMensaje.postMensaje(Mapper.toMensaje(mensajeNuevoDTO));
            URI uri = builder.path("/mensaje/centro/{idMensaje}").buildAndExpand(mensaje.getIdMensaje()).toUri();
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
    @DeleteMapping("/{idMensaje}")
    public ResponseEntity<?> eliminarMensaje(@PathVariable(name="idMensaje") Integer idMensaje) {
        try{
            //CODE 200: Devuelve la lista de mensajes de cierto centro
            servicioMensaje.deleteMensaje(idMensaje);
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