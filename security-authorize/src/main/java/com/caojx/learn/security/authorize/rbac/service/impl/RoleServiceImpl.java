/**
 * 
 */
package com.caojx.learn.security.authorize.rbac.service.impl;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.caojx.learn.security.authorize.rbac.domain.Role;
import com.caojx.learn.security.authorize.rbac.domain.RoleResource;
import com.caojx.learn.security.authorize.rbac.dto.RoleInfo;
import com.caojx.learn.security.authorize.rbac.repository.ResourceRepository;
import com.caojx.learn.security.authorize.rbac.repository.RoleRepository;
import com.caojx.learn.security.authorize.rbac.repository.RoleResourceRepository;
import com.caojx.learn.security.authorize.rbac.repository.support.QueryResultConverter;
import com.caojx.learn.security.authorize.rbac.service.RoleService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author zhailiang
 *
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ResourceRepository resourceRepository;
	
	@Autowired
	private RoleResourceRepository roleResourceRepository;
	
	/* (non-Javadoc)
	 * @see com.imooc.security.rbac.service.RoleService#create(com.imooc.security.rbac.dto.RoleInfo)
	 */
	@Override
	public RoleInfo create(RoleInfo info) {
		Role role = new Role();
		BeanUtils.copyProperties(info, role);
		info.setId(roleRepository.save(role).getId());
		return info;
	}

	/* (non-Javadoc)
	 * @see com.imooc.security.rbac.service.RoleService#update(com.imooc.security.rbac.dto.RoleInfo)
	 */
	@Override
	public RoleInfo update(RoleInfo info) {
		Role role = roleRepository.findById(info.getId()).orElse(null);
		BeanUtils.copyProperties(info, role);
		return info;
	}

	/**
	 * (non-Javadoc)
	 * @see com.idea.ams.service.RoleService#delete(Long)
	 */
	@Override
	public void delete(Long id) {
		Role role = roleRepository.findById(id).orElse(null);
		if(CollectionUtils.isNotEmpty(role.getAdmins())){
			throw new RuntimeException("不能删除有下挂用户的角色");
		}
		roleRepository.deleteById(id);
	}
//
//	@Override
//	public String[] getRoleMenus(Long id) {
//		return StringUtils.split(roleRepository.findOne(id).getMenus(), ",");
//	}
//
//	/**
//	 * (non-Javadoc)
//	 * @see com.idea.ams.service.RoleService#setRoleMenu(java.lang.Long, java.lang.String)
//	 */
//	@Override
//	public void setRoleMenu(Long roleId, String menuIds) {
//		Role role = roleRepository.findOne(roleId);
//		role.setMenus(menuIds);
//	}

	/**
	 * (non-Javadoc)
	 * @see com.idea.ams.service.RoleService#getRoleInfo(Long)
	 */
	@Override
	public RoleInfo getInfo(Long id) {
		Role role = roleRepository.findById(id).orElse(null);
		RoleInfo info = new RoleInfo();
		BeanUtils.copyProperties(role, info);
		return info;
	}

	/* (non-Javadoc)
	 * @see com.imooc.security.rbac.service.RoleService#findAll()
	 */
	@Override
	public List<RoleInfo> findAll() {
		return QueryResultConverter.convert(roleRepository.findAll(), RoleInfo.class);
	}
	
	@Override
	public String[] getRoleResources(Long id) {
		Role role = roleRepository.findById(id).orElse(null);
		Set<String> resourceIds = new HashSet<>();
		for (RoleResource resource : role.getResources()) {
			resourceIds.add(resource.getResource().getId().toString());
		}
		return resourceIds.toArray(new String[resourceIds.size()]);
	}

	/**
	 * (non-Javadoc)
	 * @see com.idea.ams.service.RoleService#setRoleMenu(Long, String)
	 */
	@Override
	public void setRoleResources(Long roleId, String resourceIds) {
		resourceIds = StringUtils.removeEnd(resourceIds, ",");
		Role role = roleRepository.findById(roleId).orElse(null);
		roleResourceRepository.deleteAll(role.getResources());
		String[] resourceIdArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(resourceIds, ",");
		for (String resourceId : resourceIdArray) {
			RoleResource roleResource = new RoleResource();
			roleResource.setRole(role);
			roleResource.setResource(resourceRepository.getOne(new Long(resourceId)));
			roleResourceRepository.save(roleResource);
		}
	}

}
