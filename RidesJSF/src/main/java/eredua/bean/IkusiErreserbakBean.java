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

@Named("ikusiErreserbak")
@SessionScoped
public class IkusiErreserbakBean implements Serializable {
	private Profile user;
	private String rideRequestGood = "";
	private String rideRequestBad = "";
	@Inject
	private LoginBean loginBean;
	private List<RideRequest> rideRequests = new ArrayList<RideRequest>();
	private static BLFacade blfacade = new BLFacadeImplementation();
	private RideRequest aukeratutakoErreserba;

	private EgoeraRide rideEgoera;
	private String rideDriver;
	private Date rideDate;
	private Kotxe rideKotxe;
	private String rideIbilbidea;
	private int rideId;
	private String mota;
	private int balorazioa;
	private String textua;
	private String errorLabel = "";
	private float rideRating;

	private List<Ride> rides = new ArrayList<Ride>();
	private Ride aukeratutakoBidaia;
	private List<Traveller> travellerList = new ArrayList<Traveller>();
	private Traveller aukeratutakoBidaiaria;
	private String bidaiariUser;

	public IkusiErreserbakBean() {

	}

	@PostConstruct
	public void init() {
		this.user = loginBean.getOraingoUser();
		if (user instanceof Traveller) {
			mota = "TRATATU_GABE";

		} else {
			mota = "MARTXAN";
		}
	}

	public void aukeratuErreserba(RideRequest request) {
		this.setAukeratutakoErreserba(request);
		blfacade = FacadeBean.getBusinessLogic();
		Ride ride = blfacade.getRideFromRequest(request);
		rideDriver = ride.getDriver().getName();
		setRideRating(blfacade.getBalorazioMedia(ride.getDriver()));
		rideDate = ride.getDate();
		rideKotxe = ride.getKotxe();
		rideIbilbidea = ride.getIbilbidea();
		rideId = ride.getRideNumber();
		rideEgoera = ride.getEgoera();

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
		blfacade = FacadeBean.getBusinessLogic();
		List<RideRequest> laguntzaile = blfacade.getRidesRequestsOfTraveller((Traveller) user);

		if (mota.equals(EgoeraRideRequest.TRATATU_GABE.toString())) {
			for (RideRequest r : laguntzaile) {
				if (r.getState().equals(EgoeraRideRequest.TRATATU_GABE)
						&& !r.getRide().getEgoera().equals(EgoeraRide.PASATUA)) {
					Ride ride = blfacade.getRideFromRequest(r);
					r.setUnekoState(r.getState().toString());
					r.setIbilbidea(ride.getIbilbidea(r.getFromRequested(), r.getToRequested()));
					r.setPrezio(r.getSeats() * ride.lortuBidaiarenPrezioa(r.getFromRequested(), r.getToRequested()));
					rideRequests.add(r);

				}
			}
		} else {
			if (mota.equals(EgoeraRideRequest.ACCEPTED.toString())) {
				for (RideRequest r : laguntzaile) {

					if (r.getState().equals(EgoeraRideRequest.ACCEPTED)
							&& !r.getRide().getEgoera().equals(EgoeraRide.PASATUA)) {
						Ride ride = blfacade.getRideFromRequest(r);
						r.setUnekoState(r.getState().toString());
						r.setIbilbidea(ride.getIbilbidea(r.getFromRequested(), r.getToRequested()));
						r.setPrezio(ride.lortuBidaiarenPrezioa(r.getFromRequested(), r.getToRequested()));

						rideRequests.add(r);
					}
				}
			} else {
				if (mota.equals(EgoeraRideRequest.REJECTED.toString())) {
					for (RideRequest r : laguntzaile) {
						if (r.getState().equals(EgoeraRideRequest.REJECTED)) {
							Ride ride = blfacade.getRideFromRequest(r);
							r.setUnekoState(r.getState().toString());
							r.setPrezio(ride.lortuBidaiarenPrezioa(r.getFromRequested(), r.getToRequested()));
							r.setIbilbidea(ride.getIbilbidea(r.getFromRequested(), r.getToRequested()));

							rideRequests.add(r);
						}
					}
				} else {
					if (mota.equals(EgoeraRideRequest.DONE.toString())) {
						for (RideRequest r : laguntzaile) {
							if (r.getState().equals(EgoeraRideRequest.DONE)) {
								Ride ride = blfacade.getRideFromRequest(r);
								r.setUnekoState(r.getState().toString());
								r.setIbilbidea(ride.getIbilbidea(r.getFromRequested(), r.getToRequested()));
								r.setPrezio(r.getSeats()
										* ride.lortuBidaiarenPrezioa(r.getFromRequested(), r.getToRequested()));
								rideRequests.add(r);

							}
						}
					} else {
						if (mota.equals(EgoeraRideRequest.NOT_DONE.toString())) {
							for (RideRequest r : laguntzaile) {
								if (r.getState().equals(EgoeraRideRequest.NOT_DONE)) {
									Ride ride = blfacade.getRideFromRequest(r);
									r.setUnekoState(r.getState().toString());
									r.setIbilbidea(ride.getIbilbidea(r.getFromRequested(), r.getToRequested()));
									r.setPrezio(r.getSeats()
											* ride.lortuBidaiarenPrezioa(r.getFromRequested(), r.getToRequested()));
									rideRequests.add(r);

								}
							}

						} else {

							for (RideRequest r : laguntzaile) {
								if (r.getState().equals(EgoeraRideRequest.ACCEPTED)
										&& r.getRide().getEgoera().equals(EgoeraRide.PASATUA)) {
									Ride ride = blfacade.getRideFromRequest(r);
									r.setUnekoState(r.getState().toString());
									r.setIbilbidea(ride.getIbilbidea(r.getFromRequested(), r.getToRequested()));
									r.setPrezio(r.getSeats()
											* ride.lortuBidaiarenPrezioa(r.getFromRequested(), r.getToRequested()));
									rideRequests.add(r);

								}
							}
						}

					}
				}
			}
			Collections.sort(rideRequests, Collections.reverseOrder());
		}
		return rideRequests;
	}

