/**
 * 
 */
package wang.yongrui.wechat.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.SetJoin;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wang.yongrui.wechat.entity.jpa.ExtendedInfoEntity;
import wang.yongrui.wechat.entity.jpa.RoleEntity;
import wang.yongrui.wechat.entity.jpa.RoleEntity_;
import wang.yongrui.wechat.entity.jpa.UserEntity;
import wang.yongrui.wechat.entity.jpa.UserEntity_;
import wang.yongrui.wechat.entity.web.ExtendedInfo;
import wang.yongrui.wechat.entity.web.Role;
import wang.yongrui.wechat.entity.web.User;
import wang.yongrui.wechat.repository.RoleRepository;
import wang.yongrui.wechat.repository.UserRepository;
import wang.yongrui.wechat.service.UserService;
import wang.yongrui.wechat.utils.PatchBeanUtils;
import wang.yongrui.wechat.utils.PredicateUtils;

/**
 * @author Wang Yongrui
 *
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new User(userRepository.findByLoginName(username));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wang.yongrui.wechat.service.UserService#create(com.sap.cloud.
	 * scp.sso.security.entity.web.User)
	 */
	@Override
	public User create(User user) {
		UserEntity userEntity = new UserEntity();
		copyProperties(user, userEntity);
		userEntity = userRepository.saveAndFlush(userEntity);
		// Detach the entity to get PrivilegeEntity from database
		entityManager.detach(userEntity);
		userEntity = userRepository.findOne(userEntity.getId());

		return new User(userEntity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wang.yongrui.wechat.service.UserService#retrieveOneById(java.
	 * lang.Long)
	 */
	@Override
	public User retrieveOneById(Long id) {
		return new User(userRepository.findOne(id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wang.yongrui.wechat.service.UserService#retrieveAll()
	 */
	@Override
	public Set<User> retrieveAll(User preciseUser) {
		UserEntity preciseUserEntity = new UserEntity();
		copyProperties(preciseUser, preciseUserEntity);

		List<UserEntity> userEntityList = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
			criteriaQuery.distinct(true);
			root.fetch(UserEntity_.roleEntitySet, JoinType.LEFT).fetch(RoleEntity_.privilegeEntitySet, JoinType.LEFT);
			root.fetch(UserEntity_.extendedInfoEntityList, JoinType.LEFT);
			return PredicateUtils.preciseAndRestrictions(preciseUserEntity, root, criteriaBuilder);
		});

		Set<User> userSet = new LinkedHashSet<>();
		for (UserEntity eachUserEntity : userEntityList) {
			userSet.add(new User(eachUserEntity));
		}

		return userSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wang.yongrui.wechat.service.UserService#retrieveAllIntoPage(
	 * org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<User> retrieveAllIntoPage(User preciseUser, Pageable pageable) {
		UserEntity preciseUserEntity = new UserEntity();
		copyProperties(preciseUser, preciseUserEntity);

		Page<UserEntity> userEntityPage = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
			criteriaQuery.distinct(true);
			if (Long.class != criteriaQuery.getResultType()) {
				root.fetch(UserEntity_.roleEntitySet, JoinType.LEFT).fetch(RoleEntity_.privilegeEntitySet,
						JoinType.LEFT);
				root.fetch(UserEntity_.extendedInfoEntityList, JoinType.LEFT);
			}
			return PredicateUtils.preciseAndRestrictions(preciseUserEntity, root, criteriaBuilder);
		}, pageable);

		List<User> userList = new ArrayList<>();
		for (UserEntity eachUserEntity : userEntityPage.getContent()) {
			userList.add(new User(eachUserEntity));
		}

		return new PageImpl<User>(userList, pageable, userEntityPage.getTotalElements());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wang.yongrui.wechat.service.UserService#
	 * retrieveAllIntoPageByRoleId(java.lang.Long,
	 * org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<User> retrieveAllIntoPageByRoleId(Long roleId, Pageable pageable) {
		Page<UserEntity> userEntityPage = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
			criteriaQuery.distinct(true);
			root.fetch(UserEntity_.roleEntitySet, JoinType.LEFT).fetch(RoleEntity_.privilegeEntitySet, JoinType.LEFT);
			root.fetch(UserEntity_.extendedInfoEntityList, JoinType.LEFT);
			SetJoin<UserEntity, RoleEntity> roleJoin = root.join(UserEntity_.roleEntitySet);
			return criteriaBuilder.and(criteriaBuilder.equal(roleJoin.get(RoleEntity_.id), roleId));
		}, pageable);

		List<User> userList = new ArrayList<>();
		for (UserEntity eachUserEntity : userEntityPage.getContent()) {
			userList.add(new User(eachUserEntity));
		}

		return new PageImpl<User>(userList, pageable, userEntityPage.getTotalElements());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wang.yongrui.wechat.service.UserService#putUpdate(com.sap.
	 * cloud.scp.sso.security.entity.web.User)
	 */
	@Override
	public User putUpdate(User user) {
		User updatedUser = null;
		if (null != user.getId()) {
			UserEntity userEntity = userRepository.findOne(user.getId());
			if (null != userEntity) {
				putProperties(user, userEntity);
				updatedUser = new User(userRepository.saveAndFlush(userEntity));
			}
		}

		return updatedUser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wang.yongrui.wechat.service.UserService#patchUpdate(com.sap.
	 * cloud.scp.sso.security.entity.web.User)
	 */
	@Override
	public User patchUpdate(User user) {
		User updatedUser = null;
		if (null != user.getId()) {
			UserEntity userEntity = userRepository.findOne(user.getId());
			if (null != userEntity) {
				patchProperties(user, userEntity);
				updatedUser = new User(userRepository.saveAndFlush(userEntity));
			}
		}

		return updatedUser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wang.yongrui.wechat.service.UserService#patchUpdate(java.util. List)
	 */
	@Override
	public boolean massPatchUpdate(Set<User> userSet) {
		boolean isSucceed = false;
		Set<UserEntity> effectedUserEntitySet = new HashSet<>();
		for (User user : userSet) {
			if (null != user.getId()) {
				UserEntity userEntity = userRepository.findOne(user.getId());
				if (null != userEntity) {
					patchProperties(user, userEntity);
					effectedUserEntitySet.add(userRepository.saveAndFlush(userEntity));
				}
			}
		}
		if (effectedUserEntitySet.size() == userSet.size()) {
			isSucceed = true;
		}

		return isSucceed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wang.yongrui.wechat.service.UserService#delete(java.lang.Long)
	 */
	@Override
	public Boolean delete(Long id) {
		userRepository.delete(id);
		return true;
	}

	/**
	 * @param user
	 * @param userEntity
	 */
	private void copyProperties(User user, UserEntity userEntity) {
		if (null != user) {
			if (CollectionUtils.isNotEmpty(user.getRoleSet())) {
				Set<RoleEntity> roleEntitySet = new HashSet<>();
				for (Role eachRole : user.getRoleSet()) {
					RoleEntity eachRoleEntity = new RoleEntity();
					BeanUtils.copyProperties(eachRole, eachRoleEntity);
					roleEntitySet.add(eachRoleEntity);
				}
				userEntity.setRoleEntitySet(roleEntitySet);
			} else {
				userEntity.setRoleEntitySet(null);
			}

			if (null != user.getExtendedInfoMap()) {
				List<ExtendedInfoEntity> extendedInfoEntityList = new ArrayList<>();
				Set<String> nameSet = new HashSet<>();
				for (String eachKey : user.getExtendedInfoMap().keySet()) {
					if (!nameSet.contains(eachKey)) {
						ExtendedInfoEntity eachExtendedInfoEntity = new ExtendedInfoEntity();
						BeanUtils.copyProperties(user.getExtendedInfoMap().get(eachKey), eachExtendedInfoEntity);
						eachExtendedInfoEntity.setName(eachKey);
						extendedInfoEntityList.add(eachExtendedInfoEntity);
					}
					nameSet.add(eachKey);
				}
				userEntity.setExtendedInfoEntityList(extendedInfoEntityList);
			} else {
				userEntity.setExtendedInfoEntityList(null);
			}

			BeanUtils.copyProperties(user, userEntity);
		}
	}

	/**
	 * PUT means add new properties, replace existing properties, remove other
	 * properties
	 * 
	 * @param user
	 * @param userEntity
	 */
	private void putProperties(User user, UserEntity userEntity) {
		if (null != user) {
			updateRole(user, userEntity, true);
			updateExtendedInfo(user, userEntity, true);
			BeanUtils.copyProperties(user, userEntity);
		}
	}

	/**
	 * PATCH means only update not-null properties and add new ones
	 * 
	 * @param user
	 * @param userEntity
	 */
	private void patchProperties(User user, UserEntity userEntity) {
		if (null != user) {
			updateRole(user, userEntity, false);
			updateExtendedInfo(user, userEntity, false);
			PatchBeanUtils.patchProperties(user, userEntity);
		}
	}

	/**
	 * @param user
	 * @param userEntity
	 * @param isPutUpdate
	 */
	private void updateRole(User user, UserEntity userEntity, boolean isPutUpdate) {
		if (CollectionUtils.isNotEmpty(user.getRoleSet())) {
			Set<RoleEntity> roleEntitySet = (CollectionUtils.isNotEmpty(userEntity.getRoleEntitySet()) && !isPutUpdate)
					? userEntity.getRoleEntitySet() : new HashSet<>();

			List<Long> roleIdList = new ArrayList<>();
			for (Role eachRole : user.getRoleSet()) {
				if (null != eachRole.getId()) {
					roleIdList.add(eachRole.getId());
				}
			}
			if (CollectionUtils.isNotEmpty(roleIdList)) {
				List<RoleEntity> roleEntityList = roleRepository.findAll(roleIdList);
				roleEntitySet.addAll(roleEntityList);
				if (isPutUpdate) {
					roleEntitySet.retainAll(roleEntityList);
				}
				userEntity.setRoleEntitySet(roleEntitySet);
			}
		} else if (isPutUpdate) {
			userEntity.getRoleEntitySet().clear();
		}
	}

	/**
	 * This method is only for PUT or PATCH updating
	 * 
	 * @param user
	 * @param userEntity
	 * @param isPutUpdate
	 */
	private void updateExtendedInfo(User user, UserEntity userEntity, boolean isPutUpdate) {
		if (null != user.getExtendedInfoMap()) {
			List<ExtendedInfoEntity> existExtendedInfoEntityList = userEntity.getExtendedInfoEntityList();
			List<ExtendedInfoEntity> toBeRemovedExtendedInfoEntityList = new ArrayList<>();
			toBeRemovedExtendedInfoEntityList.addAll(existExtendedInfoEntityList);
			Set<String> nameSet = new HashSet<>();
			Set<ExtendedInfoEntity> stayExtendedInfoEntitySet = new HashSet<>();
			for (ExtendedInfoEntity eachExistExtendedInfoEntity : existExtendedInfoEntityList) {
				for (String eachKey : user.getExtendedInfoMap().keySet()) {
					ExtendedInfo eachExtendedInfo = user.getExtendedInfoMap().get(eachKey);
					if (null != eachExtendedInfo.getId()
							&& eachExistExtendedInfoEntity.getId().equals(eachExtendedInfo.getId())) {
						// Update exist ones
						PatchBeanUtils.updateProperties(eachExtendedInfo, eachExistExtendedInfoEntity, isPutUpdate);
						eachExistExtendedInfoEntity.setName(eachKey);
						stayExtendedInfoEntitySet.add(eachExistExtendedInfoEntity);
						nameSet.add(eachExistExtendedInfoEntity.getName());
						break;
					}
				}
			}

			// Remove others
			/*
			 * Below code does not work, will check later
			 * existExtendedInfoEntityList.retainAll(stayExtendedInfoEntitySet);
			 */
			toBeRemovedExtendedInfoEntityList.removeAll(stayExtendedInfoEntitySet);
			existExtendedInfoEntityList.removeAll(toBeRemovedExtendedInfoEntityList);

			for (String eachKey : user.getExtendedInfoMap().keySet()) {
				ExtendedInfo eachExtendedInfo = user.getExtendedInfoMap().get(eachKey);
				if (null == eachExtendedInfo.getId() && !nameSet.contains(eachKey)) {
					// Add new ones
					ExtendedInfoEntity extendedInfoEntity = new ExtendedInfoEntity();
					PatchBeanUtils.updateProperties(eachExtendedInfo, extendedInfoEntity, isPutUpdate);
					extendedInfoEntity.setName(eachKey);
					existExtendedInfoEntityList.add(extendedInfoEntity);
				}
				nameSet.add(eachKey);
			}
			userEntity.setExtendedInfoEntityList(existExtendedInfoEntityList);
		} else if (isPutUpdate) {
			// DO NOT USE userEntity.setExtendedInfoEntityList(null),
			// this may cause: "A collection with cascade="all-delete-orphan"
			// was no longer referenced by the owning entity instance" error
			userEntity.getExtendedInfoEntityList().clear();
		}
	}

}
