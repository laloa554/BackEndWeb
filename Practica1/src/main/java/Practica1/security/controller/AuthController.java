package Practica1.security.controller;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.Valid;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import Practica1.correo.Correo;
import Practica1.dto.Mensaje;
//import Practica1.dto.configuracionXD;
//import Practica1.entity.configuracion;
import Practica1.pdf.pdf_crear;
import Practica1.security.dto.JwtDto;
import Practica1.security.dto.LoginUsuario;
import Practica1.security.dto.NuevoUsuario;
import Practica1.security.encryption.DesCipherUtil;
import Practica1.security.entity.Rol;
import Practica1.security.entity.Usuario;
import Practica1.security.enums.RolNombre;
import Practica1.security.jwt.JwtProvider;
import Practica1.security.repository.UsuarioRepository;
import Practica1.security.service.RolService;
import Practica1.security.service.UsuarioService;
//import Practica1.service.configuracionService;


@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController{
	@Value("${my.property.s}")
	private String ss;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;
    
    @Autowired
    UsuarioRepository u;
    
   /* @Autowired
    configuracionService configuracionService;*/
    /*este sí*/
	@PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){
       if(bindingResult.hasErrors()) {
            return new ResponseEntity(new Mensaje("campos mal puestos o email inválido"), HttpStatus.BAD_REQUEST);
        }else if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()) || usuarioService.existsByEmail(nuevoUsuario.getEmail())) {
            return new ResponseEntity(new Mensaje("Id y/o Email ya existente"), HttpStatus.BAD_REQUEST);
        }else {
        	 Usuario usuario =
                     new Usuario(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(), nuevoUsuario.getEmail(),
                             passwordEncoder.encode(nuevoUsuario.getPassword()),nuevoUsuario.getTelefono(),nuevoUsuario.getDireccion());
             Set<Rol> roles = new HashSet<>();
             roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
             if(nuevoUsuario.getRoles().contains("admin")) {
                 roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
             }
             usuario.setRoles(roles);
             usuarioService.save(usuario);
        }
        return new ResponseEntity(new Mensaje("usuario guardado"), HttpStatus.CREATED);
		//return new ResponseEntity(new Mensaje(nuevoUsuario.getRoles().toString()), HttpStatus.CREATED);
		
    }
    /*este sí*/   
    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
    	if(bindingResult.hasErrors()) {
            return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
        }
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<?>  forgotPassword(@RequestBody String datos) {
    	JwtProvider jwt=new JwtProvider();
    	if(!datos.isEmpty()) {
    		JSONObject json = new JSONObject(datos);
    		Optional<Usuario> x = u.findByNombreUsuario(json.getString("nombreUsuario"));
    		Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)(\\.[A-Za-z]{2,})$");
    		Matcher mEmail = pattern.matcher(x.get().getEmail().toLowerCase());
    		if(mEmail.matches()) {
    			Correo c = new Correo("alx591535@gmail.com","prueba542318",x.get().getEmail());
    			c.enviar_correo("lalo",jwt.generateTokenReset(x.get()));
    			return new ResponseEntity(new Mensaje("Se ha enviado un correo con el token"), HttpStatus.OK);
    		}else {
    			return new ResponseEntity(new Mensaje("No tiene asignado un correo o no es valido"), HttpStatus.OK);
    		}
    	}
    	return new ResponseEntity(new Mensaje("no ha mandado nada"), HttpStatus.BAD_REQUEST);
    }
   
    @PostMapping("/new-password")
    public ResponseEntity<?> newPassword(@RequestBody String datos) {
    	if(!datos.isEmpty()) {
    		JSONObject json = new JSONObject(datos);
    		String token = json.getString("token");
    		JwtProvider jwt=new JwtProvider();
    		if(jwt.validateTokenReset(token)) {
    			String user = jwt.getNombreUsuarioFromTokenReset(token);
    			if(u.existsByNombreUsuario(user) && json.getString("password").equals(json.getString("confirmaPass"))) {
    				Optional<Usuario> x = u.findByNombreUsuario(user);
    				x.get().setPassword(passwordEncoder.encode(json.getString("password")));
    				u.save(x.get());
    				return new ResponseEntity(new Mensaje("Contraseña actualizada"), HttpStatus.OK);
    			}else {
    				return new ResponseEntity(new Mensaje("Las contraseñas no coinciden"), HttpStatus.BAD_REQUEST);
    			}
    		}else {
    			return new ResponseEntity(new Mensaje("El token no es valido"), HttpStatus.BAD_REQUEST);
    		}
    	}else {
    		return new ResponseEntity(new Mensaje("no ha mandado nada"), HttpStatus.BAD_REQUEST);
    	}
    	
    }
    /*
    @PostMapping("/agregarAspirante")
    public ResponseEntity<?> agregarAspirante(@Valid @RequestBody configuracionXD conf, BindingResult bindingResult){
    	if(bindingResult.hasErrors()) {
            return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
        }else if(configuracionService.existsByCurp(conf.getCurp())) {
            return new ResponseEntity(new Mensaje("Ese niño/a ya esta registrado"), HttpStatus.BAD_REQUEST);
        }else {
        	List<configuracion> c = configuracionService.list();
        	int cont = 0;
        	for(Object b : c) {
        		cont ++;
        	}
        	if(cont<50) {
        		if(Integer.parseInt(conf.getEdad())>=3&&Integer.parseInt(conf.getEdad())<=6) {
                	Calendar fecha = Calendar.getInstance();
                    int año = fecha.get(Calendar.YEAR);
                    int mes = fecha.get(Calendar.MONTH) + 1;
                    int dia = fecha.get(Calendar.DAY_OF_MONTH);
                    String fol = String.valueOf(año)+String.valueOf(conf.getCurp().substring(0, 5));
                    System.out.println(fol);
                	configuracion nuevo = new configuracion(fol,conf.getNombreInfante(),conf.getTutor(),conf.getFechaNacimiento(),conf.getGenero(),conf.getEdad(),
                											conf.getCurp(),false,conf.getDireccion(),conf.getNumeroTelefonicoPrimero(),conf.getNumeroTelefonicoSegundo(),
                											conf.getCorreo(), conf.getGrado(),String.valueOf(dia+"/"+mes+"/"+año));
                	configuracionService.save(nuevo);
                	return new ResponseEntity(new Mensaje("usuario guardado, su folio es: " + fol), HttpStatus.CREATED);
            	}else {
            		return new ResponseEntity(new Mensaje("Ese niño/a no cumple con la edad requerida"), HttpStatus.BAD_REQUEST);
            	}
        	}else {
        		return new ResponseEntity(new Mensaje("Lo sentimos, el cupo de 50 ya ha sido cubierto"), HttpStatus.BAD_REQUEST);
        	}
        	
        }
    }
    
    @PostMapping("/ObtenerRegistro")
    public ResponseEntity<?> ObtenerRegistro(@RequestBody String folio){
    	if(!folio.isBlank()) {
    		JSONObject jsonObject = new JSONObject(folio);
    		String f = jsonObject.getString("folio");
    		if(configuracionService.existsByFolio(f)) {
    			Optional<configuracion> c = configuracionService.findByFolio(f);
            	return new ResponseEntity(c, HttpStatus.OK);
    		}else {
    			return new ResponseEntity(new Mensaje("Registro no encontrado"), HttpStatus.BAD_REQUEST);
    		}
    	}else {
    		return new ResponseEntity(new Mensaje("hay nada que buscar"), HttpStatus.BAD_REQUEST);
    	}
    }
    
    @PutMapping("/modifica")
    public ResponseEntity<?> modifica(@RequestBody String folio){
    	if(!folio.isBlank()) {
    		JSONObject jsonObject = new JSONObject(folio);
    		String f = jsonObject.getString("folio");
    		if(configuracionService.existsByFolio(f)) {
    			Optional<configuracion> c = configuracionService.findByFolio(f);
    			c.get().setNombreInfante(jsonObject.getString("nombreInfante"));
    			c.get().setTutor(jsonObject.getString("tutor"));
    			c.get().setFechaNacimiento(jsonObject.getString("fechaNacimiento"));
    			c.get().setGenero(jsonObject.getString("genero"));
    			c.get().setEdad(jsonObject.getString("edad"));
    			c.get().setCurp(jsonObject.getString("curp"));
    			c.get().setDireccion(jsonObject.getString("direccion"));
    			c.get().setNumeroTelefonicoPrimero(jsonObject.getString("numeroTelefonicoPrimero"));
    			c.get().setNumeroTelefonicoSegundo(jsonObject.getString("numeroTelefonicoSegundo"));
    			c.get().setCorreo(jsonObject.getString("correo"));
    			c.get().setGrado(jsonObject.getInt("grado"));
    			configuracionService.save(c.get());
            	return new ResponseEntity(new Mensaje("Se han guardado los cambios"), HttpStatus.OK);
    		}else {
    			return new ResponseEntity(new Mensaje("Registro no encontrado, no se modifico nada"), HttpStatus.BAD_REQUEST);
    		}
    	}else {
    		return new ResponseEntity(new Mensaje("hay nada que buscar"), HttpStatus.BAD_REQUEST);
    	}
    }
    
    @GetMapping("/descargaComprobante/{folio}")
    public @ResponseBody byte[] getFile(@PathVariable String folio) throws IOException {
    	if(!folio.isBlank()) {
    		if(configuracionService.existsByFolio(folio)) {
    			Optional<configuracion> c = configuracionService.findByFolio(folio);
    			File file = new File("C:/Users/psp_g/Desktop/archivosEscuela/"+c.get().getFolio()+".pdf");
    			pdf_crear pdf = new pdf_crear(c.get().getFolio(),c.get().getNombreInfante(),c.get().getGrado(),c.get().getTutor(),c.get().getFecha());
    			pdf.crearPlantilla();
    	    	InputStream inputStream = new FileInputStream(file);
    	        return IOUtils.toByteArray(inputStream);
    	        }
    		}
    	return null;
    }
   */
    
    
}
