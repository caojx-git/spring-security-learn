/**
 * 
 */
package com.caojx.learn.security.authorize.rbac.repository.spec;


import com.caojx.learn.security.authorize.rbac.domain.Admin;
import com.caojx.learn.security.authorize.rbac.dto.AdminCondition;
import com.caojx.learn.security.authorize.rbac.repository.support.ImoocSpecification;
import com.caojx.learn.security.authorize.rbac.repository.support.QueryWraper;

/**
 * @author zhailiang
 *
 */
public class AdminSpec extends ImoocSpecification<Admin, AdminCondition> {

	public AdminSpec(AdminCondition condition) {
		super(condition);
	}

	@Override
	protected void addCondition(QueryWraper<Admin> queryWraper) {
		addLikeCondition(queryWraper, "username");
	}

}
