/**
 * 
 */
package wang.yongrui.wechat.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import wang.yongrui.wechat.entity.web.User;

/**
 * @author Wang Yongrui
 *
 */
@Configuration
public class UserUtils {

	private static final String CURRENT_SSO_USER = "CURRENT_SSO_USER";

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private HttpServletRequest request;

	/**
	 * @return
	 */
	public User getCurrentApplicationUser() {
		User currentUser = (User) request.getSession().getAttribute(CURRENT_SSO_USER);
		User applicationUser = null != currentUser ? currentUser : null;
		if (null == applicationUser) {
			applicationUser = (User) userDetailsService.loadUserByUsername(request.getRemoteUser());
		}

		return applicationUser;
	}

}
