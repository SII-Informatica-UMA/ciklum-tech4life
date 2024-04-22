package TECH4LIFE.entidadesJPA.entities;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Centro {
    @Id @GeneratedValue
    private Integer idCentro ;
    private String nombre ;
    private String direccion ;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Centro centro = (Centro) o;
        return idCentro == centro.idCentro && Objects.equals(nombre, centro.nombre) && Objects.equals(direccion, centro.direccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCentro, nombre, direccion);
    }

    @Override
    public String toString() {
        return "Centro{" +
                "idCentro=" + idCentro +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }
}
