package com.creationguts.alfi.jpa.manager;

import java.util.List;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.vo.Client;

public class ClientEntityManager extends EntityManager<Client> {

	public ClientEntityManager() {
		super(Client.class);
	}

	public ClientEntityManager(Class<Client> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}

	public List<Client> getClients() {
		logger.debug("Getting clients");
		getEntityManager().getTransaction().begin();
		List<Client> result = getEntityManager()
				.createQuery("from Client", Client.class).getResultList();
		logger.debug("total clients returned: " + result.size());
		getEntityManager().getTransaction().commit();
		closeEntityManager();

		return result;
	}
	
	public List<Client> getClientsInDebt() {
		logger.debug("Getting clients in debt");
		getEntityManager().getTransaction().begin();
		List<Client> result = getEntityManager()
				.createQuery(
						"from Client c where c in (select Client from Order o join o.client where o.paidStatus = 0)",
						Client.class).getResultList();
		getEntityManager().getTransaction().commit();
		closeEntityManager();
		
		return result;
	}

	public List<Client> getClientsByName(String name) {
		logger.debug("Getting clients by name: " + name);
		getEntityManager().getTransaction().begin();
		List<Client> result = getEntityManager()
				.createQuery(
						"from Client c where upper(c.name) like upper('%" + name
								+ "%') order by c.name", Client.class)
				.getResultList();
		logger.debug("total clients returned: " + result.size());
		getEntityManager().getTransaction().commit();
		closeEntityManager();

		return result;
	}

	public Client getClientByCpf(String cpf) throws Exception {
		logger.debug("Getting clients by name: " + cpf);
		getEntityManager().getTransaction().begin();
		List<Client> result = getEntityManager()
				.createQuery("from Client c where c.cpf = '" + cpf + "'",
						Client.class).getResultList();
		logger.debug("total clients returned: " + result.size());
		getEntityManager().getTransaction().commit();
		closeEntityManager();

		if (result.size() > 1)
			throw new Exception("CPF should be unique on clients table");

		return result.get(0);
	}

	public Client getClientByPhone(String phone) throws Exception {
		logger.debug("Getting clients by phone: " + phone);
		getEntityManager().getTransaction().begin();
		List<Client> result = getEntityManager()
				.createQuery("from Client c where c.phone = '" + phone + "'",
						Client.class).getResultList();
		logger.debug("total clients returned: " + result.size());
		getEntityManager().getTransaction().commit();
		closeEntityManager();

		if (result.size() > 1)
			throw new Exception(
					"Phone number should be unique on clients table");
		else if (result.isEmpty())
			return null;

		return result.get(0);
	}

	@Override
	public Client loadAll(Client client) {
		getEntityManager().getTransaction().begin();
		client = getEntityManager().createQuery(
				"from Client c left join fetch c.orders o where c.id = "
						+ client.getId(), Client.class).getSingleResult();
		getEntityManager().getTransaction().commit();
		closeEntityManager();
		
		logger.debug("All attributes loaded from client: " + client);
		return client;
	}

	private static Logger logger = Logger.getLogger(ClientEntityManager.class);
}
