package domain;

import java.io.Serializable;
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

public class Alerta implements Serializable,Comparable<Alerta>{
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
	private int mezuKopuruaLaguntzaile;
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
		return("Alerta id: "+id+" Nondik:"+
				" "+from+" "+"Nora:"+
				" "+ to + " "+"noiz:"+ when);
	}
	
	public int compareTo(Alerta alerta) {
		return this.getMezuKopuruaLaguntzaile()-alerta.getMezuKopuruaLaguntzaile();
	
	}
	
	/*public boolean berdinaDa(Ride ride) {
		if(ride.badaBide(from, to)&&ride.getDate().equals(when)) {
			return true;
		}else {
			return false;
		}
	}*/
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alerta other = (Alerta) obj;
		if (this.getFrom()!=other.getFrom()
				||this.getTo()!=other.getTo()
				||(this.getWhen().compareTo(other.getWhen())!=0)
				||(this.isEzabatuta()!=other.isEzabatuta()))
			return false;
			
		return true;
	}
	public Traveller getTraveller() {
		return traveller;
	}
	public String getFrom() {
		return from;
	}
	public String getTo() {
		return to;
	}

	public int getMezuKopuruaLaguntzaile() {
		return mezuKopuruaLaguntzaile;
	}

	public void setMezuKopuruaLaguntzaile(int mezuKopuruaLaguntzaile) {
		this.mezuKopuruaLaguntzaile = mezuKopuruaLaguntzaile;
	}
}
