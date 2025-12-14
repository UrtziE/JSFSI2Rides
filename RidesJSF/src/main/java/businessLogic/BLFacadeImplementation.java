package businessLogic;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;




import configuration.ConfigXML;

import domain.Ride;
import domain.RideContainer;
import domain.RideRequest;
import domain.Traveller;
import domain.Admin;
import domain.Alerta;
import domain.Driver;
import domain.Erreklamazioa;
import dataAccess.HibernateDataAccess;
import domain.Kotxe;
import domain.Mezua;
import domain.Profile;
import exceptions.RideMustBeLaterThanTodayException;

import exceptions.RideAlreadyExistException;

/**
 * It implements the business logic as a web service.
 */

public class BLFacadeImplementation implements BLFacade {
	HibernateDataAccess dbManager;
	Logger logger = Logger.getLogger(getClass().getName());

	public BLFacadeImplementation() {
		logger.info("Creating BLFacadeImplementation instance");

		dbManager = new HibernateDataAccess();

	}

	public BLFacadeImplementation(HibernateDataAccess da) {

		System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");

		dbManager = da;
	}

	/**
	 * {@inheritDoc}
	 */

	public List<String> getDepartCities() {

		List<String> departLocations = dbManager.getDepartCities();


		return departLocations;

	}


	

	/**
	 * {@inheritDoc}
	 */

	public List<String> getDestinationCities(String from) {

		List<String> targetCities = dbManager.getArrivalCities(from);


		return targetCities;
	}

	/**
	 * {@inheritDoc}
	 */

	public Ride createRide(String from, String to, Date date, int nPlaces,/* float price ,*/List<Float> price,
			String driverUser, Kotxe kotxe, List<String> ibilbide)
			throws RideMustBeLaterThanTodayException, RideAlreadyExistException {

		Ride ride = dbManager.createRide(from, to, date, nPlaces, price, driverUser, kotxe, ibilbide);
		return ride;
	};


	public Profile register(Profile profile, String type) {
		Profile p = dbManager.register(profile, type);
		return p;
	}


	public Profile login(String user, String password) {
		Profile p = dbManager.login(user, password);
		return p;
	}


	public void sartuDirua(float dirua, Traveller traveller) {

		dbManager.sartuDirua(dirua, traveller);

	}


	public void ateraDirua(float dirua, Driver driver) {

		dbManager.kenduDirua(dirua, driver);

	}


	public boolean gehituDirua(float dirua, Profile p) {
		return dbManager.sartuDirua(dirua, p);
	}


	public boolean kenduDirua(float dirua, Profile p) {
		return dbManager.kenduDirua(dirua, p);
	}
	/*
	 * @WebMethod public RideRequest erreserbatu(Date time, Ride ride, Traveller
	 * traveller,int seats,String from, String to) {
	 * 
	 * ErreserbaEskaera ee = new ErreserbaEskaera(time, ride, traveller, seats,
	 * from, to); dbManager.open(); RideRequest request=dbManager.erreserbatu(ee);
	 * dbManager.close(); return request;
	 * 
	 * }
	 */


	public RideRequest erreserbatu(RideRequest r) {
		System.out.println("Erreserbatu");
		RideRequest request = dbManager.erreserbatu(r);
		return request;

	}


	public Ride getRideFromRequest(RideRequest erreserba) {
		Ride ride = dbManager.getRideFromRequest(erreserba);
		return ride;

	}


	public boolean onartuEdoDeuseztatu(RideRequest request, boolean onartuta) {
		boolean emaitza=dbManager.onartuEdoDeuseztatuErreserba(request, onartuta);
		return emaitza;
	}

	/**
	 * {@inheritDoc}
	 */

	public List<Ride> getRides(String from, String to, Date date) {
		List<Ride> rides = dbManager.getRides(from, to, date);
		return rides;
	}


	public List<Ride> getRidesOfDriver(Driver driver) {
		List<Ride> rides = dbManager.getRidesOfDriver(driver);
		return rides;
	}


	public List<RideContainer> getAllRidesOfDriver(Driver driver) {
		List<RideContainer> rides = dbManager.getAllRidesOfDriver(driver);
		return rides;
	}


	public List<RideRequest> getRidesRequestsOfRide(Ride ride) {
		List<RideRequest> request = dbManager.getRidesRequestsOfRide(ride);
		return request;

	}

	
	public List<RideRequest> getRidesRequestsOfTraveller(Traveller traveller) {
		
		List<RideRequest> request = dbManager.getRidesRequestsOfTraveller(traveller);
		
		return request;
	}

	
	public float getMoney(Profile p) {
		
		float money = dbManager.getMoney(p);
		
		return money;

	}

