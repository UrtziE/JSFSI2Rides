package dataAccess;

import java.io.File;
import java.io.IOException;
import java.net.NoRouteToHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.swing.JFrame;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Admin;
import domain.Alerta;
import domain.Driver;
import domain.EgoeraErreklamazioa;
import domain.EgoeraRide;
import domain.EgoeraRideRequest;
import domain.Erreklamazioa;

import domain.Kotxe;
import domain.Mezua;
import domain.Profile;
import domain.Ride;
import domain.RideContainer;
import domain.RideRequest;
import domain.Traveller;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

/**
 * It implements the data access to the objectDb database
 */
public class DataAccess {
	private EntityManager db;
	private EntityManagerFactory emf;

	ConfigXML c = ConfigXML.getInstance();

	public DataAccess() {
		if (c.isDatabaseInitialized()) {
			String fileName = c.getDbFilename();
			Path fileToDelete =Paths.get(fileName);
			try {
			Files.delete(fileToDelete);
			Path fileToDeleteTemp = Paths.get(fileName + "$");
				try {
					Files.delete(fileToDeleteTemp);
				} catch (IOException e) {
					System.out.println("Ezin izan da ezabatu temp fitxategia: " + e.getMessage());
				}
			} catch (IOException e) {
			
			 System.out.println("Ezin izan da ezabatu fitxategia: " + e.getMessage());
            }
			
				System.out.println("File deleted");
			
		}
		open();
		if (c.isDatabaseInitialized())
			initializeDB();

		System.out.println("DataAccess created => isDatabaseLocal: " + c.isDatabaseLocal() + " isDatabaseInitialized: "
				+ c.isDatabaseInitialized());

		close();

	}

	public DataAccess(EntityManager db) {
		this.db = db;
	}

	/**
	 * This is the data access method that initializes the database with some events
	 * and questions. This method is invoked by the business logic (constructor of
	 * BLFacadeImplementation) when the option "initialize" is declared in the tag
	 * dataBaseOpenMode of resources/config.xml file
	 */
	public void initializeDB() {

		db.getTransaction().begin();

		/*
		 * try {
		 * 
		 * Calendar today = Calendar.getInstance();
		 * 
		 * int month = today.get(Calendar.MONTH); int year = today.get(Calendar.YEAR);
		 * if (month == 12) { month = 1; year += 1; } // Create travellers Traveller
		 * traveller1 = new Traveller("urtzi@gmail.com", "Urtzi", "Etxegarai", "urtzi1",
		 * "ajdlkfj", "613054264"); db.persist(traveller1);
		 * 
		 * // Create drivers Driver driver1 = new Driver("driver1@gmail.com",
		 * "Aitor Fernandez"); Driver driver2 = new Driver("driver2@gmail.com",
		 * "Ane Gaztañaga"); Driver driver3 = new Driver("driver3@gmail.com",
		 * "Test driver");
		 * 
		 * // Create rides driver1.addRide("Donostia", "Bilbo", UtilDate.newDate(year,
		 * month, 15), 4, 7); driver1.addRide("Donostia", "Gazteiz",
		 * UtilDate.newDate(year, month, 6), 4, 8); driver1.addRide("Bilbo", "Donostia",
		 * UtilDate.newDate(year, month, 25), 4, 4);
		 * 
		 * driver1.addRide("Donostia", "Iruña", UtilDate.newDate(year, month, 7), 4, 8);
		 * 
		 * driver2.addRide("Donostia", "Bilbo", UtilDate.newDate(year, month, 15), 3,
		 * 3); driver2.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 25),
		 * 2, 5); driver2.addRide("Eibar", "Gasteiz", UtilDate.newDate(year, month, 6),
		 * 2, 5);
		 * 
		 * driver3.addRide("Bilbo", "Donostia", UtilDate.newDate(year, month, 14), 1,
		 * 3);
		 * 
		 * db.persist(driver1); db.persist(driver2); db.persist(driver3);
		 * 
		 * db.getTransaction().commit(); System.out.println("Db initialized"); } catch
		 * (Exception e) { e.printStackTrace(); }
		 */
	}

