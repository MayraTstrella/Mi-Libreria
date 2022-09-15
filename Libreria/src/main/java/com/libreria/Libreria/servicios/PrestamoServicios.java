package com.libreria.Libreria.servicios;

import com.libreria.Libreria.entidades.Usuario;
import com.libreria.Libreria.entidades.Libro;
import com.libreria.Libreria.entidades.Prestamo;
import com.libreria.Libreria.errores.ErrorServicio;
import com.libreria.Libreria.repositorio.LibroRepositorio;
import com.libreria.Libreria.repositorio.PrestamoRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrestamoServicios {

    @Autowired
    private PrestamoRepositorio prepositorio;
    
    @Autowired
    private LibroRepositorio lrepositorio;

    @Autowired
    private UsuarioServicios uservicio;

    @Autowired
    private LibroServicios lservicio;

    @Transactional
    public void cargarPrestamo( String id, Date fechaPrestamo, Date fechaDev, String nomUsuario) throws ErrorServicio {

        Usuario usuario = uservicio.buscarPorNombre(nomUsuario);
        validarDatos(usuario, fechaPrestamo, fechaDev);
        
        Optional <Libro> respuesta = lrepositorio.findById(id);   
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();           
            
                  Prestamo pres = new Prestamo();
                pres.setFechaPrestamo(fechaPrestamo);
                pres.setFechaDev(fechaDev);
                pres.setUsuario(usuario);
                pres.setLibro(libro);
                pres.setAlta(true);
                libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() + 1);
                libro.setEjemplaresRestantes(libro.getEjemplares() - libro.getEjemplaresPrestados());
        
                prepositorio.save(pres);
      
    } else {
             throw new ErrorServicio("El libro no se encuentra por el id ingresado");
        }
    
}
    

        @Transactional
        public Prestamo modificarPrestamo (String id, Date fechaPrestamo , Date fechaDev, String idUsuario , String idLibro) throws ErrorServicio {

            Usuario usuario = uservicio.buscarUsuarioID(idUsuario);
            Libro libro = lservicio.buscarLibroID(idLibro);

            Optional<Prestamo> respuesta = prepositorio.findById(id);
            if (respuesta.isPresent()) {
                Prestamo pres = respuesta.get();
                pres.setFechaPrestamo(fechaPrestamo);
                pres.setFechaDev(fechaDev);
                pres.setUsuario(usuario);
                pres.setLibro(libro);
                

                return prepositorio.save(pres);
            } else {
                throw new ErrorServicio("No se encontro el prestamo x el id ingresado");
            }
        }
        
        //Alta y baja
        
        @Transactional
        public void bajaPrestamo(String id) throws ErrorServicio {
            
            Optional<Prestamo> respuesta = prepositorio.findById(id);
            if(respuesta.isPresent()) {
                Prestamo prestamo = respuesta.get();
                prestamo.setAlta(false);                 
                prepositorio.save(prestamo);
            } else {
                throw new ErrorServicio ("No se encuentra por el id ingresado");
            }
        }
        
        
              @Transactional
        public void altaPrestamo(String id) throws ErrorServicio {
            
            Optional<Prestamo> respuesta = prepositorio.findById(id);
            if(respuesta.isPresent()) {
                Prestamo prestamo = respuesta.get();
                prestamo.setAlta(true);
                prepositorio.save(prestamo);
            } else {
                throw new ErrorServicio ("No se encuentra por el id ingresado");
            }
        }
        

        // Busquedas 
        
        @Transactional
        public Prestamo buscarPrestamoID  (String id) throws ErrorServicio {
            Optional<Prestamo> respuesta = prepositorio.findById(id);
            if (respuesta.isPresent()) {
                return prepositorio.findById(id).get();

            } else {
                throw new ErrorServicio("No se encuentra el prestamo por el id ingresado");
            }
        }
        
        public List<Prestamo> buscarPorUsuario (String id) {
            return prepositorio.listarPUsuario(id);
        }
        
        public List<Prestamo> buscarPorFecha (Date fechaPrestamo ) {
        return prepositorio.buscarPorFecha(fechaPrestamo);
        }

        public List<Prestamo> buscarPorFechaDev (Date fechaDev) {
        return prepositorio.buscarPorFechaDev(fechaDev);
        }
        
        public List<Prestamo> listarTodos() {
        return prepositorio.findAll();
        }
        
        public List<Prestamo> listaBuscar(String palabrapr, Boolean alta) {
            if (palabrapr != null) {
                return prepositorio.findAll(palabrapr, alta);
            }
        return prepositorio.findAll();
        }
        

        
        
        public void validarDatos(Usuario usuario, Date fechaPrestamo, Date fechaDev) throws ErrorServicio {
              
            
            if (usuario.getAlta() == false || usuario.getAlta() == null) {
                throw new ErrorServicio("El usuario esta dado de baja o no existe");
            }
            
            if (fechaPrestamo == null || fechaPrestamo.toString().isEmpty()) {
                throw new ErrorServicio ("El campo Fecha Prestamo no puede encontrarse vacio");
            }
            
            if (fechaDev == null || fechaDev.toString().isEmpty()) {
                throw new ErrorServicio("El campo Fecha de Devolucion no puede encontrarse vacio");
            }
            
             if ( diasPrestamo(fechaPrestamo, fechaDev) > 30) {
                throw new ErrorServicio("El prestamo no debe exceder los 30 días");
            }
        
        }
        
        
        public Integer diasPrestamo (Date fechaPrestamo, Date fechaDev) {
            
            int cantDias = (int) ((fechaDev.getTime() - fechaPrestamo.getTime()) / 86400000);
            
            return cantDias;
        }  
}   
            
            
 
            
            
        
           




