
package com.libreria.Libreria.repositorio;

import com.libreria.Libreria.entidades.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
    
     @Query ("SELECT u FROM Usuario u WHERE u.id = :id")
    public Usuario buscarPorId (@Param ("id") String id);
    
    @Query ("SELECT u FROM Usuario u WHERE u.mail = :mail AND alta IS TRUE")
    public Usuario buscarPorMail (@Param ("mail") String mail);
    
    @Query ("SELECT u FROM Usuario u WHERE u.nombreC = :nombreC")
    public Usuario buscarPorNombre (@Param ("nombreC") String nombreC);
    
    @Query ("SELECT u FROM Usuario u WHERE u.alta IS TRUE")
    public List<Usuario> findAllActive ();
 
       @Query("SELECT u FROM Usuario u WHERE "
                    + "CONCAT(u.id, u.nombreC, u.mail, u.dni)"
                    + "LIKE %?1% ")
    public List<Usuario> findAll(@Param("palabrauser") String palabrauser);
    
}
