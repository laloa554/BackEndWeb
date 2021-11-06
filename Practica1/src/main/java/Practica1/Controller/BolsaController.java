package Practica1.Controller;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import Practica1.dto.Mensaje;
import Practica1.dto.bolsaDTO;
import Practica1.entity.Bolsa;
//import Practica1.dto.configuracionDTO;
//import Practica1.dto.configuracionXD;
//import Practica1.entity.configuracion;
import Practica1.security.dto.NuevoUsuario;
import Practica1.security.entity.Rol;
import Practica1.security.entity.Usuario;
import Practica1.security.enums.RolNombre;
import Practica1.security.service.RolService;
import Practica1.security.service.UsuarioService;
import Practica1.service.bolsaService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/Bolsa")
@CrossOrigin(origins = "*")
public class BolsaController {
	
	private static Logger logJava = Logger.getLogger(BolsaController.class);
	
	@Autowired
    UsuarioService usuarioService;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    RolService rolService;
    
    @Autowired
    bolsaService bolsaService;
	
    private final ResourceLoader resourceLoader;

    @Autowired
    public BolsaController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    @GetMapping("/ObtenerBolsas")
    public ResponseEntity<Bolsa> obtenerBolsas(){
    	List<Bolsa> bolsas = bolsaService.list();
    	return new ResponseEntity(bolsas, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/agregaBolsa")
    public ResponseEntity<?> agregaBolsa(@Valid @RequestBody Bolsa bolsa, BindingResult bindingResult){
    	if(bindingResult.hasErrors()) {
            return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
        }
    	//if(!imagen.isEmpty()) {
    		if(!bolsaService.existsByModelo(bolsa.getModelo())) {
    			/*Path directorioImagenes = Paths.get("src//main//resources/imagenes");
    			String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
    			try {
					byte[] bytesImg = imagen.getBytes();
					Path rutaCompleta = Paths.get(rutaAbsoluta + "//"+imagen.getOriginalFilename());
					Files.write(rutaCompleta, bytesImg);
					bolsa.setImagen(imagen.getOriginalFilename());
				} catch (IOException e) {
					e.printStackTrace();
				}*/
    			
    			bolsaService.save(bolsa);
    			return new ResponseEntity(new Mensaje("Agregado Corectamente"), HttpStatus.OK);
    		}
    		return new ResponseEntity(new Mensaje("Ese modelo ya existe"), HttpStatus.BAD_REQUEST);
       	/*}else {
       		return new ResponseEntity(new Mensaje("No hay una imagen"), HttpStatus.BAD_REQUEST);
       	}*/
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/borraBolsa")
    public ResponseEntity<?> agregaBolsa(@Valid @RequestBody String modelo){
    	if(modelo.isBlank()) {
            return new ResponseEntity(new Mensaje("No ha enviado nada"), HttpStatus.BAD_REQUEST);
        }else { 
        	JSONObject model = new JSONObject(modelo);
        	if(bolsaService.existsByModelo(model.getString("modelo"))) {
        			//bolsaService.delete(bolsaService.findByModelo(model.getString("modelo")).get().getId());
        		return new ResponseEntity(new Mensaje("Eliminacion Exitosa"), HttpStatus.OK);
        	}else {
        		return new ResponseEntity(new Mensaje("Modelo no encontrado"), HttpStatus.BAD_REQUEST);
        	}
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/modificaBolsa")
    public ResponseEntity<?> modificaBolsa(@Valid @RequestBody Bolsa bolsa, BindingResult bindingResult){
    	if(bindingResult.hasErrors()) {
            return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
        }else if(bolsaService.existsByModelo(bolsa.getModelo())) {
        	Bolsa b = bolsaService.findByModelo(bolsa.getModelo()).get();
        	b.setModelo(bolsa.getModelo());
        	b.setPrecio(bolsa.getPrecio());
        	b.setDescripcion(bolsa.getDescripcion());
        	bolsaService.save(b);
        	return new ResponseEntity(new Mensaje("Agregado Corectamente"), HttpStatus.OK);
        }else {
        	return new ResponseEntity(new Mensaje("Ese modelo no existe"), HttpStatus.BAD_REQUEST);
        }
    }
}
   
    

