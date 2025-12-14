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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import domain.Alerta;
import domain.Driver;
import domain.Kotxe;
import domain.Mezua;
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
@Named("createAlerta")
@ViewScoped
public class CreateAlertaBean implements Serializable{
	@Inject
	private LoginBean loginBean;
	private Profile user;
	private BLFacade blfacade;
	private Date data;
	private String from;
	private String to;
	private float price;
	private int places;
	private String alertaGood="";
	private String alertaExists="";
	private List<Alerta> alertak;
	private List<Mezua>mezuak;
	private Alerta aukeratutakoAlerta;
	public CreateAlertaBean() {

	}
	@PostConstruct
	public void init() {
		this.user = loginBean.getOraingoUser();
		blfacade=FacadeBean.getBusinessLogic();
        this.alertak = blfacade.kargatuTravellerAlertak((Traveller) user);
        for(Alerta alerta:alertak) {
        	alerta.setMezuKopuruaLaguntzaile(blfacade.getAlertaMezuak(alerta).size());
        }
        System.out.println(alertak);
        Collections.sort(alertak,Collections.reverseOrder());
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
		Date dataP=(Date)event.getObject();
		if(dataP.compareTo(new Date())>0) {
			alertaGood="";
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(" Ezin da gaur baina lehenagoko datarik aukeratu"));

		}
		
	}
	public void clearGood() {
		alertaGood="";
	}


	public String getAlertaGood() {
		return alertaGood;
	}
	public void setAlertaGood(String alertaGood) {
		this.alertaGood = alertaGood;
	}
	public String getAlertaExists() {
		return alertaExists;
	}
	public void setAlertaExists(String alertaExists) {
		this.alertaExists = alertaExists;
	}
	public void createAlerta() {
		blfacade=FacadeBean.getBusinessLogic();
		if(data.compareTo(new Date())>0) {
		boolean emaitza=blfacade.sortuAlerta((Traveller)user, from, to, data);
		
		if(emaitza) {
			 alertaGood="Ondo sortu da alerta!";
			 alertaExists="";
             this.alertak = blfacade.kargatuTravellerAlertak((Traveller) user);

		}else {
			alertaGood="";
			 alertaExists="Alerta jadanik existitzen da!!";
		}
		}else {
			alertaGood="";
			 alertaExists="Aukeratu baliozko data bat!!";
		}
	}
	public List<Alerta> getAlertak() {

	
		

		return alertak;
	}
	public void setAlertak(List<Alerta> alertak) {
		this.alertak = alertak;
	}
	public List<Mezua> getMezuak() {
		return mezuak;
	}
	public void setMezuak(List<Mezua> mezuak) {
		this.mezuak = mezuak;
	}
	public void aukeratuAlerta(Alerta alerta) {
		System.out.println("Alerta aukeratuta");
		this.aukeratutakoAlerta=alerta;
		blfacade=FacadeBean.getBusinessLogic();
		mezuak=blfacade.getAlertaMezuak(aukeratutakoAlerta);
		
	}
	public void ezabatuAlerta(Alerta alerta) {
		this.aukeratutakoAlerta=null;
		this.mezuak=null;
		blfacade=FacadeBean.getBusinessLogic();
		alertaGood="Ondo deuseztatu da alerta";
		alertaExists="";
		blfacade.deuseztatuAlerta(alerta);
        this.alertak = blfacade.kargatuTravellerAlertak((Traveller) user);

	}
	public Alerta getAukeratutakoAlerta() {
		return aukeratutakoAlerta;
	}
	public void setAukeratutakoAlerta(Alerta aukeratutakoAlerta) {
		this.aukeratutakoAlerta = aukeratutakoAlerta;
	}
}
