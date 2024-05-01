package TECH4LIFE.entidadesJPA.repositories;

import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroRepository extends JpaRepository <Centro, Integer> {
   
    @Query("select g from Gerente g  where g.centro.idCentro= :id")
    Gerente FindGerenteByCentro(@Param("id") Integer id);

    

}
