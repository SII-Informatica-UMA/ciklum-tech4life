package TECH4LIFE.entidadesJPA.controladores;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import TECH4LIFE.entidadesJPA.dtos.GerenteDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import TECH4LIFE.entidadesJPA.excepciones.GerenteNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.UsuarioNoAutorizado;
import TECH4LIFE.entidadesJPA.servicios.LogicaGerente;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/gerente")
public class ControladorGerente {
    
    private LogicaGerente servicio;


    //Constructor
    public ControladorGerente(LogicaGerente servicioGerente){
        this.servicio = servicioGerente;
    }

    //GET Lista Gerentes
    @GetMapping
    public ResponseEntity<List<GerenteDTO>> listaGerentes(@RequestHeader("Authorization") String authHeader){

        try {
            List<GerenteDTO> gerentes = servicio.getGerentes(authHeader).stream().map(Mapper::toGerenteDTO).toList();
            return ResponseEntity.ok(gerentes);
        } catch (GerenteNoExistente e) {
             //No encontrado 404
             return ResponseEntity.notFound().build();
        }catch(UsuarioNoAutorizado e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    //GET Gerente {idGerente}
    @GetMapping("/{idGerente}")
    public ResponseEntity<GerenteDTO> obtenerGerentePorId(@PathVariable(name="idGerente") Integer idGerente,@RequestHeader("Authorization") String authHeader){
        try{  
            GerenteDTO gerenteDTO = Mapper.toGerenteDTO(servicio.getGerente(idGerente,authHeader));
                //Todo bien 200
                return ResponseEntity.ok(gerenteDTO);
        }catch(UsuarioNoAutorizado e){
            //No autorizado 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch(GerenteNoExistente e){
            //No encontrado 404
            return ResponseEntity.notFound().build();
        }
    }

    
    //PUT Gerente {idGerente}
    @PutMapping("/{idGerente}")
    public ResponseEntity<GerenteDTO> modificarGerente(@PathVariable(name="idGerente") Integer idGerente, @RequestBody GerenteDTO gerente,@RequestHeader("Authorization") String authHeader){

        try{
            servicio.modificarGerente(idGerente, Mapper.toGerente(gerente),authHeader);
           // [200] El gerente se ha actualizado
            return ResponseEntity.ok().build();
        }catch (GerenteNoExistente e){
            // [404] Gerente no existente
            return ResponseEntity.notFound().build();
        }catch(UsuarioNoAutorizado e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    //DELETE Gerente {idGerente}
    @DeleteMapping("/{idGerente}")
    public ResponseEntity<?> eliminarGerentePorId(@PathVariable(name="idGerente") Integer idGerente,@RequestHeader("Authorization") String authHeader){
        try{
            servicio.eliminarGerente(idGerente,authHeader);
             // [200] El gerente se ha borrado con éxito
            return ResponseEntity.ok().build();
        }catch (GerenteNoExistente e){
             // [404] Gerente no existente
             return ResponseEntity.notFound().build();
        }catch(UsuarioNoAutorizado e){
             // [403] Acceso no autorizado
             return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        
        }
    }

    //POST Lista Gerentes
     
    @PostMapping
    public ResponseEntity<GerenteDTO> addGerente(@RequestBody GerenteNuevoDTO gerente, UriComponentsBuilder builder,@RequestHeader("Authorization") String authHeader ){

        try{
            Gerente nuevoGerente = servicio.addGerente(Mapper.toGerente(gerente),authHeader);
            URI uri= builder
                .path(String.format("/%d",nuevoGerente.getId()))
                .build()
                .toUri();
                //Todo bien 201
            return ResponseEntity.created(uri).body(Mapper.toGerenteDTO(nuevoGerente));  
        }catch(UsuarioNoAutorizado e){
            // [403] Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
       }


    }


}