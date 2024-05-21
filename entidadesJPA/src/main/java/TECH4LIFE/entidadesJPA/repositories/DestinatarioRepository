package TECH4LIFE.entidadesJPA.repositories;

import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Destinatario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinatarioRepository extends JpaRepository <Destinatario, Integer> {

    @Query("select c from Centro c where c.idCentro= :id")
    Centro FindCentroByDestinatario(@Param("id") Integer id);

}