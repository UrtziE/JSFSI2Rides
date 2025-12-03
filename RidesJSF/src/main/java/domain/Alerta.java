package domain;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity

public class Alerta {
	@Id 
	
	

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
	private Traveller traveller;
    @Column(name="origin") 
	private String from;
    @Column(name="destination") 

	private String to;
    @Column(name="alert_date") 

	private Date when;
	private boolean ezabatuta;
	private static final String ETIQUETAS = "Etiquetas";
	public Alerta() {}
	
	public Alerta(Traveller t, String from, String to, Date when) {
		this.traveller=t;
		this.from=from;
		this.to=to;
		this.when=when;
		ezabatuta=false;
		
	}
	public void setEzabatuta(boolean aurkitu) {
		ezabatuta=aurkitu;
	}
	public Date getWhen() {
		return when;
	}
	public int getId() {
		return id;
	}
	public boolean isEzabatuta() {
		return ezabatuta;
	}
	
	
	public String toString() {
		return("Nondik"+
				" "+from+" "+"Nora"+
				" "+ to + " "+"noiz"+ when);
	}
	
	
	
	/*public boolean berdinaDa(Ride ride) {
		if(ride.badaBide(from, to)&&ride.getDate().equals(when)) {
			return true;
		}else {
			return false;
		}
	}*/
	public Traveller getTraveller() {
		return traveller;
	}
	public String getFrom() {
		return from;
	}
	public String getTo() {
		return to;
	}
}
