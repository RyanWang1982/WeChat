/**
 * 
 */
package wang.yongrui.wechat.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import wang.yongrui.wechat.entity.web.Role;

/**
 * @author Wang Yongrui
 *
 */
public interface RoleService {

	/**
	 * @param role
	 * @return
	 */
	public Role create(Role role);

	/**
	 * @param id
	 * @return
	 */
	public Role retrieveOneById(Long id);

	/**
	 * @param preciseRole
	 * @return
	 */
	public Set<Role> retrieveAll(Role preciseRole);

	/**
	 * @param preciseRole
	 * @param pageable
	 * @return
	 */
	public Page<Role> retrieveAllIntoPage(Role preciseRole, Pageable pageable);

	/**
	 * @param role
	 * @return
	 */
	public Role putUpdate(Role role);

	/**
	 * @param role
	 * @return
	 */
	public Role patchUpdate(Role role);

	/**
	 * @param id
	 * @return
	 */
	public boolean delete(Long id);

}
