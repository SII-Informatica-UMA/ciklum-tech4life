package TECH4LIFE.entidadesJPA;
import TECH4LIFE.entidadesJPA.controladores.Mapper;
import TECH4LIFE.entidadesJPA.dtos.CentroDTO;
import TECH4LIFE.entidadesJPA.dtos.CentroNuevoDTO;
import TECH4LIFE.entidadesJPA.dtos.MensajeDTO;
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
import org.springframework.http.HttpStatus;
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
	 Pruebas de ControladorMensaje y LogicaMensaje
	 Realizado por: Ana María Calvente Bonvie
	---------------------------------------------
	*/



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

			/*@BeforeEach
			public void insertarCentro(){
				Centro centro1 = new Centro();
				centro1.setIdCentro(1);
				centroRepository.save(centro1);
			}*/

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
			@DisplayName("Devuelve la lista de mensajes asociada a un centro vacía")
			public void devuelveListaMensajes(){
				var peticion = get("http", "localhost", port, "/mensaje/centro");
				var respuesta = restTemplate.exchange(peticion,
						new ParameterizedTypeReference<List<MensajeDTO>>() {
						});
				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				assertThat(respuesta.getBody().isEmpty());
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

}






