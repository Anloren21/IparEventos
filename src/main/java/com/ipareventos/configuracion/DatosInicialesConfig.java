package com.ipareventos.configuracion;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ipareventos.servicios.DatosEjemploService;

@Configuration
public class DatosInicialesConfig {

	@Bean
	public CommandLineRunner cargarDatosIniciales(DatosEjemploService datosEjemploService) {

		return args -> datosEjemploService.cargar();
	}
}