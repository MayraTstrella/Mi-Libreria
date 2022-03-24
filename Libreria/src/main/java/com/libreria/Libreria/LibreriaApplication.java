package com.libreria.Libreria;

import com.libreria.Libreria.servicios.UsuarioServicios;
import com.libreria.Libreria.servicios.LibroServicios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class LibreriaApplication {
    
    @Autowired
    private UsuarioServicios uservicio;

	public static void main(String[] args) {
		SpringApplication.run(LibreriaApplication.class, args);
                
	}
        
        @Autowired
        public void ConfigureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(uservicio)
                    .passwordEncoder(new BCryptPasswordEncoder());
        }

}
