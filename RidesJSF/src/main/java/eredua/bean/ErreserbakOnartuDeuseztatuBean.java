package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


import org.primefaces.event.DateViewChangeEvent;
import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import domain.Driver;
import domain.EgoeraRideRequest;
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

@Named("erreserbakOnartuDeuseztatu")
@ViewScoped
public class ErreserbakOnartuDeuseztatuBean implements Serializable {
	private Profile user;
	private String rideRequestGood="";
	private String rideRequestBad="";
	@Inject
	private LoginBean loginBean;
	private List<Ride> rides;
	private List<RideRequest> rideRequests=new ArrayList<RideRequest>();
	private static BLFacade blfacade =new BLFacadeImplementation();
	private Ride aukeratutakoBidaia;

	private String ibilbidea;


	public ErreserbakOnartuDeuseztatuBean() {



	}
	@PostConstruct
	public void init() {
		this.user = loginBean.getOraingoUser();
	}

	public List<Ride> getRides() {

		List<Ride>ridesLagun = blfacade.getRidesOfDriver((Driver)user);
		for(Ride ride:ridesLagun) {
			ride.setUnekoIbilbide(ride.getIbilbidea());
			ride.setZenbatErreserba(ride.getTGEskakizunTamaina());
		}
		 Collections.sort(ridesLagun);
		 rides=ridesLagun;
		return rides;
	}







	public void aukeratuBidaia(Ride ride) {
		this.setAukeratutakoBidaia(ride); 
		ibilbidea=ride.getIbilbidea();

	}
	public Ride getAukeratutakoBidaia() {
		return aukeratutakoBidaia;
	}
	public void setAukeratutakoBidaia(Ride aukeratutakoBidaia) {
		this.aukeratutakoBidaia = aukeratutakoBidaia;
	}

	public String getIbilbidea() {
		return ibilbidea;
	}
	public void setIbilbidea(String ibilbidea) {
		this.ibilbidea = ibilbidea;
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
	public List<RideRequest> getRideRequests() {
		 this.rideRequests = new ArrayList<>();
		if(aukeratutakoBidaia!=null) {
			System.out.println("aukeratutako bidaia:"+ aukeratutakoBidaia);
			blfacade=FacadeBean.getBusinessLogic();
			List<RideRequest> requests=blfacade.getRidesRequestsOfRide(aukeratutakoBidaia);
			for(RideRequest rq:requests) {
				System.out.print(rq+"rideRequest");
				if(rq.getState().equals(EgoeraRideRequest.TRATATU_GABE)) {
					System.out.println("Tratatu gabe");
					rq.setIbilbidea(aukeratutakoBidaia.getIbilbidea(rq.getFromRequested(), rq.getToRequested()));
					rq.setPrezio(rq.getSeats()*aukeratutakoBidaia.lortuBidaiarenPrezioa(rq.getFromRequested(), rq.getToRequested()));
					rideRequests.add(rq);
				}
			}
		}
		return rideRequests;
	}
	public void setRideRequests(List<RideRequest> rideRequests) {
		this.rideRequests = rideRequests;
	}
	public void onartuDeuseztatu(RideRequest request, boolean onartu) {
		blfacade=FacadeBean.getBusinessLogic();
		boolean emaitza=blfacade.onartuEdoDeuseztatu(request, onartu);
		if(emaitza) {
			rideRequestGood="Ondo egin da eragiketa!";
			rideRequestBad="";
			this.getRides();
		}else {
			rideRequestGood="";
			rideRequestBad="Errore bat gertatu da eragiketan";
		}
	}

}
