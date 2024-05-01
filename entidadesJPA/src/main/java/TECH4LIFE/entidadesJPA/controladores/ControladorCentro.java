package TECH4LIFE.entidadesJPA.controladores;

import TECH4LIFE.entidadesJPA.dtos.CentroDTO;
import TECH4LIFE.entidadesJPA.dtos.CentroNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.excepciones.CentroExistente;
import TECH4LIFE.entidadesJPA.excepciones.CentroNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.UsuarioNoAutorizado;
import TECH4LIFE.entidadesJPA.servicios.LogicaCentro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
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

    // Obtener la lista de todos los centros
    @GetMapping
    public ResponseEntity<List<CentroDTO>> listaDeCentros() {
        return ResponseEntity.ok(servicio.getTodosCentros().stream()
                .map(Mapper::toCentroDTO)
                .toList());
    }

    // Obtener la lista de todos los centros asociados a un gerente
    @GetMapping("{idGerente}")
    public ResponseEntity<List<CentroDTO>> listaDeCentrosByidGerente(@PathVariable(name="idGerente")Integer idGerente) {
        return ResponseEntity.ok(servicio.getTodosCentrosByIdgerente(idGerente).stream()
                .map(Mapper::toCentroDTO)
                .toList());
    }

    // Obtener un centro por la id
    @GetMapping("{id}")
    public ResponseEntity<Centro> verCentro(@PathVariable(name="id")Integer id){
        // TO DO -> Duda: ¿Tendría que ser ResponseEntity<CentroDTO>?

        try {
            return ResponseEntity.ok(servicio.getCentroById(id));
        } catch (CentroNoExistente u) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (UsuarioNoAutorizado n) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /*
     *   DELETES
     *   -------
     */

    // Elimina un centro por la id
    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<CentroDTO>> eliminarUnidad(@PathVariable(name="id") Long id) {
        try {
            return ResponseEntity.ok(servicio.eliminarCentro(id).stream()
                    .map(Mapper::toCentroDTO)
                    .toList());
        } catch (CentroNoExistente u) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    /*
     *   POSTS
     *   -----
     */

    @PostMapping
    public ResponseEntity<CentroDTO> addCentro(@RequestBody CentroNuevoDTO centro, UriComponentsBuilder builder) {
        Centro centroEntity = Mapper.toCentro(centro);
        centroEntity.setIdCentro(null);
        try {
            CentroDTO centroDTO = servicio.postCentro(centroEntity);
            URI uri = builder
                    .path("/centro")
                    .path(String.format("/%d", centroEntity.getIdCentro()))
                    .build()
                    .toUri();
            return ResponseEntity.created(uri).build();
        } catch (CentroExistente u) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
