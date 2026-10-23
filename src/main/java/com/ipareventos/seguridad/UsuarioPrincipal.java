package com.ipareventos.seguridad;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ipareventos.entidades.Usuario;

public class UsuarioPrincipal implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final String nombre;
	private final String email;
	private final String password;
	private final List<GrantedAuthority> authorities;

	public UsuarioPrincipal(Usuario usuario) {
		this.nombre = usuario.getNombre();
		this.email = usuario.getEmail();
		this.password = usuario.getPassword();

		this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
	}

	public String getNombre() {
		return nombre;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}