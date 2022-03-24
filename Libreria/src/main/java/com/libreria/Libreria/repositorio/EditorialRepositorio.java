
package com.libreria.Libreria.repositorio;

import com.libreria.Libreria.entidades.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String> {
    
      @Query ("SELECT e FROM Editorial e WHERE e.nombre = :nombre")
    public Editorial buscarPorNombre (@Param ("nombre") String nombre) ;
    
    @Query ("SELECT e FROM Editorial e WHERE e.id = :id")
    public Editorial buscarPorId (@Param ("id") String id);
    
    

}
