package com.libreria.Libreria.controladores;

import com.libreria.Libreria.entidades.Usuario;
import com.libreria.Libreria.entidades.Libro;
import com.libreria.Libreria.entidades.Prestamo;
import com.libreria.Libreria.errores.ErrorServicio;
import com.libreria.Libreria.servicios.PrestamoServicios;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.libreria.Libreria.servicios.LibroServicios;
import com.libreria.Libreria.servicios.UsuarioServicios;
import javax.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/prestamo")
public class PrestamoControlador {
    
    @Autowired
    private PrestamoServicios pservic;
    
    @Autowired
    private LibroServicios lservicio;
    
    @Autowired
    private UsuarioServicios uservicio;
    
     @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    @GetMapping ("/registro/{id}")
    public String formulario( @PathVariable String id, ModelMap modelo) throws ErrorServicio {
        
         try {
                    modelo.put("libro", lservicio.buscarLibroID(id));
//        List<Libro> libros = lservicio.listarTodos();
      
        List<Usuario> usuarios = uservicio.listarTodos();
        modelo.put("usuarios", usuarios);
        
         } catch (ErrorServicio ex) {
             throw new ErrorServicio(ex.getMessage());
         }

        return "registroprestamo";
    }
    
      @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
     @PostMapping ("/registro/{id}")
    public String guardarPrestamo (ModelMap modelo, @PathVariable String id, @RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaPrestamo, @RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fechaDev, @RequestParam String nomUsuario) {
       
           try {     
            pservic.cargarPrestamo( id, fechaPrestamo, fechaDev, nomUsuario);
            modelo.put("exito", "Se registró su prestamo exitosamente!");
            return "listarlibro";
    
        } catch (ErrorServicio ex) {
            modelo.put("fechaPrestamo", fechaPrestamo);
            modelo.addAttribute("error", ex.getMessage()); 
            modelo.put("fechaDev", fechaDev);
            modelo.put("nomUsuario", nomUsuario);
          
            
//            List<Libro> libros = lservicio.listarTodos();
//            modelo.put("libros", libros);
//            List<Usuario> usuarios = uservicio.listarTodos();
//            modelo.put("usuarios", usuarios);

            return "registroprestamo";
        }
    }
    
    @GetMapping ("/mislibros")
    public String mlibros (HttpSession session, ModelMap modelo) {
        Usuario login = (Usuario)session.getAttribute("usuariosession");
        if(login == null) {
            return "redirect:/inicio";
        }
        List<Prestamo> prestamos = pservic.buscarPorUsuario(login.getId());
        modelo.put("prestamos", prestamos);
        
        return "mislibros";
    }
    

    
 
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
//    @GetMapping("/listarprestamo")
//    public String lista(ModelMap modelo) {
//
//        List<Prestamo> prestamosLista = pservic.listarTodos();
//        modelo.addAttribute("prestamos", prestamosLista);
//
//        return "listarprestamo";
//    }
}
 

