package Practica1.dto;

import javax.validation.constraints.NotBlank;

public class bolsaDTO {
	@NotBlank
	private String modelo;
	@NotBlank
	private double precio;
	@NotBlank
	private String imagen; 
	@NotBlank
	private String descripcion;
	
	public bolsaDTO() {
		super();
	}

	public bolsaDTO(@NotBlank String modelo,@NotBlank double precio,@NotBlank String imagen,@NotBlank String descripcion) {
		super();

		this.modelo = modelo;
		this.precio = precio;
		this.imagen = imagen;
		this.descripcion = descripcion;
	}
	
	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}