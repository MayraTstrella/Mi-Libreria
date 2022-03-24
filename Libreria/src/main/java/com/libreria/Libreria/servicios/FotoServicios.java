
package com.libreria.Libreria.servicios;

import com.libreria.Libreria.entidades.Foto;
import com.libreria.Libreria.errores.ErrorServicio;
import com.libreria.Libreria.repositorio.FotoRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoServicios {
    
    @Autowired
    private FotoRepositorio frepositorio;
    
    @Transactional
    public Foto guardarFoto(MultipartFile archivo) throws ErrorServicio {
        
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Foto foto = new Foto();
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());
                
                
                return frepositorio.save(foto);
                
            } catch (Exception e) {
                 System.err.println(e.getMessage());
            }
        }
        return null;
    }
    
       @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo) throws ErrorServicio{
    
        if (archivo != null) {
            try {
        
                Foto foto = new Foto();
                
                if (idFoto != null) {
                    Optional<Foto> respuesta = frepositorio.findById(idFoto);
                    if (respuesta.isPresent()) {
                        foto = respuesta.get();
                    }
                }
                
                foto.setMime(archivo.getContentType());
                foto.setNombre(archivo.getName());
                foto.setContenido(archivo.getBytes());

                return frepositorio.save(foto);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;

        
    }
    
}
