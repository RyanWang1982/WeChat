/**
 * 
 */
package wang.yongrui.wechat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import wang.yongrui.wechat.entity.jpa.UserEntity;

/**
 * @author Wang Yongrui
 *
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

	/**
	 * @param loginName
	 * @return
	 */
	public UserEntity findByLoginName(String loginName);

}
