package com.ProyectoAP.ProyectoAP.security.Controller;

import com.ProyectoAP.ProyectoAP.security.Dto.JwtDto;
import com.ProyectoAP.ProyectoAP.security.Dto.LoginUsuario;
import com.ProyectoAP.ProyectoAP.security.Dto.NuevoUsuario;
import com.ProyectoAP.ProyectoAP.security.Entity.Rol;
import com.ProyectoAP.ProyectoAP.security.Entity.Usuario;
import com.ProyectoAP.ProyectoAP.security.Enums.RolNombre;
import com.ProyectoAP.ProyectoAP.security.Service.RolService;
import com.ProyectoAP.ProyectoAP.security.Service.UsuarioService;
import com.ProyectoAP.ProyectoAP.security.jwt.JwtProvider;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

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

    //crear usuario nuevo //
    /* 
    @PostMapping ("/nuevo")
    public ResponseEntity <?> nuevo (@Valid @RequestBody NuevoUsuario nuevoUsuario,
            BindingResult bindingResult ){
        
    if (bindingResult.hasErrors())
    return new ResponseEntity (new Mensaje("Campos mal puestos o mail invalido"),HttpStatus.BAD_REQUEST);
    
    if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
    return new ResponseEntity(new Mensaje ("Este nombre de usuario ya existe"),HttpStatus.BAD_REQUEST);
    
    if(usuarioService.existsByEmail(nuevoUsuario.getEmail()))
    return new ResponseEntity(new Mensaje ("Este mail ya existe"),HttpStatus.BAD_REQUEST);
    
    Usuario usuario = new Usuario(nuevoUsuario.getNombre(),nuevoUsuario.getNombreUsuario(),
            nuevoUsuario.getEmail(), passwordEncoder.encode(nuevoUsuario.getPassword()));
    
    Set <Rol> roles = new HashSet<>();
    
    roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
    
        if(nuevoUsuario.getRoles().contains("admin"))
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        
        usuario.setRoles(roles);
        usuarioService.save(usuario);
        
        return new ResponseEntity(new Mensaje("Usuario Guardado"),HttpStatus.CREATED); 
    
    }*/
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(new Mensaje("campos mal puestos o email inválido"), HttpStatus.BAD_REQUEST);
        }
        if (usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario())) {
            return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
        }
        if (usuarioService.existsByEmail(nuevoUsuario.getEmail())) {
            return new ResponseEntity(new Mensaje("ese email ya existe"), HttpStatus.BAD_REQUEST);
        }
        Usuario usuario = new Usuario(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(), nuevoUsuario.getEmail(), passwordEncoder.encode(nuevoUsuario.getPassword()));
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
        if (nuevoUsuario.getRoles().contains("admin")) {
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        }
        usuario.setRoles(roles);
        usuarioService.save(usuario);
        return new ResponseEntity(new Mensaje("usuario guardado"), HttpStatus.CREATED);
    }

    /*
    @PostMapping("/login")
    public  ResponseEntity<JwtDto> login (@Valid @RequestBody LoginUsuario loginUsuario,BindingResult bindingResult){
    
        if(bindingResult.hasErrors())
            return  new ResponseEntity(new Mensaje("Campos mal puestos"),HttpStatus.BAD_REQUEST);
        
        Authentication authentication = authenticationManager.authenticate
        (new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(),loginUsuario.getPassword()));
    
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = jwtProvider.generateToken(authentication);
        
        UserDetails userDetails =(UserDetails) authentication.getPrincipal();
        
        JwtDto jwtDto= new JwtDto(jwt,userDetails.getUsername(),userDetails.getAuthorities());
        
        return new ResponseEntity(jwtDto,HttpStatus.OK);
    
    }*/
    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
        }
        Authentication authentication
                = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }

}