	/**
	 * This method returns all the cities where rides depart
	 * 
	 * @return collection of cities
	 */

	public List<String> getDepartCities() {
		TypedQuery<Ride> query = db.createQuery("SELECT DISTINCT r FROM Ride r  WHERE r.egoera=?1 ORDER BY r.from",
				Ride.class);
		query.setParameter(1, EgoeraRide.MARTXAN);

		List<Ride> rides = query.getResultList();
		List<String> cities = new ArrayList<String>();
		for (Ride ride : rides) {
			cities = ride.addDepartingCities(cities);
		}
		return cities;

	}

	/**
	 * This method returns all the arrival destinations, from all rides that depart
	 * from a given city
	 * 
	 * @param from the depart location of a ride
	 * @return all the arrival destinations
	 */

	public List<String> getArrivalCities(String from) {
		TypedQuery<Ride> query = db.createQuery("SELECT DISTINCT r FROM Ride r WHERE  r.egoera=?2 ORDER BY r.to",
				Ride.class);
		query.setParameter(2, EgoeraRide.MARTXAN);

		List<Ride> rides = query.getResultList();
		List<String> cities = new ArrayList<String>();
		for (Ride ride : rides) {
			cities = ride.addArrivalCities(from, cities);
		}
		return cities;

	}

	/**
	 * This method creates a ride for a driver
	 * 
	 * @param from        the origin location of a ride
	 * @param to          the destination location of a ride
	 * @param date        the date of the ride
	 * @param nPlaces     available seats
	 * @param driverEmail to which ride is added
	 * 
	 * @return the created ride, or null, or an exception
	 * @throws RideMustBeLaterThanTodayException if the ride date is before today
	 * @throws RideAlreadyExistException         if the same ride already exists for
	 *                                           the driver
	 */
	public Ride createRide(String from, String to, Date date, int nPlaces, /* float price */ List<Float> price,
			String driverUser, Kotxe kotxe, List<String> ibilbide)
			throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
		System.out.println(
				">> DataAccess: createRide=> from= " + from + " to= " + to + " driver=" + driverUser + " date " + date);
		try {

			
			  if (new Date().compareTo(date) > 0) { throw new
			 RideMustBeLaterThanTodayException(
			  ResourceBundle.getBundle("Etiquetas").getString(
			  "CreateRideGUI.ErrorRideMustBeLaterThanToday")); }
			 

			db.getTransaction().begin();

			Driver driver = db.find(Driver.class, driverUser);
			if (driver.doesRideExists(ibilbide, date)) {
				db.getTransaction().commit();
				throw new RideAlreadyExistException(
						ResourceBundle.getBundle("Etiquetas").getString("DataAccess.RideAlreadyExist"));
			}
			Ride ride = driver.addRide(from, to, date, nPlaces, price, kotxe, ibilbide);

			// next instruction can be obviated
			//db.persist(driver);
			konprobatuAlertak(ride);
			db.getTransaction().commit();

			return ride;
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			db.getTransaction().commit();
			return null;
		}

	}

	/**
	 * Metodo honek bueltatzen ditu bi tokietatik pasatzen diren eta eselekuak libre   dituzten bidaiak
	 * 
	  * @param from bidaian lehendabiziko dagoen geltokia 
	  * @param to bidaian “from" eta gero dagoen geltokia 
	 * @param date the date of the ride
	 * @return collection of rides
	 */
	
	public List<Ride> getRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getRides=> from= " + from + " to= " + to + " date " + date);

		List<Ride> res = new ArrayList<>();
		TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r WHERE r.date=?3 AND r.egoera=?4", Ride.class);

		query.setParameter(3, date);
		query.setParameter(4, EgoeraRide.MARTXAN);

		List<Ride> rides = query.getResultList();
		for (Ride ride : rides) {

			if (ride.badaBideSeatekin(from, to)) {
				res.add(ride);
			}
		}
		return res;
	}

	

	

	public List<RideContainer> getRidesOfDriver(Driver driver) {
		List<RideContainer> res = new ArrayList<>();
		TypedQuery<Ride> query = db
				.createQuery("SELECT r FROM Ride r WHERE r.egoera=?1 AND r.driver=?2 AND r.nPlaces>?3 ", Ride.class);
		query.setParameter(1, EgoeraRide.MARTXAN);
		query.setParameter(2, driver);
		query.setParameter(3, 0);

		List<Ride> rides = query.getResultList();
		for (Ride ride : rides) {
			res.add(new RideContainer(ride));
		}
		return res;
	}
	
	/**
	 * Metodo honek gidari batek martxan dituen bidaiak itzultzen ditu nahiz eta
	 * tokirik gabe izatea. RideContainer itzultzen dugu web zerbitzuagatik
	 * 
	 * @param d gidaria
	 * @return gidariak martxan dituen bidaien lista
	 */
	public List<RideContainer> getEginRidesOfDriver(Driver d) {
		Driver driver = db.find(Driver.class, d.getUser());
		List<Ride> rideList = driver.getEgitenRidesOfDriver();
		List<RideContainer> emaitza = new ArrayList<RideContainer>();
		for (Ride ride : rideList) {
			emaitza.add(new RideContainer(ride));
		}
		return emaitza;
	}

	public List<RideContainer> getAllRidesOfDriver(Driver driver) {
		Driver d = db.find(Driver.class, driver.getUser());
		List<Ride>rides=d.getRides();
		List<RideContainer> rideContainerList= new LinkedList<RideContainer>();
		for(Ride ride: rides) {
			rideContainerList.add(new RideContainer(ride));
		}
		return rideContainerList;
	}

	public List<RideRequest> getRidesRequestsOfRide(Ride ride) {

		Ride r = db.find(Ride.class, ride.getRideNumber());

		return r.getEskakizunak();
	}

	public List<Traveller> getTravellersOfRideDone(Ride ride) {

		Ride r = db.find(Ride.class, ride.getRideNumber());
		
		return r.travellersDone();
	}
	public List<Traveller> getTravellersOfRideNotDone(Ride ride) {

       Ride r = db.find(Ride.class, ride.getRideNumber());
		
		return r.travellersNotDone();
	}


	public List<RideRequest> getRidesRequestsOfTraveller(Traveller traveller) {

		Traveller t = db.find(Traveller.class, traveller.getUser());

		return t.getRequests();
	}

	public float getMoney(Profile profile) {
		Profile p = db.find(Profile.class, profile.getUser());
		return p.getWallet();
	}

	public List<Kotxe> getKotxeGuztiak(Driver driver) {
		Driver d = db.find(Driver.class, driver.getUser());
		return d.getKotxeGuztiak();
	}

	public boolean createCar(String marka, String modelo, String matrikula, int tokiKop, Driver driver) {

		if (db.find(Kotxe.class, matrikula) == null) {
			db.getTransaction().begin();
			Driver d = db.find(Driver.class, driver.getUser());
			d.addKotxe(marka, modelo, tokiKop, matrikula);
			db.getTransaction().commit();
			return true;
		} else {

			return false;
		}
	}

	/**
	 * This method retrieves from the database the dates a month for which there are
	 * events
	 * 
	 * @param from the origin location of a ride
	 * @param to   the destination location of a ride
	 * @param date of the month for which days with rides want to be retrieved
	 * @return collection of rides
	 */
	public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
		System.out.println(">> DataAccess: getEventsMonth");
		List<Date> res = new ArrayList<>();

		Date firstDayMonthDate = UtilDate.firstDayMonth(date);
		Date lastDayMonthDate = UtilDate.lastDayMonth(date);

		TypedQuery<Ride> query = db.createQuery(
				"SELECT DISTINCT r FROM Ride r WHERE  r.date BETWEEN ?3 and ?4 AND r.egoera=?5", Ride.class);

		// query.setParameter(1, from);
		// query.setParameter(2, to);
		query.setParameter(3, firstDayMonthDate);
		query.setParameter(4, lastDayMonthDate);
		query.setParameter(5, EgoeraRide.MARTXAN);

		List<Ride> rides = query.getResultList();
		for (Ride r : rides) {

			if (r.badaBideSeatekin(from, to)) {
				res.add(r.getDate());
			}
		}
		return res;
	}

	
	
	public Profile register(Profile p,String type) {

		Profile u = db.find(Profile.class, p.getUser());
		Profile user;
		if (u != null) {
			return null;
		} else {
			
			user=createTravellerOrDriver(p,type);
			
			db.getTransaction().begin();
			db.persist(user);
			db.getTransaction().commit();
			System.out.println("Ondo gorde da");
			return user;
		}

	}
	private Profile createTravellerOrDriver(Profile p,String type) {
		String email=p.getEmail();
		String name=p.getName();
		String surname= p.getSurname();
		String username=p.getUser();
		String password=p.getPassword();
		String telf=p.getTelf();
		
		if (type.equals("Traveller")) {
			return new Traveller(email, name, surname, username, password, telf);

		} else {
			return new Driver(email, name, surname, username, password, telf);
		}
		
	}

	public Profile login(String user, String password) {
		Profile p = db.find(Profile.class, user);

		if (p == null) {

			return null;

		} else {
			if (p.getPassword().equals(password)) {
				return p;
			} else {

				return null;
			}
		}
	}

	public void sartuDirua(float dirua, Profile user) {
		db.getTransaction().begin();
		Profile u = db.find(Profile.class, user.getUser());
		u.gehituDirua(dirua);
		u.gehituMezuaTransaction(3, dirua);
		db.getTransaction().commit();

		//System.out.println(user + " has been updated");

	}

	public void kenduDirua(float dirua, Profile user) {

		db.getTransaction().begin();

		Profile u = db.find(Profile.class, user.getUser());

		u.kenduDirua(dirua);

		u.gehituMezuaTransaction(6, dirua);

		db.getTransaction().commit();

		//System.out.println(user + " has been updated");

	}
     //kz2
	public void kantzelatu(Ride r) {
		db.getTransaction().begin();

		Ride ride = db.find(Ride.class, r.getRideNumber());
		ride.setEgoera(EgoeraRide.KANTZELATUA);

		for (RideRequest request : ride.getEskakizunak()) {
			if (!request.getState().equals(EgoeraRideRequest.REJECTED)) {
				Traveller t = request.getTraveller();
				float prezioa = request.getPrezioa();
				t.gehituDirua(prezioa);
				request.setState(EgoeraRideRequest.REJECTED);
				t.gehituMezuaTransaction(5, prezioa, request);
				
			}
		}
		
		db.getTransaction().commit();

	}
  //kz 3
	public void egindaEdoEzEgina(RideRequest request, boolean onartuta) {

		db.getTransaction().begin();

		RideRequest r = db.find(RideRequest.class, request.getId());
		Ride ride = r.getRide();
		Driver d = ride.getDriver();
		Traveller t = r.getTraveller();
		
		t.doneNotDoneErreserbaBerdinak(onartuta, ride,d);
	
		
		db.getTransaction().commit();
	}

	public void onartuEdoDeuseztatuErreserba(RideRequest request, boolean onartuta) {

		db.getTransaction().begin();

		RideRequest r = db.find(RideRequest.class, request.getId());
		Ride ride = r.getRide();

		if (onartuta) {
			onartuErreserba(ride,r);
		} else {
			deuseztatuErreserba(r);
		}
		r.setWhenDecided(new Date());
		db.getTransaction().commit();
	}
	


	public void erreklamazioaProzesatu(Erreklamazioa erreklamazioa, float kantitatea, boolean onartuta) {
		db.getTransaction().begin();
		Erreklamazioa e = db.find(Erreklamazioa.class, erreklamazioa.getId());
		e.setEgoera(EgoeraErreklamazioa.BUKATUA);
		Profile nork = db.find(Profile.class, e.getNork().getUser());
		Profile nori = db.find(Profile.class, e.getNori().getUser());
		Profile admin = e.getAdmin();
		RideRequest request = e.getErreserba();
		Ride r = request.getRide();
		if (onartuta) {
			erreklamazioaOnartu(nork, nori, admin, kantitatea, erreklamazioa);
		} else {
			erreklamazioaDeuseztatu(nork, admin, kantitatea, erreklamazioa);
		}
		e.setWhenDecided(new Date());
		db.getTransaction().commit();
	}
	
	
	
	public RideRequest erreserbatu(RideRequest request) {
		Ride ride = request.getRide();
		Ride rd = db.find(Ride.class, ride.getRideNumber());
		db.getTransaction().begin();
		
		
		if(!eserlekuKopEgokia(rd,request)) {
			db.getTransaction().commit();
			return null;
		}
		
		RideRequest rq=createRideRequest(rd,request);
		rd.addRequest(request);	
		db.getTransaction().commit();
			
		return rq;

	}
	
	private boolean eserlekuKopEgokia(Ride rd,RideRequest request) {
		String requestFrom = request.getFromRequested();
		String requestTo = request.getToRequested();
		int seats = request.getSeats();
		
		return (rd.lortuEserlekuKopMin(requestFrom, requestTo)>=seats); 
	}
	private RideRequest createRideRequest(Ride ride,RideRequest request) {
		Traveller traveller = request.getTraveller();
		String requestFrom = request.getFromRequested();
		String requestTo = request.getToRequested();
		int seats = request.getSeats();
		Date time = request.getWhenRequested();
		Traveller t = db.find(Traveller.class, traveller.getUser());
		t.kenduDirua(ride.lortuBidaiarenPrezioa(requestFrom, requestTo) * seats);
		request = t.addRequest(time, ride, seats, requestFrom, requestTo);
		
		t.gehituMezuaTransaction(0, ride.lortuBidaiarenPrezioa(requestFrom, requestTo) * seats, request);
	
		return request;
	}
	


	public Ride getRideFromRequest(RideRequest erreserba) {
		RideRequest request = db.find(RideRequest.class, erreserba.getId());
		return request.getRide();

	}


	public List<Erreklamazioa> getErreklamazioak(Profile p) {
		db.getTransaction().begin();
		Profile profile = db.find(Profile.class, p.getUser());
		List<Erreklamazioa> erreklamazioak = profile.getErreklamazioak();
		db.getTransaction().commit();
		if (erreklamazioak != null)
			return erreklamazioak;
		else
			return new ArrayList<Erreklamazioa>();
	}
	//Profile p, Profile nori, String deskripzioa, float prezioa, RideRequest r
   
	public void gehituErreklamazioa( Erreklamazioa erreklam) {
		Profile nork= erreklam.getNork();
		RideRequest r= erreklam.getErreserba();
		db.getTransaction().begin();
		Profile profile = db.find(Profile.class, nork.getUser());
		RideRequest request = db.find(RideRequest.class, r.getId());
		if (profile instanceof Traveller) {
			request.setErreklamatuaDriver(true);
		} else {
			request.setErreklamatuaTraveller(true);
		}
		profile.gehituErreklamazioa(erreklam.getNori(), erreklam.getDeskripzioa(), erreklam.getPrezioa(), request);
		db.getTransaction().commit();
	}

	public Erreklamazioa takeNewErreklamazioa(Admin a) {

		TypedQuery<Erreklamazioa> query = db.createQuery("SELECT e FROM Erreklamazioa e WHERE e.egoera=?1 ",
				Erreklamazioa.class);

	

		query.setParameter(1, EgoeraErreklamazioa.ESLEITU_GABE);

		List<Erreklamazioa> erreklamazioak = query.getResultList();
		if (!erreklamazioak.isEmpty()) {
			db.getTransaction().begin();
			Erreklamazioa erreklamazioa = erreklamazioak.get(0);
			Admin admin = db.find(Admin.class, a.getUser());

			erreklamazioa.setEgoera(EgoeraErreklamazioa.PROZESUAN);


			erreklamazioa.setAdmin(admin);
			admin.addErreklamazioa(erreklamazioa);
			admin.gehituMezuaTransaction(8, 0, erreklamazioa.getErreserba());
			db.getTransaction().commit();

			return erreklamazioa;
		} else
			return null;
	}

	
	/**
	 * Metodo honek erabiltzaile batek dituen mezuen lista itzultzen du
	 * 
	 * @param p erabiltzaile horren profila
	 * @return mezuen lista
	 */
	public List<Mezua> getMezuak(Profile p) {
		return lortuMezuak(p,1);
	}

	public List<Mezua> getErreklamazioMezuak(Profile p) {
		return lortuMezuak(p,3);
	}
	private List<Mezua> lortuMezuak(Profile p,int type){
		db.getTransaction().begin();
		Profile profile = db.find(Profile.class, p.getUser());
		List<Mezua> mList1 = profile.getMezuList();
		db.getTransaction().commit();
		List<Mezua> mList = new LinkedList<Mezua>();
		for (Mezua mezu : mList1) {
			if (mezu.getType() == type) {
				mList.add(mezu);
			}
		}
		return mList;
	}

	public void baloratu(int balorazioa, Profile nori, RideRequest r,String mezua,Profile nork) {
		db.getTransaction().begin();
		Profile p = db.find(Profile.class, nori.getUser());
		RideRequest request = db.find(RideRequest.class, r.getId());
		
		if (p instanceof Driver) {
			request.setBaloratuaDriver(true);

		} else {
			request.setBaloratuaTraveller(true);
		}

	//	p.addBalorazioa(nork, mezua, balorazioa);
		
		db.getTransaction().commit();
		

	}

	public float getBalorazioMedia(Driver d) {
		Driver driver = db.find(Driver.class, d.getUser());
		return driver.kalkulatuBalorazioMedia();
	}

	public void sortuAlerta(Traveller t, String from, String to, Date when) {
		db.getTransaction().begin();
		Traveller traveller = db.find(Traveller.class, t.getUser());
		traveller.addAlerta(from, to, when);
		db.getTransaction().commit();
	}

	public Alerta getAlerta(Traveller traveller,String from, String to, Date when) {
		TypedQuery<Alerta> query = db.createQuery("SELECT a FROM Alerta a WHERE  a.from=?2 AND a.to=?3 AND a.when=?4 AND a.traveller=?5 AND a.ezabatuta=?6",
				Alerta.class);
		query.setParameter(2, from);
		query.setParameter(3, to);
		query.setParameter(4, when);
		query.setParameter(5, traveller);
		query.setParameter(6, false);

		List<Alerta> alertak = query.getResultList();
		if (alertak.size() == 0) {
			return null;
		} else {
			return alertak.get(0);
		}

	}

	// aldatu
	public void mezuaIrakurrita(Mezua alerta) {
		db.getTransaction().begin();
		Mezua a = db.find(Mezua.class, alerta.getId());
		a.setIrakurritaTrue();
		db.getTransaction().commit();
	}
	
	public List<Mezua> getAlerta(Traveller traveller, boolean irakurrita) {
		TypedQuery<Mezua> query = db
				.createQuery("SELECT m FROM Mezua m WHERE  m.type=?2 AND m.irakurrita=?3 AND m.p=?4", Mezua.class);
		query.setParameter(2, 2);
		query.setParameter(3, irakurrita);
		query.setParameter(4, traveller);
		List<Mezua> alertaMezuak = query.getResultList();
		return alertaMezuak;
	}

	public List<Mezua> ikusitakoAlerta(Traveller traveller) {
		return getAlerta(traveller, true);
	}

	public List<Mezua> getIkusiGabeAlerta(Traveller traveller) {
		return getAlerta(traveller, false);
	}
	

	

	public List<Alerta> kargatuTravellerAlertak(Traveller traveller) {
		TypedQuery<Alerta> query = db.createQuery("SELECT a FROM Alerta a WHERE  a.traveller=?2 AND a.ezabatuta=?3",
				Alerta.class);
		query.setParameter(2, traveller);
		query.setParameter(3, false);

		List<Alerta> alertak = query.getResultList();
		return alertak;
	}

	public void deuseztatuAlerta(Alerta alerta) {
		db.getTransaction().begin();
		Alerta a = db.find(Alerta.class, alerta.getId());
		a.setEzabatuta(true);
		db.getTransaction().commit();
	}

	public boolean isBaloratua(RideRequest request, boolean gidari) {
		RideRequest r = db.find(RideRequest.class, request.getId());
		if (gidari) {
			return r.isBaloratuaTraveller();
		} else {
			return r.isBaloratuaDriver();

		}
	}

	public boolean isErreklamatua(RideRequest request, boolean gidari) {
		RideRequest r = db.find(RideRequest.class, request.getId());
		if (gidari) {
			return r.isErreklamatuaTraveller();
		} else {
			return r.isErreklamatuaDriver();

		}
	}
	public List<Erreklamazioa> lortuErreklamazioakProzesuan(Profile a) {
		//Profile admin = db.find(Profile.class, a.getUser());
		TypedQuery<Erreklamazioa> query = db.createQuery("SELECT e FROM Erreklamazioa e WHERE  e.admin=?2 AND e.egoera=?3",
				Erreklamazioa.class);
		query.setParameter(2, a);

		query.setParameter(3, EgoeraErreklamazioa.PROZESUAN);

		List<Erreklamazioa> erreklam = query.getResultList();
		return erreklam;
	}
	   public RideRequest lortuLehenRequestBidaiakoa(Ride r,Traveller t) {
		   Traveller traveller= db.find(Traveller.class, t.getUser());
		   return traveller.lortuLehenRequestBidaiakoa(r);
	   }
	   public int lortuZenbatEserlekuGeratu(Ride ride, RideRequest request) {
		   Ride r= db.find(Ride.class, ride.getRideNumber());
		   int i= r.lortuEserlekuKopMin(request.getFromRequested(), request.getToRequested());
		  
		   return i;
	   }

	// aldatu
	private void konprobatuAlertak(Ride ride) {
		// Aldatu
		TypedQuery<Alerta> query = db.createQuery("SELECT a FROM Alerta a WHERE  a.when=?4", Alerta.class);
		query.setParameter(4, ride.getDate());

		List<Alerta> alertak = query.getResultList();

		for (Alerta alerta : alertak) {
			if (ride.badaBideSeatekin(alerta.getFrom(), alerta.getTo()) && !alerta.isEzabatuta()) {
				Traveller traveller = db.find(Traveller.class, alerta.getTraveller().getUser());
				traveller.addAlertaMezu(ride, alerta);
			}
		}
	}

	
	private void konprobatuBidaienEgunak() {
		TypedQuery<Ride> query = db.createQuery("SELECT r FROM Ride r", Ride.class);
		List<Ride> rideP = query.getResultList();
		db.getTransaction().begin();
		Date gaur = new Date();
		for (Ride ride : rideP) {
			konprobatuBidaia(gaur, ride);
		}
		db.getTransaction().commit();
	}
	
	private void konprobatuBidaia(Date gaur, Ride ride) {
		if ((ride.getEgoera().equals(EgoeraRide.MARTXAN) || ride.getEgoera().equals(EgoeraRide.TOKIRIK_GABE))
			&& ride.getDate().before(new Date())) {
			bidaiaItxi(ride);
		} else if ((gaur.getTime() - ride.getDate().getTime()) / (1000 * 60 * 60 * 24) > 3) {
			bidaiEskaerakProzesatu(ride);
		}
	}
	
	private void bidaiaItxi(Ride ride) {
		Ride ri = db.find(Ride.class, ride.getRideNumber());
		ri.setEgoera(EgoeraRide.PASATUA);
	}
	
	private void bidaiEskaerakProzesatu(Ride ride) {
		if (ride.getEgoera().equals(EgoeraRide.PASATUA)) {
			for (RideRequest rr : ride.getEskakizunak()) {
				pasatutakoBidaiEskaerakProzesatu(rr);
			}
		}
	}
	
	private void pasatutakoBidaiEskaerakProzesatu(RideRequest rr) {
		if(rr.getState().equals(EgoeraRideRequest.ACCEPTED)) {
			rr.setState(EgoeraRideRequest.DONE);
		}
		rr.setBaloratuaDriver(true);
		rr.setBaloratuaTraveller(true);
		rr.setErreklamatuaDriver(true);
		rr.setErreklamatuaTraveller(true);
	}
	
	private void erreklamazioaOnartu(Profile nork, Profile nori, Profile admin, float kantitatea, Erreklamazioa e) {
		RideRequest request = e.getErreserba();
		Ride r = request.getRide();
		
		nork.gehituDirua(kantitatea);
		nori.kenduDirua(kantitatea);
		nork.addErreklamazioMezu(0, r, e);
		nork.gehituMezuaTransaction(9, kantitatea, request);
		nori.gehituMezuaTransaction(11, kantitatea, request);
		admin.addErreklamazioMezu(2, r, e);
	}

	private void erreklamazioaDeuseztatu(Profile nork, Profile admin, float kantitatea, Erreklamazioa e) {
		RideRequest request = e.getErreserba();
		Ride r = request.getRide();
		
		nork.addErreklamazioMezu(1, r, e);
		admin.addErreklamazioMezu(3, r, e);
	}
	
	private void onartuErreserba(Ride ride, RideRequest r) {
		ride.kenduSeatGeltokiei(r.getSeats(), r.getFromRequested(), r.getToRequested());
		r.setState(EgoeraRideRequest.ACCEPTED);
		ride.deuseztatuSeatKopuruBainaHandiagoa(r);
		if (ride.getnPlaces() == 0) {
			ride.setEgoera(EgoeraRide.TOKIRIK_GABE);
		}
	}
	private void deuseztatuErreserba(RideRequest r) {
		Traveller t = r.getTraveller();
		t.gehituDirua(r.getPrezioa());
		r.setState(EgoeraRideRequest.REJECTED);
		t.gehituMezuaTransaction(1, r.getPrezioa(), r); // Dirua itzuli
	}

	public void open() {

		String fileName = c.getDbFilename();
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<>();
			properties.put("javax.persistence.jdbc.user", c.getUser());
			properties.put("javax.persistence.jdbc.password", c.getPassword());

			emf = Persistence.createEntityManagerFactory(
					"objectdb://" + c.getDatabaseNode() + ":" + c.getDatabasePort() + "/" + fileName, properties);
			db = emf.createEntityManager();
		}
		konprobatuBidaienEgunak();
		System.out.println("DataAccess opened => isDatabaseLocal: " + c.isDatabaseLocal());

	}

	public void close() {
		db.close();
		System.out.println("DataAcess closed");
	}
	
	public Driver getDriver(String izena) {
		return db.find(Driver.class, izena);
	}
	

}
