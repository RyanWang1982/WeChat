/**
 * 
 */
package wang.yongrui.wechat.entity.jpa;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import wang.yongrui.wechat.entity.basic.PrivilegeBasic;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Wang Yongrui
 *
 */
@Entity(name = "PRIVILEGE")
@Getter
@Setter
public class PrivilegeEntity extends PrivilegeBasic {

	@ManyToMany
	@JoinTable(name = "ROLE_PRIVILEGE", joinColumns = { @JoinColumn(name = "privilege_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<RoleEntity> roleEntitySet;

}
