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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class CreateRidesBean implements Serializable {
	@Inject
	private LoginBean loginBean;
	private Profile user;
	private BLFacade blfacade;
	private Date data;
	private String from;
	private String to;
	private float price;
	private int places;
	private String rideGood = "";
	private String rideExists = "";
	private float tartekoPrezioa;

	private List<String> tartekoGeltokiak = new ArrayList<>();
	private List<String> tartekoHerriak = new ArrayList<>();
	private List<Float> tartekoPrezioak = new ArrayList<>();
	private List<String> geltokiBerri = new ArrayList<>();
	private List<Float> prezioBerri = new ArrayList<>();
	private List<String> ibilbideak = new ArrayList<>();
	private List<Float> prezioFinalak = new ArrayList<>();
	private List<String> geltokiFinalak = new ArrayList<>();
	private String tartekoHerria;

	private Kotxe kotxea;
	private List<Kotxe> kotxeList;

	private String ibilbideFinala = "";
	private float prezioFinala = 0;
	private String mota = "direkto";
	private String kotxeMatrikula;

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
		rideGood = "";
	}

	public void clearGood() {
		rideGood = "";
	}

	public void kargatuIzenak(List<String> geltokiak) {
		tartekoGeltokiak.clear();
		ibilbideFinala = "";
		prezioFinala = 0;
		for (int i = 0; i < geltokiak.size() - 1; i = i + 1) {
			System.out.println(geltokiak.get(i) + "tartekoGeltokiak");
			if (ibilbideFinala.equals("")) {
				ibilbideFinala = geltokiak.get(i);
			} else {
				ibilbideFinala = ibilbideFinala + "->" + geltokiak.get(i);
			}
			tartekoGeltokiak.add(geltokiBerri.get(i) + "->" + geltokiBerri.get(i + 1));
			if (i != 0) {
				prezioFinala = prezioFinala + prezioBerri.get(i - 1);
			}
		}
		ibilbideFinala = ibilbideFinala + "->" + geltokiak.get(geltokiak.size() - 1);
		prezioFinala = prezioFinala + prezioBerri.get(geltokiak.size() - 2);

	}

	public void kargatuDena() {
		prezioBerri.clear();
		prezioBerri.addAll(prezioFinalak);
		prezioBerri.add(price);
		geltokiBerri.clear();
		geltokiBerri.add(from);
		geltokiBerri.addAll(geltokiFinalak);
		geltokiBerri.add(to);

	}

	public void fromListener() {
		if (from != null && to != null && price > 0) {

			kargatuDena();
			kargatuIzenak(geltokiBerri);
		}
	}

	public void toListener() {
		if (from != null && to != null && price > 0) {
			if (!from.equals(to) && !geltokiFinalak.contains(from)) {
				kargatuDena();
				kargatuIzenak(geltokiBerri);
			} else {
				rideExists = "From eta to ezin dira berdinak izan eta from ezin da toki berdina egon geltokiListan bi aldiz";
			}
		}
	}

	public void priceListener() {
		if (from != null && to != null && price > 0) {

			kargatuDena();
			kargatuIzenak(geltokiBerri);
		}
	}

	public void onMotaChange() {
		if ("direkto".equals(mota)) {
			this.tartekoHerriak.clear();
			this.tartekoPrezioak.clear();
			this.geltokiFinalak.clear();
			this.prezioFinalak.clear();
			kargatuDena();
			kargatuIzenak(geltokiBerri);
		}
	}

	public void ezabatuAzkena() {
		if (prezioFinalak.size() > 0) {
			prezioFinalak.remove(prezioFinalak.size() - 1);
		}
		if (geltokiFinalak.size() > 0) {
			geltokiFinalak.remove(geltokiFinalak.size() - 1);
		}
		if (from != null && to != null && price > 0) {
			kargatuDena();
			kargatuIzenak(geltokiBerri);
		}

	}

	public void gehituTartekoa() {

		if (tartekoHerria != null && !tartekoHerria.trim().isEmpty() && tartekoPrezioa > 0
				&& !geltokiBerri.contains(tartekoHerria.trim())) {
			geltokiFinalak.add(tartekoHerria.trim());
			prezioFinalak.add(tartekoPrezioa);
			kargatuDena();
			kargatuIzenak(geltokiBerri);
			System.out.println(geltokiBerri);
			tartekoHerria = "";
			tartekoPrezioa = 0;
		} else {
			FacesContext.getCurrentInstance().addMessage("nireForm:tHerria",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errorea",
							"Gogoratu prezioa eta herria jartzeaz, herria ezin da errepikatu ibilbidean"));
		}
	}

	public void createRides() {
		blfacade = FacadeBean.getBusinessLogic();

		kotxea = blfacade.getKotxeByMatrikula(kotxeMatrikula);
		System.out.println(" sartuta create rides bidaia sortuta");
		if (data == null || kotxea == null) {
			rideExists = "Aukeratu kotxe bat";
			rideGood = "";
		} else {
			if (from.equals(to)) {
				rideExists = "From eta to ezin dira berdinak izan";

			} else {
				if (kotxea.getTokiKopurua() < places) {
					rideExists = "Kotxeak dituenak baina eserleku gehiago aukeratu dituzu";
					rideGood = "";
					return;
				}
				try {
					kargatuDena();
					Set<String> unikoak = new HashSet<>();
					boolean badiraErrepikatuak = false;

					for (String hiria : geltokiBerri) {
						if (!unikoak.add(hiria.trim().toLowerCase())) {
							badiraErrepikatuak = true;
							break;
						}
					}

					if (badiraErrepikatuak) {
						rideExists = "Errorea: Herriren bat errepikatuta dago ibilbidean (From, To edo tartekoetan).";
						rideGood = "";
						return;
					}
					System.out.println("bidaia sortzen");
					Ride ride = blfacade.createRide(from, to, data, places, prezioBerri, user.getUser(), kotxea,
							geltokiBerri);
					System.out.println(ride + "bidaia sortuta");
					if (ride == null) {
						rideExists = "Errorea bidaia sortzean";
						rideGood = "";
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Errorea biaia sortzean"));
					} else {
						rideExists = "";
						rideGood = "Ondo sortu da ridea";

						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ondo sortu da ride-a"));
					}
				} catch (RideMustBeLaterThanTodayException e) {
					rideExists = "Datak gaur baina berandoago izan behar du";
					rideGood = "";
				} catch (RideAlreadyExistException e) {
					rideExists = "Bidaia jadanik existitzen da";
					rideGood = "";
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ride already exists"));
				}
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

	public float getTartekoPrezioa() {
		return tartekoPrezioa;
	}

	public void setTartekoPrezioa(float tartekoPrezioa) {
		this.tartekoPrezioa = tartekoPrezioa;
	}

	public List<String> getTartekoHerriak() {
		return tartekoHerriak;
	}

	public void setTartekoHerriak(List<String> tartekoHerriak) {
		this.tartekoHerriak = tartekoHerriak;
	}

	public List<Float> getTartekoPrezioak() {
		return tartekoPrezioak;
	}

	public void setTartekoPrezioak(List<Float> tartekoPrezioak) {
		this.tartekoPrezioak = tartekoPrezioak;
	}

	public String getTartekoHerria() {
		return tartekoHerria;
	}

	public void setTartekoHerria(String tartekoHerria) {
		this.tartekoHerria = tartekoHerria;
	}

	public List<String> getTartekoGeltokiak() {
		return tartekoGeltokiak;
	}

	public void setTartekoGeltokiak(List<String> tartekoGeltokiak) {
		this.tartekoGeltokiak = tartekoGeltokiak;
	}

	public List<String> getIbilbideak() {
		return ibilbideak;
	}

	public void setIbilbideak(List<String> ibilbideak) {
		this.ibilbideak = ibilbideak;
	}

	public List<Float> getPrezioFinalak() {
		return prezioFinalak;
	}

	public void setPrezioFinalak(List<Float> prezioFinalak) {
		this.prezioFinalak = prezioFinalak;
	}

	public List<String> getGeltokiFinalak() {
		return geltokiFinalak;
	}

	public void setGeltokiFinalak(List<String> geltokiFinalak) {
		this.geltokiFinalak = geltokiFinalak;
	}

	public List<String> getGeltokiBerri() {
		return geltokiBerri;
	}

	public void setGeltokiBerri(List<String> geltokiBerri) {
		this.geltokiBerri = geltokiBerri;
	}

	public List<Float> getPrezioBerri() {
		return prezioBerri;
	}

	public void setPrezioBerri(List<Float> prezioBerri) {
		this.prezioBerri = prezioBerri;
	}

	public String getIbilbideFinala() {
		return ibilbideFinala;
	}

	public void setIbilbideFinala(String ibilbideFinala) {
		this.ibilbideFinala = ibilbideFinala;
	}

	public float getPrezioFinala() {
		return prezioFinala;
	}

	public void setPrezioFinala(float prezioFinala) {
		this.prezioFinala = prezioFinala;
	}

	public String getMota() {
		return mota;
	}

	public void setMota(String mota) {
		this.mota = mota;
	}

	public Kotxe getKotxea() {
		return kotxea;
	}

	public void setKotxea(Kotxe kotxea) {
		this.kotxea = kotxea;
	}

	public List<Kotxe> getKotxeList() {
		blfacade = FacadeBean.getBusinessLogic();
		kotxeList = blfacade.getKotxeGuztiak((Driver) user);
		return kotxeList;
	}

	public void setKotxeList(List<Kotxe> kotxeList) {
		this.kotxeList = kotxeList;
	}

	public String getKotxeMatrikula() {
		if (kotxeMatrikula == null && kotxea != null) {
			kotxeMatrikula = kotxea.getMatrikula();
		}
		return kotxeMatrikula;
	}

	public void setKotxeMatrikula(String kotxeMatrikula) {
		this.kotxeMatrikula = kotxeMatrikula;
	}
}
