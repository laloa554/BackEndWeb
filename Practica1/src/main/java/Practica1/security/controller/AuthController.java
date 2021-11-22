package Practica1.security.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Practica1.correo.Correo;
import Practica1.dto.Mensaje;
import Practica1.entity.Bolsa;
import Practica1.security.dto.JwtDto;
import Practica1.security.dto.LoginUsuario;
import Practica1.security.dto.NuevoUsuario;
import Practica1.security.entity.Rol;
import Practica1.security.entity.Usuario;
import Practica1.security.enums.RolNombre;
import Practica1.security.jwt.JwtProvider;
import Practica1.security.repository.UsuarioRepository;
import Practica1.security.service.RolService;
import Practica1.security.service.UsuarioService;
import Practica1.service.bolsaService;


@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
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

	@Autowired
	bolsaService bolsaService;

	@GetMapping("/ObtenerBolsas")
	public ResponseEntity<Bolsa> obtenerBolsas() {
		List<Bolsa> bolsas = bolsaService.list();
		return new ResponseEntity(bolsas, HttpStatus.OK);
	}

	@PostMapping("/Modelo")
	public ResponseEntity<Bolsa> obtenerModelo(@Valid @RequestBody String modelo) {
		Optional<Bolsa> bolsa = bolsaService.findByModelo(modelo);
		return new ResponseEntity(bolsa, HttpStatus.OK);
	}

	@PostMapping("/nuevo")
	public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity(new Mensaje("campos mal puestos o email inválido"), HttpStatus.BAD_REQUEST);
		} else if (usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario())
				|| usuarioService.existsByEmail(nuevoUsuario.getEmail())) {
			return new ResponseEntity(new Mensaje("Id y/o Email ya existente"), HttpStatus.BAD_REQUEST);
		} else {
			Usuario usuario = new Usuario(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(),
					nuevoUsuario.getEmail(), passwordEncoder.encode(nuevoUsuario.getPassword()),
					nuevoUsuario.getTelefono(), nuevoUsuario.getDireccion());
			Set<Rol> roles = new HashSet<>();
			roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
			if (nuevoUsuario.getRoles().contains("admin")) {
				roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
			}
			usuario.setRoles(roles);
			usuarioService.save(usuario);
		}
		return new ResponseEntity(new Mensaje("usuario guardado"), HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
		}
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
		return new ResponseEntity(jwtDto, HttpStatus.OK);
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody String datos) {
		JwtProvider jwt = new JwtProvider();
		if (!datos.isEmpty()) {
			JSONObject json = new JSONObject(datos);
			Optional<Usuario> x = u.findByNombreUsuario(json.getString("nombreUsuario"));
			Pattern pattern = Pattern
					.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)(\\.[A-Za-z]{2,})$");
			Matcher mEmail = pattern.matcher(x.get().getEmail().toLowerCase());
			if (mEmail.matches()) {
				Correo c = new Correo("alx591535@gmail.com", "prueba542318", x.get().getEmail());
				c.enviar_correo("lalo", jwt.generateTokenReset(x.get()));
				return new ResponseEntity(new Mensaje("Se ha enviado un correo con el token"), HttpStatus.OK);
			} else {
				return new ResponseEntity(new Mensaje("No tiene asignado un correo o no es valido"), HttpStatus.OK);
			}
		}
		return new ResponseEntity(new Mensaje("no ha mandado nada"), HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/new-password")
	public ResponseEntity<?> newPassword(@RequestBody String datos) {
		if (!datos.isEmpty()) {
			JSONObject json = new JSONObject(datos);
			String token = json.getString("token");
			JwtProvider jwt = new JwtProvider();
			if (jwt.validateTokenReset(token)) {
				String user = jwt.getNombreUsuarioFromTokenReset(token);
				if (u.existsByNombreUsuario(user)
						&& json.getString("password").equals(json.getString("confirmaPass"))) {
					Optional<Usuario> x = u.findByNombreUsuario(user);
					x.get().setPassword(passwordEncoder.encode(json.getString("password")));
					u.save(x.get());
					return new ResponseEntity(new Mensaje("Contraseña actualizada"), HttpStatus.OK);
				} else {
					return new ResponseEntity(new Mensaje("Las contraseñas no coinciden"), HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity(new Mensaje("El token no es valido"), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity(new Mensaje("no ha mandado nada"), HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes)
			throws IOException {
		
		if (file == null || file.isEmpty()) {
			attributes.addFlashAttribute("message", "Por favor seleccione un archivo");
			return (ResponseEntity<?>) ResponseEntity.badRequest();
		}

		StringBuilder builder = new StringBuilder();
		builder.append(System.getProperty("user.home"));
		builder.append(File.separator);
		builder.append("spring_upload_example");
		builder.append(File.separator);
		builder.append(file.getOriginalFilename());

		byte[] fileBytes = file.getBytes();
		Path path = Paths.get(builder.toString());
		Files.write(path, fileBytes);

		attributes.addFlashAttribute("message", "Archivo cargado correctamente [" + builder.toString() + "]");

		return ResponseEntity.ok(path.toString());
	}

}
