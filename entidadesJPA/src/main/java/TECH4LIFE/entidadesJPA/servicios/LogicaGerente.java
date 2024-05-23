package TECH4LIFE.entidadesJPA.servicios;

import java.util.List;
import java.util.Optional;

import TECH4LIFE.entidadesJPA.repositories.CentroRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import TECH4LIFE.entidadesJPA.dtos.GerenteDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import TECH4LIFE.entidadesJPA.excepciones.CentroNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.GerenteExistente;
import TECH4LIFE.entidadesJPA.excepciones.GerenteNoExistente;
import TECH4LIFE.entidadesJPA.excepciones.UsuarioNoAutorizado;
import TECH4LIFE.entidadesJPA.repositories.GerenteRepository;
import TECH4LIFE.entidadesJPA.security.JwtUtil;
import TECH4LIFE.entidadesJPA.security.SecurityConfguration;

@Service
@Transactional
public class LogicaGerente {
    private GerenteRepository repo;
    private final JwtUtil jwtUtil;

    public LogicaGerente(GerenteRepository gerenterepo, JwtUtil jwtUtil){
        this.repo = gerenterepo;
        this.jwtUtil=jwtUtil;
    }  

    //Devuelve lista de gerentes
    public List<Gerente> getGerentes() {

        //Optional<UserDetails>userDetailsOpt= SecurityConfguration.getAuthenticatedUser();
        List<Gerente> gerentes = repo.findAll() ;
        if (gerentes.isEmpty()/*||userDetailsOpt.isEmpty()*/){
            throw new GerenteNoExistente();
        } 
        //UserDetails userDetails = userDetailsOpt.get();

        // Verifica si el usuario tiene el rol de administrador
        //boolean isAdmin = userDetails.getUsername().
        /*if(!isAdmin){
            throw new UsuarioNoAutorizado();
        }*/
        return gerentes;
    }
    //Devuelve un gerente por id
    public Gerente getGerente(Integer id, boolean admin) {
        if(id==null || id<0){
            throw new UsuarioNoAutorizado();
        }
        if(!admin){
            throw new UsuarioNoAutorizado();
        }
        Optional<Gerente> gerente = repo.findById(id);
        if(gerente.isEmpty()){
            throw new GerenteNoExistente();
        }
        return gerente.get();
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
        return repo.save(gerente);
    }


}