	/**
	 * {@inheritDoc}
	 */
	
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		List<Date> dates = dbManager.getThisMonthDatesWithRides(from, to, date);
		return dates;
	}

	
	

	

	
	@Override
	public List<Kotxe> getKotxeGuztiak(Driver driver) {
		List<Kotxe> kotxeList = dbManager.getKotxeGuztiak(driver);
		return kotxeList;
	}

	
	public List<RideContainer> getEginRidesOfDriver(Driver d) {
		List<RideContainer> rideList = dbManager.getEginRidesOfDriver(d);
		return rideList;
	}

	
	@Override
	public boolean createCar(String marka, String modelo, String matrikula, int tokiKop, Driver driver) {
		boolean ondo = dbManager.createCar(marka, modelo, matrikula, tokiKop, driver);
		return ondo;
	}

	/*
	 * public void gehituMezuaRide( int i,RideRequest erreserba, int norentzat) {
	 * dbManager.open(); dbManager.gehituMezuaRide( i, erreserba,norentzat);
	 * dbManager.close(); } public void gehituMezua( int i,float kantitatea
	 * ,RideRequest erreserba,Profile profile ) { dbManager.open();
	 * dbManager.gehituMezua( i,kantitatea, erreserba, profile); dbManager.close();
	 * }
	 * 
	 * public void gehituMezua( int i,float kantitatea,Profile p) {
	 * dbManager.open(); dbManager.gehituMezua( i, kantitatea, p);
	 * dbManager.close(); }
	 */
	
	public List<Mezua> getMezuak(Profile p) {
		List<Mezua> mList = dbManager.getMezuak(p);
		return mList;
	}

	
	public List<Mezua> getErreklamazioMezuak(Profile p) {
		List<Mezua> mList = dbManager.getErreklamazioMezuak(p);
		return mList;
	}

	
	public void kantzelatu(Ride r) {
		dbManager.kantzelatu(r);
	}

	
	public void egindaEdoEzEgina(RideRequest request, boolean onartuta) {
		dbManager.egindaEdoEzEgina(request, onartuta);
	}

	
	public void baloratu(int balorazioa, Profile nori, RideRequest r,String mezua,Profile nork) {
		dbManager.baloratu(balorazioa, nori, r, mezua,nork);
	}

	
	public float getBalorazioMedia(Profile driver) {
		float media = dbManager.getBalorazioMedia(driver);
		return media;

	}

	
	public boolean sortuAlerta(Traveller t, String from, String to, Date when) {
		return dbManager.sortuAlerta(t, from, to, when);
	}

	
	public Alerta getAlerta(Traveller traveller, String from, String to, Date when) {
		Alerta alerta = dbManager.getAlerta(traveller, from, to, when);
		return alerta;
	}

	
	public List<Erreklamazioa> getErreklamazioak(Profile p) {
		List<Erreklamazioa> erreklamazioak = dbManager.getErreklamazioak(p);
		return erreklamazioak;
	}

	
	public void gehituErreklamazioa(Profile p, Profile nori, String deskripzioa, float prezioa, RideRequest r) {
		Erreklamazioa erreklam = new Erreklamazioa(p, nori, deskripzioa, prezioa, r);
		dbManager.gehituErreklamazioa(erreklam);

	}

	
	public Erreklamazioa takeNewErreklamazioa(Admin a) {
		Erreklamazioa erreklamazioa = dbManager.takeNewErreklamazioa(a);
		return erreklamazioa;
	}

	
	public List<Traveller> getTravellersOfRideDone(Ride ride) {
		List<Traveller> r = dbManager.getTravellersOfRideDone(ride);
		return r;
	}

	
	public List<Traveller> getTravellersOfRideNotDone(Ride ride) {
		List<Traveller> r = dbManager.getTravellersOfRideNotDone(ride);
		return r;
	}

	
	public void erreklamazioaProzesatu(Erreklamazioa erreklamazioa, float kantitatea, boolean onartuta) {
		dbManager.erreklamazioaProzesatu(erreklamazioa, kantitatea, onartuta);
	}

	
	public void mezuaIrakurrita(Mezua alerta) {
		dbManager.mezuaIrakurrita(alerta);
	}

	
	public List<Mezua> ikusitakoAlerta(Traveller traveller) {
		List<Mezua> mezuak = dbManager.ikusitakoAlerta(traveller);
		return mezuak;
	}

	
	public List<Mezua> getIkusiGabeAlerta(Traveller traveller) {
		List<Mezua> mezuak = dbManager.getIkusiGabeAlerta(traveller);
		return mezuak;
	}

	
	public List<Alerta> kargatuTravellerAlertak(Traveller traveller) {
		List<Alerta> alertak = dbManager.kargatuTravellerAlertak(traveller);
		return alertak;
	}

	
	public void deuseztatuAlerta(Alerta alerta) {
		dbManager.deuseztatuAlerta(alerta);
	}

	
	public boolean isBaloratua(RideRequest request, boolean gidari) {
		boolean baloratuta = dbManager.isBaloratua(request, gidari);
		return baloratuta;
	}

	
	public boolean isErreklamatua(RideRequest request, boolean gidari) {
		boolean baloratuta = dbManager.isErreklamatua(request, gidari);
		return baloratuta;
	}

	
	public RideRequest lortuLehenRequestBidaiakoa(Ride r, Traveller t) {
		RideRequest baloratuta = dbManager.lortuLehenRequestBidaiakoa(r, t);
		return baloratuta;
	}

	
	public int lortuZenbatEserlekuGeratu(Ride ride, RideRequest request) {
		int seats = dbManager.lortuZenbatEserlekuGeratu(ride, request);
		return seats;
	}

	
	public List<Erreklamazioa> lortuErreklamazioakProzesuan(Profile a) {
		List<Erreklamazioa> erreklam = dbManager.lortuErreklamazioakProzesuan(a);
		return erreklam;
	}
	
	
	
	public Driver getDriver(String izena) {
		/*Driver d = dbManager.getDriver(izena);*/
		return null;
	}
	public List<Mezua> getAlertaMezuak(Alerta alerta){
		return dbManager.getAlertaMezuak(alerta);
	}
	public List<Ride> getRides(String from) {
		List<Ride> rides = dbManager.getRides(from);
		return rides;
	}
	public List<String> getDepartCitiesProba() {

		List<String> departLocations = dbManager.getDepartCitiesProba();


		return departLocations;

	}
	public Profile getProfileByUser(String user) {
	return dbManager.getProfileByUser(user);
	}
	public Kotxe getKotxeByMatrikula(String matrikula) {
	return dbManager.getKotxeByMatrikula(matrikula);
}
}
