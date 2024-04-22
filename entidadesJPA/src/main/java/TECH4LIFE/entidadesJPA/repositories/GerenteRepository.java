package TECH4LIFE.entidadesJPA.repositories;

import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GerenteRepository extends JpaRepository<Gerente, Integer> {
    /*
    Duda:
        ¿Buscar un gerente por su id o por objeto gerente?
        No entendemos a cuál de las dos cosas se refiere la OpenAPI
     */
    Gerente findGerenteById(Integer id) ;
    Gerente updateGerenteBy(Integer id) ;
    Gerente deleteGerenteById(Integer id) ;
    List<Gerente> findAllGerentes () ; // Duda: ¿Existe en Spring? ¿Es correcto?
    Gerente insertGerente() ; // Duda: ¿Cómo insertamos/creamos nuevo gerente?
}
