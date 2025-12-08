package eredua.bean;

import java.io.Serializable;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import domain.Driver;
import domain.Kotxe;
import domain.Profile;
import domain.Ride;
import domain.Traveller;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
@Named("createRides")
@ViewScoped
public class CreateRidesBean implements Serializable{
	@Inject
	private LoginBean loginBean;
	private Profile user;
	private BLFacade blfacade;
	private Date data;
	private String from;
	private String to;
	private float price;
	private int places;
	private String rideGood="";
	private String rideExists="";
	public CreateRidesBean() {

	}
	@PostConstruct
	public void init() {
		this.user = loginBean.getOraingoUser();

		if (this.user == null) {
			System.out.println("¡Acceso no autorizado!");


		}

	}


	public Profile getUser() {
		return user;
	}

	public void setUser(Profile user) {
		this.user = user;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getPlaces() {
		return places;
	}
	public void setPlaces(int places) {
		this.places = places;
	}
	public void onDateSelect(SelectEvent event) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data aukeratua: " + event.getObject()));
		rideGood="";
	}
	public void clearGood() {
		rideGood="";
	}


	public void createRides() {
		if(from==null||to==null||data==null||price==0||price==0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Errorea Ride-ak sortzean"));
		}else {
			try {
				blfacade =FacadeBean.getBusinessLogic();
				List<Float>prezioak= new ArrayList<Float>();
				List<String> ibilbide= new ArrayList<String>();
				
				prezioak.add(price);
				ibilbide.add(from);
				ibilbide.add(to);
				Ride ride=blfacade.createRide(from, to, data, places, prezioak,user.getUser(),new Kotxe("Seat","Ibiza",4,"9321CRN",(Driver)user),ibilbide);
				if(ride==null) {
					rideExists="Errorea bidaia sortzean";
					rideGood="";
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Errorea biaia sortzean"));
				}else {
					rideExists="";
					rideGood="Ondo sortu da ridea";
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ondo sortu da ride-a"));
				}
			} catch (RideMustBeLaterThanTodayException e) {
				rideExists="Datak gaur baina berandoago izan behar du";
				rideGood="";
			} catch (RideAlreadyExistException e) {
				rideExists="Bidaia jadanik existitzen da";
				rideGood="";
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ride already exists"));
			}
		}
	}
	public String getRideGood() {
		return rideGood;
	}
	public void setRideGood(String rideGood) {
		this.rideGood = rideGood;
	}
	public String getRideExists() {
		return rideExists;
	}
	public void setRideExists(String rideExists) {
		this.rideExists = rideExists;
	}
}
