package TECH4LIFE.entidadesJPA;
import TECH4LIFE.entidadesJPA.controladores.Mapper;
import TECH4LIFE.entidadesJPA.dtos.CentroDTO;
import TECH4LIFE.entidadesJPA.dtos.CentroNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
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

	@Value(value="${local.server.port}")
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
	 Pruebas de ControladorCentro y LogicaCentro
	 Realizado por: Raúl García Balongo
	---------------------------------------------
	*/

	private void compruebaCamposCentro(CentroDTO expected, CentroDTO actual) {
		assertThat(actual.getDireccion()).isEqualTo(expected.getDireccion());
		assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
	}

	private void compruebaCamposCentro(CentroNuevoDTO expected, CentroDTO actual) {
		assertThat(actual.getDireccion()).isEqualTo(expected.getDireccion());
		assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
	}

	private void compruebaCamposCentro(CentroNuevoDTO expected, Centro actual) {
		assertThat(actual.getDireccion()).isEqualTo(expected.getDireccion());
		assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
	}

	@Nested
	@DisplayName("Cuando no hay centros")
	public class ListaCentrosVacia {

		@Test
		@DisplayName("Devuelve la lista de centros vacía")
		public void devuelveListaCentro() {

			var peticion = get("http", "localhost", port, "/centro"); // Revisar el path

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<CentroDTO>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isEmpty();
		}

		@Nested
		@DisplayName("Intenta insertar un centro")
		public class InsertaCentro {
			@Test
			@DisplayName("y se guarda con éxito")
			public void sinID() {
				var centro = CentroNuevoDTO.builder()
						.nombre("egeFIT")
						.direccion("Calle merluza, 56")
						.build();
				var peticion = post("http", "localhost", port, "/centro", centro);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				compruebaRespuestaCentro(centro, respuesta);
			}

			private void compruebaRespuestaCentro(CentroNuevoDTO centro, ResponseEntity<Void> respuesta) {
				assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
				assertThat(respuesta.getHeaders().get("Location").get(0))
						.startsWith("http://localhost:" + port + "/centro");

				List<Centro> centros = centroRepository.findAll();
				assertThat(centros).hasSize(1);
				assertThat(respuesta.getHeaders().get("Location").get(0))
						.endsWith("/" + centros.get(0).getIdCentro());
				compruebaCamposCentro(centro, centros.get(0));
			}
		}

		@Test
		@DisplayName("Devuelve error cuando se pide un centro concreto")
		public void devuelveErrorAlConsultarCentro() {
			var peticion = get("http", "localhost", port, "/centro");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<CentroDTO>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			assertThat(respuesta.hasBody()).isEqualTo(false);
		}

		@Test
		@DisplayName("devuelve error cuando se modifica un centro concreto")
		public void devuelveErrorAlModificarCentro() {
			var centro = CentroNuevoDTO.builder()
					.nombre("KKFit")
					.direccion("Calle la calle KK, 56")
					.build();
			var peticion = put("http", "localhost",port, "/centro", centro);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error cuando se elimina un centro concreto")
		public void devuelveErrorAlEliminarCentro() {
			var peticion = delete("http", "localhost",port, "/centro");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
	}

	@Nested
	@DisplayName("Cuando hay centros")
	public class ListaCentrosConDatos {

		private CentroNuevoDTO centro1 = CentroNuevoDTO.builder()
				.nombre("BasicFit")
				.direccion("Calle la calle bonita, 56")
				.build();

		private CentroNuevoDTO centro2 = CentroNuevoDTO.builder()
				.nombre("ProGYM")
				.direccion("Calle avestruz, 44")
				.build();


		@BeforeEach
		public void introduceDatosCentro() {
			centroRepository.save(Mapper.toCentro(centro1));
			centroRepository.save(Mapper.toCentro(centro2));
		}

		@Test
		@DisplayName("Devuelve la lista de centros correctamente")
		public void devuelveListaCentro() {
			var peticion = get("http", "localhost", port, "/centro"); // Revisar el path

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<CentroDTO>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).hasSize(2);
		}

		@Nested
		@DisplayName("Intenta insertar un centro")
		public class InsertaCentros {

			// TO DO

			@Test
			@DisplayName("")
			public void prueba1() {
				// TO DO
			}

			@Test
			@DisplayName("")
			public void prueba2() {
				// TO DO
			}

			private void compruebaRespuesta(CentroNuevoDTO centro, ResponseEntity<Void> respuesta) {
				// TO DO
			}
		}

		@Nested
		@DisplayName("Al consultar un centro concreto")
		public class ObtenerCentros {

			@Test
			@DisplayName("lo devuelve cuando existe")
			public void devuelveCentro() {
				var peticion = get("http", "localhost", port, "/centro/1");

				var respuesta = restTemplate.exchange(peticion, CentroDTO.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				assertThat(respuesta.hasBody()).isTrue();
				assertThat(respuesta.getBody()).isNotNull();
			}

			@Test
			@DisplayName("da error cuando no existe")
			public void errorCuandoCentroNoExiste() {
				var peticion = get("http", "localhost", port, "/centro/28");

				var respuesta = restTemplate.exchange(peticion,
						new ParameterizedTypeReference<List<CentroDTO>>() {
						});

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				assertThat(respuesta.hasBody()).isEqualTo(false);
			}
		}

		@Nested
		@DisplayName("Al modificar un centro")
		public class ModificarCentros {

			@Test
			@DisplayName("Lo modifica correctamente cuando existe")
			@DirtiesContext
			public void modificaCorrectamenteCentro() {
				var centro = CentroNuevoDTO.builder()
						.nombre("PepeFit")
						.direccion("Calle la gaviota, 56")
						.build();

				var peticion = put("http", "localhost", port, "/centro/1", centro);

				var respuesta = restTemplate.exchange(peticion, CentroDTO.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				Centro centroBD = centroRepository.findById(1).get();
				compruebaCamposCentro(centro, centroBD);
			}

			@Test
			@DisplayName("Da error cuando no existe")
			public void errorCuandoNoExiste() {
				var centro = CentroNuevoDTO.builder()
						.nombre("PepeitoFit")
						.direccion("Calle la gata, 56")
						.build();
				var peticion = put("http", "localhost", port, "/centro/28", centro);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				assertThat(respuesta.hasBody()).isEqualTo(false);
			}
		}

		@Nested
		@DisplayName("Al eliminar un centro")
		public class EliminarCentros {
			@Test
			@DisplayName("Lo elimina cuando existe")
			public void eliminaCorrectamenteCentro() {
				//List<Centro> centrosAntes = centroRepository.findAll();
				//centrosAntes.forEach(c->System.out.println(c));
				var peticion = delete("http", "localhost",port, "/centro/1");

				var respuesta = restTemplate.exchange(peticion,Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				List<Centro> centros = centroRepository.findAll();
				assertThat(centros).hasSize(1);
				assertThat(centros).allMatch(c->c.getIdCentro()!=1);
			}

			@Test
			@DisplayName("da error cuando no existe")
			public void errorCuandoNoExisteCentro() {
				var peticion = delete("http", "localhost",port, "/centro/28");

				var respuesta = restTemplate.exchange(peticion,Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				assertThat(respuesta.hasBody()).isEqualTo(false);
			}
		}
	}


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

	// TO DO

}






