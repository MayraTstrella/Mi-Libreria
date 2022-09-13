package com.libreria.Libreria.controladores;

import com.libreria.Libreria.entidades.Autor;
import com.libreria.Libreria.errores.ErrorServicio;
import com.libreria.Libreria.servicios.AutorServicios;
import java.util.List;
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
@RequestMapping("/autor")
public class AutorControlador {

    @Autowired
    private AutorServicios aservicio;

 
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/registro")
    public String formulario() {
        return "registroautor";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/registro")
    public String guardarAutor(ModelMap modelo,  @RequestParam String nombre )  {

        try {
            aservicio.cargarAutor(nombre);
            modelo.put("exito", "Carga exitosa");
            return "registroautor";

        } catch (ErrorServicio e) {
            modelo.put("error", "Falta algun dato");
            return "registroautor";
        }

    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String modificarAu(ModelMap modelo, @PathVariable String id) throws ErrorServicio{
        
        try {
            Autor autor = aservicio.buscarAutorID(id);
            modelo.put("autor", autor);
         
        } catch (ErrorServicio ex) {
           modelo.put("error", ex.getMessage());

        }
        return "modificarautor";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/modificar/{id}")
    public String modificarAu(ModelMap modelo, @PathVariable String id, @RequestParam String nombre) {
        
        try {
            Autor autor = aservicio.buscarAutorID(id);
            aservicio.modificarAutor(autor.getId(), nombre);
            modelo.put("autor", autor);
            modelo.put("exito", "Modificación exitosa");
            return "redirect:/admin/dashboard";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            return "accesoAdmin";
        }
    }
   
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    @GetMapping("/listarautor")
    public String lista(ModelMap modelo) {

        List<Autor> autoresLista = aservicio.listarTodos();
        modelo.addAttribute("autores", autoresLista);

        return "listarautor";
    }
 
  
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/baja/{id}")
    public String bajaA(@PathVariable String id) {

    try {
        aservicio.bajaAutor(id);
        return "redirect:/admin/dashboard";
    } catch (Exception e) {
        return "accesoAdmin";
    }

}

@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@GetMapping("/alta/{id}")
public String altaA(@PathVariable String id) {

    try {
        aservicio.altaAutor(id);
        return  "redirect:/admin/dashboard";
    } catch (Exception e) {
        return "accesoAdmin";
    }

}


}