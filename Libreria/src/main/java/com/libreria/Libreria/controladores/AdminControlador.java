package com.libreria.Libreria.controladores;

import com.libreria.Libreria.entidades.Autor;
import com.libreria.Libreria.entidades.Editorial;
import com.libreria.Libreria.entidades.Libro;
import com.libreria.Libreria.entidades.Prestamo;
import com.libreria.Libreria.entidades.Usuario;
import com.libreria.Libreria.servicios.AutorServicios;
import com.libreria.Libreria.servicios.EditorialServicios;
import com.libreria.Libreria.servicios.LibroServicios;
import com.libreria.Libreria.servicios.PrestamoServicios;
import com.libreria.Libreria.servicios.UsuarioServicios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminControlador {
    
    @Autowired
    private UsuarioServicios uservicio;
    
    @Autowired
    private PrestamoServicios pservic;
    
    @Autowired
    private LibroServicios lservicio;
    
    @Autowired
    private AutorServicios aservicio;
    
    @Autowired
    private EditorialServicios eservicio;
    
    @GetMapping("/dashboard")
    public String accesoAdmin(ModelMap modelo, @Param("palabra, palabraaut, palabraed, palabrauser, palabrapr, alta") String palabra, String palabraaut, String palabraed, String palabrauser, String palabrapr, Boolean alta) {
        
        List<Usuario> usuarios = uservicio.listaBuscar(palabrauser);
           
        List<Prestamo> prestamos = pservic.listaBuscar(palabrapr,true);
        
        List<Libro> librosLista = lservicio.listarTodos(palabra);
         
        List<Autor> autoresLista = aservicio.listaBuscar(palabraaut);
         
        List<Editorial> editorialesLista = eservicio.listaBuscar(palabraed);

        modelo.put("usuarios", usuarios);
        modelo.put("palabrauser", palabrauser);
        modelo.put("prestamos", prestamos);
        modelo.put("palabrapr", palabrapr);
        modelo.put("libros", librosLista);
        modelo.put("palabra", palabra);
        modelo.put("autores", autoresLista);
        modelo.put("palabraaut", palabraaut);
        modelo.put("editoriales", editorialesLista);
        modelo.put("palabraed", palabraed);
        
        return "accesoAdmin";
    }
    
       
    //USUARIOS
     @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id) {
        try {
            uservicio.darBajaUsuario(id);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            return "accesoAdmin";
        }

    }
   
    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id) {
        try {
            uservicio.darAltaUsuario(id);
           return "redirect:/admin/dashboard";
        } catch (Exception e) {
            return "accesoAdmin";
        }
    }
    
    //PRESTAMOS
    
    @GetMapping("/bajap/{id}")
    public String bajaP(@PathVariable String id) {
        try {
            pservic.bajaPrestamo(id);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            return "accesoAdmin";
        }
    }
    
    
        @GetMapping("/altap/{id}")
    public String altaP(@PathVariable String id) {
        try {
            pservic.altaPrestamo(id);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            return "accesoAdmin";
        }
    }
    

}
