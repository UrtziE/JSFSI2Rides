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
import domain.RideRequest;
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

@Named("erreserbatu")
@ViewScoped
public class ErreserbatuBean implements Serializable {
	private Profile user;
	private String rideRequestGood="";
	private String rideRequestBad="";
	@Inject
    private LoginBean loginBean;
	private List<Ride> rides;
	private static BLFacade blfacade =new BLFacadeImplementation();
	private Date data;
	private String from;
	private String to;
	private List<String> fromCities = new ArrayList<String>();
	private List<String> toCities = new ArrayList<String>();
	private List<Date> egunakBidaiekin = new ArrayList<>();
	private Ride aukeratutakoBidaia;
	private List<Integer> asientoak=new ArrayList<Integer>();
	private String ibilbidea;
	private int aukeratutakoSeats=1;
	private float prezioTotala=0;

	public ErreserbatuBean() {
		
		data=new Date();

	}
	 @PostConstruct
	    public void init() {
	        this.user = loginBean.getOraingoUser();
	    }

	public List<Ride> getRides() {
		if (from == null || to == null || data == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Eremu bat hutsa dago"));
			return null;
		} else {
			rides = blfacade.getRides(from, to, data);
			for(Ride ride: rides) {
				ride.setPrice(ride.lortuBidaiarenPrezioa(from, to));
				ride.setUnekoIbilbide(ride.getIbilbidea(from,to));
			}
			
			return rides;
		}
	}

	public List<String> getFromCities() {
		fromCities = new ArrayList<String>();
		fromCities = blfacade.getDepartCities();
		if (from == null && fromCities.size() > 0) {
			from = fromCities.get(0);
		}
		return fromCities;
	}

	/*
	 * private List<String> getToCities(String fromCities){ toCities=new
	 * ArrayList<String>(); toCities=blfacade.getDestinationCities(fromCities);
	 * System.out.println("To:"+ toCities); return toCities; }
	 */
	public List<String> getToCities() {
		toCities = new ArrayList<String>();
		if (from != null) {
			toCities = blfacade.getDestinationCities(from);
			if (toCities.size() > 0) {
				to = toCities.get(0);
				aurkituDatakBidaiekin();
			}
		}
		return toCities;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date date) {
		this.data = date;
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

	public void onDateSelect(SelectEvent event) {
		this.getRides();
		setRideRequestGood("");
		setRideRequestBad("");
		aukeratutakoBidaia=null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(""));
	}

	public void fromListener(AjaxBehaviorEvent event) {
		this.getRides();
		aurkituDatakBidaiekin();
		setRideRequestGood("");
		setRideRequestBad("");
		aukeratutakoBidaia=null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("To aldatuta" + from + "-en arabera"));
	}

	public void toListener(AjaxBehaviorEvent event) {
		this.getRides();
		aurkituDatakBidaiekin();
		setRideRequestGood("");
		setRideRequestBad("");
		aukeratutakoBidaia=null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("From Listener aktibatuta"));
	}
	public void onViewChange(DateViewChangeEvent event) {
		  int year = event.getYear();
		    int month = event.getMonth()-1;
		    Calendar cal = Calendar.getInstance();
		    cal.setTime(this.data); 
		    cal.set(Calendar.YEAR, year);
		    cal.set(Calendar.MONTH, month);
		    this.data = cal.getTime();		    
		    aurkituDatakBidaiekin();
	}

	private void aurkituDatakBidaiekin() {
		if (from != null && to != null && data != null) {
			this.egunakBidaiekin = blfacade.getThisMonthDatesWithRides(from, to, data);
		} else {
			this.egunakBidaiekin.clear();
		}
	}
	

	public String getBidaiakDituztenEgunakStringModuan() {
	    if (egunakBidaiekin == null || egunakBidaiekin.isEmpty()) {
	        return "[]";
	    }

	    List<Integer> egunak = new ArrayList<>();
	    Calendar cal = Calendar.getInstance(); 

	    for (Date d : egunakBidaiekin) {
	        cal.setTime(d); 
	        egunak.add(cal.get(Calendar.DAY_OF_MONTH)); 
	    }
	    
	    return egunak.toString(); 
	}
	public String exit() {
		if (user==null){
			return "menu";
		}else {
			if (user instanceof Traveller) {
				return "menuTraveller";
			}else {
				return "menuDriver";
			}
		}
	}
	public void aukeratuBidaia(Ride ride) {
	    this.setAukeratutakoBidaia(ride); 
	    asientoak.clear();
	    for(int i=1;i<=ride.lortuEserlekuKopMin(from, to);i++) {
	    	asientoak.add(i);
	    }
	    aukeratutakoSeats=1;
	   
	    ibilbidea=ride.getIbilbidea(from,to);

	}
	public Ride getAukeratutakoBidaia() {
		return aukeratutakoBidaia;
	}
	public void setAukeratutakoBidaia(Ride aukeratutakoBidaia) {
		this.aukeratutakoBidaia = aukeratutakoBidaia;
	}
	public List<Integer> getAsientoak() {
		return asientoak;
	}
	public void setAsientoak(List<Integer> asientoak) {
		this.asientoak = asientoak;
	}
	public String getIbilbidea() {
		return ibilbidea;
	}
	public void setIbilbidea(String ibilbidea) {
		this.ibilbidea = ibilbidea;
	}

	public int getAukeratutakoSeats() {
		return aukeratutakoSeats;
	}
	public void setAukeratutakoSeats(int aukeratutakoSeats) {
		this.aukeratutakoSeats = aukeratutakoSeats;
	}
	public float getPrezioTotala() {
		prezioTotala=aukeratutakoSeats*aukeratutakoBidaia.lortuBidaiarenPrezioa(from, to);
		return aukeratutakoSeats*aukeratutakoBidaia.lortuBidaiarenPrezioa(from, to);
	}
	public void setPrezioTotala(float prezioTotala) {
		this.prezioTotala = prezioTotala;
	}
	
	public void erreserbatu() {
		System.out.println("Erreserbatu");
		blfacade=FacadeBean.getBusinessLogic();
		if(prezioTotala>user.getWallet()) {
			setRideRequestGood("");
			setRideRequestBad("Ez duzu diru nahikorik erreserba egiteko!!! Zure saldoa:"+user.getWallet()+"€");
		}else {
		RideRequest rideRequest=new RideRequest(new Date(),aukeratutakoBidaia,(Traveller)user,aukeratutakoSeats,from,to);
		RideRequest r=blfacade.erreserbatu(rideRequest);
		if(r!=null) {
			
			System.out.println("ondo erreserba");
			setRideRequestGood("Ondo Sortu da"+aukeratutakoBidaia+"-ren erreserba");
			setRideRequestBad("");
			aukeratutakoBidaia=null;
			
		}else {
			System.out.println("Gaizki erreserba");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage());
			setRideRequestGood("");
			setRideRequestBad("Errore bat egon da erreserba sortzean...");
		}
	
	}
	}
	public String getRideRequestGood() {
		return rideRequestGood;
	}
	public void setRideRequestGood(String rideRequestGood) {
		this.rideRequestGood = rideRequestGood;
	}
	public String getRideRequestBad() {
		return rideRequestBad;
	}
	public void setRideRequestBad(String rideRequestBad) {
		this.rideRequestBad = rideRequestBad;
	}
	public Profile getUser() {
		return user;
	}
	public void setUser(Profile user) {
		this.user = user;
	}

}
