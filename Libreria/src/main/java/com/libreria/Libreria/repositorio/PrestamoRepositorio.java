
package com.libreria.Libreria.repositorio;

import com.libreria.Libreria.entidades.Prestamo;
import com.libreria.Libreria.entidades.Usuario;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String> {
    
    @Query ("SELECT p FROM Prestamo p WHERE p.id = :id")
    public Prestamo buscarPorId (@Param ("id") String id);
    
    @Query ("SELECT p FROM Prestamo p WHERE p.fechaPrestamo = :fechaPrestamo")
    public List<Prestamo> buscarPorFecha (@Param ("fechaPrestamo") Date fechaPrestamo );
    
    @Query ("SELECT p FROM Prestamo p WHERE p.fechaDev = :fechaDev")
    public List<Prestamo> buscarPorFechaDev (@Param ("fechaDev") Date fechaDev);

    @Query ("SELECT p FROM Prestamo p WHERE p.libro.titulo = :titulo")
    public List<Prestamo> buscarPorTitulo (@Param ("titulo") String titulo);
    
    @Query ("SELECT p FROM Prestamo p WHERE p.usuario.id =:id AND p.alta IS TRUE")
    public List<Prestamo> listarPUsuario(@Param("id") String id);
    
    @Query ("SELECT p FROM Prestamo p WHERE p.alta IS TRUE")
    public List<Prestamo> listarTodosAlta(@Param("alta") Boolean alta);

}
/*   */