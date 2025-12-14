package domain;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;









@Entity
public class Ride implements Serializable, Comparable<Ride> {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer rideNumber;
    @Column(name = "origin")
	private String from;
    @Column(name = "destination")

	private String to;
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(
		    name = "Ride_Geltokiak", 
		    joinColumns = @JoinColumn(name = "ride_id") 
		)
    @OrderColumn(name = "geltoki_ordena")
	private List<Geltoki> geltokiList;
	private int nPlaces;
	private Date date;
	private float price;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kotxe_matrikula") 
	private Kotxe kotxe;
    @Enumerated(EnumType.STRING)
	private EgoeraRide egoera;

	
	@ManyToOne(fetch=FetchType.EAGER)
	private Driver driver;  

	@OneToMany(targetEntity = RideRequest.class, mappedBy = "ride",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<RideRequest> requests;
	
	private String unekoIbilbide;
	private int zenbatErreserba;
	private float unekoPrezioa;
	

	public Ride(){
		super();
		requests=new ArrayList<RideRequest>();
		 geltokiList=new LinkedList<Geltoki>();
	}
	
	public Ride(String from, String to, Date date, int nPlaces, /*float price*/  List<Float> prezioLista, Driver driver,Kotxe kotxe, List<String>ibilbideList) {
		super();
		this.from = from;
		this.to = to;
		this.nPlaces = nPlaces;
		this.date=date;
		this.driver = driver;
		this.kotxe=kotxe;
		 geltokiList=new LinkedList<Geltoki>();
		requests=new ArrayList<RideRequest>();
		sortuGeltokiGuztiak(ibilbideList, prezioLista,nPlaces);
		this.price= this.lortuBidaiarenPrezioa(from, to);
		egoera=EgoeraRide.MARTXAN;

	}
	

	

	public Ride(String from, String to,  Date date, int nPlaces,/*float price*/  LinkedList<Float> prezioLista, Driver driver,Kotxe kotxe,LinkedList<String>ibilbideList) {
		super();
		this.from = from;
		this.to = to;
		this.nPlaces = nPlaces;
		this.date=date;
		//this.price=price;
		this.driver = driver;
	//	this.kotxe=kotxe;
		 geltokiList=new LinkedList<Geltoki>();
		requests=new ArrayList<RideRequest>();
		sortuGeltokiGuztiak(ibilbideList, prezioLista,nPlaces);
		this.price= this.lortuBidaiarenPrezioa(from, to);
		egoera=EgoeraRide.MARTXAN;

	}
	
	/**
	 * Get the  number of the ride
	 * 
	 * @return the ride number
	 */
	public Integer getRideNumber() {
		return rideNumber;
	}
	public void addRequest(RideRequest t) {
		
		requests.add(t);
		
	}
	public EgoeraRide getEgoera() {
		return egoera;
	}
	
	public List<Geltoki> getGeltokiList() {
		return geltokiList;
	}

	public void setGeltokiList(List<Geltoki> geltokiList) {
		this.geltokiList = geltokiList;
	}

	//public Kotxe getKotxe() {
	//	return kotxe;
	//}

	public void setKotxe(Kotxe kotxe) {
		//this.kotxe = kotxe;
	}

	

	public void setnPlaces(int nPlaces) {
		this.nPlaces = nPlaces;
	}

	/**
	 * Set the ride number to a ride
	 * 
	 * @param ride Number to be set	 */
	
	public void setRideNumber(Integer rideNumber) {
		this.rideNumber = rideNumber;
	}
	public void setEgoera(EgoeraRide egoera) {
		this.egoera=egoera;
	}

	/**
	 * Get the origin  of the ride
	 * 
	 * @return the origin location
	 */

	public String getFrom() {
		return from;
	}

	
	public boolean badaBideSeatekin(String nondik, String nora) {
		List<String>ibilbide= this.geltokiListToString();
		if(ibilbide.contains(nondik)) {
			int nondikIndize= ibilbide.indexOf(nondik);
			int bukaera =seat0DuenLehenIndi(nondikIndize);
			List<String> posibleak=ibilbide.subList(nondikIndize,bukaera+1);
			if(posibleak.contains(nora)) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
		
	}

	private List<String> geltokiListToString0SeatBainaGehiago() {
		List<String>geltokiak=new LinkedList<String>();
		for(Geltoki geltoki:geltokiList) {
			if(geltoki.getEserleku()>0) {
			geltokiak.add(geltoki.getTokiIzen());
			}
		}
		return geltokiak;
	}
	
	//EZ NAGO SEGURO EA JARRI BEHAR  DEN
	public List<String> addDepartingCities(List<String> citiesList){
		List<String>ibilbide=geltokiListToString0SeatBainaGehiago();
		List<String> jatorri= ibilbide.subList(0, ibilbide.size()-1);
		for(String city:jatorri) {
			if(!city.isEmpty() && !citiesList.contains(city)) {
				citiesList.add(city);
			}
		}
		return citiesList;
	}
	private int seat0DuenLehenIndi(int hasiera) {
		
		for(int i=hasiera; i<geltokiList.size();i++) {
			if(geltokiList.get(i).getEserleku()==0) {
				return i;
			}
		}
		return geltokiList.size()-1;
	}
	private List<String> geltokiListToString(){
		List<String>geltokiak=new LinkedList<String>();
		for(Geltoki geltoki:geltokiList) {
			geltokiak.add(geltoki.getTokiIzen());
			
		}
		return geltokiak;
	}
	public List<String>addArrivalCities(String depart, List<String>arrivalCitiesList){
		List<String>ibilbide= geltokiListToString();
		if(ibilbide.contains(depart)) {
			int hasierako= ibilbide.indexOf(depart);
			int bukaerakoa= seat0DuenLehenIndi(hasierako);
			if(bukaerakoa!=hasierako) {
			hasierako=hasierako+1;
			List<String>helmuga=ibilbide.subList(hasierako, bukaerakoa+1);
			for(String arrival: helmuga) {
				if(!arrivalCitiesList.contains(arrival)&&!arrival.isEmpty()) {
					arrivalCitiesList.add(arrival);
				}
			}
			}
		}
			return arrivalCitiesList;
		
		
	}
	
	/**
	 * Set the origin of the ride
	 * 
	 * @param origin to be set
	 */	
	
	public void setFrom(String origin) {
		this.from = origin;
	}

	/**
	 * Get the destination  of the ride
	 * 
	 * @return the destination location
	 */
	
	public int getTGEskakizunTamaina() {
		int kontagailua=0;
		for(RideRequest request :this.requests) {
			if(request.getState().equals(EgoeraRideRequest.TRATATU_GABE)) {
				kontagailua++;
			}
		}
		return kontagailua;
	}
	public String getTo() {
		return to;
	}
	
	public int compareTo(Ride ride) {
		if(this.getTGEskakizunTamaina()>ride.getTGEskakizunTamaina()) {
			return -1;
		}else if(this.getTGEskakizunTamaina()<ride.getTGEskakizunTamaina()) {
			return 1;
		}else {
			return 0;
		}
	
	}

	/**
	 * Set the origin of the ride
	 * 
	 * @param destination to be set
	 */	
	public void setTo(String destination) {
		this.to = destination;
	}

	/**
	 * Get the free places of the ride
	 * 
	 * @return the available places
	 */
	
	/**
	 * Get the date  of the ride
	 * 
	 * @return the ride date 
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * Set the date of the ride
	 * 
	 * @param date to be set
	 */	
	public void setDate(Date date) {
		this.date = date;
	}

	
	public int getnPlaces() {
		return nPlaces;
	}


	/**
	 * Get the driver associated to the ride
	 * 
	 * @return the associated driver
	 */
	public Driver getDriver() {
		return driver;
	}

	/**
	 * Set the driver associated to the ride
	 * 
	 * @param driver to associate to the ride
	 */
	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}
	public List<RideRequest> getEskakizunak(){
		return requests;
	}
	public void removeOfTheList(RideRequest request) {
		requests.remove(request);
	}
	public RideRequest requestOnTheList(RideRequest request) {
		for(RideRequest r:requests) {
			if(r.equals(request)) {
				return r;
			}
		}
		return null;
	}

	public void deuseztatuSeatKopuruBainaHandiagoa(RideRequest r) {
		
		for (RideRequest request : requests) {
			
			if (request.getSeats() >this.lortuEserlekuKopMin(request.getFromRequested(), request.getToRequested()) 
					&& request.getState().equals(EgoeraRideRequest.TRATATU_GABE)&&request.getId()!=r.getId()) {
				request.setState(EgoeraRideRequest.REJECTED);
				request.setWhenDecided(new Date());
				Traveller t = request.getTraveller();	
				t.gehituDirua(request.getPrezioa());
				t.gehituMezuaTransaction(1, request.getPrezioa() , request); 

			}
		}
	}
	public String getIbilbidea() {
		String ibilbidea = "";
		for(Geltoki g: geltokiList) {
			if(ibilbidea.equals("")) {
				ibilbidea=ibilbidea+g.getTokiIzen();
			}else {
			ibilbidea = ibilbidea+"->"+g.getTokiIzen();
			}
		}
		return ibilbidea;
	}
	//BETI HAU DEITZEAN, EXISTITUKO DA IBILBIDEA 
	public String getIbilbidea(String from,String to) {
		int i=0;
		boolean hasi=false;
		boolean amaitu=false;
		String emaitza="";
		while(i<geltokiList.size()&&!amaitu) {
			if(hasi) {
				emaitza=emaitza+"->"+geltokiList.get(i).getTokiIzen();
				if(geltokiList.get(i).getTokiIzen().equals(to)) {
					amaitu=true;
				}
			}else {
				if(geltokiList.get(i).getTokiIzen().equals(from)) {
					hasi=true;
					emaitza=geltokiList.get(i).getTokiIzen();
				}
			}
			i=i+1;
		}
		return emaitza;
		
	}
	public String getInfo() {
		return rideNumber+" ; "+" ; "+getIbilbidea()+" ; "+date;  
	}
	public String toString(){
		return rideNumber+" ; "+" ; "+getIbilbidea()+" ; "+date;  
		}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ride other = (Ride) obj;
		if (!this.getRideNumber().equals(other.getRideNumber()))
			return false;
		return true;
	}


