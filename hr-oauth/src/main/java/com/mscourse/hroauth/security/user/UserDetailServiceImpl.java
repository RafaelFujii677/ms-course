package com.mscourse.hroauth.security.user;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mscourse.hroauth.model.User;
import com.mscourse.hroauth.service.UserService;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
	private final UserService userService;

	@Autowired
	public UserDetailServiceImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		try {
			User user = userService.findByEmail(username);			
			if(user == null) throw new UsernameNotFoundException(String.format("Usuário não '%s' encontrado", username));
			return new CustomUserDetails(user);
		} catch (Exception e) {
			 throw new UsernameNotFoundException(String.format("Usuário não '%s' encontrado", username));
		}
	}

	// User detail privado onde só o impl tem acesso
	private static final class CustomUserDetails extends User implements UserDetails{
		private static final long serialVersionUID = 1L;

		CustomUserDetails(User user) {
			super(user);
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return this.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName())).collect(Collectors.toList());
		}
		@Override
		public String getUsername() {
			return this.getUsername();
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
}
