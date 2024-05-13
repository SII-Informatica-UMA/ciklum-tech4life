package TECH4LIFE.entidadesJPA.repositories;


import TECH4LIFE.entidadesJPA.dtos.MensajeDTO;
import TECH4LIFE.entidadesJPA.entities.Mensaje;
import TECH4LIFE.entidadesJPA.entities.Destinatario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {

    /*@Query("select m from Mensaje m where m.remitente = :remite")
    List<Mensaje> bandejaSalida(@Param("remite") Destinatario remite);
    
    @Query("select m from Mensaje m join m.destinatarios d where d = :destino")
    List<Mensaje> bandejaEntrada(@Param("destino") Destinatario destino);
*/
    //@Query("SELECT m FROM Mensaje m WHERE (m.destinatario.id = :idCentro AND m.destinatario.tipo = 'CENTRO') OR (m.remitente.id = :idCentro AND m.remitente.tipo = 'CENTRO')")
    @Query("select m from Mensaje m where m.destinatario.id = :idCentro or m.remitente.id = :idCentro")
    List<MensajeDTO> bandejaTodos(@Param("idCentro") Integer idCentro);


    // @Query("select m from Mensaje m join m.destinatarios d where d.id = :idCentro and d.tipo = :CENTRO or m.remitente.id = : idCentro and m.remitente.tipo = :CENTRO")

}
