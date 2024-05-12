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
import org.springframework.http.*;
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
	@DisplayName("En cuanto a los mensajes")
	public class PruebasMensajes{
		@Nested
		@DisplayName("Cuando no hay mensajes")
		public class ListaMensajesVacia{

			@BeforeEach
			public void insertarCentro(){
				Centro centro1 = new Centro();
				centro1.setIdCentro(1);
				centroRepository.save(centro1);
			}
			@Test
			@DisplayName("Devuelve la lista de mensajes asociada a un centro vacía")
			public void devuelveListaMensajes(){
				// Supongamos que el ID del centro para el cual se quieren consultar los mensajes es 1
				long idCentro = 1;

				// Construir la URL del endpoint GET /mensaje/centro
				String url = "http://localhost:" + port + "/mensaje/centro?centro=" + idCentro;

				// Supongamos que enviamos una solicitud GET al endpoint
				ResponseEntity<List<MensajeDTO>> response = restTemplate.exchange(
						url,
						HttpMethod.GET,
						null,
						new ParameterizedTypeReference<List<MensajeDTO>>() {} // Tipo de respuesta esperado
				);

				// Verificar el código de estado de la respuesta
				assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

				// Verificar que el cuerpo de la respuesta está vacío porque hay un centro pero no tiene mensajes
				assertThat(response.getBody()).isEmpty();
			}
			@Test
			@DisplayName("Inserta un mensaje en una lista vacía asociada a un mensaje")
			public void insertaMensaje(){
				//TO DO
			}
			@Test
			@DisplayName("Devuelve un mensaje concreto asociado a un centro dado el idMensaje")
			public void devuelveMensajeConcreto(){
				// ID de un mensaje de centro que no existe
				long idMensaje = 9999;

				// Construir la URL del endpoint GET /mensaje/centro/{idMensaje}
				String url = "http://localhost:" + port + "/mensaje/centro/" + idMensaje;

				// Supongamos que enviamos una solicitud GET al endpoint
				ResponseEntity<MensajeDTO> response = restTemplate.getForEntity(url, MensajeDTO.class);

				// Verificar el código de estado de la respuesta
				assertThat(response.getStatusCode().value()).isEqualTo(404);
			}
			}
			@Test
			@DisplayName("Elimina un mensaje concreto asociado a un centro dado el idMensaje")
			public void eliminaMensajeConcreto(){
				//TO DO
			}
		}
	@Nested
	@DisplayName("Cuando sí hay mensajes")
	public class ListaMensajesConDatos{
		@Test
		@DisplayName("Devuelve la lista de mensajes asociada a un centro")
		public void devuelveListaMensajes(){
			//TO DO
		}
		@Test
		@DisplayName("Inserta un mensaje en una lista vacía asociada a un mensaje")
		public void insertaMensaje(){
			//TO DO
		}
		@Test
		@DisplayName("Devuelve un mensaje concreto asociado a un centro dado el idMensaje")
		public void devuelveMensajeConcreto(){
			//TO DO
		}
		@Test
		@DisplayName("Elimina un mensaje concreto asociado a un centro dado el idMensaje")
		public void eliminaMensajeConcreto(){
			//TO DO
		}
	}

	}
	@Nested
	@DisplayName("Cuando no hay mensajes")
	public class ListaMensajesVacia {

		/*
		1- hay que hacer bien la consulta bandejaTodos. En caso deberia funcionar el metodo
		 */
	//query, get
		@Test
		@DisplayName("Devuelve la lista de mensajes vacía")
		public void devuelveListaMensaje() {
			// ID del centro para el cual se quieren consultar los mensajes
			long idCentro = 1;
			String BASE_URL = "http://localhost:" + port;
			// Construir la URL del endpoint GET /mensaje/centro
			String url = BASE_URL + "/mensaje/centro?centro=" + idCentro;

			// Enviar la solicitud GET al endpoint
			ResponseEntity<List<MensajeDTO>> response = restTemplate.exchange(
					url,
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<List<MensajeDTO>>() {} // Tipo de respuesta esperado
			);

			// Verificar el código de estado de la respuesta
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

			// Verificar que el cuerpo de la respuesta no esté vacío
			assertThat(response.getBody()).isEmpty();

			// Puedes realizar más aserciones para verificar los datos específicos de los mensajes devueltos si lo deseas
		}
		}
//--------------------------------------------------------------------------------------
	//query, post
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
		//path, get
		@Test
		@DisplayName("Devuelve error cuando se pide un mensaje concreto")
		public void devuelveErrorAlConsultarMensaje() {
			// ID del centro para el cual se quieren consultar los mensajes
			long idCentro = 1;
			long idMensaje = 1;
			String BASE_URL = "http://localhost:" + port;
			// Construir la URL del endpoint GET /mensaje/centro
			String url = BASE_URL + "/mensaje/centro?mensaje=" + idMensaje + "centro=" + idCentro;

			// Enviar la solicitud GET al endpoint
			ResponseEntity<List<MensajeDTO>> response = restTemplate.exchange(
					url,
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<List<MensajeDTO>>() {} // Tipo de respuesta esperado
			);

			// Verificar el código de estado de la respuesta
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		}

		//FUNCIONA
	//path
		@Test
		@DisplayName("devuelve error cuando se elimina un mensaje concreto")
		public void devuelveErrorAlEliminarMensaje() {
			var peticion = delete("http", "localhost", port, "/mensaje/centro/45");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
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
			@DisplayName("y lo consigue cuando el mensaje a insertar no es null")
			public void mensajeNoNull() {
				var mensaje = MensajeNuevoDTO.builder()
						.asunto("Un asunto")
						.contenido("Un contenido")
						.build();
				var peticion = post("http", "localhost", port, "/mensaje/centro", mensaje);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				compruebaRespuestaMensaje(mensaje, respuesta);
			}

			@Test
			@DisplayName("pero da error cuando el mensaje a insertar es null")
			public void mensajeNull() {
				MensajeNuevoDTO mensaje= null;
				var peticion = post("http", "localhost", port, "/mensaje/centro", mensaje);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				compruebaRespuestaMensaje(mensaje, respuesta);
			}

	private void compruebaRespuestaMensaje(MensajeNuevoDTO mensaje, ResponseEntity<Void> respuesta) {

		assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
		assertThat(respuesta.getHeaders().get("Location").get(0))
				.startsWith("http://localhost:"+port+"/mensaje/centro");

		List<Mensaje> mensajes = mensajeRepository.bandejaTodos(12);
		assertThat(mensajes).hasSize(3);

		Mensaje msj = mensajes.stream()
				.filter(c->c.getAsunto().equals("Un asunto"))
				.filter(c->c.getContenido().equals("Un contenido"))
				.findAny()
				.get();

		assertThat(respuesta.getHeaders().get("Location").get(0))
				.endsWith("/"+msj.getIdMensaje());
		compruebaCamposMensaje(mensaje, msj);
	}
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
