/**
 * The Logic Lab
 * @author jpk
 * @since May 16, 2009
 */
package com.tll.di;

import org.springframework.security.userdetails.UserDetailsService;

import com.tll.service.entity.user.IUserService;


/**
 * SmbizAcegiModule
 * @author jpk
 */
public class SmbizAcegiModule extends AcegiModule {

	@Override
	protected void bindUserDetailsService() {
		// UserDetailsService
		bind(UserDetailsService.class).to(IUserService.class);
	}

}
