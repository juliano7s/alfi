package com.creationguts.alfi.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;

import com.creationguts.alfi.jpa.manager.ClientEntityManager;
import com.creationguts.alfi.jpa.vo.Client;
import com.creationguts.alfi.jpa.vo.Order;
import com.creationguts.alfi.jpa.vo.Order.Status;
import com.creationguts.alfi.jpa.vo.Phone;

@ManagedBean
@SessionScoped
public class ClientManagedBean implements Serializable {

	/**
	 * Action: execute search for name, phone and cpf
	 */
	public String executeWholeSearch() {
		logger.debug("Executing whole client search.");
		logger.debug("Search string: " + wholeSearch);
		logger.debug("Clearing client and client list from bean");
		client = null;
		clients = null;

		wholeSearch = wholeSearch.replace("-", "");
		wholeSearch = wholeSearch.replace(".", "");
		String view = "";

		Pattern phoneNumberPattern1 = Pattern.compile("\\d{2}|\\d{8}");
		Pattern cpfPattern1 = Pattern.compile("\\d{11}");

		ClientEntityManager cem = new ClientEntityManager();
		try {
			if (phoneNumberPattern1.matcher(wholeSearch).matches()) {
				logger.debug("wholeSearch matched phoneNumberPattern1 "
						+ phoneNumberPattern1);
				client = cem.getClientByPhone(wholeSearch);
				view = "client";
			} else if (cpfPattern1.matcher(wholeSearch).matches()) {
				logger.debug("wholeSearch matched cpfPattern1 " + cpfPattern1);
				client = cem.getClientByCpf(wholeSearch);
				view = "client";
			} else {
				logger.debug("wholeSearch matched no pattern.");
				clients = cem.getClientsByName(wholeSearch);
				if (clients.size() == 1) {
					client = clients.get(0);
					view = viewClient();
				} else
					view = "list_clients";
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			view = "index";
			UIComponent mainSearchButton = UIComponent
					.getCurrentComponent(FacesContext.getCurrentInstance());
			FacesContext.getCurrentInstance().addMessage(
					mainSearchButton.getClientId(FacesContext
							.getCurrentInstance()),
					new FacesMessage(e.getMessage()));
		}

		if (client == null && clients == null || clients.size() == 0) {
			logger.debug("Client not found, going to new client view");
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Cliente não encontrado."));
			newClient();
			view = "edit_client";
		} else {
			String foundClients = (clients.size() > 1) ? " clientes econtrados."
					: " cliente encontrado.";
			logger.debug(clients.size() + foundClients);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(clients.size() + foundClients));
		}

		return view;
	}

	/**
	 * Action: create new client to persist and redirect to edit_client.xhtml
	 */
	@PostConstruct
	public void newClient() {
		client = new Client();
		phoneNumbers = new ArrayList<Phone>();
		phoneNumbers.add(new Phone());
	}

	/**
	 * Action: choose client to edit on client.xhtml
	 */
	public String editClient() {
		logger.debug("Editing client");
		Long clientId = Long.parseLong(FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("clientId"));
		logger.debug("Id: " + clientId);

		for (Client c : clients) {
			if (c.getId() == clientId) {
				client = c;
				phoneNumbers = client.getPhoneNumbers();
			}
		}

		return "edit_client";
	}

	/**
	 * Action: choose client to view on client.xhtml
	 */
	public String viewClient() {
		logger.debug("Viewing client");
		if (client == null || client != null
				&& client.getId() == 0) {
			Long clientId = Long.parseLong(FacesContext.getCurrentInstance()
					.getExternalContext().getRequestParameterMap()
					.get("clientId"));
			logger.debug("Id: " + clientId);

			for (Client c : clients) {
				if (c.getId().equals(clientId)) {
					client = c;
				}
			}
		}
		
		logger.debug("Client to view: " + client.getName());
		client = (new ClientEntityManager()).loadAll(client);

		return "client";
	}

	/**
	 * Action: save client to the database(insert/update)
	 */
	public String saveClient() throws Throwable {
		logger.debug("Saving client on the database");
		logger.debug("Phones submitted: " + phoneNumbers);
		for (Phone p : phoneNumbers) {
			logger.debug(p);
		}
		ClientEntityManager cem = new ClientEntityManager();
		client.setPhoneNumbers(phoneNumbers);
		client = cem.save(client);
	
		logger.debug("Cliente salvo com id " + client.getId());
		FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage("Cliente salvo com id "
								+ client.getId()));

		return viewClient();
	}

	public String addPhoneNumber() {
		logger.debug("Adding phone number");
		phoneNumbers.add(new Phone());
		return "edit_client";
	}
	
	/**
	 * Validator for CPF field
	 * @param context
	 * @param component
	 * @param value
	 * @throws ValidatorException
	 */
	public void validateCpf(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String cpf = ((String) value).trim().replace(".", "").replace("-", "");
		if (cpf.length() != 11) {
			throw new ValidatorException(new FacesMessage("CPF deve ter 11 dígitos."));
		}
	}

	public String getWholeSearch() {
		return wholeSearch;
	}

	public void setWholeSearch(String search) {
		wholeSearch = search;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<Phone> getPhoneNumbers() {
		logger.debug("Getting phone numbers: size=" + phoneNumbers.size());
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<Phone> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	
	public List<Order> getOrdersInProgress() {
		logger.debug("Getting orders in progress from client " + client.getName());
		List<Order> list = new ArrayList<Order>();
		if (client != null) {
			for (Order o : client.getOrders()) {
				logger.debug("Checking order status: " + o.getStatus());
				if (o.getStatus().equals(Status.INPROGRESS)) {
					list.add(o);
				}
			}
		}
		return list;
	}
	
	public void setOrdersInProgress(List<Order> list) {}
	
	public List<Order> getOrdersReady() {
		logger.debug("Getting orders ready from client " + client.getName());
		List<Order> list = new ArrayList<Order>();
		if (client != null) {
			for (Order o : client.getOrders()) {
				logger.debug("Checking order status: " + o.getStatus());
				if (o.getStatus().equals(Status.READY)) {
					list.add(o);
				}
			}
		}
		return list;
	}
	
	public void setOrdersReady(List<Order> list) {}

	private String wholeSearch;
	private List<Client> clients;
	private Client client;
	private List<Phone> phoneNumbers;
	
	private static Logger logger = Logger.getLogger(ClientManagedBean.class);
	private static final long serialVersionUID = 2461829560777826670L;
}
