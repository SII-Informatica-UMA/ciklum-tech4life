package TECH4LIFE.entidadesJPA.controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import TECH4LIFE.entidadesJPA.dtos.CentroDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import TECH4LIFE.entidadesJPA.excepciones.CentroNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.GerenteExistente;
import TECH4LIFE.entidadesJPA.excepciones.GerenteNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.PeticionNoValida;
import TECH4LIFE.entidadesJPA.excepciones.UsuarioNoAutorizado;
import TECH4LIFE.entidadesJPA.servicios.LogicaGerente;
import java.net.URI;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<GerenteDTO>> listaGerentes(){

        try {
            List<GerenteDTO> gerentes = servicio.getGerentes().stream().map(Mapper::toGerenteDTO).toList();
            return ResponseEntity.ok(gerentes);
        } catch (GerenteNoExistente e) {
             //No encontrado 404
             return ResponseEntity.notFound().build();
        }catch(UsuarioNoAutorizado e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    //GET Gerente {idGerente}
    @GetMapping("/{id}")
    public ResponseEntity<GerenteDTO> obtenerGerentePorId(@PathVariable(name="id") Integer id){
        try{  
            GerenteDTO gerenteDTO = Mapper.toGerenteDTO(servicio.getGerente(id).get());
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
    @PutMapping("/{id}")
    public ResponseEntity<GerenteDTO> modificarGerente(@PathVariable(name="id") Integer id, @RequestBody GerenteNuevoDTO gerente){

        try{
            servicio.modificarGerente(id, Mapper.toGerente(gerente));
           // [200] El gerente se ha actualizado
            return ResponseEntity.ok().build();
        }catch (GerenteNoExistente e){
            // [404] Gerente no existente
            return ResponseEntity.notFound().build();
        }
        // [403] Acceso no autorizado
    }
    //DELETE Gerente {idGerente}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarGerentePorId(@PathVariable(name="id") Integer id){
        try{
            servicio.eliminarGerente(id);
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
    public ResponseEntity<GerenteDTO> addGerente(@RequestBody GerenteNuevoDTO gerente, UriComponentsBuilder builder ){

        try{
            Gerente nuevoGerente = servicio.addGerente(Mapper.toGerente(gerente));
            URI uri= builder
                .path(String.format("/%d",nuevoGerente.getId()))
                .build()
                .toUri() ;
                //Todo bien 200
            return ResponseEntity.created(uri).body(Mapper.toGerenteDTO(nuevoGerente));  
        }catch (GerenteNoExistente e){
            // [404] Not Found
            return ResponseEntity.notFound().build();
       }catch(GerenteExistente e){
            // [403] Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
       }


    }


}