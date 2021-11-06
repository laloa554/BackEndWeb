package Practica1.security.entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import Practica1.security.enums.RolNombre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "Roles")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private RolNombre rolNombre;

    public Rol(@NotNull RolNombre rolNombre) {
        this.rolNombre = rolNombre;
    }


}
