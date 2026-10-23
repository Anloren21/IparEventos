package com.ipareventos.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SeguridadConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/evento/**", "/registro", "/login", "/error", "/css/**", "/js/**", "/images/**",
						"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**")
				.permitAll()

				.requestMatchers(HttpMethod.GET, "/api/v2/eventos", "/api/v2/eventos/**", "/api/v1/categorias",
						"/api/v1/categorias/**")
				.permitAll()

				.requestMatchers(HttpMethod.POST, "/api/v2/usuarios/registrar").permitAll()

				.requestMatchers("/admin/**", "/api/v1/**").hasRole("ADMINISTRADOR")

				.anyRequest().authenticated()).csrf(csrf -> csrf.ignoringRequestMatchers("/api/v2/usuarios/registrar"))
				.formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults());

		return http.build();
	}
}
