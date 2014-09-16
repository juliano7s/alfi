package com.creationguts.alfi.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
import com.creationguts.alfi.jpa.vo.Purchase;
import com.creationguts.alfi.jpa.vo.PurchaseProduct;

@ManagedBean
@SessionScoped
public class ClientManagedBean implements Serializable {

	@PostConstruct
	public void init() {
		logger.debug("Initializing clientManagedBean");
		newClient();
	}

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
					new FacesMessage("Cliente n�o encontrado."));
			newClient();
			view = "edit_client";
		} else {
			String foundClients = (clients.size() > 1) ? " clientes econtrados "
					: " cliente encontrado ";
			foundClients += "para o termo de busca '" + wholeSearch + "'";
			logger.debug(clients.size() + foundClients);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(clients.size() + foundClients));
		}

		return view;
	}

	/**
	 * Action: create new client to persist and redirect to edit_client.xhtml
	 */
	public String newClient() {
		logger.debug("Clearing client bean");
		client = new Client();
		client.getPhoneNumbers().add(new Phone());
		phoneNumbers = new ArrayList<Phone>();
		phoneNumbers.add(new Phone());
		return "edit_client";
	}

	/**
	 * Action: choose client to edit on edit_client.xhtml
	 */
	public String editClient() {
		logger.debug("Editing client");
		Long clientId = Long.parseLong(FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("clientId"));
		logger.debug("Id: " + clientId);

		client = (new ClientEntityManager()).findById(clientId);
		client = (new ClientEntityManager()).loadAll(client);
		logger.debug("Client to edit: " + client);

		return "edit_client";
	}

	/**
	 * Action: choose client to view on client.xhtml
	 */
	public String viewClient() {
		logger.debug("Viewing client");
		String clientIdParam = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("clientId");
		if (clientIdParam != null && !clientIdParam.trim().equals("")) {
			Long clientId = Long.parseLong(clientIdParam);
			logger.debug("Id: " + clientId);

			client = (new ClientEntityManager()).findById(clientId);

			orderManagedBean.setOrder(new Order()); // Clearing order form
			purchaseManagedBean.setPurchase(new Purchase());
			purchaseManagedBean.setPurchasedProducts(new HashSet<PurchaseProduct>());
			clientOpenOrders = null;
			clientClosedOrders = null;
			clientOpenPurchases = null;
			clientClosedPurchases = null;

			logger.debug("Client to view: " + client.getName());
			client = (new ClientEntityManager()).loadAll(client);
		}

		return "client";
	}

	/**
	 * Action: save client to the database(insert/update)
	 */
	public String saveClient() throws Throwable {
		logger.debug("Saving client on the database");
		ClientEntityManager cem = new ClientEntityManager();
		client = cem.save(client);

		logger.debug("Cliente salvo com id " + client.getId());
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Cliente salvo com id " + client.getId()));

		return viewClient();
	}

	public String addPhoneNumber() {
		logger.debug("Adding phone number");
		client.getPhoneNumbers().add(new Phone());
		return "edit_client";
	}

	/**
	 * Validator for CPF field
	 * 
	 * @param context
	 * @param component
	 * @param value
	 * @throws ValidatorException
	 */
	public void validateCpf(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String cpf = ((String) value).trim().replace(".", "").replace("-", "");
		if (cpf.length() != 11) {
			throw new ValidatorException(new FacesMessage(
					"CPF deve ter 11 d�gitos."));
		}
	}
	
	public List<Client> getInDebtClients() {
		ClientEntityManager cem = new ClientEntityManager();
		return cem.getClientsInDebt();
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

	public List<Order> getOrdersOpened() {
		logger.debug("Getting orders in progress from client "
				+ client.getName());
		if (clientOpenOrders == null) {
			clientOpenOrders = new ArrayList<Order>();
			if (client != null) {
				for (Order o : client.getOrders()) {
					logger.debug("Checking order status: " + o.getStatus());
					if (o.getStatus().equals(Status.INPROGRESS)
							|| o.getStatus().equals(Status.READY)) {
						clientOpenOrders.add(o);
					}
				}
			}
		}
		return clientOpenOrders;
	}

	public void setOrdersOpened(List<Order> list) {
	}

	public List<Order> getOrdersDelivered() {
		logger.debug("Getting orders ready from client " + client.getName());
		
		if (clientClosedOrders == null) {
			clientClosedOrders = new ArrayList<Order>();
			if (client != null) {
				for (Order o : client.getOrders()) {
					logger.debug("Checking order status: " + o.getStatus());
					if (o.getStatus().equals(Status.DELIVERED)) {
						clientClosedOrders.add(o);
					}
				}
			}
		}
		return clientClosedOrders;
	}
	
	public List<Purchase> getPurchasesOpened() {
		logger.debug("Getting opened purchases for client");
		if (clientOpenPurchases == null) {
			clientOpenPurchases = new ArrayList<Purchase>();
			for (Purchase p : client.getPurchases()) {
				if (p.getPaidStatus() == false) {
					clientOpenPurchases.add(p);
				}
			}
		}
		return clientOpenPurchases;
	}
	
	public List<Purchase> getPurchasesClosed() {
		logger.debug("Getting closed purchases for client");
		if (clientClosedPurchases == null) {
			clientClosedPurchases = new ArrayList<Purchase>();
			for (Purchase p : client.getPurchases()) {
				if (p.getPaidStatus() == true) {
					clientClosedPurchases.add(p);
				}
			}
		}
		return clientClosedPurchases;
	}

	public void setOrdersReady(List<Order> list) {
	}

	public OrderManagedBean getOrderManagedBean() {
		return orderManagedBean;
	}

	public void setOrderManagedBean(OrderManagedBean orderManagedBean) {
		this.orderManagedBean = orderManagedBean;
	}

	public PurchaseManagedBean getPurchaseManagedBean() {
		return purchaseManagedBean;
	}

	public void setPurchaseManagedBean(PurchaseManagedBean purchaseManagedBean) {
		this.purchaseManagedBean = purchaseManagedBean;
	}

	private String wholeSearch;
	private List<Client> clients;
	private Client client;
	private List<Order> clientOpenOrders;
	private List<Order> clientClosedOrders;
	private List<Purchase> clientOpenPurchases;
	private List<Purchase> clientClosedPurchases;
	private List<Phone> phoneNumbers;

	@ManagedProperty(value = "#{orderManagedBean}")
	private OrderManagedBean orderManagedBean;

	@ManagedProperty(value="#{purchaseManagedBean}")
	private PurchaseManagedBean purchaseManagedBean;


	private static Logger logger = Logger.getLogger(ClientManagedBean.class);
	private static final long serialVersionUID = 2461829560777826670L;
}
