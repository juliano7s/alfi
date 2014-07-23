package com.creationguts.alfi.jpa.manager;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import com.creationguts.alfi.Constants;
import com.creationguts.alfi.jpa.model.User;

public class UserEntityManager {
	
	public List<User> getUsers() {
		
		logger.debug("Getting users");
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(Constants.POBS_PERSISTENCE_UNIT);
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		List<User> result = entityManager.createQuery( "from User", User.class ).getResultList();
		logger.debug("total users returned: " + result.size());
		entityManager.getTransaction().commit();
		entityManager.close();
	
		return result;
	}
	
	private static Logger logger = Logger.getLogger(UserEntityManager.class);
}
