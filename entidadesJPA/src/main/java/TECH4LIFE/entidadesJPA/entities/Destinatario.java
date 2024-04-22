



/*


REALMENTE ES NECESARIA?????????????????

 */



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
public class Destinatario {
    @Id @GeneratedValue
    private Integer id ;
    private enum tipo {
        CENTRO, ENTRENADOR, CLIENTE
    } ;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Destinatario that = (Destinatario) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Destinatario{" +
                "id=" + id +
                '}';
    }
}