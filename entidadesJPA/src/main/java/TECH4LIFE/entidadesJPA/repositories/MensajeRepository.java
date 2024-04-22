package TECH4LIFE.entidadesJPA.repositories;

import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Destinatario;
import TECH4LIFE.entidadesJPA.entities.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {

    Mensaje findMensajeByRemitente(Destinatario remitente) ;
    Mensaje insertMensaje(Destinatario remitente) ; // Duda: ¿Cómo insertamos/creamos nuevo mensaje?
    Mensaje findMensajeByIdMensaje(Integer idMensaje) ;
    Mensaje deleteMensajeByIdMensaje(Integer idMensaje) ;
}
