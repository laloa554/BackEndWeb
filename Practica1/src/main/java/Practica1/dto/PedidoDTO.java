package Practica1.dto;

import java.util.List;
import javax.validation.constraints.NotNull;

import Practica1.entity.Bolsa;
import Practica1.security.dto.LoginUsuario;
import Practica1.security.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO{
	
	/*@NotNull
	private Usuario usuario; 
	@NotNull
	private List<Bolsa> bolsa;
	@NotNull
	private double montoTotal;*/
    public String userName;
    public List<Bolsa> bolsa;
    public double montoTotal;
}