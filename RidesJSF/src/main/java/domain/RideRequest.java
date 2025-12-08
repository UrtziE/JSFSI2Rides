package domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.ResourceBundle;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


@Entity

public class RideRequest implements Serializable,Comparable<RideRequest>{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer requestId;
	private Date whenRequested;
	@Enumerated(EnumType.STRING)
	private EgoeraRideRequest state;
	private Date whenDecided;
	private int seats;
	private String fromRequested;
	private String toRequested;
	private boolean baloratuaDriver;
	private boolean erreklamatuaDriver;
	private boolean baloratuaTraveller;
	private boolean erreklamatuaTraveller;
	private boolean bidaiaEsandaZer=false;
	private float prezio;
	
	private String ibilbidea;
	@ManyToOne(fetch = FetchType.EAGER)
	private Traveller traveller;

	@ManyToOne(fetch = FetchType.EAGER)
	private Ride ride;

	public RideRequest(Date whenRequested, Ride ride, Traveller traveller, int seats,String from, String to) {
		this.seats = seats;
		this.whenRequested = whenRequested;
		this.ride = ride;
		this.traveller = traveller;
		this.state = EgoeraRideRequest.TRATATU_GABE;
		this.fromRequested=from;
		this.toRequested=to;
		this.baloratuaDriver =false;
		this.erreklamatuaDriver=false;
		this.baloratuaTraveller=false;
		this.erreklamatuaTraveller=false;
	}
	public RideRequest() {
		
	}
  public String getToRequested() {
	  return toRequested;
  }
  public String getFromRequested() {
	  return fromRequested;
  }
  public boolean isBidaiaEsandaZer() {
	  return bidaiaEsandaZer;
  }
  public void setBidaiaEsandaTrue() {
	  bidaiaEsandaZer=true;
  }
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RideRequest other = (RideRequest) obj;
		if (this.getId() != other.getId())
			return false;
		return true;
	}
	
     public int compareTo(RideRequest request) {
		
		if(this.state.equals(request.getState())) {
			if(!this.state.equals(EgoeraRideRequest.TRATATU_GABE)) {
				return(this.whenDecided.compareTo(request.getWhenDecided()));
			}else {
				float GureaRating= this.getTraveller().kalkulatuBalorazioMedia();
				float BesteRating= request.getTraveller().kalkulatuBalorazioMedia();
				if(GureaRating>BesteRating) {
					return 1;
				}else {
					if(GureaRating<BesteRating) {
						return -1;
					}else {
						int GureaRatingKop= this.getTraveller().getBalorazioKop();
						int BesteaRatingKop= request.getTraveller().getBalorazioKop();
						if(GureaRatingKop>BesteaRatingKop) {
							return 1;
						}else {
							if(GureaRatingKop<BesteaRatingKop) {
								return -1;
							}else {
								return 0;
							}
						}
						}
				}
				
			}
		}
		return(this.whenRequested.compareTo(request.getWhenRequested()));
		
	}

	public int getId() {
		return requestId;
	}

	public String toString() {
		return   this.traveller.getUser()+" " 
		+  /*this.getId()*/" " 
		+ this.getSeats();
	}
	
	
	public String getIbilbidea() {
		
		return ibilbidea;
	}
	public void setIbilbidea(String ibilbidea) {
		this.ibilbidea=ibilbidea;
	}

	public Traveller getTraveller() {
		return traveller;
	}

	public Date getWhenRequested() {
		return whenRequested;
	}

	public Ride getRide() {
		return ride;
	}
	
	public boolean isBaloratuaDriver() {
		return baloratuaDriver;
	}
	public void setBaloratuaDriver(boolean baloratuaDriver) {
		this.baloratuaDriver = baloratuaDriver;
	}
	public boolean isErreklamatuaDriver() {
		return erreklamatuaDriver;
	}
	public void setErreklamatuaDriver(boolean erreklamatuaDriver) {
		this.erreklamatuaDriver = erreklamatuaDriver;
	}
	public boolean isBaloratuaTraveller() {
		return baloratuaTraveller;
	}
	public void setBaloratuaTraveller(boolean baloratuaTraveller) {
		this.baloratuaTraveller = baloratuaTraveller;
	}
	public boolean isErreklamatuaTraveller() {
		return erreklamatuaTraveller;
	}
	public void setErreklamatuaTraveller(boolean erreklamatuaTraveller) {
		this.erreklamatuaTraveller = erreklamatuaTraveller;
	}
	public void setWhenDecided(Date whenAccepted) {
		this.whenDecided = whenAccepted;
	}

	public Date getWhenDecided() {
		return whenDecided;
	}

	public int getSeats() {
		return this.seats;
	}

	public EgoeraRideRequest getState() {
		return state;
	}

	public void setState(EgoeraRideRequest state) {
		this.state = state;
	}
	
	public String requestInfo() {
		return " request:" + this.getId() + this.ride.toString() + " seats: " + this.getSeats();
	}
	public float getPrezioa() {
		float prezioa=ride.lortuBidaiarenPrezioa(fromRequested, toRequested);
		return seats*prezioa;
	}
	public String mezua()
	{
		return(" "+seats);
				//Aldatu
	}
	
	public void setId(Integer id) {
		this.requestId = id;
	}
	public float getPrezio() {
		return prezio;
	}
	public void setPrezio(float prezio) {
		this.prezio = prezio;
	}
	
}
