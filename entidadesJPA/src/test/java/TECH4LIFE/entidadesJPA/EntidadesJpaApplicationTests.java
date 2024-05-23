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
import TECH4LIFE.entidadesJPA.security.JwtUtil;
import java.util.Base64;
import java.util.List;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.apache.tomcat.util.http.parser.Authorization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de de gestión de centros y gerentes")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
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

	// Agregamos seguridad
	@Autowired
	private JwtUtil jwtUtil;
	private UserDetails userDetails;
	private String token;
	@Autowired
    private MockMvc mockMvc;


	@BeforeEach
	public void initializeDatabase() {
		centroRepository.deleteAll();
		gerenteRepository.deleteAll();
		mensajeRepository.deleteAll();
		userDetails = jwtUtil.createUserDetails("1", "", List.of("ROLE_USER", "ROLE_ADMIN", "ROLE_GERENTE"));
		token = jwtUtil.generateToken(userDetails);
	}

	/*
	 * ---------------------------------------------
	 * Métodos comunes a todas las pruebas
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
				.header("Authorization", "Bearer " + token)
				.build();
		return peticion;
	}

	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.delete(uri)
				.header("Authorization", "Bearer " + token)
				.build();
		return peticion;
	}

	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.post(uri).header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.put(uri)
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	/*
	 * ---------------------------------------------
	 * Pruebas de ControladorGerente y LogicaGerente
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

	private String createBasicAuthHeader(String username, String password) {
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
		return "Basic " + new String(encodedAuth);
	}

	@Nested
	@DisplayName("Cuando no hay Gerentes")
	public class ListaGerentesVacia {
		@Nested
		@DisplayName("Metodos GET gerente lista vacia")
		public class GetGerentesVacia {

			@Test
			@DisplayName("Devuelve la lista de gerentes vacía")
			@WithMockUser(username = "admin", roles = {"ROLE_ADMIN"})
			public void devuelveListaGerentes() {
				 // Construye la URL de la petición utilizando el método get
				 URI url = uri("http", "localhost", port, "/gerente");

				 // Realiza la solicitud HTTP GET con los encabezados de autenticación
				 ResponseEntity<List<GerenteDTO>> respuesta = restTemplate.exchange(
						 url,
						 HttpMethod.GET,
						 null,
						 new ParameterizedTypeReference<List<GerenteDTO>>() {});
			 
				 // Verifica la respuesta
				 assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				 assertThat(respuesta.hasBody()).isEqualTo(false);
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

		}

		@Nested
		@DisplayName("Metodos POST gerente lista vacia")
		public class PostGerentesVacia {
			@Test
			@DisplayName("Intenta crear un gerente cuando no hay gerentes")
			public void CrearGerenteBien() {
				var gerente = Gerente.builder()
						.empresa("patatas")
						.idUsuario(6)
						.build();
				var peticion = post("http", "localhost", port, "/gerente", gerente);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			}

		}

		@Nested
		@DisplayName("Metodos PUT gerente lista vacia")
		public class PutGerentesVacia {
			@Test
			@DisplayName("devuelve error cuando se modifica un gerente concreto")
			public void devuelveErrorAlModificarGerente() {
				var gerente = GerenteNuevoDTO.builder()
						.empresa("kfc")
						.build();
				var peticion = put("http", "localhost", port, "/gerente/2", gerente);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			}
		}

		@Nested
		@DisplayName("Metodos DELETE gerente lista vacia")
		public class DeleteGerentesVacia {
			@Test
			@DisplayName("devuelve error cuando se elimina un Gerente concreto")
			public void devuelveErrorAlEliminarGerente() {
				var peticion = delete("http", "localhost", port, "/gerente/40");

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			}
		}
	}

	@Nested
	@DisplayName("Cuando hay Gerentes")
	public class ListaGerentesConDatos {

		private GerenteNuevoDTO gerente1 = GerenteNuevoDTO.builder()
				.empresa("patatas")
				.idUsuario(1)
				.build();

		private GerenteNuevoDTO gerente2 = GerenteNuevoDTO.builder()
				.empresa("brocoli")
				.idUsuario(2)
				.build();

		@BeforeEach
		public void introduceDatosGerente() {
			gerenteRepository.save(Mapper.toGerente(gerente1));
			gerenteRepository.save(Mapper.toGerente(gerente2));
		}

		@Nested
		@DisplayName("Metodos Get gerente en Lista llena")
		public class GetGerente {
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

			@Test
			@DisplayName("lo devuelve cuando existe")
			public void devuelveGerente() {
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
		@DisplayName("Metodos PUT gerente lista llena")
		public class PutGerentes {

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
		@DisplayName("Metodos DELETE un gerente lista llena")
		public class DeleteGerentes {
			@Test
			@DisplayName("Lo elimina cuando existe")
			public void eliminaCorrectamenteGerente() {

				var peticion = delete("http", "localhost", port, "/gerente/1");

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				List<Gerente> gerentes = gerenteRepository.findAll();
				assertThat(gerentes).hasSize(1);
				assertThat(gerentes).allMatch(c -> c.getId() != 1);
			}

			@Test
			@DisplayName("da error cuando no existe")
			public void errorCuandoNoExisteGerente() {
				var peticion = delete("http", "localhost", port, "/gerente/40");

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				assertThat(respuesta.hasBody()).isEqualTo(false);
			}
		}

		@Nested
		@DisplayName("Metodos POST gerente en lista llena")
		public class PostGerente {
			@Test
			@DisplayName("Crea un gerente bien")
			public void CrearGerenteBien() {
				var gerente = Gerente.builder()
						.empresa("patatas")
						.idUsuario(6)
						.build();
				var peticion = post("http", "localhost", port, "/gerente", gerente);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			}

		}

	}
}
