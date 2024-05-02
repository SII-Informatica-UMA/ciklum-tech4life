package TECH4LIFE.entidadesJPA.controladores;

import TECH4LIFE.entidadesJPA.dtos.CentroDTO;
import TECH4LIFE.entidadesJPA.dtos.CentroNuevoDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.excepciones.CentroExistente;
import TECH4LIFE.entidadesJPA.excepciones.CentroNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.PeticionNoValida;
import TECH4LIFE.entidadesJPA.excepciones.UsuarioNoAutorizado;
import TECH4LIFE.entidadesJPA.servicios.LogicaCentro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/centro")
public class ControladorCentro {

    private LogicaCentro servicio ;

    public ControladorCentro(LogicaCentro servicioCentro) {
        this.servicio = servicioCentro ;
    }

    // A continuación los gets, posts, puts y deletes que tenga Centro

    /*
    *   GETS
    *   ----
    */

    // Obtener un centro concreto (por la id) (CREO que lo hace un usuario Administrador)
    @GetMapping("/{idCentro}")
    public ResponseEntity<CentroDTO> verCentro(@PathVariable(name="idCentro")Integer id){

        try {
            // CODE 200: El centro existe
            CentroDTO centroDTO = Mapper.toCentroDTO(servicio.getCentroById(id)) ;
            return ResponseEntity.ok(centroDTO);

        } catch (PeticionNoValida e) {
            // CODE 400: Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }catch (UsuarioNoAutorizado e) {
            // CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (CentroNoExistente e) {
            // CODE 404: El centro no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Permite consultar el gerente de un centro (por la id del centro) (CREO que lo hace un usuario Administrador)
    @GetMapping("/{idCentro}/gerente")
    public ResponseEntity<GerenteDTO> verGerenteCentro(@PathVariable(name="idCentro")Integer id){
        try {
            // CODE 200: Devuelve el gerente del centro especificado
            GerenteDTO gerenteDTO = Mapper.toGerenteDTO(servicio.getGerenteCentroById(id));
            return ResponseEntity.ok(gerenteDTO);

        } catch (PeticionNoValida e) {
            // CODE 400: Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }catch (UsuarioNoAutorizado e) {
            // CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (CentroNoExistente e) {
            // CODE 404: El centro no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Obtener la lista de todos los centros (Lo puede hacer usuario Administrador o Gerente)
    @GetMapping
    public ResponseEntity<List<CentroDTO>> listaDeCentros() {

        try {
            // CODE 200: Devuelve la lista de centros
            List<CentroDTO> listaCentrosDTO = servicio.getTodosCentros().stream().map(Mapper::toCentroDTO).toList();
            return ResponseEntity.ok(listaCentrosDTO);

        } catch (PeticionNoValida e) {
            // CODE 400: Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }catch (UsuarioNoAutorizado e) {
            // CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (CentroNoExistente e) {
            // CODE 404: El centro no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Obtener la lista de todos los centros asociados a un gerente ESTE MÉTODO NO SE ESPECIFICA EN OPENAPI
    // TO DO -> SEGÚN LA TUTORÍA UN GERENTE NO PUEDE TENER ASOCIADO MÁS DE UN CENTRO

    /*
     *   DELETES
     *   -------
     */

    // Elimina un centro (por la id) (CREO que lo hace un usuario Administrador)
    @DeleteMapping("/{idCentro}")
    public ResponseEntity<?> eliminarCentro(@PathVariable(name="idCentro") Integer id) {
        try {
            // CODE 200: El centro se ha eliminado
            servicio.eliminarCentro(id) ;
            return ResponseEntity.ok().build();

        } catch (PeticionNoValida e) {
            // CODE 400: Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }catch (UsuarioNoAutorizado e) {
            // CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (CentroNoExistente e) {
            // CODE 404: El centro no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Permite eliminar una asociación entre un centro y un gerente. (CREO que lo hace un usuario Administrador)
    public ResponseEntity<?> eliminarGerenteCentro(@PathVariable(name="idCentro")Integer id) {
        try {
            // CODE 200: Devuelve el centro al que se ha asociado el gerente (CREO QUE ES ERRATA DE LA API)
            servicio.eliminarGerenteCentroById(id) ;
            return ResponseEntity.ok().build();

        } catch (PeticionNoValida e) {
            // CODE 400: Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }catch (UsuarioNoAutorizado e) {
            // CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (CentroNoExistente e) {
            // CODE 404: El centro no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /*
     *   POSTS
     *   -----
     */

    // Permite crear un centro nuevo a un administrador (Lo hace un usuario Administrador)
    @PostMapping
    public ResponseEntity<CentroDTO> addCentro(@RequestBody CentroNuevoDTO centroNuevoDTO, UriComponentsBuilder builder) {

        try {

            // CODE 201: Se crea el centro y lo devuelve
            Centro centro = servicio.postCentro(Mapper.toCentro(centroNuevoDTO)) ;
            URI uri = builder
                    .path(String.format("/%d",centro.getIdCentro()))
                    .build()
                    .toUri() ;
            return ResponseEntity.created(uri).body(Mapper.toCentroDTO(centro)) ;

        } catch (PeticionNoValida e) {
            // CODE 400: Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }catch (UsuarioNoAutorizado e) {
            // CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (CentroExistente e) {
            // CODE 404: El centro no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /*
     *   PUTS
     *   ----
     */

    // Actualiza un centro (por la id) (CREO que lo hace un usuario Administrador)
    @PutMapping("/{idCentro}")
    public ResponseEntity<CentroDTO> editarCentro(@PathVariable(name="idCentro")Integer id, @RequestBody CentroNuevoDTO centroNuevoDTO) {

        try {

            // CODE 200: El centro se ha actualizado
            servicio.modificarCentro(id, Mapper.toCentro(centroNuevoDTO)) ;
            return ResponseEntity.ok().build();

        } catch (PeticionNoValida e) {
            // CODE 400: Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }catch (UsuarioNoAutorizado e) {
            // CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (CentroNoExistente e) {
            // CODE 404: El centro no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Permite añadir una asociación entre un centro y un gerente. (CREO que lo hace un usuario Administrador)
    @PutMapping("/{idCentro}/gerente")
    public ResponseEntity<CentroDTO> editarGerenteCentro (@PathVariable(name="idCentro")Integer id, @RequestBody GerenteNuevoDTO gerenteNuevoDTO) {
        try {
            // CODE 200: Devuelve el centro al que se ha asociado el gerente

            CentroDTO centroDTO = Mapper.toCentroDTO(servicio.modificarGerenteCentroById(id,Mapper.toGerente(gerenteNuevoDTO)));
            return ResponseEntity.ok(centroDTO);

        } catch (PeticionNoValida e) {
            // CODE 400: Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }catch (UsuarioNoAutorizado e) {
            // CODE 403: Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (CentroNoExistente e) {
            // CODE 404: El centro no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
