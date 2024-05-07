package TECH4LIFE.entidadesJPA;
import TECH4LIFE.entidadesJPA.controladores.Mapper;
import TECH4LIFE.entidadesJPA.dtos.*;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Mensaje;
import TECH4LIFE.entidadesJPA.entities.TipoDestinatario;
import TECH4LIFE.entidadesJPA.repositories.CentroRepository;
import TECH4LIFE.entidadesJPA.repositories.GerenteRepository;
import TECH4LIFE.entidadesJPA.repositories.MensajeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


/*
 *  ANOTACIONES IMPORTANTES SOBRE LAS PRUEBAS JUNIT
 * ---------------------------------------------------
 *
 * 	- Añadir jacoco como plugin al pom.xml del proyecto Maven (Ya lo he añadido)
 * 	- Ejecutar desde consola de comandos mvn test o desde intellij -> icono maven -> Lifecycle -> test
 * 		OJO porque para que salga el informe de jacoco no se puede ejecutar desde intellij
 * 	- El informe de cobertura de código de jacoco se encuentra
 * 	  en el siguiente path -> target/site/jacoco/index.html
 *
 * 	- Error común: He seguido todos los pasos y no aparece la carpeta site:
 *    Jacoco algunas veces falla y en vez de generar un .exec genera un ".exe" o un ".ex"
 * 	  Solución: añadir la c, y hacer mvn test de nuevo y debería generar la carpeta test
 * */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de de gestión de centros y gerentes")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EntidadesJpaApplicationTests {

	/*
	---------------------------------------------
	 Ajustes iniciales
	---------------------------------------------
	*/

	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value = "${local.server.port}")
	private int port;

	/*
	---------------------------------------------
	 Inyección de los repositorios
	---------------------------------------------
	*/

	@Autowired
	private CentroRepository centroRepository;
	@Autowired
	private GerenteRepository gerenteRepository;
	@Autowired
	private MensajeRepository mensajeRepository;

	/*
	---------------------------------------------
	 Inicialización de la base de datos
	---------------------------------------------
	*/

	@BeforeEach
	public void initializeDatabase() {
		centroRepository.deleteAll();
		gerenteRepository.deleteAll();
		mensajeRepository.deleteAll();
	}

	/*
	---------------------------------------------
	 Métodos comunes a todas las pruebas
	 Realizado por: Raúl García Balongo
	---------------------------------------------
	*/

	private URI uri(String scheme, String host, int port, String... paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path : paths) {
			ub = ub.path(path);
		}
		return ub.build();
	}

	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.get(uri)
				.accept(MediaType.APPLICATION_JSON)
				.build();
		return peticion;
	}

	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.delete(uri)
				.build();
		return peticion;
	}

	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.put(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	/*
	---------------------------------------------
	 Pruebas de ControladorMensaje y LogicaMensaje
	 Realizado por: Ana María Calvente
	---------------------------------------------
	*/

	private void compruebaCamposMensaje(MensajeDTO expected, MensajeDTO actual) {
		assertThat(actual.getAsunto()).isEqualTo(expected.getAsunto());
		assertThat(actual.getContenido()).isEqualTo(expected.getContenido());
	}

	private void compruebaCamposMensaje(MensajeNuevoDTO expected, MensajeDTO actual) {
		assertThat(actual.getAsunto()).isEqualTo(expected.getAsunto());
		assertThat(actual.getContenido()).isEqualTo(expected.getContenido());
	}

	private void compruebaCamposMensaje(MensajeNuevoDTO expected, Mensaje actual) {
		assertThat(actual.getAsunto()).isEqualTo(expected.getAsunto());
		assertThat(actual.getContenido()).isEqualTo(expected.getContenido());
	}
//-------------------------------------------------------------------------------------------
	@Nested
	@DisplayName("Cuando no hay mensajes")
	public class ListaMensajesVacia {

		/*
		DUDA: NO FUNCIONA PORQUE CREEMOS QUE BANDEJATODOS DE MENSAJEREPOSITORY
		 */
		@Test
		@DisplayName("Devuelve la lista de mensajes vacía")
		public void devuelveListaMensaje() {

			var peticion = get("http", "localhost", port, "/mensaje/centro"); // Este será el path de los mensajes de un centro o será "/centro/mensaje"??

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<MensajeDTO>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			assertThat(respuesta.hasBody()).isEqualTo(false);
		}
//--------------------------------------------------------------------------------------
		@Nested
		@DisplayName("Intenta insertar un mensaje")
		public class InsertaMensaje {
			@Test
			@DisplayName("y se guarda con éxito")
			public void sinID() {
				var centro = CentroDTO.builder()
						.idCentro(12)
						.nombre("PepeitoFit")
						.direccion("Calle la gata, 56")
						.build();
				// Establecer destinatarios
				Set<DestinatarioDTO> destinatarios = new HashSet<>();
				DestinatarioDTO destinatario1 = new DestinatarioDTO();
				destinatario1.setId(1);
				destinatario1.setTipo(TipoDestinatario.CENTRO);
				destinatarios.add(destinatario1);

				// Establecer copias
				Set<DestinatarioDTO> copias = new HashSet<>();
				DestinatarioDTO copia1 = new DestinatarioDTO();
				copia1.setId(2);
				copia1.setTipo(TipoDestinatario.CENTRO);
				copias.add(copia1);

				// Establecer copias ocultas
				Set<DestinatarioDTO> copiasOcultas = new HashSet<>();
				DestinatarioDTO copiaOculta1 = new DestinatarioDTO();
				copiaOculta1.setId(3);
				copiaOculta1.setTipo(TipoDestinatario.CLIENTE);
				copiasOcultas.add(copiaOculta1);

				// Establecer remitente
				DestinatarioDTO remitente = new DestinatarioDTO();
				remitente.setId(4);
				remitente.setTipo(TipoDestinatario.CLIENTE);


				var mensaje = MensajeNuevoDTO.builder()
						.asunto("Cambio entrenador")
						.destinatarios(destinatarios)
						.copia(copias)
						.copiaOculta(copiasOcultas)
						.remitente(remitente)
						.contenido("Buenos días, quiero cambiar de entrenador.")
						.build();

				var completa = centro.toString() + mensaje; //correcto???

				//el path será /mensaje o /centro/mensaje
				var peticion = post("http", "localhost", port, "/mensaje/centro", completa);


				var respuesta = restTemplate.exchange(peticion, Void.class);

				compruebaRespuestaMensaje(mensaje, centro, respuesta);
			}

			private void compruebaRespuestaMensaje(MensajeNuevoDTO mensaje, CentroDTO centro, ResponseEntity<Void> respuesta) {
				assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
				assertThat(respuesta.getHeaders().get("Location").get(0))
						.startsWith("http://localhost:" + port + "/mensaje/centro");

				//creeis que hay que cambiar mensajeRepository para que devuelva List<Mensaje> en lugar de List<MensajeDTO>???
				List<Mensaje> mensajes = mensajeRepository.bandejaTodos(centro.getIdCentro());
				assertThat(mensajes).hasSize(1);
				assertThat(respuesta.getHeaders().get("Location").get(0))
						.endsWith("/" + mensajes.get(0).getIdMensaje());
				compruebaCamposMensaje(mensaje, mensajes.get(0));
			}
		}

		@Test
		@DisplayName("Devuelve error cuando se pide un mensaje concreto")
		public void devuelveErrorAlConsultarMensaje() {
			var peticion = get("http", "localhost", port, "/mensaje/centro");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<MensajeDTO>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			assertThat(respuesta.hasBody()).isEqualTo(false);
		}

		//FUNCIONA
		@Test
		@DisplayName("devuelve error cuando se elimina un mensaje concreto")
		public void devuelveErrorAlEliminarMensaje() {
			var peticion = delete("http", "localhost", port, "/mensaje/centro/45");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
	}
//-----------------------------------------------------------------------------------------
	@Nested
	@DisplayName("Cuando hay mensajes")
	public class ListaMensajeConDatos {

		// Establecer destinatarios
		private Set<DestinatarioDTO> destinatarios = new HashSet<>();
		// Establecer copias
		Set<DestinatarioDTO> copias = new HashSet<>();
		//Establecer copias ocultas
		Set<DestinatarioDTO> copiasOcultas = new HashSet<>();
		// Establecer remitente
		DestinatarioDTO remitente1 = new DestinatarioDTO();
		DestinatarioDTO remitente2 = new DestinatarioDTO();

		public ListaMensajeConDatos() {
			establecerMensajes();
		}

		void establecerMensajes() {
			//destinatario
			DestinatarioDTO destinatario1 = new DestinatarioDTO();
			destinatario1.setId(1);
			destinatario1.setTipo(TipoDestinatario.CENTRO);
			destinatarios.add(destinatario1);
			//copia
			DestinatarioDTO copia1 = new DestinatarioDTO();
			copia1.setId(2);
			copia1.setTipo(TipoDestinatario.CENTRO);
			copias.add(copia1);
			//copia oculta
			DestinatarioDTO copiaOculta1 = new DestinatarioDTO();
			copiaOculta1.setId(3);
			copiaOculta1.setTipo(TipoDestinatario.CLIENTE);
			copiasOcultas.add(copiaOculta1);
			//remitente
			remitente1.setId(4);
			remitente1.setTipo(TipoDestinatario.CLIENTE);
			remitente1.setId(5);
			remitente1.setTipo(TipoDestinatario.CENTRO);
		}

		private MensajeNuevoDTO mensaje1 = MensajeNuevoDTO.builder()
				.asunto("Cambio entrenador")
				.destinatarios(destinatarios)
				.copia(copias)
				.copiaOculta(copiasOcultas)
				.remitente(remitente1)
				.contenido("Buenos días, quiero cambiar de entrenador.")
				.build();
		private MensajeNuevoDTO mensaje2 = MensajeNuevoDTO.builder()
				.asunto("Cambio en el evento")
				.destinatarios(destinatarios)
				.copia(copias)
				.copiaOculta(copiasOcultas)
				.remitente(remitente2)
				.contenido("Buenos días, les informo del siguiente cambio en el evento")
				.build();

		@BeforeEach
		public void insertaMensajes() {
			mensajeRepository.save(Mapper.toMensaje(mensaje1));
			mensajeRepository.save(Mapper.toMensaje(mensaje2));
		}

		@Test
		@DisplayName("Devuelve la lista de mensajes correctamente")
		public void devuelveListaMensajes() {
			var peticion = get("http", "localhost", port, "/mensaje/centro"); // Revisar el path

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<MensajeDTO>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).hasSize(2);
		}
//------------------------------------------------------------------------------------
		@Nested
		@DisplayName("Intenta insertar un mensaje")
		public class InsertaMensaje {


			@Test
			@DisplayName("Cuando se inserta un mensaje correctamente")
			public void prueba1() {
				var centro = CentroDTO.builder()
						.idCentro(12)
						.nombre("PepeitoFit")
						.direccion("Calle la gata, 56")
						.build();
				// Establecer destinatarios
				Set<DestinatarioDTO> destinatarios = new HashSet<>();
				DestinatarioDTO destinatario1 = new DestinatarioDTO();
				destinatario1.setId(1);
				destinatario1.setTipo(TipoDestinatario.CENTRO);
				destinatarios.add(destinatario1);

				// Establecer copias
				Set<DestinatarioDTO> copias = new HashSet<>();
				DestinatarioDTO copia1 = new DestinatarioDTO();
				copia1.setId(2);
				copia1.setTipo(TipoDestinatario.CENTRO);
				copias.add(copia1);

				// Establecer copias ocultas
				Set<DestinatarioDTO> copiasOcultas = new HashSet<>();
				DestinatarioDTO copiaOculta1 = new DestinatarioDTO();
				copiaOculta1.setId(3);
				copiaOculta1.setTipo(TipoDestinatario.CLIENTE);
				copiasOcultas.add(copiaOculta1);

				// Establecer remitente
				DestinatarioDTO remitente = new DestinatarioDTO();
				remitente.setId(4);
				remitente.setTipo(TipoDestinatario.CLIENTE);


				var mensaje = MensajeNuevoDTO.builder()
						.asunto("Cambio entrenador")
						.destinatarios(destinatarios)
						.copia(copias)
						.copiaOculta(copiasOcultas)
						.remitente(remitente)
						.contenido("Buenos días, quiero cambiar de entrenador.")
						.build();

				var completa = centro.toString() + mensaje; //correcto???

				//el path será /mensaje o /centro/mensaje
				var peticion = post("http", "localhost", port, "/mensaje/centro", completa);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				compruebaRespuestaMensaje(mensaje, centro, respuesta);
			}

			@Test
			@DisplayName("Cuando falla la inserción de un mensaje")
			public void prueba2() {
				// TO DO
			}

	private void compruebaRespuestaMensaje(MensajeNuevoDTO mensaje, CentroDTO centro, ResponseEntity<Void> respuesta) {
		assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
		assertThat(respuesta.getHeaders().get("Location").get(0))
				.startsWith("http://localhost:" + port + "/mensaje");

		//creeis que hay que cambiar mensajeRepository para que devuelva List<Mensaje> en lugar de List<MensajeDTO>???
		List<Mensaje> mensajes = mensajeRepository.bandejaTodos(centro.getIdCentro());
		assertThat(mensajes).hasSize(1);
		assertThat(respuesta.getHeaders().get("Location").get(0))
				.endsWith("/" + mensajes.get(0).getIdMensaje());
		compruebaCamposMensaje(mensaje, mensajes.get(0));
	}
		}
//----------------------------------------------------------------------------------------------
		@Nested
		@DisplayName("Al consultar un mensaje concreto")
		public class ObtenerMensajes {

			@Test
			@DisplayName("lo devuelve cuando existe")
			public void devuelveMensaje() {
				var peticion = get("http", "localhost", port, "/mensaje/centro/1");

				var respuesta = restTemplate.exchange(peticion, MensajeDTO.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				assertThat(respuesta.hasBody()).isTrue();
				assertThat(respuesta.getBody()).isNotNull();
			}

			@Test
			@DisplayName("da error cuando no existe")
			public void errorCuandoMensajeNoExiste() {
				var peticion = get("http", "localhost", port, "/mensaje/centro/28");//el path es el correcto????

				var respuesta = restTemplate.exchange(peticion,
						new ParameterizedTypeReference<List<MensajeDTO>>() {
						});

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				assertThat(respuesta.hasBody()).isEqualTo(false);
			}
		}
//----------------------------------------------------------------------------------------------
		@Nested
		@DisplayName("Al eliminar un mensaje")
		public class EliminarMensaje {
			@Test
			@DisplayName("Lo elimina cuando existe")
			public void eliminaCorrectamenteMensaje() {
				var centro = CentroDTO.builder()
						.idCentro(12)
						.nombre("PepeitoFit")
						.direccion("Calle la gata, 56")
						.build();

				var peticion = delete("http", "localhost", port, "/mensaje/1");

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				List<Mensaje> mensajes = mensajeRepository.bandejaTodos(centro.getIdCentro());
				assertThat(mensajes).hasSize(1);
				assertThat(mensajes).allMatch(c -> c.getIdMensaje() != 1);
			}

			@Test
			@DisplayName("da error cuando no existe")
			public void errorCuandoNoExisteMensaje() {
				var peticion = delete("http", "localhost", port, "/mensaje/centro/28");

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				assertThat(respuesta.hasBody()).isEqualTo(false);
			}
		}
	}

}
