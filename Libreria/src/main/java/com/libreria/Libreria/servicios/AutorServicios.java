package com.libreria.Libreria.servicios;

import com.libreria.Libreria.entidades.Autor;
import com.libreria.Libreria.errores.ErrorServicio;
import com.libreria.Libreria.repositorio.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServicios {

    @Autowired
    private AutorRepositorio arepositorio;

    @Transactional
    public Autor cargarAutor(String nombre) throws ErrorServicio {
        validarDatosA(nombre);
        Autor autor = new Autor();

        autor.setNombre(nombre);
        autor.setAlta(true);

        return arepositorio.save(autor);
    }

//Busquedas
    
    public Autor buscarAutorID(String id) throws ErrorServicio {
        Optional<Autor> respuesta = arepositorio.findById(id);
        if (respuesta.isPresent()) {
            return arepositorio.findById(id).get();
            
        } else {
            throw new ErrorServicio ("No se encuentra el autor por el id ingresado");
        }
    }
    
    //Listas

    public List<Autor> listaBuscar(String palabraaut) {
        if (palabraaut != null) {
            return arepositorio.findAll(palabraaut);
        }
        return arepositorio.findAll();
    }
    
    public List<Autor> listarTodos() {
      
        return arepositorio.findAll();
    }
    
    //Modificaciones
    
      @Transactional
    public void bajaAutor(String id) throws ErrorServicio {

        Optional<Autor> respuesta = arepositorio.findById(id);
        if (respuesta.isPresent()) {

            Autor autor = respuesta.get();
            autor.setAlta(false);
            arepositorio.save(autor);
        } else {

            throw new ErrorServicio("No se encuentra el autor por el id ingresado");
        }

    }
    
          @Transactional
    public void altaAutor(String id) throws ErrorServicio {

        Optional<Autor> respuesta = arepositorio.findById(id);
        if (respuesta.isPresent()) {

            Autor autor = respuesta.get();
            autor.setAlta(true);
            arepositorio.save(autor);
        } else {

            throw new ErrorServicio("No se encuentra el autor por el id ingresado");
        }

    }
    
        @Transactional
    public Autor modificarAutor(String id, String nombre) throws ErrorServicio {
        validarDatosA(nombre);
        Optional<Autor> respuesta = arepositorio.findById(id);

        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);

            return arepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encuentra el autor por el id ingresado");
        }
    }

    public void validarDatosA(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El campo Nombre de Autor no puede encontrarse vacio");
        }
    }
}
