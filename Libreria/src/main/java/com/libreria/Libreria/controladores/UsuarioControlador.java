
package com.libreria.Libreria.controladores;

import com.libreria.Libreria.entidades.Usuario;
import com.libreria.Libreria.errores.ErrorServicio;
import com.libreria.Libreria.servicios.UsuarioServicios;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String guardarUsuario(ModelMap modelo, @RequestParam(required=false) Long dni, @RequestParam String nombreC, @RequestParam String telefono, @RequestParam String mail, @RequestParam String clave, @RequestParam String clave2) throws ErrorServicio{

            try {
             
               uservicio.guardarUsuario(dni, nombreC, telefono, mail, clave, clave2);
               modelo.put("exito", "Usuario Registrado!");
            return "redirect:/login";

        } catch (ErrorServicio ex) {
            modelo.put("dni", dni);
            modelo.put("error", ex.getMessage());
            modelo.put("nombreC", nombreC);
            modelo.put("telefono", telefono);
            modelo.put("mail", mail);

            return "registrousuario";
        }
    }
 
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
    @GetMapping("/modificar")
    public String modificar(ModelMap modelo, HttpSession session, @RequestParam(required=false) String id) throws ErrorServicio{
   
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
   
        try {
            Usuario usuario = uservicio.buscarUsuarioID(id);
            modelo.put("usuario", usuario);
         
        } catch (ErrorServicio ex) {
            throw new ErrorServicio(ex.getMessage());
           
        }
        return "modificarUser";
    }
    
   @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USUARIO')")
    @PostMapping("/modificar")
    public String modificarUser(ModelMap modelo, HttpSession session, @RequestParam(required=false) String id, @RequestParam(required=false) Long dni, @RequestParam String nombreC, @RequestParam String telefono, @RequestParam String mail, @RequestParam String clave, @RequestParam String clave2) {

        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
      
            Usuario usuario = uservicio.buscarUsuarioID(login.getId());
            uservicio.modificarUsuario(usuario.getId(), dni, nombreC, telefono, mail, clave, clave2);
            
            session.setAttribute("usuariosession", usuario);
            modelo.put("exito", "Modificación exitosa");
            
            return "modificarUser";
        } catch (ErrorServicio ex) {
            modelo.put("dni", dni);
            modelo.put("error", ex.getMessage());
            modelo.put("nombreC", nombreC);
            modelo.put("telefono", telefono);
            modelo.put("mail", mail);
            
            return "modificarUser";
        }
    }
   
}