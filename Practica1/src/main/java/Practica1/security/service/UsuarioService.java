package Practica1.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Practica1.security.entity.Usuario;
import Practica1.security.repository.UsuarioRepository;

import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Optional<Usuario> findByNombreUsuario(String nombreUsuario){
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public boolean existsByNombreUsuario(String nombreUsuario){
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }

    public boolean existsByEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public void save(Usuario usuario){
        usuarioRepository.save(usuario);
    }
    
    public boolean existsByNombreUsuarioAndPassword(String nombreUsuario, String password) {
    	return usuarioRepository.existsByNombreUsuarioAndPassword(nombreUsuario, password);
    }
    
    public Optional<Usuario> findById(int id){
    	return usuarioRepository.findById(id);
    }
}
