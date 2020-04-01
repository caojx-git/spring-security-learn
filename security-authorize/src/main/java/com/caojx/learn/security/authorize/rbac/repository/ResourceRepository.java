/**
 * 
 */
package com.caojx.learn.security.authorize.rbac.repository;

import com.caojx.learn.security.authorize.rbac.domain.Resource;
import org.springframework.stereotype.Repository;


/**
 * @author zhailiang
 *
 */
@Repository
public interface ResourceRepository extends ImoocRepository<Resource> {

	Resource findByName(String name);

}
