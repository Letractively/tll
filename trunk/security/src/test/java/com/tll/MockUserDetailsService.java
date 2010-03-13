/**
 * The Logic Lab
 * @author jpk
 * @since Apr 3, 2009
 */
package com.tll;

import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;


/**
 * MockUserDetailsService
 * @author jpk
 */
public final class MockUserDetailsService implements UserDetailsService {

	@SuppressWarnings("serial")
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		return new UserDetails() {

			@Override
			public boolean isEnabled() {
				return false;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return false;
			}

			@Override
			public boolean isAccountNonLocked() {
				return false;
			}

			@Override
			public boolean isAccountNonExpired() {
				return false;
			}

			@Override
			public String getUsername() {
				return null;
			}

			@Override
			public String getPassword() {
				return null;
			}

			@Override
			public GrantedAuthority[] getAuthorities() {
				return null;
			}
		};
	}

}
