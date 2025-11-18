package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
@Named("createRides")
@ViewScoped
public class CreateRidesBean implements Serializable{
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
	}
	public void createRides() {
		if(from==null||to==null||data==null||price==0||price==0) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Errorea Ride-ak sortzean"));
		}else {
			try {
				blfacade = FacadeBean.getBusinessLogic();
				Ride ride=blfacade.createRide(from, to, data, places, places, "driver1@gmail.com");
				rideExists="";
				rideGood="Ondo sortu da ondorengo ride-a:  " + ride;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ondo sortu da ondorengo ride-a" + ride));
			} catch (RideMustBeLaterThanTodayException e) {
				rideExists="Ride must be Later Than today";
				rideGood="";
			} catch (RideAlreadyExistException e) {
				rideExists="Ride already exists";
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
