package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.primefaces.event.DateViewChangeEvent;
import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import domain.Profile;
import domain.Ride;
import domain.Traveller;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("proba")
@SessionScoped
public class ProbaBean implements Serializable {
	private Profile user;
	@Inject
	private LoginBean loginBean;
	private List<Ride> rides= new ArrayList<Ride>();
	private static BLFacade blfacade = new BLFacadeImplementation();
	private String from;

	private List<String> fromCities = new ArrayList<String>();


	public ProbaBean() {



	}

	@PostConstruct
	public void init() {
		this.user = loginBean.getOraingoUser();
	}

	public List<Ride> getRides() {
		if (from == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eremu bat hutsa dago"));
			return new ArrayList<Ride>();
		} else {
			rides = blfacade.getRides(from);
			return rides;
		}
	}

	public List<String> getFromCities() {
		fromCities = new ArrayList<String>();
		fromCities = blfacade.getDepartCitiesProba();
		if (from == null && fromCities.size() > 0) {
			from = fromCities.get(0);
		}
		System.out.println("From:" + fromCities);
		return fromCities;
	}
	public String konprobatu() {
		if(fromCities.size()==0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ez dago departing cities-ik"));
			return "error";
		}else {
			return "proba2";
		}
	}

	/*
	 * private List<String> getToCities(String fromCities){ toCities=new
	 * ArrayList<String>(); toCities=blfacade.getDestinationCities(fromCities);
	 * System.out.println("To:"+ toCities); return toCities; }
	 */
	

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	

	



}
