package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.primefaces.event.DateViewChangeEvent;
import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import domain.Driver;
import domain.EgoeraRide;
import domain.EgoeraRideRequest;
import domain.Kotxe;
import domain.Mezua;
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

@Named("ikusiMezuak")
@ViewScoped
public class IkusiMezuakBean implements Serializable {
	private Profile user;
	private String mota = "kajero";
	@Inject
	private LoginBean loginBean;
	private List<Mezua> mezuak = new ArrayList<Mezua>();
	private String aukeratutakoMezua = "Hona emen zure diru mugimenduak";
	private List<RideRequest> rideRequests = new ArrayList<RideRequest>();
	private static BLFacade blfacade = new BLFacadeImplementation();

	public IkusiMezuakBean() {

	}

	@PostConstruct
	public void init() {
		this.user = loginBean.getOraingoUser();
	}

	public List<Mezua> getMezuak() {
		mezuak.clear();
		blfacade = FacadeBean.getBusinessLogic();
		List<Mezua> mezuLaguntzaile = blfacade.getMezuak(user);
		if (mota.equals("kajero")) {
			for (Mezua mezu : mezuLaguntzaile) {
				if (mezu.getType() == 1 && (mezu.getMezutype() == 3 || mezu.getMezutype() == 6)) {
					mezuak.add(mezu);
				}
			}
		} else {
			if (mota.equals("erreserbak")) {
				for (Mezua mezu : mezuLaguntzaile) {
					if (mezu.getType() == 1 && (mezu.getMezutype() == 0 || mezu.getMezutype() == 1)) {
						mezuak.add(mezu);
					}
				}
			} else {
				for (Mezua mezu : mezuLaguntzaile) {
					if (mezu.getMezutype() != 0 && mezu.getMezutype() != 1 &&mezu.getMezutype() != 3 && mezu.getMezutype() != 6) {
						mezuak.add(mezu);
					}
				}

			}
		}
		Collections.sort(mezuak,Collections.reverseOrder());
		return mezuak;
	}

	public Profile getUser() {
		return user;
	}

	public void setUser(Profile user) {
		this.user = user;
	}


	public void setMezuak(List<Mezua> mezuak) {
		this.mezuak = mezuak;
	}

	public String getAukeratutakoMezua() {
		return aukeratutakoMezua;
	}

	public void setAukeratutakoMezua(String aukeratutakoMezua) {
		this.aukeratutakoMezua = aukeratutakoMezua;
	}

	public String getMota() {
		return mota;
	}

	public void setMota(String mota) {
		this.mota = mota;
	}

	public List<RideRequest> getRideRequests() {
		return rideRequests;
	}

	public void setRideRequests(List<RideRequest> rideRequests) {
		this.rideRequests = rideRequests;
	}

}
