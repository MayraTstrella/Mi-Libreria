
package com.libreria.Libreria.repositorio;

import com.libreria.Libreria.entidades.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, String> {
    

    @Query ("SELECT a FROM Autor a WHERE a.nombre = :nombre")
    public Autor buscarPorNombre (@Param ("nombre") String nombre) ;
    
    @Query ("SELECT a FROM Autor a WHERE a.id = :id")
    public Autor buscarPorId (@Param ("id") String id) ;
    
    
    
}