	public void ezabatuRideRequest(RideRequest r) {
		this.getEskakizunak().remove(r);
	}
	
	public void ezabatuAllRideRequest() {
		this.setRequests(new ArrayList<RideRequest>());  
	}

	public void setRequests(List<RideRequest> requests) {
		this.requests = requests;
	}

	public String mezua() {
		return("RIDE:ID :"+ rideNumber+" "+
				
				"Ibilbide osoa:"+ " "+ this.getIbilbidea()+" "+
				"Gidaria"+this.driver.getName()+
				"Noiz:"+" "+date);
	}
	public String getIbilbide() {
		String u="";
		   for(Geltoki a: geltokiList) {
			   if(u.isEmpty()) {
					u=u+a.getTokiIzen();
				}else {
				u=u+"->"+a.getTokiIzen();
				}
		   }
		return u;
	}
   public void gehituSeatGeltokiei(int seats) {
	   for(Geltoki gel: geltokiList) {
		   gel.gehituSeatKop(seats);
	   }
   }
   private boolean geltokiListContains(String from, String to) {
	   LinkedList<String> izenak= new LinkedList<String>();
	   for(Geltoki a: geltokiList) {
		   izenak.add(a.getTokiIzen());
	   }
	   return (izenak.contains(from)&&izenak.contains(to));
   }
   
