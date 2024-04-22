package TECH4LIFE.entidadesJPA.repositories;

import TECH4LIFE.entidadesJPA.entities.Centro;
import TECH4LIFE.entidadesJPA.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findUsuarioById(Integer id) ;
    Usuario updateUsuarioById(Integer id) ;
    Usuario deleteUsuarioById(Integer id) ;
    List<Usuario> findAllUsuarios() ; // Duda: ¿Existe en Spring? ¿Es correcto?
    // Duda método de arriba: ¿Buscar por email según la OpenAPI? Solo tendría que buscar por id no??
    Usuario insertUsuario() ; // Duda: ¿Cómo insertamos/creamos nuevo Usuario?

    // Entendemos que los métodos restantes de contraseñas y login se refieren
    // a otro microservicio.
}
