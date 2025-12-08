package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import domain.Driver;
import domain.Profile;
import domain.Ride;
import domain.Traveller;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
@Named("register")
@RequestScoped
public class RegisterBean implements Serializable{
	private BLFacade blfacade;
	private String email;
	private String name;
	private String surname;
	private String user;
	private String password;
	private String password2;
	private String telf;
	private String mota="Traveller";
	 @Inject
	private LoginBean loginBean;


	public RegisterBean() {

	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	public String getTelf() {
		return telf;
	}
	public void setTelf(String telf) {
		this.telf = telf;
	}
	public LoginBean getLoginBean() {
		return loginBean;
	}
	public boolean comparePassword() {
		return password.equals(password2);
	}

	public String register() {
		if(this.comparePassword()) {

			Profile profila= new Traveller(email,name,surname,user,password,telf);
			blfacade=FacadeBean.getBusinessLogic();
			Profile emaitza= blfacade.register(profila, mota);
			if(emaitza!=null) {
				loginBean.setOraingoUser(emaitza);
				if(mota.equals("Traveller")) {
					return "menu";
				}else {
					return "menu";
				}

			}else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Usuarioa Existitzen da jadanik"));
				return "error";
			}
		}else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Bi Pasahitzak berdinak izan behar dute."));
			return "error";
		}
	}
	public String getMota() {
		return mota;
	}
	public void setMota(String mota) {
		this.mota = mota;
	}

}
