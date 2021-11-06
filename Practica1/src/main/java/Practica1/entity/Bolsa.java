package Practica1.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Bolsas")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bolsa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "modelo", nullable = false, length = 150)
	private String modelo;
	
	@Column(name = "precio", nullable = false)
	private double precio;
	
	@Column(name = "imagen", nullable = false, length = 300)
	private String imagen;
	
	@Column(name = "cantidad", nullable =  false)
	private int cantidad;
	
	@Column(name = "descripcion", nullable = false, length = 500)
	private String descripcion;
	
}
