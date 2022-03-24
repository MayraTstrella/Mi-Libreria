package com.libreria.Libreria.repositorio;

import com.libreria.Libreria.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    @Query("SELECT l FROM Libro l WHERE l.id = :id")
    public Libro buscarPorId(@Param("id") String id);

    @Query("SELECT l From Libro l WHERE l.titulo = :titulo")
    public Libro buscarPorTitulo(@Param("titulo") String titulo);

    @Query("SELECT l FROM Libro l WHERE l.autor.id = :id")
    public List<Libro> buscarLibroPorAutor(@Param("id") String id);

    @Query("SELECT l FROM Libro l WHERE l.editorial.nombre = :nombre")
    public List<Libro> buscarLibroPorEditorial(@Param("nombre") String nombre);

    @Query("SELECT l FROM Libro l WHERE l.isbn= :isbn")
    public Libro buscarporIsbn(@Param("isbn") Long isbn);
    

}
