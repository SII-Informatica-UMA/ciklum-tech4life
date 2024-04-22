package TECH4LIFE.entidadesJPA.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Entity
public class Usuario {
    @Id @GeneratedValue
    private Integer id ;
    private String nombre ;
    private String apellido1 ;
    private String apellido2 ;
    private String email ;
    private String password ;
    private boolean administrador ;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return administrador == usuario.administrador && Objects.equals(id, usuario.id) && Objects.equals(nombre, usuario.nombre) && Objects.equals(apellido1, usuario.apellido1) && Objects.equals(apellido2, usuario.apellido2) && Objects.equals(email, usuario.email) && Objects.equals(password, usuario.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido1, apellido2, email, password, administrador);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", administrador=" + administrador +
                '}';
    }
}
