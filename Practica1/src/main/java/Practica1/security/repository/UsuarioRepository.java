package Practica1.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Practica1.security.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    Optional<Usuario> findByNombreUsuarioAndEmail(String nombreUsuario, String email); 
    boolean existsByNombreUsuario(String nombreUsuario);
    boolean existsByEmail(String email);
    boolean existsByNombreUsuarioAndPassword(String nombreUsuario, String password);
   
}