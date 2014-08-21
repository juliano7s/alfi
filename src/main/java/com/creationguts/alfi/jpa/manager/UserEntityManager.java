package com.creationguts.alfi.jpa.manager;

import java.util.List;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.vo.User;

public class UserEntityManager extends EntityManager<User> {
	
	public UserEntityManager() {
		super(User.class);
		// TODO Auto-generated constructor stub
	}
	
	public UserEntityManager(Class<User> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}

	public List<User> getUsers() {
		
		logger.debug("Getting users");
		javax.persistence.EntityManager entityManager = getEntityManager();
		entityManager.getTransaction().begin();
		List<User> result = entityManager.createQuery( "from User", User.class ).getResultList();
		logger.debug("total users returned: " + result.size());
		entityManager.getTransaction().commit();
		entityManager.close();
	
		return result;
	}
	
	@Override
	public User loadAll(User user) {
		return user;
	}
	
	private static Logger logger = Logger.getLogger(UserEntityManager.class);


}
