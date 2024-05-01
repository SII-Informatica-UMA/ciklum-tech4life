package TECH4LIFE.entidadesJPA.servicios;

import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.excepciones.*;
import TECH4LIFE.entidadesJPA.repositories.CentroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LogicaCentro {

   private CentroRepository centroRepo ;

   @Autowired
    public LogicaCentro(CentroRepository centroRepo) {
       this.centroRepo = centroRepo;
   }

    /*
     *   GETS
     *   ----
     */

    // Obtener la lista de todos los centros
    public List<Centro> getTodosCentros() {
        // TO DO -> DUDA: ¿Cómo sé si no hay centros? -> imagino que será algo relacionado con le findAll
        // TO DO -> DUDA: ¿Cómo sé si el usuario está autorizado o no?
        // TO DO -> DUDA: ¿Cómo sé si hay BadRequest?

        if (centroRepo.findAll().isEmpty()){
            throw new CentroNoExistente();
        } else if (usuario no autorizado){
            throw new UsuarioNoAutorizado();
        } else if (bad request){
            throw new PeticionNoValida();
        }else {
            return centroRepo.findAll();
        }
    }

    // Obtener un centro por la id
    public Centro getCentroById(Integer id) throws CentroNoExistente, UsuarioNoAutorizado {
        // TO DO -> DUDA: ¿Cómo sé si el usuario está autorizado o no?
        // TO DO -> DUDA: ¿Cómo sé si hay BadRequest?

        return centroRepo.findById(id).get();
        if(usuario autorizado){
        if (centroRepo.findById(id).isPresent()){
            return centroRepo.findById(id).get();
        } else {
            throw new CentroNoExistente();
        }
        }
    }

    /*
     *   DELETES
     *   -------
     */

    public List<Centro> eliminarCentro(Integer id) throws CentroNoExistente, UsuarioNoAutorizado {
        // TO DO -> DUDA: ¿Cómo sé si el usuario está autorizado o no?

        // TO DO -> DUDA: ¿Cómo sé si hay BadRequest?

        if (centroRepo.existsById(id)){
            centroRepo.deleteById(id);
            return getTodosCentros();
        } else {
            throw new CentroNoExistente() ;
        }
    }

    /*
     *   POSTS
     *   -----
     */

    public CentroDTO postCentro(Centro centroEntity) throws CentroExistente {

        // TO DO

        if (){
            throw new CentroExistente() ;
        }

        Integer idCentro = centroEntity.getIdCentro();
        centroEntity.setIdCentro(idCentro);
        centroRepo.save(centroEntity) ;
        CentroDTO centro = Mapper.toCentroDTO(centroEntity) ;
        return centro ;
    }

    /*
     *   PUTS
     *   ----
     */

    public Centro modificarCentro(Centro centro) throws CentroNoExistente , CentroExistente{
        if (centroRepo.existsById(centro.getIdCentro())) {
            centroRepo.save(centro);
            if (){
                throw new CentroExistente() ;
            } else {
                return centro;
            }
        } else {
            throw new CentroNoExistente();
        }
    }
}
