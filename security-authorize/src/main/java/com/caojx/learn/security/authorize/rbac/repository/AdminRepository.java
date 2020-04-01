/**
 * 
 */
package com.caojx.learn.security.authorize.rbac.repository;

import com.caojx.learn.security.authorize.rbac.domain.Admin;
import org.springframework.stereotype.Repository;


/**
 * @author zhailiang
 *
 */
@Repository
public interface AdminRepository extends ImoocRepository<Admin> {

	Admin findByUsername(String username);

}
