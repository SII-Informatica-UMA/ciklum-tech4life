package TECH4LIFE.entidadesJPA.servicios;

import java.util.List;
import java.util.Optional;

import TECH4LIFE.entidadesJPA.repositories.CentroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import TECH4LIFE.entidadesJPA.dtos.GerenteDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import TECH4LIFE.entidadesJPA.excepciones.GerenteExistente;
import TECH4LIFE.entidadesJPA.excepciones.GerenteNoExistente;
import TECH4LIFE.entidadesJPA.repositories.GerenteRepository;

@Service
@Transactional
public class LogicaGerente {
    private GerenteRepository repo;
    private CentroRepository crep;
    //Devuelve lista de gerentes
    public List<Gerente> getGerentes() {
        return repo.findAll();  
    }
    //Devuelve un gerente por id
    public Optional<Gerente> getGerente(Integer id) {
        return repo.findById(id);
    }

    //Modificar Gerente
    public void modificarGerente(Integer id, Gerente gerente) {
        //HAY QUE MANEJAR LA EXCEPCION DE USUARIONOAUTORIZADO
        Optional<Gerente> gerenteExistente = repo.findById(id);

        if( gerenteExistente.isEmpty()){
            throw new GerenteNoExistente();
        }
       
        gerente.setIdUsuario(id);
        repo.save(gerente);
    }

    //Eliminar un gerente
    public void eliminarGerente(Integer id) {
        //HAY QUE MANEJAR LA EXCEPCION DE USUARIONOAUTORIZADO
        Optional<Gerente> gerenteOptional = repo.findById(id);
        if(gerenteOptional.isPresent()){
            repo.deleteById(id);
        }else{
            throw new GerenteNoExistente();
        }
    }

    //Añadir un Gerente
    public Gerente addGerente(Gerente gerente) {
        
        if(repo.findById(gerente.getIdUsuario()).isPresent()){
            throw new GerenteExistente();
        }else if(gerente.isEmpty()){
            throw new GerenteNoExistente();
        }
        return repo.save(gerente);
    }


}