   public void kenduSeatGeltokiei(int seats, String from, String to) {
	   boolean fromToBarruan=false;
	   boolean daude= geltokiListContains(from,to);
	   if(daude) {
	   for(Geltoki g: geltokiList) {
		   fromToBarruan=frometikToraDagoIGeltokia(g,from,to,fromToBarruan);
		   if(fromToBarruan) {
			   g.kenduSeatKop(seats);
		   }
		   
	   }
	   }
	   
   }
  
   
   public boolean badaBiderenBat(List<String>ibil) {
	   boolean emaitza=false;
	   for(int i=0;i<ibil.size();i++) {
		   String irteera= ibil.get(i);
		   for(int j=i+1; j<ibil.size();j++) {
			   String helmuga= ibil.get(j);
			  emaitza= this.geltokiListContains(irteera, helmuga);
			  if(emaitza) {
				  return true;
			  }
		   }
	   }
	   return false;
   }
   public float lortuBidaiarenPrezioa(String from, String to) {
	   float emaitza=0;
	   boolean hasi=false;
	   boolean daude= geltokiListContains(from,to);
	   if(daude) {
	   for(int i=0;i<geltokiList.size();i++) {
		   
		   if(hasi) {
			   emaitza=emaitza+geltokiList.get(i).getPrezioa();
		   }
		   if(geltokiList.get(i).getTokiIzen().equals(from)) {
			   hasi=true;
		   }
		   if(geltokiList.get(i).getTokiIzen().equals(to)) {
			   hasi=false;
		   }
	   }
	   }
	   return emaitza;
	   
   }
   private void sortuGeltokiGuztiak(List<String>geltokiIzen, List<Float>prezioak,int places){
	   for(int i=0; i<geltokiIzen.size();i++) {
		   int j=i-1;
		   Geltoki geltokia;
		   if(j==-1) {
			   geltokia=new Geltoki(geltokiIzen.get(i),0,places);
		   }else {
			    geltokia=new Geltoki(geltokiIzen.get(i),prezioak.get(j),places);
		   }
		   
		   geltokiList.add(geltokia);
		   
	   }
   }
  
