package com.libreria.Libreria.controladores;

import com.libreria.Libreria.entidades.Autor;
import com.libreria.Libreria.entidades.Editorial;
import com.libreria.Libreria.entidades.Libro;
import com.libreria.Libreria.entidades.Usuario;
import com.libreria.Libreria.errores.ErrorServicio;
import com.libreria.Libreria.servicios.AutorServicios;
import com.libreria.Libreria.servicios.EditorialServicios;
import com.libreria.Libreria.servicios.LibroServicios;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/libro")
public class LibroControlador {

    @Autowired
    private LibroServicios lservicio;
    
    @Autowired
    private AutorServicios aservicio;
    
    @Autowired
    private EditorialServicios eservicio;
    
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/registro")
    public String formulario(ModelMap modelo) {
        List<Autor> autores = aservicio.listarTodos();
        modelo.addAttribute("autores", autores);
        List<Editorial> editoriales = eservicio.listarTodos();  
        modelo.addAttribute("editoriales", editoriales);
        return "registrolibro";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/registro")
    public String guardarLibro(ModelMap modelo, MultipartFile archivo, Long isbn, @RequestParam String titulo, @RequestParam (required= false) Integer anio, @RequestParam (required= false) Integer ejemplares, @RequestParam(required=false) Integer ejemplaresPrestados, @RequestParam(required=false) Integer ejemplaresRestantes,  @RequestParam String idAutor, @RequestParam String idEditorial)  {

        try {
            lservicio.cargarLibro(archivo, isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, idAutor, idEditorial);
            modelo.put("exito", "Carga Exitosa");
            return "registrolibro";

        } catch (ErrorServicio ex) {
            List<Autor> autores = aservicio.listarTodos();
            modelo.put("autores", autores);
            List<Editorial> editoriales = eservicio.listarTodos();
            modelo.put("editoriales", editoriales);
            modelo.addAttribute("error", ex.getMessage()); 
            modelo.put("isbn", isbn);
            modelo.put("titulo", titulo);
            modelo.put("anio", anio);
            modelo.put("ejemplares", ejemplares);
            modelo.put("ejemplaresPrestados", ejemplaresPrestados);
            modelo.put("ejemplaresRestantes", ejemplaresRestantes);
      
            return "registrolibro";
        }

    }
    
    //  Modificaciones
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
        @GetMapping("/modificar/{id}") 
    public String modificarLibro(ModelMap modelo, @PathVariable String id) throws ErrorServicio {

        try {
         modelo.addAttribute("libro", lservicio.buscarLibroID(id));
        List<Autor> autores = aservicio.listarTodos();
        modelo.addAttribute("autores", autores);
        List<Editorial> editoriales = eservicio.listarTodos(); 
        modelo.addAttribute("editoriales", editoriales);
        } catch (ErrorServicio ex) {
            throw new ErrorServicio(ex.getMessage());
        }
      
        return "modificarlibro";
    } 
    
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/modificar/{id}")
    public String modificarLibro(ModelMap modelo, MultipartFile archivo, @PathVariable String id, Long isbn, @RequestParam String titulo, @RequestParam(required= false) Integer anio, @RequestParam(required= false) Integer ejemplares, @RequestParam(required= false) Integer ejemplaresPrestados , @RequestParam(required= false) Integer ejemplaresRestantes, @RequestParam String idAutor, @RequestParam String idEditorial) {
        
        try {
            lservicio.modificarLibro(archivo, id, isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, idAutor, idEditorial);
            modelo.put("exito", "Modificacion exitosa");
            
            return "listarlibro";
            
        } catch (ErrorServicio ex) {
             List<Autor> autores = aservicio.listarTodos();
            modelo.addAttribute("autores", autores);
             List<Editorial> editoriales = eservicio.listarTodos();
            modelo.addAttribute("editoriales", editoriales);
            modelo.put("error", "Falta algun dato");
            modelo.addAttribute("error", ex.getMessage());
            modelo.put("isbn", isbn);
            modelo.put("titulo", titulo);
            modelo.put("anio", anio);
            modelo.put("ejemplares", ejemplares);
            modelo.put("ejemplaresPrestados", ejemplaresPrestados);
            modelo.put("ejemplaresRestantes", ejemplaresRestantes);
           
          
            return "modificarlibro";
        }
    }
    
    
    //Baja Alta
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
        @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id) {

        try {
            lservicio.darBaja(id);
            return "redirect:/admin/dashboard";
        } catch (ErrorServicio e) {
            return "accesoAdmin";
        }

    }
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id) {

        try {
            lservicio.darAlta(id);
            return "redirect:/admin/dashboard";
        } catch (ErrorServicio e) {
            return "accesoAdmin";
        }
    }
   
     
    // Listas Busquedas
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    @GetMapping("/listarlibro")
    public String lista(ModelMap modelo) {

        List<Libro> librosLista = lservicio.listarTodos();
        modelo.put("libros", librosLista);

        return "listarlibro";
    }

    //Devolucion de libros
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/devolucion/{id}")
    public String devoLibro(@PathVariable String id, ModelMap modelo) {
         try {
            lservicio.devLibro(id);
            return "redirect:/admin/dashboard";
        } catch (ErrorServicio e) {
            return "accesoAdmin";
        }
           
           
    } 
}
