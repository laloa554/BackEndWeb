package Practica1.entity;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import Practica1.security.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Pedidos")
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idPedido")
	private int idPedido;
	
	@NotNull
	@ManyToMany
	@JoinColumn(name = "Bolsa")
	private Set<Bolsa> Bolsa= new HashSet<>();
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "Usuario")
	private Usuario Usuario;
	
	@NotNull
	@Column(name = "fecha")
	private Date fecha;
	
	@NotNull
	@Column(name = "montoTotal")
	private double montoTotal;
	
}

