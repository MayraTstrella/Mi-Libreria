package com.libreria.Libreria.controladores;

import com.libreria.Libreria.entidades.Editorial;
import com.libreria.Libreria.errores.ErrorServicio;
import com.libreria.Libreria.servicios.EditorialServicios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/editorial")
public class EditorialControlador {

    @Autowired
    private EditorialServicios eservicio;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/registro")
    public String formulario() {
        return "registroeditorial";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/registro")
    public String guardarEditorial(ModelMap modelo, @RequestParam String nombre)  {

        try {
            eservicio.cargarEditorial(nombre);
            modelo.put("exito", "Carga exitosa");
            return "registroeditorial";

        } catch (ErrorServicio e) {
            modelo.put("error", "Falta algun dato");
            return "registroeditorial";
        }
    }
    
      @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String modificarEd(ModelMap modelo, @PathVariable String id) throws ErrorServicio{
        
        try {
            Editorial editorial = eservicio.buscarEditID(id);
            modelo.put("editorial", editorial);
        } catch (ErrorServicio ex) {
            throw new ErrorServicio(ex.getMessage());
        }
        return "modificareditorial";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/modificar/{id}")
    public String modificarEd(ModelMap modelo, @PathVariable String id, @RequestParam String nombre) {
        
        try {
            Editorial editorial = eservicio.buscarEditID(id);
            eservicio.modificarEd(editorial.getId(), nombre);
            modelo.put("editorial", editorial);
            modelo.put("exito", "Modificación exitosa");
            return "redirect:/admin/dashboard";
        } catch (ErrorServicio ex) {
          
            modelo.put("error", ex.getMessage());
            return "accesoAdmin";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO')")
    @GetMapping("/listareditorial")
    public String lista(ModelMap modelo, @Param("palabraed") String palabraed) {

        List<Editorial> editorialesLista = eservicio.listaBuscar(palabraed);
        modelo.addAttribute("editoriales", editorialesLista);
        modelo.put("palabraed", palabraed);

        return "listareditorial";
    }

    //Alta Baja
    
      @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
        @GetMapping("/baja/{id}")
    public String bajaE(@PathVariable String id) {

        try {
            eservicio.bajaEditorial(id);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            return "accesoAdmin";
        }

    }
    

      @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/alta/{id}")
    public String altaE(@PathVariable String id) {

        try {
            eservicio.altaEditorial(id);
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            return "accesoAdmin";
        }

    }

}
