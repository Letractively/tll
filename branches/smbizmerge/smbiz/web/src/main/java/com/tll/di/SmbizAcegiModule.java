/**
 * The Logic Lab
 * @author jpk
 * @since May 16, 2009
 */
package com.tll.di;

import org.springframework.security.userdetails.UserDetailsService;

import com.tll.server.AcegiModule;
import com.tll.service.entity.user.IUserService;


/**
 * SmbizAcegiModule
 * @author jpk
 */
public class SmbizAcegiModule extends AcegiModule {

	@Override
	protected Class<? extends UserDetailsService> getUserDetailsImplType() {
		return IUserService.class;
	}
}
