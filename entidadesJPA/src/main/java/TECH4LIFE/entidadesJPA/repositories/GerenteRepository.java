package TECH4LIFE.entidadesJPA.repositories;

import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GerenteRepository extends JpaRepository<Gerente, Integer> {
 
    @Query("select c from Centro c where c.gerente = :gerente")
    Centro FindCentroByGerente(@Param("gerente") Gerente gerente);

    @Query("select g from Gerente g where g.idUsuario = :idUsuario")
    Gerente FindGerenteByidUsuario(@Param("idUsuario") Integer idUsuario);

}
