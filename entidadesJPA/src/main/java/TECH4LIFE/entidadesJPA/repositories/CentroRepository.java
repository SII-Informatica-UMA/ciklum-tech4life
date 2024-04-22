package TECH4LIFE.entidadesJPA.repositories;

import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CentroRepository extends JpaRepository <Centro, Integer> {
    /*
       Duda:
          ¿Buscar un centro por su id o por objeto Centro?
           No entendemos a cuál de las dos cosas se refiere la OpenAPI.
    */
    Centro findCentroByIdCentro(Integer idCentro) ;
    Centro updateCentroByIdCentro(Integer idCentro) ;
    Centro deleteCentroByIdCentro(Integer idCentro) ;
    List<Centro> findAllCentros() ; // Duda: ¿Existe en Spring? ¿Es correcto?
    List<Centro> findCentrosByGerente() ; // ¿¿¿Duda: Relación con centro???
    Centro insertCentro() ; // Duda: ¿Cómo insertamos/creamos nuevo centro?
}
