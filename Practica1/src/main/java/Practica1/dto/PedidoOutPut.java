package Practica1.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoOutPut {

	private String Usuario;
	private Date fecha;
	private double montoTotal;

}
