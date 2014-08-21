package com.creationguts.alfi.jpa.manager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import com.creationguts.alfi.Constants;

public abstract class EntityManager<T> {
	
	public EntityManager(Class<T> clazz) {
		entityManagerType = clazz;
	}
	
	public T save(T entity) {
		this.getEntityManager().getTransaction().begin();
		entity = this.getEntityManager().merge(entity);
		logger.debug("Saving entity " + entity);
		this.getEntityManager().getTransaction().commit();
		this.closeEntityManager();
		
		return entity;
	}
	
	public T findById(Long id) {
		this.getEntityManager().getTransaction().begin();
		T entity = this.getEntityManager().find(entityManagerType, id);
		this.getEntityManager().getTransaction().commit();
		this.closeEntityManager();
		
		return entity;
	}
	
	public abstract T loadAll(T entity);

	protected javax.persistence.EntityManager getEntityManager() {
		if (this.e == null) {
			EntityManagerFactory entityManagerFactory = Persistence
					.createEntityManagerFactory(Constants.ALFI_PERSISTENCE_UNIT);
			javax.persistence.EntityManager entityManager = entityManagerFactory
					.createEntityManager();

			this.e = entityManager;
		}

		return this.e;
	}

	protected void closeEntityManager() {
		if (this.e != null) {
			this.e.close();
		}

		this.e = null;
	}

	private final Class<T> entityManagerType;
	private javax.persistence.EntityManager e;
	private static Logger logger = Logger.getLogger(EntityManager.class);

}
