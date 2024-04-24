package TECH4LIFE.entidadesJPA.repositories;

import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GerenteRepository extends JpaRepository<Gerente, Integer> {
 
    @Query("select c from Centro c where c.Gerente = :gerente") 
    Centro FindCentroByGerente(@Param("gerente") Gerente gerente);

}
