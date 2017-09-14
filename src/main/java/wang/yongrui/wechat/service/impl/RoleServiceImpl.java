/**
 * 
 */
package wang.yongrui.wechat.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.JoinType;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wang.yongrui.wechat.entity.jpa.PrivilegeEntity;
import wang.yongrui.wechat.entity.jpa.RoleEntity;
import wang.yongrui.wechat.entity.jpa.RoleEntity_;
import wang.yongrui.wechat.entity.web.Privilege;
import wang.yongrui.wechat.entity.web.Role;
import wang.yongrui.wechat.repository.RoleRepository;
import wang.yongrui.wechat.service.RoleService;
import wang.yongrui.wechat.utils.PatchBeanUtils;
import wang.yongrui.wechat.utils.PredicateUtils;

/**
 * @author Wang Yongrui
 *
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wang.yongrui.wechat.service.RoleService#create(com.sap.cloud.
	 * scp.sso.security.entity.web.Role)
	 */
	@Override
	public Role create(Role role) {
		RoleEntity roleEntity = new RoleEntity();
		copyProperties(role, roleEntity);
		return new Role(roleRepository.saveAndFlush(roleEntity));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wang.yongrui.wechat.service.RoleService#retrieveOneById(java.
	 * lang.Long)
	 */
	@Override
	public Role retrieveOneById(Long id) {
		return new Role(roleRepository.findOne(id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wang.yongrui.wechat.service.RoleService#retrieveAll(com.sap.
	 * cloud.scp.sso.security.entity.web.Role)
	 */
	@Override
	public Set<Role> retrieveAll(Role preciseRole) {
		RoleEntity preciseRoleEntity = new RoleEntity();
		copyProperties(preciseRole, preciseRoleEntity);

		List<RoleEntity> roleEntityList = roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
			criteriaQuery.distinct(true);
			root.fetch(RoleEntity_.privilegeEntitySet, JoinType.LEFT);
			return PredicateUtils.preciseAndRestrictions(preciseRoleEntity, root, criteriaBuilder);
		});

		Set<Role> roleSet = new LinkedHashSet<>();
		for (RoleEntity eachRoleEntity : roleEntityList) {
			roleSet.add(new Role(eachRoleEntity));
		}

		return roleSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wang.yongrui.wechat.service.RoleService#retrieveAllIntoPage(
	 * wang.yongrui.wechat.entity.web.Role,
	 * org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Role> retrieveAllIntoPage(Role preciseRole, Pageable pageable) {
		RoleEntity preciseRoleEntity = new RoleEntity();
		copyProperties(preciseRole, preciseRoleEntity);

		Page<RoleEntity> roleEntityPage = roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
			criteriaQuery.distinct(true);
			if (Long.class != criteriaQuery.getResultType()) {
				root.fetch(RoleEntity_.privilegeEntitySet, JoinType.LEFT);
			}
			return PredicateUtils.preciseAndRestrictions(preciseRoleEntity, root, criteriaBuilder);
		}, pageable);

		List<Role> roleList = new ArrayList<>();
		for (RoleEntity eachRoleEntity : roleEntityPage.getContent()) {
			roleList.add(new Role(eachRoleEntity));
		}

		return new PageImpl<Role>(roleList, pageable, roleEntityPage.getTotalElements());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wang.yongrui.wechat.service.RoleService#putUpdate(com.sap.
	 * cloud.scp.sso.security.entity.web.Role)
	 */
	@Override
	public Role putUpdate(Role role) {
		Role updatedRole = null;
		if (null != role.getId()) {
			RoleEntity roleEntity = roleRepository.findOne(role.getId());
			if (null != roleEntity) {
				copyProperties(role, roleEntity);
				updatedRole = new Role(roleRepository.saveAndFlush(roleEntity));
			}
		}

		return updatedRole;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wang.yongrui.wechat.service.RoleService#patchUpdate(com.sap.
	 * cloud.scp.sso.security.entity.web.Role)
	 */
	@Override
	public Role patchUpdate(Role role) {
		Role updatedRole = null;
		if (null != role.getId()) {
			RoleEntity roleEntity = roleRepository.findOne(role.getId());
			if (null != roleEntity) {
				patchProperties(role, roleEntity);
				updatedRole = new Role(roleRepository.saveAndFlush(roleEntity));
			}
		}

		return updatedRole;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wang.yongrui.wechat.service.RoleService#delete(java.lang.Long)
	 */
	@Override
	public boolean delete(Long id) {
		roleRepository.delete(id);
		return true;
	}

	/**
	 * @param role
	 * @param roleEntity
	 */
	private static void copyProperties(Role role, RoleEntity roleEntity) {
		if (null != role) {
			if (CollectionUtils.isNotEmpty(role.getPrivilegeSet())) {
				Set<PrivilegeEntity> privilegeEntitySet = new HashSet<>();
				for (Privilege eachPrivilege : role.getPrivilegeSet()) {
					PrivilegeEntity eachPrivilegeEntity = new PrivilegeEntity();
					BeanUtils.copyProperties(eachPrivilege, eachPrivilegeEntity);
					privilegeEntitySet.add(eachPrivilegeEntity);
				}
				roleEntity.setPrivilegeEntitySet(privilegeEntitySet);
			}
			BeanUtils.copyProperties(role, roleEntity);
		}
	}

	/**
	 * @param role
	 * @param roleEntity
	 */
	private static void patchProperties(Role role, RoleEntity roleEntity) {
		if (null != role) {
			if (CollectionUtils.isNotEmpty(role.getPrivilegeSet())) {
				Set<PrivilegeEntity> privilegeEntitySet = roleEntity.getPrivilegeEntitySet();
				for (Privilege eachPrivilege : role.getPrivilegeSet()) {
					boolean isNewOne = true;
					for (PrivilegeEntity privilegeEntity : privilegeEntitySet) {
						if (null != eachPrivilege.getId() && privilegeEntity.getId().equals(eachPrivilege.getId())) {
							// Update exist ones
							PatchBeanUtils.patchProperties(eachPrivilege, privilegeEntity);
							isNewOne = false;
							break;
						}
					}

					if (isNewOne && null == eachPrivilege.getId()) {
						// Add new ones
						PrivilegeEntity eachPrivilegeEntity = new PrivilegeEntity();
						PatchBeanUtils.patchProperties(eachPrivilege, eachPrivilegeEntity);
						privilegeEntitySet.add(eachPrivilegeEntity);
					}
				}
			}

			PatchBeanUtils.patchProperties(role, roleEntity);
		}
	}

}
