package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;



import java.util.Set;


@Entity
public class Driver extends Profile implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@OneToMany(targetEntity = Ride.class, mappedBy = "driver",fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	private List<Ride> rides=new ArrayList<Ride>();

	@OneToMany(targetEntity = Kotxe.class, mappedBy = "driver",fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	private List<Kotxe> kotxeList=new ArrayList<Kotxe>();
	
	

	public Driver(String email, String name, String surname, String user, String password, String telefono) {
		super(email, name, surname, user, password, telefono);
	}
	public Driver(String user, String email) {
		super(user,email);
	}

	public Driver() {
		super();
	}
	
	
	
	
	public String toString(){
		return(super.toString()+rides);
	}
	
	
	/**
	 * This method creates a bet with a question, minimum bet ammount and percentual profit
	 * 
	 * @param question to be added to the event
	 * @param betMinimum of that question
	 * @return Bet
	 */
	public Ride addRide(String from, String to, Date date, int nPlaces, /*float price*/ List<Float>price,Kotxe kotxe,List<String>ibilbide)  {
        Ride ride=new Ride(nPlaces, from,to,date,nPlaces,price, this,kotxe,ibilbide);
        rides.add(ride);
        return ride;
	}
	public Kotxe addKotxe(String marka,String modelo, int tokiKopurua, String matrikula) {
		Kotxe kotxe=new Kotxe(marka,modelo,tokiKopurua,matrikula,this);
		kotxeList.add(kotxe);
		return kotxe;
	}
	public String getIzenaAbizenaUser() {
		return(this.getName()+" "+this.getSurname()+" user: "+this.getUser());
	}

	/**
	 * This method checks if the ride already exists for that driver
	 * 
	 * @param from the origin location 
	 * @param to the destination location 
	 * @param date the date of the ride 
	 * @return true if the ride exists and false in other case
	 */
	public boolean doesRideExists(List<String>ibil, Date date)  {	
		for (Ride r:rides)
			if ( r.badaBiderenBat(ibil) && (java.util.Objects.equals(r.getDate(),date)) )
			 return true;
		
		return false;
	}
		
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Driver other = (Driver) obj;
		if (this.getUser() != other.getUser())
			return false;
		return true;
	}
	public List<Ride> getRides(){
		return rides;
	}

	public Ride removeRide(int num) {
		int i=0;
		Ride ride=null;
		boolean borratuta=false;
		while(!borratuta&&i<rides.size()) {
			if(rides.get(i).getRideNumber()==num) {
				ride=rides.remove(i);
				borratuta=true;
			}
			i++;
		}
		return ride;
	}
	public List<Kotxe> getKotxeGuztiak(){
		return kotxeList;
	}
	
	public List<Ride>getEgitenRidesOfDriver(){
		List<Ride>emaitza=new ArrayList<Ride>();
		for(Ride ride:rides) {
			if(ride.getEgoera().equals(EgoeraRide.MARTXAN)||ride.getEgoera().equals(EgoeraRide.TOKIRIK_GABE)) {
				emaitza.add(ride);
			}
		}
		return emaitza;
	}
	
	
	
	
}
