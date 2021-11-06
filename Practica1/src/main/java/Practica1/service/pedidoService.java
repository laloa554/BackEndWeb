package Practica1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Practica1.entity.Pedido;
import Practica1.repository.PedidoRepository;
import Practica1.security.entity.Usuario;

@Service
@Transactional
public class pedidoService {
	@Autowired
	PedidoRepository pedidoRepository;
	
    public List<Pedido> list(){
        return pedidoRepository.findAll();
    }
    public void save(Pedido pedido){
    	pedidoRepository.save(pedido);
    }

    public void delete(int id){
    	pedidoRepository.deleteById(id);
    }

    public boolean existsById(int id){
        return pedidoRepository.existsById(id);
    }

    /*List <Pedido> findByUsuario(<Usuario> usuario){
    	return pedidoRepository.findAll(usuario.getId());
    }*/
	
   /* boolean existByUsuario(Usuario usuario) {
    	return pedidoRepository.existByUsuario(usuario);
    }
*/
}
