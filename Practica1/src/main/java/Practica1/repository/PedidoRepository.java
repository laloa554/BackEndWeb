package Practica1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import Practica1.entity.Pedido;
import Practica1.security.entity.Usuario;

public interface PedidoRepository extends JpaRepository<Pedido, Integer>{

	/*List <Pedido> findByUsuario(Usuario usuario);
	boolean existByUsuario(Usuario usuario);*/
	

}
