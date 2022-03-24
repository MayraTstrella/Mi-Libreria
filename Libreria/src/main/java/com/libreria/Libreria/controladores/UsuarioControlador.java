
package com.libreria.Libreria.controladores;

import com.libreria.Libreria.entidades.Usuario;
import com.libreria.Libreria.errores.ErrorServicio;
import com.libreria.Libreria.servicios.UsuarioServicios;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {
    
    @Autowired
    private UsuarioServicios uservicio; 
    
    
    @GetMapping("/registro")
    public String formulario() {
        return "registrousuario";
    }
    
 
    @PostMapping("/registro")
    public String guardarUsuario(ModelMap modelo, Long dni, @RequestParam String nombreC, @RequestParam String telefono, @RequestParam String mail, @RequestParam String clave, @RequestParam String clave2) throws ErrorServicio{

            try {
                
                uservicio.guardarUsuario(dni, nombreC, telefono, mail, clave, clave2);
                modelo.put("exito", "Registro exitoso!");
            return "login";

        } catch (ErrorServicio ex) {
            modelo.put("dni", dni);
            modelo.put("error", ex.getMessage());
            modelo.put("nombreC", nombreC);
            modelo.put("telefono", telefono);
            modelo.put("mail", mail);
            
            return "registrousuario";
        }
    }
    
     @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    @GetMapping("/editar-perfil")
    public String actualizarPerfil(HttpSession session, @RequestParam String id, ModelMap modelo) {
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }
        try {
            Usuario usuario = uservicio.buscarUsuarioID(id);
            modelo.addAttribute("perfil", usuario);
        } catch (ErrorServicio ex) {
            modelo.addAttribute("error", ex.getMessage());
        }
        return "perfilusuario";
    }
    
    
      @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    @PostMapping("/actualizar-perfil")
    public String actualizar(ModelMap modelo, HttpSession session, Long dni, @RequestParam String id, @RequestParam String nombreC, @RequestParam String telefono, @RequestParam String mail, @RequestParam String clave, @RequestParam String clave2) throws ErrorServicio {
        Usuario usuario = null;
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if(login == null || !login.getId().equals(id)) {
                return "redirect:/inicio";
            }
            usuario = uservicio.buscarUsuarioID(id);
            uservicio.modificarUsuario(id, dni, nombreC, telefono, mail, clave, clave2);
            session.setAttribute("usuariosession", usuario);
            
            return "redirect:/inicio";
        } catch (ErrorServicio ex) {
            modelo.put("dni", dni);
            modelo.put("Error", ex.getMessage());
            modelo.put("nombreC", nombreC);
            modelo.put("telefono", telefono);
            modelo.put("mail", mail);
        }
        return "perfilusuario";
    }

}