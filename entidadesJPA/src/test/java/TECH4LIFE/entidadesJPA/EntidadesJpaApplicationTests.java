package TECH4LIFE.entidadesJPA;
import TECH4LIFE.entidadesJPA.controladores.Mapper;
import TECH4LIFE.entidadesJPA.dtos.CentroDTO;
import TECH4LIFE.entidadesJPA.dtos.CentroNuevoDTO;
import TECH4LIFE.entidadesJPA.dtos.DestinatarioDTO;
import TECH4LIFE.entidadesJPA.dtos.MensajeDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.List;

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
	 Pruebas de ControladorCentro y LogicaCentro
	 Realizado por: Raúl García Balongo
	---------------------------------------------
	*/


	/*
	---------------------------------------------
	 Pruebas de ControladorGerente y LogicaGerente
	 Realizado por:
	---------------------------------------------
	*/

	// TO DO

	/*
	---------------------------------------------
	 Pruebas de ControladorMensaje y LogicaMensaje
	 Realizado por:
	---------------------------------------------
	*/
	@Nested
	@DisplayName("Tests de Mensajes")
	public class PruebasMensajes {
		@Nested
		@DisplayName("Cuando no hay mensajes")
		public class ListaMensajesVacia {
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

			private DestinatarioDTO remitente1 = DestinatarioDTO.builder()
					.id(3)
					.tipo(TipoDestinatario.CENTRO)
					.build();


			@BeforeEach
			public void insertarCentro() {
				centroRepository.save(Mapper.toCentro(centro1));
			}

			@Test
			@DisplayName("Devuelve la lista de mensajes asociada a un centro vacía")
			public void devuelveListaMensajes() {
				var peticion = get("http", "localhost", port, "/mensaje/centro");
				ResponseEntity<List<MensajeDTO>> responseEntity = restTemplate.exchange("http://localhost:" + port + "/mensaje/centro?centro=1",
						HttpMethod.GET,
						null,
						new ParameterizedTypeReference<List<MensajeDTO>>() {
						});

				assertThat(responseEntity.getStatusCode().value()).isEqualTo(404);
				List<MensajeDTO> listaMensajes = responseEntity.getBody(); // Obtener el cuerpo de la respuesta
				//assertThat(listaMensajes).size().isEqualTo(0); // Verificar que la lista de mensajes está vacía
			}


			// TO DO

		}
	}
}






