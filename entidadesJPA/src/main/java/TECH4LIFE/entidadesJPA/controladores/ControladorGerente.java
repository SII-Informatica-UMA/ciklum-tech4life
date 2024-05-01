package TECH4LIFE.entidadesJPA.controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import TECH4LIFE.entidadesJPA.dtos.GerenteDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import TECH4LIFE.entidadesJPA.excepciones.GerenteExistente;
import TECH4LIFE.entidadesJPA.excepciones.GerenteNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.UsuarioNoAutorizado;
import TECH4LIFE.entidadesJPA.servicios.LogicaGerente;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class ControladorGerente {
    
    private LogicaGerente servicio;

    public ControladorGerente(LogicaGerente servicioGerente){
        servicio = servicioGerente;
    }

    //GET Lista Gerentes
    @GetMapping
    public ResponseEntity<List<GerenteDTO>> listaGerentes(){

        return ResponseEntity.ok(servicio.getGerentes().stream().map(Mapper::toGerenteDTO).toList());
    }

    //GET Gerente {idGerente}
    @GetMapping("id")
    public ResponseEntity<?> obtenerGerentePorId(@PathVariable(name="id") Integer id){
        try{
            Optional<Gerente> gerenteOptional = servicio.getGerente(id);
            if(gerenteOptional.isPresent()){ 
            
                GerenteDTO gerenteDTO = Mapper.toGerenteDTO(gerenteOptional.get());
                //Todo bien 200
                return ResponseEntity.ok(gerenteDTO);
            } else {
                //No encontrado 404
                return ResponseEntity.notFound().build();
            }
        }catch(UsuarioNoAutorizado e){
            //No autorizado 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
           
    }

    
    //PUT Gerente {idGerente}
    @PutMapping("{id}")
    public ResponseEntity modificarGerente(@PathVariable(name="id") Integer id, @RequestBody GerenteNuevoDTO gerente){

        try{
            servicio.modificarGerente(id, gerente);
           // [200] El gerente se ha actualizado
            return ResponseEntity.ok().build();
        }catch (GerenteNoExistente e){
            // [404] Gerente no existente
            return ResponseEntity.notFound().build();
        }catch (GerenteExistente e){
            // [403] Acceso no autorizado
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    //DELETE Gerente {idGerente}
    @DeleteMapping("{id}")
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
            Gerente nuevoGerente = servicio.addGerente(gerente);
            URI uri= builder
                .path("/gerente")
                .path(String.format("/%d", nuevoGerente.getId()))
                .build()
                .toUri();
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