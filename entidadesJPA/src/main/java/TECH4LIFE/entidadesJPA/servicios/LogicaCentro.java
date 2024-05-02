package TECH4LIFE.entidadesJPA.servicios;

import TECH4LIFE.entidadesJPA.controladores.Mapper;
import TECH4LIFE.entidadesJPA.dtos.CentroDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import TECH4LIFE.entidadesJPA.excepciones.*;
import TECH4LIFE.entidadesJPA.repositories.CentroRepository;
import jakarta.annotation.security.RolesAllowed;
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

    // Obtener un centro concreto (por la id) (CREO que lo hace un usuario Administrador)
    // TO DO -> DUDA: ¿Cómo sé si el usuario está autorizado o no?
    @RolesAllowed("Administrador")
    public Centro getCentroById(Integer id) throws PeticionNoValida, UsuarioNoAutorizado, CentroNoExistente {

        if (id == null || id < 0) throw new PeticionNoValida();

        Optional<Centro> centro = centroRepo.findById(id);

        if (centro.isEmpty()) throw new CentroNoExistente();

        return centro.get();
    }

    // Permite consultar el gerente de un centro (por la id del centro) (CREO que lo hace un usuario Administrador)
    // TO DO -> DUDA: ¿Cómo sé si el usuario está autorizado o no?
    @RolesAllowed("Administrador")
    public Gerente getGerenteCentroById(Integer id) throws PeticionNoValida, UsuarioNoAutorizado, CentroNoExistente {

        if (id == null || id < 0) throw new PeticionNoValida();

        Gerente gerente = centroRepo.FindGerenteByCentro(id) ;

        if (gerente == null) throw new CentroNoExistente();

        return gerente;
    }

    // Obtener la lista de todos los centros (Lo puede hacer usuario Administrador o Gerente)
    // TO DO -> DUDA: ¿Cómo sé si el usuario está autorizado o no?
    @RolesAllowed({"Administrador","Gerente"})
    public List<Centro> getTodosCentros() throws PeticionNoValida, UsuarioNoAutorizado, CentroNoExistente {

        // NO HAY PARÁMETRO DE ENTRADA PARA QUE PUDIERA SER UNA PETICIÓN NO VÁLIDA
        //if (id == null || id < 0) throw new PeticionNoValida();

        List<Centro> centros = centroRepo.findAll() ;

        if (centros.isEmpty()) throw new CentroNoExistente();

        return centros;
    }

    // Obtener la lista de todos los centros asociados a un gerente ESTE MÉTODO NO SE ESPECIFICA EN OPENAPI
    // TO DO -> SEGÚN LA TUTORÍA UN GERENTE NO PUEDE TENER ASOCIADO MÁS DE UN CENTRO

    /*
     *   DELETES
     *   -------
     */

    // Elimina un centro (por la id) (CREO que lo hace un usuario Administrador)
    // TO DO -> DUDA: ¿Cómo sé si el usuario está autorizado o no?
    @RolesAllowed("Administrador")
    public void eliminarCentro(Integer id) throws PeticionNoValida, UsuarioNoAutorizado, CentroNoExistente {

        if (id == null || id < 0) throw new PeticionNoValida();

        Optional<Centro> centro = centroRepo.findById(id);

        if (centro.isEmpty()) throw new CentroNoExistente();

        centroRepo.deleteById(id);
    }

    // Permite eliminar una asociación entre un centro y un gerente. (CREO que lo hace un usuario Administrador)
    // TO DO -> DUDA: ¿Cómo sé si el usuario está autorizado o no?
    // TO DO -> El final del método no estoy seguro si es correcto
    @RolesAllowed("Administrador")
    public void eliminarGerenteCentroById(Integer id) throws PeticionNoValida, UsuarioNoAutorizado, CentroNoExistente {

        if (id == null || id < 0) throw new PeticionNoValida();

        Optional<Centro> centro = centroRepo.findById(id);

        if (centro.isEmpty()) throw new CentroNoExistente();

        // NO ESTOY SEGURO SI ESTO ES CORRECTO
        centro.get().setGerente(null); // Elimino asociación (poniendo a null el atributo gerente de Centro)
        centroRepo.save(centro.get()); // Guardo
    }

    /*
     *   POSTS
     *   -----
     */

    // Permite crear un centro nuevo a un administrador (Lo hace un usuario Administrador)
    // TO DO -> DUDA: ¿Cómo sé si el usuario está autorizado o no?
    @RolesAllowed("Administrador")
    public Centro postCentro(Centro centroEntity) throws PeticionNoValida, UsuarioNoAutorizado, CentroExistente {

        if (centroEntity == null) throw new PeticionNoValida();

        Optional<Centro> centro = centroRepo.findById(centroEntity.getIdCentro());

        if (centro.isPresent()) throw new CentroExistente();

        return centroRepo.save(centroEntity);
    }

    /*
     *   PUTS
     *   ----
     */

    // Actualiza un centro (por la id) (CREO que lo hace un usuario Administrador)
    // TO DO -> DUDA: ¿Cómo sé si el usuario está autorizado o no?
    // TO DO -> El final del método no estoy seguro si es correcto
    // TO DO -> ¿Necesaria la excepción que señalo?
    @RolesAllowed("Administrador")
    public void modificarCentro(Integer id, Centro centroEntity) throws PeticionNoValida, UsuarioNoAutorizado, CentroNoExistente {

        if (id == null || id < 0 || centroEntity == null) throw new PeticionNoValida();

        Optional<Centro> centro = centroRepo.findById(id);

        if (centro.isEmpty()) throw new CentroNoExistente();

        // NO ESTOY SEGURO SI ESTO ES CORRECTO
        Centro centroAmodificar = centro.get() ;
        centroAmodificar.setIdCentro(centroEntity.getIdCentro());
        centroAmodificar.setNombre(centroEntity.getNombre());
        centroAmodificar.setDireccion(centroEntity.getDireccion());
        centroAmodificar.setGerente(centroEntity.getGerente());

        // ¿Necesaria esta excepción?
        //if (centroRepo.findById(centroAmodificar.getIdCentro()).isPresent()) throw new CentroExistente();

        centroRepo.save(centroAmodificar) ;
    }

    // Permite añadir una asociación entre un centro y un gerente. (CREO que lo hace un usuario Administrador)
    // TO DO -> DUDA: ¿Cómo sé si el usuario está autorizado o no?
    // TO DO -> El final del método no estoy seguro si es correcto
    // TO DO -> ¿Necesaria la excepción que señalo?
    @RolesAllowed("Administrador")
    public Centro modificarGerenteCentroById (Integer id, Gerente gerenteEntity) throws PeticionNoValida, UsuarioNoAutorizado, CentroNoExistente {

        if (id == null || id < 0 || gerenteEntity == null) throw new PeticionNoValida();

        Optional<Centro> centro = centroRepo.findById(id);

        if (centro.isEmpty()) throw new CentroNoExistente();

        // NO ESTOY SEGURO SI ESTO ES CORRECTO
        Centro centroAmodificar = centro.get() ;
        centroAmodificar.setGerente(gerenteEntity);

        // ¿Necesaria esta excepción?
        //if (centroRepo.findById(centroAmodificar.getIdCentro()).isPresent()) throw new CentroExistente();

        centroRepo.save(centroAmodificar) ;
        return centroAmodificar;
    }

}
