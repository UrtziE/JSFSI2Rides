package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


import java.util.Locale;
import java.util.ResourceBundle;
@Entity
public class Erreklamazioa implements Serializable{


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nork")
	private Profile nork;
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nori")
	private Profile nori;
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin")
	private Profile admin;
	private String deskripzioa;
	@Enumerated(EnumType.STRING)
	private EgoeraErreklamazioa egoera;
	@ManyToOne (fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private RideRequest erreserba;
	@ManyToOne (fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Ride ride;
    @Column(name="claim_date")

	private Date when;
	private Date whenDecided;
	private float prezioa;
	
	public Erreklamazioa(Profile nork, Profile nori, String deskripzioa,float prezioa, RideRequest erreserba) {
		this.nork = nork;
		this.nori = nori;
		this.deskripzioa = deskripzioa;
		this.erreserba = erreserba;
		this.when=new Date();
		this.prezioa=prezioa;

		this.egoera=EgoeraErreklamazioa.ESLEITU_GABE;

		this.ride=erreserba.getRide();
	}
	public Erreklamazioa() {
		
	}
	
	public Date getWhenDecided() {
		return whenDecided;
	}
	public void setWhenDecided(Date whenDecided) {
		this.whenDecided = whenDecided;
	}
	public float getPrezioa() {
		return prezioa;
	}
	public void setPrezioa(float prezioa) {
		this.prezioa = prezioa;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Profile getNork() {
		return nork;
	}

	public void setNork(Profile nork) {
		this.nork = nork;
	}

	public Profile getNori() {
		return nori;
	}

	public void setNori(Profile nori) {
		this.nori = nori;
	}

	public Profile getAdmin() {
		return admin;
	}

	public void setAdmin(Profile admin) {
		this.admin = admin;
	}

	public String getDeskripzioa() {
		return deskripzioa;
	}

	public void setDeskripzioa(String deskripzioa) {
		this.deskripzioa = deskripzioa;
	}

	public EgoeraErreklamazioa getEgoera() {
		return egoera;
	}

	public void setEgoera(EgoeraErreklamazioa egoera) {
		this.egoera = egoera;
	}

	public RideRequest getErreserba() {
		return erreserba;
	}

	public void setErreserba(RideRequest erreserba) {
		this.erreserba = erreserba;
	}

	public Ride getRide() {
		return ride;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}

	public Date getWhen() {
		return when;
	}

	public void setWhen(Date when) {
		this.when = when;
	}

	@Override
	public String toString() {
		return ResourceBundle.getBundle("Etiquetas").getString("Erreklamazioa")+": ["+ResourceBundle.getBundle("Etiquetas").getString("Erreklamazioa.nork")+ nork.getName() + 
				", "+ ResourceBundle.getBundle("Etiquetas").getString("Erreklamazioa.nori") + nori.getName() + ", "+ResourceBundle.getBundle("Etiquetas").getString("Erreklamazioa.deskripzioa")
				+ deskripzioa + ", "+ResourceBundle.getBundle("Etiquetas").getString("Erreklamazioa.egoera")+ egoera + ", "+ResourceBundle.getBundle("Etiquetas").getString("Erreklamazioa.Ride") + ride.getInfo() +" "+ResourceBundle.getBundle("Etiquetas").getString("Erreklamazioa.Seats")+ erreserba.getSeats()
				+ "]";
	}
	
	
	

	

}
