package TECH4LIFE.entidadesJPA.servicios;

import java.util.List;
import java.util.Optional;

import TECH4LIFE.entidadesJPA.repositories.CentroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import TECH4LIFE.entidadesJPA.dtos.GerenteDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import TECH4LIFE.entidadesJPA.excepciones.CentroNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.GerenteExistente;
import TECH4LIFE.entidadesJPA.excepciones.GerenteNoExistente;
import TECH4LIFE.entidadesJPA.repositories.GerenteRepository;

@Service
@Transactional
public class LogicaGerente {
    private GerenteRepository repo;

    public LogicaGerente(GerenteRepository gerenterepo){
        this.repo = gerenterepo;
    }

    //Devuelve lista de gerentes
    public List<Gerente> getGerentes() {
        
        List<Gerente> gerentes = repo.findAll() ;
        if (gerentes.isEmpty()){
            throw new GerenteNoExistente();
        } 
        return gerentes;
    }
    //Devuelve un gerente por id
    public Optional<Gerente> getGerente(Integer id) {
        if(id==null){
            throw new GerenteNoExistente();
        }
        Optional<Gerente> gerente = repo.findById(id);
        if(gerente.isEmpty()){
            throw new GerenteNoExistente();
        }
        return gerente;
    }

    //Modificar Gerente
    public void modificarGerente(Integer id, Gerente gerente) {
        //HAY QUE MANEJAR LA EXCEPCION DE USUARIONOAUTORIZADO
        Optional<Gerente> gerenteExistente = repo.findById(id);

        if( gerenteExistente.isEmpty()){
            throw new GerenteNoExistente();
        }
       
        gerente.setId(id);;
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
        
        if(gerente==null){
            throw new GerenteNoExistente();
        }
        Optional<Gerente> gerentecheck = repo.findById(gerente.getId());
        if(gerentecheck.isPresent()){
            throw new GerenteExistente();
        }
        return repo.save(gerente);
    }


}
