/**
 * 
 */
package com.caojx.learn.security.authorize.rbac.service.impl;

import java.util.List;

import com.caojx.learn.security.authorize.rbac.domain.Admin;
import com.caojx.learn.security.authorize.rbac.domain.Resource;
import com.caojx.learn.security.authorize.rbac.dto.ResourceInfo;
import com.caojx.learn.security.authorize.rbac.repository.AdminRepository;
import com.caojx.learn.security.authorize.rbac.repository.ResourceRepository;
import com.caojx.learn.security.authorize.rbac.service.ResourceService;
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
public class ResourceServiceImpl implements ResourceService {
	
	@Autowired
	private ResourceRepository resourceRepository;
	@Autowired
	private AdminRepository adminRepository;

	/* (non-Javadoc)
	 * @see com.idea.ams.service.ResourceService#getResourceTree(java.lang.Long, com.idea.ams.domain.Admin)
	 */
	@Override
	public ResourceInfo getTree(Long adminId) {
		Admin admin = adminRepository.findById(adminId).orElse(null);
		return resourceRepository.findByName("根节点").toTree(admin);
	}

	/* (non-Javadoc)
	 * @see com.imooc.security.rbac.service.ResourceService#getInfo(java.lang.Long)
	 */
	@Override
	public ResourceInfo getInfo(Long id) {
		Resource resource = resourceRepository.findById(id).orElse(null);
		ResourceInfo resourceInfo = new ResourceInfo();
		BeanUtils.copyProperties(resource, resourceInfo);
		return resourceInfo;
	}

	@Override
	public ResourceInfo create(ResourceInfo info) {
		Resource parent = resourceRepository.findById(info.getParentId()).orElse(null);
		if(parent == null){
			parent = resourceRepository.findByName("根节点");
		}
		Resource resource = new Resource();
		BeanUtils.copyProperties(info, resource);
		parent.addChild(resource);
		info.setId(resourceRepository.save(resource).getId());
		return info;		
	}

	@Override
	public ResourceInfo update(ResourceInfo info) {
		Resource resource = resourceRepository.findById(info.getId()).orElse(null);
		BeanUtils.copyProperties(info, resource);
		return info;
	}

	@Override
	public void delete(Long id) {
		resourceRepository.deleteById(id);
	}
	/* (non-Javadoc)
	 * @see com.imooc.security.rbac.service.ResourceService#move(java.lang.Long, boolean)
	 */
	@Override
	public Long move(Long id, boolean up) {
		Resource resource = resourceRepository.findById(id).orElse(null);
		int index = resource.getSort();
		List<Resource> childs = resource.getParent().getChilds();
		for (int i = 0; i < childs.size(); i++) {
			Resource current = childs.get(i);
			if(current.getId().equals(id)) {
				if(up){
					if(i != 0) {
						Resource pre = childs.get(i - 1);
						resource.setSort(pre.getSort());
						pre.setSort(index);
						resourceRepository.save(pre);
					}
				}else{
					if(i != childs.size()-1) {
						Resource next = childs.get(i + 1);
						resource.setSort(next.getSort());
						next.setSort(index);
						resourceRepository.save(next);
					}
				}
			}
		}
		resourceRepository.save(resource);
		return resource.getParent().getId();
	}

}
