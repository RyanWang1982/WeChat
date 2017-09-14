/**
 * 
 */
package wang.yongrui.wechat.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import wang.yongrui.wechat.entity.enumeration.ActionPermission;
import wang.yongrui.wechat.entity.enumeration.TargetDomain;
import wang.yongrui.wechat.entity.web.Privilege;
import wang.yongrui.wechat.entity.web.Role;
import wang.yongrui.wechat.entity.web.User;

/**
 * @author Wang Yongrui
 *
 */
@Component
public class WeChatPermissionEvaluator implements PermissionEvaluator {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private UserDetailsService userDetailsService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.access.PermissionEvaluator#hasPermission(org
	 * .springframework.security.core.Authentication, java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		boolean hasPermission = false;

		String userName = (null != authentication && StringUtils.isNotBlank(authentication.getName()))
				? authentication.getName() : request.getRemoteUser();

		if (StringUtils.isNotBlank(userName)) {
			Privilege administratorPrivilege = new Privilege();
			administratorPrivilege.setTargetDomain(TargetDomain.All);
			administratorPrivilege.setPermission(ActionPermission.All);

			Privilege privilege = new Privilege();
			TargetDomain targetDomain = TargetDomain.valueOf(targetDomainObject.toString());
			ActionPermission actionPermission = ActionPermission.valueOf(permission.toString());
			privilege.setTargetDomain(targetDomain);
			privilege.setPermission(actionPermission);

			User user = (User) userDetailsService.loadUserByUsername(userName);
			Set<Privilege> privilegeSet = new HashSet<Privilege>();
			if (null != user && CollectionUtils.isNotEmpty(user.getRoleSet())) {
				for (Role eachRole : user.getRoleSet()) {
					privilegeSet.addAll(eachRole.getPrivilegeSet());
					// if user has All privilege or exactly the same privilege,
					// set hasPermission to true;
					if (CollectionUtils.isNotEmpty(eachRole.getPrivilegeSet())
							&& (eachRole.getPrivilegeSet().contains(administratorPrivilege)
									|| eachRole.getPrivilegeSet().contains(privilege))) {
						hasPermission = true;
						break;
					}
				}
			}

			if (!hasPermission && CollectionUtils.isNotEmpty(privilegeSet)) {
				for (Privilege eachPrivilege : privilegeSet) {
					// if user has All permission on targetDomain,
					// set hasPermission to true;
					if (ActionPermission.All.equals(eachPrivilege.getPermission())
							&& targetDomain.equals(eachPrivilege.getTargetDomain())) {
						hasPermission = true;
						break;
					}
				}
			}
		}

		return hasPermission;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.access.PermissionEvaluator#hasPermission(org
	 * .springframework.security.core.Authentication, java.io.Serializable,
	 * java.lang.String, java.lang.Object)
	 */
	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		return false;
	}

}
