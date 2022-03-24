
package com.libreria.Libreria.repositorio;

import com.libreria.Libreria.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
    
     @Query ("SELECT u FROM Usuario u WHERE u.id = :id")
    public Usuario buscarPorId (@Param ("id") String id);
    
    @Query ("SELECT u FROM Usuario u WHERE u.mail = :mail")
    public Usuario buscarPorMail (@Param ("mail") String mail);
    
    @Query ("SELECT u FROM Usuario u WHERE u.nombreC = :nombreC")
    public Usuario buscarPorNombre (@Param ("nombreC") String nombreC);
    
    
}
