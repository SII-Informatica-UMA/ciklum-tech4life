package TECH4LIFE.entidadesJPA.repositories;

import TECH4LIFE.entidadesJPA.entities.Mensaje;
import TECH4LIFE.entidadesJPA.entities.Mensaje.Destinatario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {

    @Query("select m from Mensaje m where m.remitente = :remite")
    List<Mensaje> bandejaSalida(@Param("remite") Destinatario remite);
    
    @Query("select m from Mensaje m join m.destinatarios d where d = :destino")
    List<Mensaje> bandejaEntrada(@Param("destino") Destinatario destino);
 
}