	public void setRideRequests(List<RideRequest> rideRequests) {
		this.rideRequests = rideRequests;
	}

	public String getMota() {
		return mota;
	}

	public void setMota(String mota) {

		this.mota = mota;
	}

	public String getRideIbilbidea() {
		return rideIbilbidea;
	}

	public void setRideIbilbidea(String rideIbilbidea) {
		this.rideIbilbidea = rideIbilbidea;
	}

	public int getRideId() {
		return rideId;
	}

	public void setRideId(int rideId) {
		this.rideId = rideId;
	}

	public Kotxe getRideKotxe() {
		return rideKotxe;
	}

	public void setRideKotxe(Kotxe rideKotxe) {
		this.rideKotxe = rideKotxe;
	}

	public Date getRideDate() {
		return rideDate;
	}

	public void setRideDate(Date rideDate) {
		this.rideDate = rideDate;
	}

	public String getRideDriver() {
		return rideDriver;
	}

	public void setRideDriver(String rideDriver) {
		this.rideDriver = rideDriver;
	}

	public RideRequest getAukeratutakoErreserba() {
		return aukeratutakoErreserba;
	}

	public void setAukeratutakoErreserba(RideRequest aukeratutakoRequest) {
		this.aukeratutakoErreserba = aukeratutakoRequest;
	}

	public EgoeraRide getRideEgoera() {
		return rideEgoera;
	}

	public void setRideEgoera(EgoeraRide rideEgoera) {
		this.rideEgoera = rideEgoera;
	}

	public void markatuEginda() {
		blfacade = FacadeBean.getBusinessLogic();
		blfacade.egindaEdoEzEgina(aukeratutakoErreserba, true);
		this.aukeratutakoErreserba = null;
	}

	public void markatuEzEginda() {
		blfacade = FacadeBean.getBusinessLogic();
		blfacade.egindaEdoEzEgina(aukeratutakoErreserba, false);
		this.aukeratutakoErreserba = null;
	}

	public String joanBaloratzeraTraveller() {
		blfacade = FacadeBean.getBusinessLogic();

		boolean isBaloratuta = blfacade.isBaloratua(aukeratutakoErreserba, true);
		if (!isBaloratuta) {
			return "baloratu";
		} else {
			setErrorLabel("Jadanik baloratuta duzu gidari hau bidaia honetan");
			return "error";
		}

	}

	public String joanBaloratzeraDriver() {
		aukeratutakoBidaiaria = (Traveller) blfacade.getProfileByUser(bidaiariUser);
		blfacade = FacadeBean.getBusinessLogic();
		boolean isBaloratuta = blfacade.isBaloratua(aukeratutakoBidaia.getEskakizunak().get(0), false);
		if (!isBaloratuta) {
			return "baloratu";
		} else {
			setErrorLabel("Jadanik baloratuta duzu bidaiari hau bidaia honetan");
			return "error";
		}

	}

	public String gordeBalorazioa() {
		blfacade = FacadeBean.getBusinessLogic();
		if (user instanceof Traveller) {
			blfacade.baloratu(this.balorazioa, aukeratutakoErreserba.getRide().getDriver(), aukeratutakoErreserba,
					textua, user);
			this.balorazioa = 0;
			this.textua = null;

			return "ikusiErreserbak";

		} else {
			System.out.println("baloratu bidaiaria");
			blfacade.baloratu(this.balorazioa, aukeratutakoBidaiaria, aukeratutakoBidaia.getRequests().get(0), textua, user);
			this.balorazioa = 0;
			this.textua = null;

			return "ikusiBidaiak";
		}
	}

	public int getBalorazioa() {
		return balorazioa;
	}

	public void setBalorazioa(int balorazioa) {
		this.balorazioa = balorazioa;
	}

	public String getTextua() {
		return textua;
	}

	public void setTextua(String textua) {
		this.textua = textua;
	}

	public String getErrorLabel() {
		return errorLabel;
	}

	public void setErrorLabel(String errorLabel) {
		this.errorLabel = errorLabel;
	}

	public void onChange() {
		this.aukeratutakoErreserba = null;
		this.setAukeratutakoBidaia(null);
	}

