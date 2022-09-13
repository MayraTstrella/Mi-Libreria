package com.libreria.Libreria.controladores;
import com.libreria.Libreria.entidades.Libro;
import com.libreria.Libreria.entidades.Usuario;
import com.libreria.Libreria.servicios.PrestamoServicios;
import com.libreria.Libreria.servicios.UsuarioServicios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Controlador {

    @Autowired
    private UsuarioServicios uservicio;

    @Autowired
    private PrestamoServicios pservic;

    @GetMapping(" / ")
    public String index(ModelMap modelo) {

        return "index";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    @GetMapping("/inicio")
    public String inicio(ModelMap modelo) {

//        List<Usuario> usuarios = uservicio.listarTodos();
//        modelo.addAttribute("usuarios", usuarios);
       
        return "inicio";
    }
   

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o clave incorrectos");
        }
        if (logout != null) {
            modelo.put("logout", "Ha salido correctamente.");
        }
        
        return "login";
    }

}
