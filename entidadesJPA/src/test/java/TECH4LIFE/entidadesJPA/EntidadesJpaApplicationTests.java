package TECH4LIFE.entidadesJPA;

import TECH4LIFE.entidadesJPA.controladores.Mapper;
import TECH4LIFE.entidadesJPA.dtos.CentroDTO;
import TECH4LIFE.entidadesJPA.dtos.CentroNuevoDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteNuevoDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Gerente;
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
	 * ---------------------------------------------
	 * Ajustes iniciales
	 * ---------------------------------------------
	 */

	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value = "${local.server.port}")
	private int port;

	/*
	 * ---------------------------------------------
	 * Inyección de los repositorios
	 * ---------------------------------------------
	 */

	@Autowired
	private CentroRepository centroRepository;
	@Autowired
	private GerenteRepository gerenteRepository;
	@Autowired
	private MensajeRepository mensajeRepository;

	/*
	 * ---------------------------------------------
	 * Inicialización de la base de datos
	 * ---------------------------------------------
	 */

	@BeforeEach
	public void initializeDatabase() {
		centroRepository.deleteAll();
		gerenteRepository.deleteAll();
		mensajeRepository.deleteAll();
	}

	/*
	 * ---------------------------------------------
	 * Métodos comunes a todas las pruebas
	 * Realizado por: Raúl García Balongo
	 * ---------------------------------------------
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
	 * ---------------------------------------------
	 * Pruebas de ControladorGerente y LogicaGerente
	 * Realizado por:
	 * ---------------------------------------------
	 */
	private void compruebaCamposGerente(GerenteDTO expected, GerenteDTO actual) {
		assertThat(actual.getEmpresa()).isEqualTo(expected.getEmpresa());
	}

	private void compruebaCamposGerente(GerenteNuevoDTO expected, GerenteDTO actual) {
		assertThat(actual.getEmpresa()).isEqualTo(expected.getEmpresa());
	}

	private void compruebaCamposGerente(GerenteNuevoDTO expected, Gerente actual) {
		assertThat(actual.getEmpresa()).isEqualTo(expected.getEmpresa());
	}

	@Nested
	@DisplayName("Cuando no hay Gerentes")
	public class ListaGerentesVacia {

		@Test
		@DisplayName("Devuelve la lista de gerentes vacía")
		public void devuelveListaGerentes() {

			var peticion = get("http", "localhost", port, "/gerente"); 

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<GerenteDTO>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			assertThat(respuesta.hasBody()).isEqualTo(false);
		}

		@Nested
		@DisplayName("Intenta insertar un Gerente")
		public class InsertaGerente {
			@Test
			@DisplayName("y se guarda con éxito")
			public void sinID() {
				var gerente = GerenteNuevoDTO.builder()
						.empresa("kfc")
						.build();
				var peticion = post("http", "localhost", port, "/gerente", gerente);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				compruebaRespuestaGerente(gerente, respuesta);
			}

			private void compruebaRespuestaGerente(GerenteNuevoDTO gerente, ResponseEntity<Void> respuesta) {
				assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
				assertThat(respuesta.getHeaders().get("Location").get(0))
						.startsWith("http://localhost:" + port + "/gerente");

				List<Gerente> gerentes = gerenteRepository.findAll();
				assertThat(gerentes).hasSize(1);
				assertThat(respuesta.getHeaders().get("Location").get(0))
						.endsWith("/" + gerentes.get(0).getId());
				compruebaCamposGerente(gerente, gerentes.get(0));
			}
		}

		@Test
		@DisplayName("Devuelve error cuando se pide un Gerente concreto")
		public void devuelveErrorAlConsultarGerente() {
			var peticion = get("http", "localhost", port, "/gerente");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<GerenteDTO>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			assertThat(respuesta.hasBody()).isEqualTo(false);
		}

		@Test
		@DisplayName("devuelve error cuando se modifica un gerente concreto")
		public void devuelveErrorAlModificarGerente() {
			var gerente = GerenteNuevoDTO.builder()
					.empresa("kfc")
					.build();
			var peticion = put("http", "localhost",port, "/gerente/2", gerente);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error cuando se elimina un Gerente concreto")
		public void devuelveErrorAlEliminarGerente() {
			var peticion = delete("http", "localhost",port, "/gerente/40");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
	}

	@Nested
	@DisplayName("Cuando hay Gerentes")
	public class ListaGerentesConDatos {

		private GerenteNuevoDTO gerente1 = GerenteNuevoDTO.builder()
				.empresa("patatas")
				.build();

		private GerenteNuevoDTO gerente2 = GerenteNuevoDTO.builder()
				.empresa("brocoli")
				.build();


		@BeforeEach
		public void introduceDatosGerente() {
			gerenteRepository.save(Mapper.toGerente(gerente1));
			gerenteRepository.save(Mapper.toGerente(gerente2));
		}

		@Test
		@DisplayName("Devuelve la lista de Gerentes correctamente")
		public void devuelveListaGerente() {
			var peticion = get("http", "localhost", port, "/gerente"); 

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<GerenteDTO>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).hasSize(2);
		}

		@Nested
		@DisplayName("Intenta insertar un gerente")
		public class InsertaGerente {

			@Test
			@DisplayName("y lo consigue cuando el gerente a insertar no es null")
			public void gerenteNoNull() {
				var gerente = GerenteNuevoDTO.builder()
						.empresa("patatas")
						.build();
				var peticion = post("http", "localhost", port, "/gerente", gerente);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				compruebaRespuestaGerente(gerente, respuesta);
			}

			@Test
			@DisplayName("pero da error cuando el gerente a insertar es null")
			public void gerenteNull() {
				GerenteNuevoDTO gerente= null;
				var peticion = post("http", "localhost", port, "/gerente", gerente);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				compruebaRespuestaGerente(gerente, respuesta);
			}

			private void compruebaRespuestaGerente(GerenteNuevoDTO gerente, ResponseEntity<Void> respuesta) {

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				assertThat(respuesta.getHeaders().get("Location").get(0))
					.startsWith("http://localhost:"+port+"/gerente");
				
				List<Gerente> gerentes = gerenteRepository.findAll();
				assertThat(gerentes).hasSize(3);
				
				Gerente patatas = gerentes.stream()
						.filter(c->c.getEmpresa().equals("patatas"))
						.findAny()
						.get();
				
				assertThat(respuesta.getHeaders().get("Location").get(0))
					.endsWith("/"+patatas.getId());
				compruebaCamposGerente(gerente, patatas);
			}
		}

		@Nested
		@DisplayName("Al consultar un gerente concreto")
		public class ObtenerGerentes {

			@Test
			@DisplayName("lo devuelve cuando existe")
			public void devuelveCentro() {
				var peticion = get("http", "localhost", port, "/gerente/1");

				var respuesta = restTemplate.exchange(peticion, GerenteDTO.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				assertThat(respuesta.hasBody()).isTrue();
				assertThat(respuesta.getBody()).isNotNull();
			}

			@Test
			@DisplayName("da error cuando no existe")
			public void errorCuandoGerenteNoExiste() {
				var peticion = get("http", "localhost", port, "/gerente/40");

				var respuesta = restTemplate.exchange(peticion,
						new ParameterizedTypeReference<List<GerenteDTO>>() {
						});

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				assertThat(respuesta.hasBody()).isEqualTo(false);
			}
		}

		@Nested
		@DisplayName("Al modificar un gerente")
		public class ModificarGerentes {

			@Test
			@DisplayName("Lo modifica correctamente cuando existe")
			@DirtiesContext
			public void modificaCorrectamenteGerente() {
				var gerente = GerenteNuevoDTO.builder()
						.empresa("yeehaw")
						.build();

				var peticion = put("http", "localhost", port, "/gerente/1", gerente);

				var respuesta = restTemplate.exchange(peticion, GerenteDTO.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				Gerente gerenteBD = gerenteRepository.findById(1).get();
				compruebaCamposGerente(gerente, gerenteBD);
			}

			@Test
			@DisplayName("Da error cuando no existe")
			public void errorCuandoNoExiste() {
				var gerente = GerenteNuevoDTO.builder()
						.empresa("yeehow")
						.build();
				var peticion = put("http", "localhost", port, "/gerente/40", gerente);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				assertThat(respuesta.hasBody()).isEqualTo(false);
			}
		}

		@Nested
		@DisplayName("Al eliminar un gerente")
		public class EliminarGerentes {
			@Test
			@DisplayName("Lo elimina cuando existe")
			public void eliminaCorrectamenteGerente() {
		
				var peticion = delete("http", "localhost",port, "/gerente/1");

				var respuesta = restTemplate.exchange(peticion,Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				List<Gerente> gerentes = gerenteRepository.findAll();
				assertThat(gerentes).hasSize(1);
				assertThat(gerentes).allMatch(c->c.getId()!=1);
			}

			@Test
			@DisplayName("da error cuando no existe")
			public void errorCuandoNoExisteGerente() {
				var peticion = delete("http", "localhost",port, "/gerente/40");

				var respuesta = restTemplate.exchange(peticion,Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				assertThat(respuesta.hasBody()).isEqualTo(false);
			}
		}
	}
}
/*
 * ---------------------------------------------
 * Pruebas de ControladorMensaje y LogicaMensaje
 * Realizado por:
 * ---------------------------------------------
 */

// TO DO
