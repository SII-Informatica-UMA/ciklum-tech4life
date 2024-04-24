package TECH4LIFE.entidadesJPA.repositories;

import TECH4LIFE.entidadesJPA.entities.Mensaje;
import TECH4LIFE.entidadesJPA.entities.Mensaje.Destinatario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {

    @Query("select m from Mensaje m where m.remitente = :remite") 
    List<Mensaje> BandejaSalida(@Param("remite") Destinatario remite);
    @Query("select m from Mensaje m where m.destinatario = :destino") 
    List<Mensaje> BandejaEntrada(@Param("destino") Destinatario destino);

}
