package net.msonic.out2;


import java.util.Collection;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class CustomAuthenticationProvider implements  AuthenticationManager {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		return new Authentication() {
			
			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isAuthenticated() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Object getPrincipal() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object getDetails() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object getCredentials() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
	
	
	

}
