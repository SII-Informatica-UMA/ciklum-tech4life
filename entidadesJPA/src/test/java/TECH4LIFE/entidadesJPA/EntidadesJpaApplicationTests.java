package TECH4LIFE.entidadesJPA;

import TECH4LIFE.entidadesJPA.controladores.Mapper;
import TECH4LIFE.entidadesJPA.dtos.CentroDTO;
import TECH4LIFE.entidadesJPA.dtos.CentroNuevoDTO;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de de gestión de centros y gerentes")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EntidadesJpaApplicationTests {
	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value="${local.server.port}")
	private int port;

	@Autowired
	private CentroRepository centroRepository;
	@Autowired
	private GerenteRepository gerenteRepository;
	@Autowired
	private MensajeRepository mensajeRepository;

	@BeforeEach
	public void initializeDatabase() {
		centroRepository.deleteAll();
		gerenteRepository.deleteAll();
		mensajeRepository.deleteAll();
	}

	// Estoy trasteando

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


	@Nested
	@DisplayName("Cuando no hay centros")
	public class ListaCentrosVacia{
		@Test
		@DisplayName("Devuelve la lista de centros vacía")
		public void devuelveLista() {

			var peticion = get("http", "localhost",port, "/centro"); // Revisar el path

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<CentroDTO>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isEmpty();
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
		public void introduceDatos() {
			centroRepository.save(Mapper.toCentro(centro1));
			centroRepository.save(Mapper.toCentro(centro2));
		}

		@Test
		@DisplayName("Devuelve la lista de centros correctamente")
		public void devuelveLista() {
			var peticion = get("http", "localhost", port, "/centro"); // Revisar el path

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<CentroDTO>>() {
					});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).hasSize(2);
		}
	}
}
