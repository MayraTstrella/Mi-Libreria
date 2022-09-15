package com.libreria.Libreria.servicios;

import com.libreria.Libreria.entidades.Autor;
import com.libreria.Libreria.entidades.Editorial;
import com.libreria.Libreria.entidades.Foto;
import com.libreria.Libreria.entidades.Libro;
import com.libreria.Libreria.errores.ErrorServicio;
import com.libreria.Libreria.repositorio.LibroRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LibroServicios {

    @Autowired
    private LibroRepositorio lrepositorio;

    @Autowired
    private AutorServicios aservicio;

    @Autowired
    private EditorialServicios eservicio;
    
    @Autowired
    private FotoServicios fservicio;

    @Transactional
    public void cargarLibro(MultipartFile archivo, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, String idAutor, String idEditorial) throws ErrorServicio {

        Autor autor = aservicio.buscarAutorID(idAutor);
        Editorial editorial = eservicio.buscarEditID(idEditorial);

        validarDatos(isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, autor, editorial);
        Libro libro = new Libro();

        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(ejemplaresRestantes);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setAlta(true);
        
        Foto foto = fservicio.guardarFoto(archivo);
        libro.setFoto(foto);

        lrepositorio.save(libro);
    
        
    }

    @Transactional
    public Libro modificarLibro( MultipartFile archivo, String id, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes , String idAutor, String idEditorial) throws ErrorServicio {

        Autor autor = aservicio.buscarAutorID(idAutor);
        Editorial editorial = eservicio.buscarEditID(idEditorial);
        validarDatos(isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes ,autor, editorial);
        Optional<Libro> respuesta = lrepositorio.findById(id);

        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(ejemplaresPrestados);
            libro.setEjemplaresRestantes(ejemplaresRestantes);
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            
            String idFoto = null;
            if (libro.getFoto() != null) {
                idFoto = libro.getFoto().getId();
            }
            
            Foto foto = fservicio.actualizar(idFoto, archivo);
            libro.setFoto(foto);
            
            return lrepositorio.save(libro);
        } else {
            throw new ErrorServicio("No se encontro el libro por el id ingresado");
        }
    }
    
         //Devolucion 
    @Transactional
           public Libro devLibro(String id) throws ErrorServicio {
            
            Optional<Libro> respuesta = lrepositorio.findById(id);
            
            if (respuesta.isPresent()) {
                Libro libro = respuesta.get();
                libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() - 1);
                libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() + 1);
                
                return lrepositorio.save(libro);
            } else {
                throw new ErrorServicio ("No se encontro el libro x el id ingresado");
            }
            
        }


//Listas o Busquedas

    public List<Libro> listarTodos(String palabra) {
        if (palabra != null) {
             return lrepositorio.findAll(palabra);
        }
       return lrepositorio.findAll();
    }
    

    public Libro buscarLibroID(String id) throws ErrorServicio {
        Optional<Libro> respuesta = lrepositorio.findById(id);
        if (respuesta.isPresent()) {
            return lrepositorio.findById(id).get();
            
        } else {
            throw new ErrorServicio ("No se encontro el libro por el id ingresado");
        }
    }
    
    public Libro buscarPorTitulo(String titulo) throws ErrorServicio {
        return lrepositorio.buscarPorTitulo(titulo);
    }
    
    public List<Libro> buscaPorEditorial(String nombre) throws ErrorServicio{
    
        return lrepositorio.buscarLibroPorEditorial(nombre);
            
    }

    @Transactional
    public void darBaja(String id) throws ErrorServicio {

        Optional<Libro> respuesta = lrepositorio.findById(id);
        if (respuesta.isPresent()) {

            Libro libro = respuesta.get();
            libro.setAlta(false);
            lrepositorio.save(libro);
        } else {

            throw new ErrorServicio("No se encontro el libro por el id ingresado");
        }

    }
    
    @Transactional
    public void darAlta(String id) throws ErrorServicio {

        Optional<Libro> respuesta = lrepositorio.findById(id);
        if (respuesta.isPresent()) {

            Libro libro = respuesta.get();
            libro.setAlta(true);
            lrepositorio.save(libro);
        } else {

            throw new ErrorServicio("No se encontro el libro por el id ingresado");
        }

    }
    

    public void validarDatos(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, Autor autor, Editorial editorial) throws ErrorServicio{

        if (isbn == null || isbn.toString().isEmpty() ) {
            throw new ErrorServicio("El campo Isbn no puede encontrarse vacio o el numero seleccionado no puede ser menor o igual a Cero");
        }
        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("El campo Titulo no puede encontrarse vacio");
        }
        if (anio == null || anio <= 0 || anio.toString().isEmpty()) {
            throw new ErrorServicio("El campo Año no puede encontrarse vacio o el numero seleccionado no puede ser menor o igual a Cero");
        }
        if (ejemplares == null || ejemplares < 0) {
            throw new ErrorServicio("El campo Ejemplares no puede encontrarse vacio o el numero seleccionado no puede ser menor o igual a Cero");
        }
         if (ejemplaresPrestados == null ||  ejemplaresPrestados < 0) {
            throw new ErrorServicio("El campo Ejemplares Prestados no puede encontrarse vacio o el numero seleccionado no puede ser menor o igual a Cero");
        }
          if (ejemplaresRestantes == null || ejemplaresRestantes < 0) {
            throw new ErrorServicio("El campo Ejemplares Restantes no puede encontrarse vacio o el numero seleccionado no puede ser menor o igual a Cero");
        }
        if (autor.getAlta() == false || autor.getAlta() == null) {
            throw new ErrorServicio ("El autor esta dado de baja");
        }
        if (editorial.getAlta() == false || editorial.getAlta() == null) {
            throw new ErrorServicio("La editorial esta dada baja");
        }
        
    }
}


