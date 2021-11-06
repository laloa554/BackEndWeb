package Practica1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Practica1.entity.Bolsa;

@Repository
public interface bolsaRepository extends JpaRepository<Bolsa, Integer>{
	Optional<Bolsa> findByModelo(String modelo);
	boolean existsByModelo(String modelo);
	/*Optional<Bolsa> findByCurp(String curp);
	boolean existsByCurp(String curp);*/
}
