/**
 * The Logic Lab
 * @author jpk
 * @since Apr 3, 2009
 */
package com.tll.server.filter;

import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.testng.annotations.Test;



/**
 * AnonymousProcessingFilterTest
 * @author jpk
 */
@Test
public class AnonymousProcessingFilterTest {

	@SuppressWarnings("serial")
	public void test() throws Exception {
		SecurityContextHolder.setContext(new SecurityContext() {

			@Override
			public void setAuthentication(Authentication authentication) {
			}

			@Override
			public Authentication getAuthentication() {
				return null;
			}
		});
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final MockFilterChain chain = new MockFilterChain();
		final MockFilterConfig config = new MockFilterConfig();
		config.addInitParameter("key", "app");
		config.addInitParameter("userAttribute", "anonymousUser,ROLE_ANONYMOUS");
		final AnonymousProcessingFilter f = new AnonymousProcessingFilter();
		f.init(config);
		f.doFilter(request, response, chain);
	}
}
