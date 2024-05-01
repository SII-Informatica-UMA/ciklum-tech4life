package TECH4LIFE.entidadesJPA.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import TECH4LIFE.entidadesJPA.dtos.GerenteNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import TECH4LIFE.entidadesJPA.excepciones.GerenteExistente;
import TECH4LIFE.entidadesJPA.excepciones.GerenteNoExistente;
import TECH4LIFE.entidadesJPA.repositories.GerenteRepository;

@Service
@Transactional
public class LogicaGerente {
    private GerenteRepository repo;
    //Devuelve lista de gerentes
    public List<Gerente> getGerentes() {
        return repo.findAll();
    }
    //Devuelve un gerente por id
    public Optional<Gerente> getGerente(Integer id) {
        return repo.findById(id);
    }

    //Modificar Gerente
    public void modificarGerente(Integer id, GerenteNuevoDTO gerente) {
        //HAY QUE MANEJAR LA EXCEPCION DE USUARIONOAUTORIZADO
        Optional<Gerente> gerenteExistente = repo.findById(id);

        if( gerenteExistente.isEmpty()){
            throw new GerenteNoExistente();
        }
       
        Gerente gerenteAModificar = gerenteExistente.get();
        gerenteAModificar.setEmpresa(gerente.getEmpresa());
        gerenteAModificar.setIdUsuario(gerente.getIdUsuario());
        if(repo.findById(gerenteAModificar.getIdUsuario()).isPresent()){
            throw new GerenteExistente();
        }
       
        repo.save(gerenteAModificar);
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
    public Gerente addGerente(GerenteNuevoDTO gerente) {
       
        if(repo.findById(gerente.getIdUsuario()).isPresent()){
            throw new GerenteExistente();
        }

        Gerente nuevoGerente = new Gerente();
        nuevoGerente.setEmpresa(gerente.getEmpresa());
        nuevoGerente.setIdUsuario(gerente.getIdUsuario());
        return repo.save(nuevoGerente);
    
    }

   




}