	public List<Ride> getRides() {
		blfacade = FacadeBean.getBusinessLogic();
		List<Ride> laguntzaile = blfacade.getRidesOfDriver((Driver) user);
		rides.clear();
		for (Ride ride : rides) {
			ride.setUnekoIbilbide(ride.getIbilbidea());
			ride.setUnekoPrezioa(ride.lortuBidaiarenPrezioa(ride.getFrom(), ride.getTo()));
		}

		if (mota.equals(EgoeraRide.MARTXAN.toString())) {
			for (Ride r : laguntzaile) {
				if (r.getEgoera().equals(EgoeraRide.MARTXAN)) {
					r.setUnekoIbilbide(r.getIbilbidea());
					r.setUnekoPrezioa(r.lortuBidaiarenPrezioa(r.getFrom(), r.getTo()));
					rides.add(r);
				}
			}
		} else {
			if (mota.equals(EgoeraRide.TOKIRIK_GABE.toString())) {
				for (Ride r : laguntzaile) {
					if (r.getEgoera().equals(EgoeraRide.TOKIRIK_GABE)) {
						r.setUnekoIbilbide(r.getIbilbidea());
						r.setUnekoPrezioa(r.lortuBidaiarenPrezioa(r.getFrom(), r.getTo()));
						rides.add(r);
					}
				}
			} else {
				if (mota.equals(EgoeraRide.KANTZELATUA.toString())) {
					for (Ride r : laguntzaile) {
						if (r.getEgoera().equals(EgoeraRide.KANTZELATUA)) {
							r.setUnekoIbilbide(r.getIbilbidea());
							r.setUnekoPrezioa(r.lortuBidaiarenPrezioa(r.getFrom(), r.getTo()));
							rides.add(r);
						}
					}
				} else {
					if (mota.equals(EgoeraRide.PASATUA.toString())) {
						for (Ride r : laguntzaile) {
							System.out.println("Printeatzen pasatutakoak" +r);
							if (r.getEgoera().equals(EgoeraRide.PASATUA)) {
								r.setUnekoIbilbide(r.getIbilbidea());
								r.setUnekoPrezioa(r.lortuBidaiarenPrezioa(r.getFrom(), r.getTo()));
								rides.add(r);
							}
						}
					} else {
						if (mota.equals(EgoeraRide.DONE.toString())) {
							for (Ride r : laguntzaile) {
								
								for(RideRequest request: r.getEskakizunak()) {
									if(request.getState().equals(EgoeraRideRequest.DONE)) {
										r.setUnekoIbilbide(r.getIbilbidea());
										r.setUnekoPrezioa(r.lortuBidaiarenPrezioa(r.getFrom(), r.getTo()));
										rides.add(r);
										break;
									}
								}
									
							
								}
							

						} else {

							if (mota.equals(EgoeraRide.NOT_DONE.toString())) {
								for (Ride r : laguntzaile) {
									
									for(RideRequest request: r.getEskakizunak()) {
										if(request.getState().equals(EgoeraRideRequest.NOT_DONE)) {
											r.setUnekoIbilbide(r.getIbilbidea());
											r.setUnekoPrezioa(r.lortuBidaiarenPrezioa(r.getFrom(), r.getTo()));
											rides.add(r);
											break;
										}
									}
									
									}
							}
						}

					}
				}
			}
		}
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}

	public Ride getAukeratutakoBidaia() {
		return aukeratutakoBidaia;
	}

	public void setAukeratutakoBidaia(Ride aukeratutakoBidaia) {
		this.aukeratutakoBidaia = aukeratutakoBidaia;
	}

	public void aukeratuBidaia(Ride ride) {
		this.aukeratutakoBidaia = ride;
	}

	public List<Traveller> getTravellerList() {
		blfacade = FacadeBean.getBusinessLogic();
		travellerList = blfacade.getTravellersOfRideDone(aukeratutakoBidaia);
		return travellerList;
	}

	public void setTravellerList(List<Traveller> travellerList) {
		this.travellerList = travellerList;
	}

	public Traveller getAukeratutakoBidaiaria() {
		return aukeratutakoBidaiaria;
	}

	public void setAukeratutakoBidaiaria(Traveller aukeratutakoBidaiaria) {
		this.aukeratutakoBidaiaria = aukeratutakoBidaiaria;
	}

	public String getBidaiariUser() {
		return bidaiariUser;
	}

	public void setBidaiariUser(String bidaiariUser) {
		blfacade = FacadeBean.getBusinessLogic();
		this.bidaiariUser = bidaiariUser;
	}
	public void kantzelatu(Ride ride) {
        try {
            blfacade = FacadeBean.getBusinessLogic();
           
             blfacade.kantzelatu(ride); 
            
            this.rides.clear(); 
            errorLabel="Bidaia ondo kantzelatu da.";
           
        } catch (Exception e) {
            e.printStackTrace();
            setErrorLabel("Errorea bidaia kantzelatzean.");
        }
    }

	public float getRideRating() {
		return rideRating;
	}

	public void setRideRating(float rideRating) {
		this.rideRating = rideRating;
	}

}
