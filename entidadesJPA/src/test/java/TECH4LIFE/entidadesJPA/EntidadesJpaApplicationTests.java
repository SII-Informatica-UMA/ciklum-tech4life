package TECH4LIFE.entidadesJPA;
import TECH4LIFE.entidadesJPA.controladores.Mapper;
import TECH4LIFE.entidadesJPA.dtos.*;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Destinatario;
import TECH4LIFE.entidadesJPA.entities.Mensaje;
import TECH4LIFE.entidadesJPA.entities.TipoDestinatario;
import TECH4LIFE.entidadesJPA.repositories.CentroRepository;
import TECH4LIFE.entidadesJPA.repositories.DestinatarioRepository;
import TECH4LIFE.entidadesJPA.repositories.GerenteRepository;
import TECH4LIFE.entidadesJPA.repositories.MensajeRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.Arrays;
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

	@Value(value="${local.server.port}")
	private int port;

	/*
	---------------------------------------------
	 Inyección de los repositorios
	---------------------------------------------
	*/
	@Autowired
	private DestinatarioRepository destinatarioRepository;
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
		destinatarioRepository.deleteAll();
	}

	/*
	---------------------------------------------
	 Métodos comunes a todas las pruebas
	 Realizado por: Raúl García Balongo
	---------------------------------------------
	*/

	private URI uri(String scheme, String host, int port, String ...paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path: paths) {
			ub = ub.path(path);
		}
		return ub.build();
	}

	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.get(uri)
				.accept(MediaType.APPLICATION_JSON)
				.build();
		return peticion;
	}

	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.delete(uri)
				.build();
		return peticion;
	}

	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.put(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	/*
	---------------------------------------------
	 Pruebas de ControladorMensaje y LogicaMensaje
	 Realizado por: Ana María Calvente Bonvie
	---------------------------------------------
	*/

	@Nested
	@DisplayName("En cuanto a los mensajes")
	public class PruebasMensajes{
		@Nested
		@DisplayName("Cuando no hay mensajes")
		public class ListaMensajesVacia{
			private DestinatarioDTO destinatario1 = DestinatarioDTO.builder()
					.id(1)
					.tipo(TipoDestinatario.CENTRO)
					.build();
			private CentroNuevoDTO centro1 = CentroNuevoDTO.builder()
					.nombre("Centro1")
					.direccion("Calle del Centro1, 1")
					.build();
			private DestinatarioDTO destinatario2 = DestinatarioDTO.builder()
					.id(2)
					.tipo(TipoDestinatario.CENTRO)
					.build();
			private CentroNuevoDTO centro2 = CentroNuevoDTO.builder()
					.nombre("Centro2")
					.direccion("Calle del Centro2, 2")
					.build();
			private DestinatarioDTO destinatario3 = DestinatarioDTO.builder()
					.id(3)
					.tipo(TipoDestinatario.CENTRO)
					.build();
			private CentroNuevoDTO centro3 = CentroNuevoDTO.builder()
					.nombre("Centro3")
					.direccion("Calle del Centro3, 3")
					.build();

			@BeforeEach
			public void insertarCentro() {
				centroRepository.save(Mapper.toCentro(centro1));
				centroRepository.save(Mapper.toCentro(centro2));
				centroRepository.save(Mapper.toCentro(centro3));
				destinatarioRepository.save(Mapper.toDestinatario(destinatario1));
				destinatarioRepository.save(Mapper.toDestinatario(destinatario2));
				destinatarioRepository.save(Mapper.toDestinatario(destinatario3));

			}

			@Test
			@DisplayName("Devuelve la lista de mensajes asociada a un centro vacía")
			public void devuelveListaMensajes(){
				ResponseEntity<List<MensajeDTO>> responseEntity = restTemplate.exchange("http://localhost:" + port + "/mensaje/centro?centro=1",
						HttpMethod.GET,
						null,
                        new ParameterizedTypeReference<List<MensajeDTO>>() {
                        });

				assertThat(responseEntity.getStatusCode().value()).isEqualTo(404);
				//List<MensajeDTO> listaMensajes = responseEntity.getBody(); // Obtener el cuerpo de la respuesta
				//assertThat(listaMensajes).isEmpty(); // Verificar que la lista de mensajes está vacía
			}

			@Test
			@DisplayName("Inserta un mensaje en una lista vacía asociada a un mensaje")
			public void insertaMensaje() {
				//Ya tenemos los destinatarios/remitentes creados
				Set<DestinatarioDTO> listaDestinatariosDTO = new HashSet<>(Arrays.asList(destinatario2, destinatario3));
				// Paso 1: Creación del objeto MensajeNuevoDTO
				MensajeNuevoDTO mensajeNuevoDTO = MensajeNuevoDTO.builder()
						.asunto("Asunto del mensaje")
						.destinatarios(listaDestinatariosDTO)
						.copia(listaDestinatariosDTO)  // Puedes agregar copias si es necesario
						.copiaOculta(listaDestinatariosDTO)  // Puedes agregar copias ocultas si es necesario
						.remitente(destinatario1)  // Agregar el remitente al mensaje
						.contenido("Contenido del mensaje")
						.build();

				// Paso 2: Realizar la solicitud HTTP
				var headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				var request = new HttpEntity<>(mensajeNuevoDTO, headers);

				var respuesta = restTemplate.exchange("http://localhost:" + port + "/mensaje/centro?centro=1",
						HttpMethod.POST,
						request,
						Void.class);

				// Paso 3: Verificar la respuesta
				assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
				//Void mensajeCreado = respuesta.getBody();
				//assertThat(mensajeCreado).isNotNull();
			}

			@Test
			@DisplayName("Devuelve un mensaje concreto asociado a un centro dado el idMensaje")
			public void devuelveMensajeConcreto(){
				// Paso 1: Preparar los datos de prueba
				int idMensaje = 1; // Supongamos que el mensaje con id 1 existe en la base de datos
				int idCentro = 1; // Supongamos que el centro con id 1 existe en la base de datos

				// Objeto MensajeDTO esperado
				MensajeDTO mensajeEsperado = MensajeDTO.builder()
						.idMensaje(idMensaje)
						.asunto("Asunto del mensaje")
						.contenido("Contenido del mensaje")
						.build();

				//DUDA: está correcto el path?
				// Paso 2: Realizar la solicitud HTTP
				String url = String.format("http://localhost:%d/mensaje/centro/%d?idMensaje=%d", port, idCentro, idMensaje);

				ResponseEntity<MensajeDTO> respuesta = restTemplate.exchange(url, HttpMethod.GET, null, MensajeDTO.class);

				// Paso 3: Verificar la respuesta
				assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
				MensajeDTO listaMensajes = respuesta.getBody(); // Obtener el cuerpo de la respuesta
				assertThat(listaMensajes).isNull();
			}
			@Test
			@DisplayName("devuelve error cuando se elimina un mensaje concreto")
			public void devuelveErrorAlEliminarMensaje() {
				//var peticion = delete("http", "localhost", port, "/mensaje");
				var peticion = delete("http", "localhost", port, "/mensaje/centro/45");

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			}
		}
		@Nested
		@DisplayName("Cuando sí hay mensajes")
		public class ListaMensajesConDatos{
			private DestinatarioDTO destinatario1 = DestinatarioDTO.builder()
					.id(1)
					.tipo(TipoDestinatario.CENTRO)
					.build();

			private CentroNuevoDTO centro1 = CentroNuevoDTO.builder()
					.nombre("Centro1")
					.direccion("Calle del Centro1, 1")
					.build();

			private DestinatarioDTO destinatario2 = DestinatarioDTO.builder()
					.id(2)
					.tipo(TipoDestinatario.CENTRO)
					.build();

			private DestinatarioDTO destinatario3 = DestinatarioDTO.builder()
					.id(3)
					.tipo(TipoDestinatario.CENTRO)
					.build();

			private CentroNuevoDTO centro2 = CentroNuevoDTO.builder()
					.nombre("Centro2")
					.direccion("Calle del Centro2, 2")
					.build();

			private CentroNuevoDTO centro3 = CentroNuevoDTO.builder()
					.nombre("Centro3")
					.direccion("Calle del Centro3, 3")
					.build();

			private DestinatarioDTO remitente1 = DestinatarioDTO.builder()
					.id(3)
					.tipo(TipoDestinatario.CENTRO)
					.build();


			@BeforeEach
			public void insertarCentro() {
				centroRepository.save(Mapper.toCentro(centro1));
				centroRepository.save(Mapper.toCentro(centro2));
				centroRepository.save(Mapper.toCentro(centro3));
				destinatarioRepository.save(Mapper.toDestinatario(destinatario1));
				destinatarioRepository.save(Mapper.toDestinatario(destinatario2));
				destinatarioRepository.save(Mapper.toDestinatario(destinatario3));
				Set<DestinatarioDTO> listaDestinatariosDTO = new HashSet<>(Arrays.asList(destinatario2, destinatario3));
				MensajeNuevoDTO mensaje1 = MensajeNuevoDTO.builder()
						.asunto("Asunto1")
						.remitente(destinatario1)
						.destinatarios(listaDestinatariosDTO)
						.copia(listaDestinatariosDTO)
						.copiaOculta(listaDestinatariosDTO)
						.build();
				mensajeRepository.save(Mapper.toMensaje(mensaje1));

			}
			@Test
			@DisplayName("Devuelve la lista de mensajes asociada a un centro")
			public void devuelveListaMensajes(){
				ResponseEntity<List<MensajeDTO>> responseEntity = restTemplate.exchange("http://localhost:" + port + "/mensaje/centro?centro=1",
						HttpMethod.GET,
						null,
						new ParameterizedTypeReference<List<MensajeDTO>>() {
						});

				assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
				List<MensajeDTO> listaMensajes = responseEntity.getBody(); // Obtener el cuerpo de la respuesta
				assertThat(listaMensajes).isNotEmpty(); // Verificar que la lista de mensajes está vacía
			}
			@Test
			@DisplayName("Inserta un mensaje en una lista vacía asociada a un mensaje")
			public void insertaMensaje(){
				Set<DestinatarioDTO> listaDestinatariosDTO = new HashSet<>(Arrays.asList(destinatario2, destinatario3));
				// Paso 1: Creación del objeto MensajeNuevoDTO
				MensajeDTO mensajeNuevoDTO = MensajeDTO.builder()
						.idMensaje(1)
						.asunto("Asunto del mensaje")
						.destinatarios(listaDestinatariosDTO)
						.copia(listaDestinatariosDTO)  // Puedes agregar copias si es necesario
						.copiaOculta(listaDestinatariosDTO)  // Puedes agregar copias ocultas si es necesario
						.remitente(destinatario1)  // Agregar el remitente al mensaje
						.contenido("Contenido del mensaje")
						.build();

				// Paso 2: Realizar la solicitud HTTP
				var headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);

				var request = new HttpEntity<>(mensajeNuevoDTO, headers);

				var respuesta = restTemplate.exchange("http://localhost:" + port + "/mensaje/centro?centro=1",
						HttpMethod.POST,
						request,
						Void.class);

				// Paso 3: Verificar la respuesta
				assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
				Void mensajeCreado = respuesta.getBody();
				assertThat(mensajeCreado).isNotNull();
			}
			@Test
			@DisplayName("Devuelve un mensaje concreto asociado a un centro dado el idMensaje")
			public void devuelveMensajeConcreto(){
				// Paso 1: Preparar los datos de prueba
				int idMensaje = 1; // Supongamos que el mensaje con id 1 existe en la base de datos
				int idCentro = 1; // Supongamos que el centro con id 1 existe en la base de datos

				// Objeto MensajeDTO esperado
				MensajeDTO mensajeEsperado = MensajeDTO.builder()
						.idMensaje(idMensaje)
						.asunto("Asunto del mensaje")
						.contenido("Contenido del mensaje")
						.build();

				//DUDA: está correcto el path?
				// Paso 2: Realizar la solicitud HTTP
				String url = String.format("http://localhost:%d/mensaje/centro/%d?idMensaje=%d", port, idCentro, idMensaje);

				ResponseEntity<MensajeDTO> respuesta = restTemplate.exchange(url, HttpMethod.GET, null, MensajeDTO.class);

				// Paso 3: Verificar la respuesta
				assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.OK);
				MensajeDTO listaMensajes = respuesta.getBody(); // Obtener el cuerpo de la respuesta
				assertThat(listaMensajes).isNotNull();
			}
			@Test
			@DisplayName("Elimina un mensaje concreto asociado a un centro dado el idMensaje")
			public void eliminaMensajeConcreto(){
				var peticion = delete("http", "localhost", port, "/mensaje/centro/1");

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			}
		}
	}

}






