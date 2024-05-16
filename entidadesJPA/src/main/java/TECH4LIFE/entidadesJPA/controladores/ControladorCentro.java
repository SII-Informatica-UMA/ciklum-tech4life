package TECH4LIFE.entidadesJPA.controladores;

import TECH4LIFE.entidadesJPA.dtos.*;
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
import TECH4LIFE.entidadesJPA.security.JwtUtil ;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/centro")
//@Tag(name="Gestión de centros", description="Operaciones para la gestión de centros")
public class ControladorCentro {

    // TO DO: Volver hacer repaso de la api revisando los métodos

    private final LogicaCentro centroService;

    public ControladorCentro(LogicaCentro centroService) {
        this.centroService = centroService ;
    }

    // A continuación los gets, posts, puts y deletes que tenga Centro

    /*
    *   GETS
    *   ----
    */

    // Obtener un centro concreto (por la id) (CREO que lo hace un usuario Administrador)
    @GetMapping("/{idCentro}")
    public ResponseEntity<CentroDTO> verCentro(@PathVariable Integer idCentro){

        try {
            // CODE 200: El centro existe
            CentroDTO centroDTO = Mapper.toCentroDTO(centroService.getCentroById(idCentro)) ;
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
    public ResponseEntity<GerenteDTO> verGerenteCentro(@PathVariable Integer idCentro){
        try {
            // CODE 200: Devuelve el gerente del centro especificado
            GerenteDTO gerenteDTO = Mapper.toGerenteDTO(centroService.getGerenteCentroById(idCentro));
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
    @GetMapping()
    public ResponseEntity<List<CentroDTO>> listaDeCentros(@RequestParam(value = "gerente", required = false) Integer gerente) {

        try {
            // CODE 200: Devuelve la lista de centros
            List<CentroDTO> listaCentrosDTO =
                    centroService.getTodosCentros(gerente).stream().map(Mapper::toCentroDTO)
                            .collect(Collectors.toList());
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
    public ResponseEntity<?> eliminarCentro(@PathVariable Integer idCentro) {
        try {
            //No estoy seguro si las siguientes líneas de código tendrían que ir aquí situadas,
            // ya que esto se repite en la mayoría de métodos

            // Petición HTTP al microservicio del profe para obtener el token del usuario logueado.
            // String token = null;

            // Llamada al método getUsernameFromToken de JwtUtil para obtener el nombre del usuario dado el token.
            // JwtUtil jwtUtil = new JwtUtil();
            // String nombreUsuario = jwtUtil.getUsernameFromToken(token) ;

            // Petición HTTP al microservicio del profe para obtener el valor del boolean admin dado un nombre de usuario
            //boolean admin = true;

            // CODE 200: El centro se ha eliminado
            centroService.eliminarCentro(idCentro) ;
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
    @DeleteMapping("/{idCentro}/gerente")
    public ResponseEntity<?> eliminarGerenteCentro(@PathVariable Integer idCentro, @RequestParam(value= "gerente", required = false) Integer gerente) {
        try {
            // CODE 200: Devuelve el centro al que se ha asociado el gerente (CREO QUE ES ERRATA DE LA API)
            centroService.eliminarGerenteCentroById(idCentro,gerente) ;
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
    // Duda: ¿Cómo que crea un centro y lo DEVUELVE? ¿Cómo lo devuelvo?
    @PostMapping()
    public ResponseEntity<CentroDTO> addCentro(@RequestBody CentroNuevoDTO centroNuevoDTO, UriComponentsBuilder builder) {

        try {

            // CODE 201: Se crea el centro y lo devuelve
            Centro centro = centroService.postCentro(Mapper.toCentro(centroNuevoDTO)) ;
            URI uri = builder
                    .path("/centro")
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

        } /*catch (CentroExistente e) {
            // CODE 404: El centro no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }*/
    }

    /*
     *   PUTS
     *   ----
     */


    // Actualiza un centro (por la id) (CREO que lo hace un usuario Gerente me parece)
    // Duda: El profe no devuelve un ResponseEntity sino un CentroDTO
    @PutMapping("/{idCentro}")
    public ResponseEntity<CentroDTO> editarCentro(@PathVariable Integer idCentro, @RequestBody CentroDTO centroDTO) {

        try {
            //No estoy seguro si las siguientes líneas de código tendrían que ir aquí situadas,
            // ya que esto se repite en la mayoría de métodos

            // Petición HTTP al microservicio del profe para obtener el token del usuario logueado.
            // String token = null;

            // Llamada al método getUsernameFromToken de JwtUtil para obtener el nombre del usuario dado el token.
            // JwtUtil jwtUtil = new JwtUtil();
            // String nombreUsuario = jwtUtil.getUsernameFromToken(token) ;

            // Ahora tendríamos que consultar la lista de todos los nombres de usuario de los gerentes
            // y ver si hay alguno que se llame como nombreUsuario. Si hay alguno, entonces
            // esta autorizado a llamar a este método

            // CODE 200: El centro se ha actualizado
            centroService.modificarCentro(idCentro, Mapper.toCentro(centroDTO)) ;
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
    public ResponseEntity<CentroDTO> editarGerenteCentro (@PathVariable Integer idCentro, @RequestBody IdGerenteDTO idgerenteDTO) {
        try {
            // CODE 200: Devuelve el centro al que se ha asociado el gerente
            CentroDTO centroDTO = Mapper.toCentroDTO(centroService.modificarGerenteCentroById(idCentro,Mapper.toGerente(idgerenteDTO)));
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
