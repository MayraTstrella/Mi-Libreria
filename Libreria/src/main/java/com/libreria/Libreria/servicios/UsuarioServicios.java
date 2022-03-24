package com.libreria.Libreria.servicios;

import com.libreria.Libreria.entidades.Usuario;
import com.libreria.Libreria.enums.Rol;
import com.libreria.Libreria.errores.ErrorServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.libreria.Libreria.repositorio.UsuarioRepositorio;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicios  implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio urepositorio;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Error.class})
    public Usuario guardarUsuario(Long dni, String nombreC, String telefono, String mail, String clave, String clave2) throws ErrorServicio {

        validarDatos(dni, nombreC, telefono, mail, clave, clave2);
        Usuario usuario = new Usuario();
        usuario.setDni(dni);
        usuario.setNombreC(nombreC);
        usuario.setTelefono(telefono);
        usuario.setMail(mail);
        usuario.setAlta(true);
        usuario.setRol(Rol.USUARIO);
        
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        
        usuario.setClave(encriptada);

        return urepositorio.save(usuario);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Error.class})
    public Usuario modificarUsuario(String id, Long dni, String nombreC, String telefono, String mail, String clave, String clave2) throws ErrorServicio {

        validarDatos(dni, nombreC, telefono, mail, clave, clave2);
        Optional<Usuario> respuesta = urepositorio.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setDni(dni);
            usuario.setNombreC(nombreC);
            usuario.setTelefono(telefono);
            usuario.setMail(mail);
            
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);

            return urepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario por el id ingresado");
        }
    }
    
        @Transactional
    public Usuario buscarUsuarioID(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = urepositorio.findById(id);
        if (respuesta.isPresent()) {
            return urepositorio.findById(id).get();
            
        } else {
            throw new ErrorServicio ("No se encuentra el usuario por el id ingresado");
        }
    }
    
    
    @Transactional(readOnly = true)
    public Usuario buscarPorNombre (String nombre) throws ErrorServicio {
        
        return urepositorio.buscarPorNombre(nombre);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return urepositorio.findAll();
    }
    
    
    
    //Alta Baja

    @Transactional
    public void darBajaUsuario(String id) throws ErrorServicio {
 
   Optional <Usuario> respuesta = urepositorio.findById(id);
   if (respuesta.isPresent()) {
       Usuario usuario = respuesta.get();
       usuario.setAlta(false);
       urepositorio.save(usuario);
   } else {
       throw new ErrorServicio("No se encuentra el usuario");
   }
}
    
        @Transactional
    public void darAltaUsuario(String id) throws ErrorServicio {
 
   Optional <Usuario> respuesta = urepositorio.findById(id);
   if (respuesta.isPresent()) {
       Usuario usuario = respuesta.get();
       usuario.setAlta(true);
       urepositorio.save(usuario);
   } else {
       throw new ErrorServicio("No se encuentra el usuario");
   }
}
 
    public void validarDatos(Long dni, String nombreC, String telefono, String mail, String clave, String clave2) throws ErrorServicio {
        if (dni == null) {
            throw new ErrorServicio("El campo dni no puede encontrarse vacio");
        }
        if (nombreC == null || nombreC.isEmpty()) {
            throw new ErrorServicio("El campo Nombre y Apellido no puede encontrarse vacio");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new ErrorServicio("El campo Telefono no puede encontrarse vacio");
        }

        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El campo mail no puede encontrarse vacio");
        }
        if (clave == null || clave.isEmpty()) {
            throw new ErrorServicio("El campo Clave no puede encontrarse vacio");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves no coinciden");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = urepositorio.buscarPorMail(mail);
        
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + usuario.getRol());      
            permisos.add(p1);
           
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            
            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            
            return user;
            
        } else {
            return null;
        } 
    }  
}
