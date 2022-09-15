package com.libreria.Libreria.servicios;

import com.libreria.Libreria.entidades.Editorial;
import com.libreria.Libreria.errores.ErrorServicio;
import com.libreria.Libreria.repositorio.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialServicios {

    @Autowired
    private EditorialRepositorio erepositorio;

    @Transactional
    public Editorial cargarEditorial(String nombre) throws ErrorServicio {

        validarDatosE(nombre);
        Editorial editorial = new Editorial();

        editorial.setNombre(nombre);

        return erepositorio.save(editorial);
    }

    //Busquedas
    @Transactional
    public Editorial buscarEditID(String id) throws ErrorServicio {
        Optional<Editorial> respuesta = erepositorio.findById(id);
        if (respuesta.isPresent()) {
            return erepositorio.findById(id).get();

        } else {
            throw new ErrorServicio("No se encuentra la editorial por el id ingresado");
        }
    }

    //Listas
    public List<Editorial> listarTodos() {
        return erepositorio.findAll();
    }
    
    public List<Editorial> listaBuscar(String palabraed) {
        if (palabraed != null) {
            return erepositorio.findAll(palabraed);
        }
        return erepositorio.findAll();
    }
    //Baja Alta Modificaciones
    
    @Transactional
    public void bajaEditorial(String id) throws ErrorServicio {

        Optional<Editorial> respuesta = erepositorio.findById(id);
        if (respuesta.isPresent()) {

            Editorial editorial = respuesta.get();
            editorial.setAlta(false);
            erepositorio.save(editorial);
        } else {

            throw new ErrorServicio("No se encuentra la editorial por el id ingresado");
        }

    }
    
    @Transactional
    public void altaEditorial(String id) throws ErrorServicio {

        Optional<Editorial> respuesta = erepositorio.findById(id);
        if (respuesta.isPresent()) {

            Editorial editorial = respuesta.get();
            editorial.setAlta(true);
            erepositorio.save(editorial);
        } else {

            throw new ErrorServicio("No se encuentra la editorial por el id ingresado");
        }

    }
    
    @Transactional
    public void modificarEd(String id, String nombre) throws ErrorServicio {
 
        validarDatosE(nombre);
        Optional<Editorial> respuesta = erepositorio.findById(id);
        if (respuesta.isPresent()) {
            
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
            erepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encuentra la editorial por el id ingresado");
        }
    }

    public void validarDatosE(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El campo Nombre de Editorial no puede encontrarse vacio");
        }
    }

}
