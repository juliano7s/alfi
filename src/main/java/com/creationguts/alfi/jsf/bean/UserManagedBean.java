package com.creationguts.alfi.jsf.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

@ManagedBean
@SessionScoped
public class UserManagedBean implements Serializable {

	@PostConstruct
	public void init() {
		
	}
	
    public String logout() {
    	logger.debug("logging out and invalidating session");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }

	private static Logger logger = Logger.getLogger(UserManagedBean.class);
	private static final long serialVersionUID = 246182952037326660L;
}
