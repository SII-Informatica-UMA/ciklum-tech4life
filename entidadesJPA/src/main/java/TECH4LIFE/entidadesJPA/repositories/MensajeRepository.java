package TECH4LIFE.entidadesJPA.repositories;


import TECH4LIFE.entidadesJPA.dtos.MensajeDTO;
import TECH4LIFE.entidadesJPA.entities.Mensaje;
import TECH4LIFE.entidadesJPA.entities.Destinatario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {

    @Query("select m from Mensaje m where m.remitente = :remite")
    List<MensajeDTO> bandejaSalida(@Param("remite") Destinatario remite);
    
    @Query("select m from Mensaje m join m.destinatarios d where d = :destino")
    List<MensajeDTO> bandejaEntrada(@Param("destino") Destinatario destino);

    @Query("select m from Mensaje m join m.destinatarios d where d.id = :idCentro and d.tipo = TipoDestinatario.CENTRO or m.remitente.id = : idCentro and m.remitente.tipo = TipoDestinatario.CENTRO")
    List<Mensaje> bandejaTodos(@Param("idCentro") Integer idCentro);
}