   public int lortuEserlekuKopMin(String from, String to) {
	   boolean hasi=false;
	   int min = Integer.MAX_VALUE;
	   for(Geltoki g: geltokiList) {
		   hasi=frometikToraDagoIGeltokia(g,from,to,hasi);
		   if(hasi) {
			   min=eserlekuKopMinKonprobatu(g,min); 
		   }
	   }
	   return min;
   }
   private int eserlekuKopMinKonprobatu(Geltoki geltoki,int min) {
	   if(min>geltoki.getEserleku()) {
		   min=geltoki.getEserleku();
	   }
	   return min;
   }
   
   private boolean frometikToraDagoIGeltokia(Geltoki g,String from,String to,boolean fromToBarruan) {
	   if(g.getTokiIzen().equals(from)) {
		   return true;
	   }
	   if(g.getTokiIzen().equals(to)) {
		   return false;
	   }
	   return fromToBarruan;
   }
   
   
   
   
   public boolean ezEserlekuakAmaituta() {
	   for(int i=0; i<geltokiList.size()-1;i++) {
		   if(geltokiList.get(i).getEserleku()>0) {
			   return true;
		   }
	   }
	   return false;
   }
   public boolean containsRequestDone() {
	   for(RideRequest request: requests) {
		   if(request.getState().equals(EgoeraRideRequest.DONE)) {
			   return true;
		   }
	   }
	   return false;
	   
   }
   
   public boolean containsRequestNotDone() {
	   for(RideRequest request: requests) {
		   if(request.getState().equals(EgoeraRideRequest.NOT_DONE)) {
			   return true;
		   }
	   }
	   return false;
	   
   }
   public List<Traveller> travellersDone(){
	   List<Traveller>travellerList= new LinkedList<Traveller>();
	   for(RideRequest request: requests) {
		   if(request.getState().equals(EgoeraRideRequest.DONE)&&!travellerList.contains(request.getTraveller())) {
			   travellerList.add(request.getTraveller());
		   }
	   }
	   return travellerList;
   }
   
   public List<Traveller> travellersNotDone(){
	   List<Traveller>travellerList= new LinkedList<Traveller>();
	   for(RideRequest request: requests) {
		   if(request.getState().equals(EgoeraRideRequest.NOT_DONE)&&!travellerList.contains(request.getTraveller())) {
			  
			   travellerList.add(request.getTraveller());
		   }
	   }
	   return travellerList;
   }

   public Kotxe getKotxe() {
	return kotxe;
   }

   public String getUnekoIbilbide() {
	return unekoIbilbide;
   }

   public void setUnekoIbilbide(String unekoIbilbide) {
	this.unekoIbilbide = unekoIbilbide;
   }

   public int getZenbatErreserba() {
	return zenbatErreserba;
   }

   public void setZenbatErreserba(int zenbatErreserba) {
	this.zenbatErreserba = zenbatErreserba;
   }

   public float getUnekoPrezioa() {
	return unekoPrezioa;
   }

   public void setUnekoPrezioa(float unekoPrezioa) {
	this.unekoPrezioa = unekoPrezioa;
   }

   public List<RideRequest> getRequests() {
	return requests;
   }
	
}
