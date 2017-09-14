/**
 * 
 */
package wang.yongrui.wechat.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import wang.yongrui.wechat.entity.web.User;

/**
 * @author Wang Yongrui
 *
 */
public interface UserService extends UserDetailsService {

	/**
	 * @param user
	 * @return
	 */
	public User create(User user);

	/**
	 * @param id
	 * @return
	 */
	public User retrieveOneById(Long id);

	/**
	 * @param preciseUser
	 * @return
	 */
	public Set<User> retrieveAll(User preciseUser);

	/**
	 * @param preciseUser
	 * @param pageable
	 * @return
	 */
	public Page<User> retrieveAllIntoPage(User preciseUser, Pageable pageable);

	/**
	 * @param roleId
	 * @param pageable
	 * @return
	 */
	public Page<User> retrieveAllIntoPageByRoleId(Long roleId, Pageable pageable);

	/**
	 * @param user
	 * @return
	 */
	public User putUpdate(User user);

	/**
	 * @param user
	 * @return
	 */
	public User patchUpdate(User user);

	/**
	 * @param userSet
	 * @return
	 */
	public boolean massPatchUpdate(Set<User> userSet);

	/**
	 * @param id
	 * @return
	 */
	public Boolean delete(Long id);

}
