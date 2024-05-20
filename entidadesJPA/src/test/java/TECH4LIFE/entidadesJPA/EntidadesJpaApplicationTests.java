package TECH4LIFE.entidadesJPA;
import TECH4LIFE.entidadesJPA.controladores.Mapper;
import TECH4LIFE.entidadesJPA.dtos.CentroDTO;
import TECH4LIFE.entidadesJPA.dtos.CentroNuevoDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteDTO;
import TECH4LIFE.entidadesJPA.dtos.GerenteNuevoDTO;
import TECH4LIFE.entidadesJPA.dtos.IdGerenteDTO;
import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Gerente;
import TECH4LIFE.entidadesJPA.repositories.CentroRepository;
import TECH4LIFE.entidadesJPA.repositories.GerenteRepository;
import TECH4LIFE.entidadesJPA.repositories.MensajeRepository;
import jakarta.transaction.Transactional;

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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isNull;


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
 *
 * 		SOLUCIÓN :
	 * 	 - Para evitar este error, ubicar el proyecto en un sitio cuyo enlace no tenga carácteres extraños, es
	 * 	   decir ni tildes, ni espacios...
 *
 * */

// Controlar el resto de excepciones para conseguir más cobertura en controladorCentro informe Jacoco

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
	@DisplayName("En cuanto a los centros")
	public class PruebasCentros {

		@Nested
		@DisplayName("Cuando no hay centros")
		public class ListaCentrosVacia {

			@Nested
			@DisplayName("y queremos obtenerlos")
			public class GetCentrosVacia {

				@Test
				@DisplayName("Devuelve la lista de centros vacía")
				public void devuelveListaCentro() {

					var peticion = get("http", "localhost", port, "/centro"); // Revisar el path

					var respuesta = restTemplate.exchange(peticion,
							new ParameterizedTypeReference<List<CentroDTO>>() {
							});

					assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
					assertThat(respuesta.hasBody()).isEqualTo(false);
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
				@DisplayName("Devuelve error cuando se pide el gerente de un centro concreto")
				public void devuelveErrorAlConsultarGerenteDeCentro() {
					var peticion = get("http", "localhost", port, "/centro/1/gerente");

					var respuesta = restTemplate.exchange(peticion,
							new ParameterizedTypeReference<List<CentroDTO>>() {
							});

					assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
					assertThat(respuesta.hasBody()).isEqualTo(false);
				}

				@Test
				@DisplayName("Devuelve error cuando se pide el gerente de un centro con id no valida")
				public void devuelveErrorAlConsultarGerenteDeCentroNoValido() {
					var peticion = get("http", "localhost", port, "/centro/-1/gerente");

					var respuesta = restTemplate.exchange(peticion,
							new ParameterizedTypeReference<List<CentroDTO>>() {
							});

					assertThat(respuesta.getStatusCode().value()).isEqualTo(400);
					assertThat(respuesta.hasBody()).isEqualTo(false);
				}

				
			}

			@Nested
			@DisplayName("y queremos insertar un centro")
			public class InsertaCentroVacia {

				@Test
				@DisplayName("y se guarda con éxito")
				public void crearCentro() {
					var centro = CentroNuevoDTO.builder()
							.nombre("egeFIT")
							.direccion("Calle merluza, 56")
							.build();
					var peticion = post("http", "localhost", port, "/centro", centro);

					var respuesta = restTemplate.exchange(peticion, Void.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(201);

					// Yo creo que faltaría verificar si se ha añadido, no solo el código
					// Y esto se haría con el método compruebaRespuestaCentro que habría que corregirlo
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

				@Test
				@DisplayName("Da error cuando la peticion no es valida")
				public void crearCentroNoValido() {
					
					var centro = CentroNuevoDTO.builder()
					.nombre(null)
					.build();

					var peticion = post("http", "localhost", port, "/centro", centro);

					var respuesta = restTemplate.exchange(peticion, Void.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(400);
				}

			}

			@Nested
			@DisplayName("y queremos modificar un centro")
			public class ModificaCentroVacia {

				@Test
				@DisplayName("devuelve error cuando se modifica un centro concreto")
				public void devuelveErrorAlModificarCentro() {
					var centro = CentroNuevoDTO.builder()
							.nombre("KKFit")
							.direccion("Calle la calle KK, 56")
							.build();
					var peticion = put("http", "localhost",port, "/centro/2", centro);

					var respuesta = restTemplate.exchange(peticion, Void.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				}

				@Test
				@DisplayName("devuelve error cuando se intenta añadir una asociacion a un centro no existente")
				public void devuelveErrorAlAñadirAsociacionNoExistente() {
					var idGerente = IdGerenteDTO.builder()
									.idGerente(1)
									.build();
					var peticion = put("http", "localhost",port, "/centro/1/gerente", idGerente);

					var respuesta = restTemplate.exchange(peticion, Void.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				}

				@Test
				@DisplayName("devuelve error cuando se intenta añadir una asociacion a un centro no valido")
				public void devuelveErrorAlAñadirAsociacionNoValido() {
					var idGerente = IdGerenteDTO.builder()
									.idGerente(1)
									.build();
					var peticion = put("http", "localhost",port, "/centro/-1/gerente", idGerente);

					var respuesta = restTemplate.exchange(peticion, Void.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(400);
				}

				@Test
				@DisplayName("devuelve error cuando se realiza una peticion no valida")
				public void devuelveErrorAlModificarCentroNoValida() {
					var centro = CentroNuevoDTO.builder()
							.nombre("FitFit")
							.direccion("Calle Fit, 8")
							.build();
					var peticion = put("http", "localhost",port, "/centro/-2", centro);

					var respuesta = restTemplate.exchange(peticion, Void.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(400);
				}
			}

			@Nested
			@DisplayName("y queremos eliminar un centro")
			public class EliminaCentroVacia {

				@Test
				@DisplayName("devuelve error cuando se elimina un centro concreto")
				public void devuelveErrorAlEliminarCentro() {
					var peticion = delete("http", "localhost",port, "/centro/2");

					var respuesta = restTemplate.exchange(peticion, Void.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				}

				@Test
				@DisplayName("devuelve error cuando se elimina un centro con id no valida")
				public void devuelveErrorAlEliminarCentroNoValidoVacio() {
					var peticion = delete("http", "localhost",port, "/centro/-2");

					var respuesta = restTemplate.exchange(peticion, Void.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(400);
				}

				@Test
				@DisplayName("devuelve error cuando se elimina una asociacion de un centro concreto")
				public void devuelveErrorAlEliminarAsociacionCentro() {
        			var peticion = delete("http", "localhost",port, "/centro/1/gerente");

					var respuesta = restTemplate.exchange(peticion, Void.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				}

				
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

			private Gerente gerente2 = Gerente.builder()
					.id(2)
					.idUsuario(3)
					.build();
					
			
			@BeforeEach
			@Transactional
			public void introduceDatosCentro() {
				centroRepository.save(Mapper.toCentro(centro1));
				centroRepository.save(Mapper.toCentro(centro2));
				gerenteRepository.save(gerente2);

				Gerente gerente1 = Gerente.builder()
					.id(1)
					.idUsuario(2)
					.build();

				Centro centro3 = Centro.builder()
					.nombre("Centro 3")
					.idCentro(3)
					.direccion("Calle del centro3, 3")
					.build();

				gerenteRepository.save(gerente1);
				centroRepository.save(centro3);
				centro3.setGerente(gerente1);
				gerente1.setCentro(centro3);
				centroRepository.save(centro3);
				gerenteRepository.save(gerente1);
			}


			@Nested
			@DisplayName("y queremos obtenerlos")
			public class GetCentrosLlena {
				@Test
				@DisplayName("Devuelve la lista de centros correctamente")
				public void devuelveListaCentro() {

					var peticion = get("http", "localhost", port, "/centro"); // Revisar el path

					var respuesta = restTemplate.exchange(peticion,
							new ParameterizedTypeReference<List<CentroDTO>>() {
							});

					assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
					assertThat(respuesta.getBody()).hasSize(3);
				}

				@Test
				@DisplayName("Da error cuando el id del gerente no es valido")
				public void ErrordevuelveListaCentroIdGerenteNoValido() {
					var gerente = new Gerente().builder()
									.id(-3)
									.build();
					String url = String.format("http://localhost:%d/centro?gerente=%d", port, gerente.getId());

        		ResponseEntity<List<CentroDTO>> respuesta = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CentroDTO>>() {}
        		);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(400);
				}


				@Test
				@DisplayName("Devuelve la lista de centros de un gerente correctamente")
				public void devuelveListaCentroGerente() {

					
					String url = String.format("http://localhost:%d/centro?gerente=%d", port, 1);

					ResponseEntity<List<CentroDTO>> respuesta = restTemplate.exchange(
                	url,
                	HttpMethod.GET,
                	null,
                	new ParameterizedTypeReference<List<CentroDTO>>() {}
        			);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
					assertThat(respuesta.getBody()).isNotNull();
    				assertThat(respuesta.getBody()).isNotEmpty();
				} 

				@Test
				@DisplayName("devuelve un centro concreto cuando existe")
				public void devuelveCentro() {
					var peticion = get("http", "localhost", port, "/centro/1");

					var respuesta = restTemplate.exchange(peticion, CentroDTO.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
					assertThat(respuesta.hasBody()).isTrue();
					assertThat(respuesta.getBody()).isNotNull();
				}
				
				@Test
				@DisplayName("devuelve el gerente de un centro concreto cuando existe")
				public void devuelveGerenteCentro() {
					//(Mapper.toCentroDTO(Mapper.toCentro(null))).
					var peticion = get("http", "localhost", port, "/centro/3/gerente");

					var respuesta = restTemplate.exchange(peticion, GerenteDTO.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
					assertThat(respuesta.hasBody()).isTrue();
					assertThat(respuesta.getBody()).isNotNull();
				}

				@Test
				@DisplayName("da error cuando no existe el centro concreto")
				public void errorCuandoCentroNoExiste() {
					var peticion = get("http", "localhost", port, "/centro/28");

					var respuesta = restTemplate.exchange(peticion,
							new ParameterizedTypeReference<List<CentroDTO>>() {
							});

					assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
					assertThat(respuesta.hasBody()).isEqualTo(false);
				}

				@Test
				@DisplayName("da error cuando el id del centro no es valido")
				public void errorCuandoCentroNoValido() {
					var peticion = get("http", "localhost", port, "/centro/-3");

					var respuesta = restTemplate.exchange(peticion,
							new ParameterizedTypeReference<List<CentroDTO>>() {
							});

					assertThat(respuesta.getStatusCode().value()).isEqualTo(400);
					assertThat(respuesta.hasBody()).isEqualTo(false);
				}

			}

			@Nested
			@DisplayName("y queremos insertar un centro")
			public class InsertaCentroLlena {

				@Test
				@DisplayName("y lo consigue cuando el centro a insertar no es null")
				public void centroNoNull() {
					var centro = CentroNuevoDTO.builder()
							.nombre("egergdrgFIT")
							.direccion("Calle pescaito, 54")
							.build();
					var peticion = post("http", "localhost", port, "/centro", centro);
					// La petición necesita una query string

					var respuesta = restTemplate.exchange(peticion, Void.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(201);

					compruebaRespuestaCentro(centro, respuesta);
				}

				@Test
				@DisplayName("pero da error cuando el centro a insertar es null")
				public void centroNull() {
					CentroNuevoDTO centro = null;
					var peticion = post("http", "localhost", port, "/centro", centro);

					var respuesta = restTemplate.exchange(peticion, Void.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(400);

					//compruebaRespuestaCentro(centro, respuesta);
				}

				private void compruebaRespuestaCentro(CentroNuevoDTO centro, ResponseEntity<Void> respuesta) {

					assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
					assertThat(respuesta.getHeaders().get("Location").get(0))
							.startsWith("http://localhost:" + port + "/centro");

					List<Centro> centros = centroRepository.findAll();
					assertThat(centros).hasSize(4);

					Centro centroStream = centros.stream()
							.filter(c -> c.getDireccion().equals("Calle pescaito, 54"))
							.findAny()
							.get();

					assertThat(respuesta.getHeaders().get("Location").get(0))
							.endsWith("/" + centroStream.getIdCentro());
					compruebaCamposCentro(centro, centroStream);
				}
			}

			@Nested
			@DisplayName("y queremos modificar un centro")
			public class ModificaCentroLlena {

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
			@DisplayName("y queremos eliminar un centro")
			public class EliminaCentroLlena {

				@Test
				@DisplayName("Lo elimina cuando existe")
				public void eliminaCorrectamenteCentro() {
					//List<Centro> centrosAntes = centroRepository.findAll();
					//centrosAntes.forEach(c->System.out.println(c));
					var peticion = delete("http", "localhost",port, "/centro/1");

					var respuesta = restTemplate.exchange(peticion,Void.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
					List<Centro> centros = centroRepository.findAll();
					assertThat(centros).hasSize(2);
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
			
				@Test
				@DisplayName("Elimina una asociacion de un gerente de un centro concreto correctamente")
				public void devuelveErrorAlEliminarAsociacionCentro() {
				String url = String.format("http://localhost:%d/centro/3/gerente?gerente=%d", port, 1);

					var respuesta = restTemplate.exchange(
                	url,
                	HttpMethod.DELETE,
                	null,
                	Void.class
        			);
					assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
					Optional<Centro> centro = centroRepository.findById(3);
					assertThat(centro.get().getGerente()).isEqualTo(null);
				}

			}

			@Nested
			@DisplayName("Y queremos añadir una asociacion entre un gerente y un centro")
			public class AsociacionLlena {
			//DA ERROR 500 ¿?
				@Test
				@DisplayName("Se añade correctamente")
				@DirtiesContext
				public void añadeAsociacionGerenteCentro() {
					var gerente = Gerente.builder()
							.id(5)
							.idUsuario(4)
							.empresa("hola")
							.build();
					var id = IdGerenteDTO.builder()
								.idGerente(gerente.getId())
								.build();

					
					var peticion = put("http", "localhost", port, "/centro/3/gerente", id);

					var respuesta = restTemplate.exchange(peticion, CentroDTO.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
					var centroBD = centroRepository.findById(3).get();
    				assertThat(centroBD.getGerente().getId()).isEqualTo(5);
				}
				
				@Test
				@DisplayName("Da error cuando no existe")
				public void errorCuandoNoExiste() {
					var centro = CentroNuevoDTO.builder()
							.nombre("PepeitoFit")
							.direccion("Calle la gata, 56")
							.build();
					var gerente = GerenteNuevoDTO.builder()
							.idUsuario(1)
							.empresa("hola")
							.build();
					var idGerenteDTO = IdGerenteDTO.builder()
					.idGerente(gerente.getIdUsuario())
					.build();
					var peticion = put("http", "localhost", port, "/centro/28", idGerenteDTO);

					var respuesta = restTemplate.exchange(peticion, Void.class);

					assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
					assertThat(respuesta.hasBody()).isEqualTo(false);
				}
				
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






