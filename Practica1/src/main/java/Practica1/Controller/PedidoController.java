package Practica1.Controller;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import Practica1.dto.PedidoDTO;
import Practica1.dto.PedidoOutPut;
import Practica1.entity.Bolsa;
import Practica1.entity.Pedido;
import Practica1.repository.PedidoRepository;
import Practica1.security.entity.Rol;
import Practica1.security.service.UsuarioService;
import Practica1.service.pedidoService;
import Practica1.service.bolsaService;

@RestController
@RequestMapping("/Pedido")
@CrossOrigin(origins = "*")
public class PedidoController {
	@Autowired
	pedidoService pedidoService;
	
	@Autowired
    UsuarioService usuarioService;
	
	@Autowired
	bolsaService bolsaService;
	

	
	@PostMapping("/hacerPedido")
	public ResponseEntity<?> pedido(@RequestBody String json /*PedidoDTO pedidoDTO*/) {
		if(json.isBlank() || json.isEmpty()) {
			return (ResponseEntity<?>) ResponseEntity.badRequest();
		}
		ObjectMapper om = new ObjectMapper();
		PedidoDTO pedidoDTO;
		Pedido pedido = new Pedido();
		Set<Bolsa> bolsa = new HashSet<>();
		try {
			pedidoDTO = om.readValue(json, PedidoDTO.class);
			for(Bolsa b : pedidoDTO.getBolsa()) {
				bolsa.add(bolsaService.findByModelo(b.getModelo()).get());
			}
			pedido.setUsuario(usuarioService.findByNombreUsuario(pedidoDTO.getUserName()).get());
			pedido.setMontoTotal(pedidoDTO.getMontoTotal());
			long millis=System.currentTimeMillis();  
	        java.sql.Date date=new java.sql.Date(millis);  
	        pedido.setFecha(date);
	        pedido.setBolsa(bolsa);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		pedidoService.save(pedido);
		return ResponseEntity.ok("Todo bien, Todo Correcto\n");
	}
	
	@GetMapping("/pedidosRelizados")
	public ResponseEntity<?> pedidosRelizados(){
		List<Pedido> p = pedidoService.list();
		List<PedidoOutPut> po= new ArrayList<PedidoOutPut>();
		for(Pedido x : p) {
			PedidoOutPut pd = new PedidoOutPut();
			pd.setUsuario(usuarioService.findById(x.getUsuario().getId()).get().getNombre());
			pd.setFecha(x.getFecha());
			pd.setMontoTotal(x.getMontoTotal());
			po.add(pd);
		}
		return ResponseEntity.ok(po);
	}
	
	@GetMapping("/pedidoBolsasUsuario")
	public ResponseEntity<?> pedidoBolsasUsuario(@RequestBody String json){
		if(json.isBlank() || json.isEmpty()) {
			return (ResponseEntity<?>) ResponseEntity.badRequest();
		}
		JSONObject j = new JSONObject(json);
		j.get("nombreUsusario");
		
		return null;
	}

}
