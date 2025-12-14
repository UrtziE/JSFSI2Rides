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

public class Balorazioa implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Date whenDone;
	private String mezua;
	private int balorazioa;
	@ManyToOne(fetch = FetchType.EAGER)
	private Profile nork;
	@ManyToOne(fetch = FetchType.EAGER)
	private Profile nori;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Ride bidaia;
	
	public Balorazioa(Profile nork, Profile nori, String mezua, int balorazioa,Ride bidaia) {
		this.nork=nork;
		this.nori=nori;
		this.mezua=mezua;
		this.balorazioa=balorazioa;
		this.bidaia= bidaia;
		this.whenDone=new Date();
	}
	public Balorazioa() {
		
	}
 
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Balorazioa other = (Balorazioa) obj;
		if (this.getId() != other.getId())
			return false;
		return true;
	}
	
     
	public int getId() {
		return id;
	}
	public Date getWhenDone() {
		return whenDone;
	}
	public void setWhenDone(Date whenDone) {
		this.whenDone = whenDone;
	}
	public String getMezua() {
		return mezua;
	}
	public void setMezua(String mezua) {
		this.mezua = mezua;
	}
	public int getBalorazioa() {
		return balorazioa;
	}
	public void setBalorazioa(int balorazioa) {
		this.balorazioa = balorazioa;
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
	public Ride getBidaia() {
		return bidaia;
	}
	public void setBidaia(Ride bidaia) {
		this.bidaia = bidaia;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	
	
	
	
}
