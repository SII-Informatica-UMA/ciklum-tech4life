package TECH4LIFE.entidadesJPA.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Entity
public class Gerente {
    @Id @GeneratedValue
    private Integer id ;
    private Integer idUsuario ;
    private String empresa ;
    @OneToOne
    private Centro centro;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gerente gerente = (Gerente) o;
        return Objects.equals(idUsuario, gerente.idUsuario) && Objects.equals(empresa, gerente.empresa) && Objects.equals(id, gerente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, empresa, id);
    }

    @Override
    public String toString() {
        return "Gerente{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", empresa='" + empresa + '\'' +
                '}';
    }
}
