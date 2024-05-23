package TECH4LIFE.entidadesJPA.controladores;

import java.util.Set;
import java.util.stream.Collectors;

import TECH4LIFE.entidadesJPA.dtos.CentroDTO;
import TECH4LIFE.entidadesJPA.dtos.CentroNuevoDTO;
import TECH4LIFE.entidadesJPA.dtos.DestinatarioDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteNuevoDTO;
import TECH4LIFE.entidadesJPA.dtos.IdGerenteDTO;
import TECH4LIFE.entidadesJPA.dtos.MensajeDTO;
import TECH4LIFE.entidadesJPA.dtos.MensajeNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import TECH4LIFE.entidadesJPA.entities.Mensaje;
import TECH4LIFE.entidadesJPA.entities.Destinatario;

public class Mapper {

    public static Centro toCentro(CentroNuevoDTO centroNuevoDTO) {
        return Centro.builder()
                .nombre(centroNuevoDTO.getNombre())
                .direccion(centroNuevoDTO.getDireccion())
                .build();
    }

    public static CentroDTO toCentroDTO(Centro centro) {
        return CentroDTO.builder()
                .idCentro(centro.getIdCentro())
                .nombre(centro.getNombre())
                .direccion(centro.getDireccion())
                .build();
    }

    public static Gerente toGerente(GerenteNuevoDTO gerenteNuevoDTO) {
        return Gerente.builder()
                .idUsuario(gerenteNuevoDTO.getIdUsuario())
                .empresa(gerenteNuevoDTO.getEmpresa())
                .build();
    }

    public static Gerente toGerente(GerenteDTO gerenteDTO) {
        return Gerente.builder()
                .idUsuario(gerenteDTO.getIdUsuario())
                .empresa(gerenteDTO.getEmpresa())
                .build();
    }

    public static GerenteDTO toGerenteDTO(Gerente gerente) {
        return GerenteDTO.builder()
                .id(gerente.getId())
                .idUsuario(gerente.getIdUsuario())
                .empresa(gerente.getEmpresa())
                .build();
    }

    public static IdGerenteDTO toIdGerenteDTO(GerenteDTO gerenteDTO){
        return IdGerenteDTO.builder()
                .idGerente(gerenteDTO.getId())
                .build();
    }

    public static DestinatarioDTO toDestinatarioDTO(Destinatario destinatario){
        return DestinatarioDTO.builder()
                .id(destinatario.getId())
                .tipo(destinatario.getTipo())
                .build();
    }

    public static Destinatario toDestinatario(DestinatarioDTO destinatarioDTO){
        return Destinatario.builder()
                .id(destinatarioDTO.getId())
                .tipo(destinatarioDTO.getTipo())
                .build();
    }

    public static Set<DestinatarioDTO> toDestinatariosDTO(Set<Destinatario> destinatarios){
        return destinatarios.stream()
            .map(destinatario -> DestinatarioDTO.builder()
                .id(destinatario.getId())
                .tipo(destinatario.getTipo())
                .build())
            .collect(Collectors.toSet());
    }

    public static Set<Destinatario> toDestinatarios(Set<DestinatarioDTO> destinatariosDTO){
        return destinatariosDTO.stream()
            .map(destinatarioDTO -> Destinatario.builder()
                .id(destinatarioDTO.getId())
                .tipo(destinatarioDTO.getTipo())
                .build())
            .collect(Collectors.toSet());
    }

    public static Mensaje toMensaje(MensajeNuevoDTO mensajeNuevoDTO){
        return Mensaje.builder()
                .asunto(mensajeNuevoDTO.getAsunto())
                .destinatarios(toDestinatarios(mensajeNuevoDTO.getDestinatarios()))
                .copia(toDestinatarios(mensajeNuevoDTO.getCopia()))
                .copiaOculta(toDestinatarios(mensajeNuevoDTO.getCopiaOculta()))
                .remitente(toDestinatario(mensajeNuevoDTO.getRemitente()))
                .contenido(mensajeNuevoDTO.getContenido())
                .build();
    }

    public static MensajeDTO toMensajeDTO(Mensaje mensaje){
        return MensajeDTO.builder()
                .asunto(mensaje.getAsunto())
                .destinatarios(toDestinatariosDTO(mensaje.getDestinatarios()))
                .copia(toDestinatariosDTO(mensaje.getCopia()))
                .copiaOculta(toDestinatariosDTO(mensaje.getCopiaOculta()))
                .remitente(toDestinatarioDTO(mensaje.getRemitente()))
                .contenido(mensaje.getContenido())
                .build();
    }

}
