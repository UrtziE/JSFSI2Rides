package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;


import java.util.Locale;
import java.util.ResourceBundle;

@Entity

public class Mezua implements Serializable, Comparable<Mezua> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String info;

	private float kantitatea;

	@ManyToOne(fetch = FetchType.EAGER)
	private RideRequest erreserba;

	@ManyToOne(fetch = FetchType.EAGER)
	private Ride ride;

	@ManyToOne(fetch = FetchType.EAGER)
	private Profile p;
	private int type = 1;
	private int mezutype;

	private String datamezua;
	private String diruMezu;
	private String typerenMezua;
    @Column(name="message_date") 
	private Date when;
	private boolean irakurrita = false;
	@ManyToOne(fetch = FetchType.EAGER)
	private Alerta alerta;
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
	private Erreklamazioa erreklamazioa;
	
	private static final String DATADONE = "Mezuak.DataDone";  
	private static final boolean EZDAERRESERBA = false;  

	
	public int getType() {
		return type;
	}

	public RideRequest getErreserba() {
		return erreserba;
	}

	public void setErreserba(RideRequest erreserba) {
		this.erreserba = erreserba;
	}

	public Profile getP() {
		return p;
	}

	/*
	 * public Mezua( int i,RideRequest erreserba) { this.type=2;
	 * this.erreserba=erreserba; this.ride=erreserba.getRide(); this.zenbakia=i;
	 * when=new Date(); zeinRide(i);
	 * 
	 * }
	 */
	// Diru transakzioak ridetan
	public Mezua(int i, float kantitatea, RideRequest erreserba, Profile p) {
		when = new Date();
		this.p=p;		
		this.kantitatea = kantitatea;
		this.erreserba = erreserba;
		this.ride = erreserba.getRide();
		this.mezutype = i;
		zeinTransaction(i);

	}

	// Dirua sartu eta atera
	public Mezua(int i, float kantitatea, Profile p) {
		when = new Date();
		this.kantitatea = kantitatea;
		// this.type=1;
		this.mezutype = i;
		this.p = p;
		zeinTransaction(i);

	}

	public Mezua(Profile p, Ride ride, Alerta alerta) {
		when = new Date();
		this.ride = ride;
		this.alerta = alerta;
		this.type = 2;
		this.p = p;
		zeinAlerta();

	}
	public Mezua(int i,Profile p, Ride ride, Erreklamazioa erreklamazioa) {
		when = new Date();
		this.ride = ride;
		this.erreklamazioa = erreklamazioa;
		this.type = 3;
		this.p = p;
		this.mezutype=i;
		zeinErreklamazio(i);

	}

	public Mezua() {

	}

	public void setInfo(String zerena, Date when, String mezua) {
		if(erreserba==null) {
		info = zerena + "__" + datamezua + "_ " + mezua;
		}else {
			info = "Erreserba ID:"+erreserba.getId() +zerena + "__" + datamezua + "_ " + mezua;
		}
	}

	public void setInfo(String zerena, Date when) {
		info = zerena + "__" + datamezua;
	}

	public void setIrakurritaTrue() {
		irakurrita = true;
	}

	public int getId() {
		return id;
	}

	public Date getWhen() {
		return this.when;
	}

	public Alerta getAlerta() {
		return alerta;
	}

	public String getInfo() {
		if (type == 1) {
			zeinTransaction(mezutype);
			setInfo(typerenMezua, when, diruMezu);
		}else if(type == 2){
			zeinAlerta();
			setInfo(typerenMezua, when);
		}else {
			zeinErreklamazio(mezutype);
			setInfo(typerenMezua, when);
		}
		return info;
	}

	public Ride getRide() {
		return ride;
	}

	public boolean isIrakurrita() {
		return irakurrita;
	}

	public String toString() {
		return getInfo();
	}

	public int compareTo(Mezua mezua) {

		return (this.when.compareTo(mezua.getWhen()));
	}

	public void zeinTransaction(int i) {
		switch (i) {
		// Traveller
		case 0:
			sortuMezua("Erreserba eskatuta","Mezuak.DataRequest","-",true);
			

			break;
		case 1:
			sortuMezua("Erreserba deuseztatuta","Mezuak.DataRejected","+",true);
			break;
		case 2:
			sortuMezua("Bidaia kantzelatu egin da","Mezuak.DataCanceled","+",EZDAERRESERBA);
			break;
		case 3:
			sortuMezua("Dirua sartu: ","Mezuak.DataDeposite","+",EZDAERRESERBA);
			break;
		// Driver
		case 4:
			sortuMezua("Bidaia ez da egin","Mezuak.DataNotDone","+",EZDAERRESERBA);
			break;
		case 5:
			sortuMezua("Bidaia kantzelatu egin da","Mezuak.DataCanceled","+",EZDAERRESERBA);
			break;
		case 6:
			sortuMezua("Dirua atera: ","Mezuak.DataWithdraw","-",EZDAERRESERBA);
			break;
		case 7:
			sortuMezua("Bidaia egin da",DATADONE,"+",EZDAERRESERBA);
			break;
		case 8:
			sortuMezua("Mezuak.NewErreklamazioa",DATADONE,"+",EZDAERRESERBA);
			break;
		case 9:
			sortuMezua("Mezuak.ErreklamazioaAccepted",DATADONE,"+",EZDAERRESERBA);
			break;
		case 10:
			sortuMezua("Mezuak.ErreklamazioaRejected",DATADONE,"",EZDAERRESERBA);
			break;
		case 11:
			sortuMezua("Mezuak.Erreklamatuta",DATADONE,"-",EZDAERRESERBA);
			break;
		}

	}

	public void zeinErreklamazio(int i) {
		switch (i) {
		case 0:
			sortuMezua("Mezuak.ErreklamazioaAccepted",DATADONE,"+",EZDAERRESERBA);
			break;
		case 1:
			sortuMezua("Mezuak.ErreklamazioaRejected",DATADONE,"",EZDAERRESERBA);
			break;
		case 2:
			sortuMezua("Mezuak.ErreklamazioBukatuta",DATADONE,"",EZDAERRESERBA);
			typerenMezua = typerenMezua+"  Accepted";
			break;
		case 3:
			sortuMezua("Mezuak.ErreklamazioBukatuta",DATADONE,"",EZDAERRESERBA);
			typerenMezua =typerenMezua +"  Rejected";
		
			break;
		}
	}

	public void zeinAlerta() {
		sortuMezua("Mezuak.EskatutakoAlerta",DATADONE,"",EZDAERRESERBA);
		typerenMezua = typerenMezua + alerta.toString();
		datamezua = " " + when;
	}

	private void sortuMezua(String mezua, String data, String gehituEdoKendu, boolean erreserbaDa) {
	
		
		if (erreserbaDa) {
			ride.toString();
			erreserba.toString();
			typerenMezua = mezua+ erreserba.mezua() + "__" + ride.mezua();
		} else {
			if (ride != null) {
				
				ride.toString();
				typerenMezua = mezua+":   " + ride.mezua();
			} else {
				typerenMezua =mezua;
			}
		}
			datamezua = " " + when;
			diruMezu = gehituEdoKendu + kantitatea + "€";
		
	}

	public String getDatamezua() {
		return datamezua;
	}

	public void setDatamezua(String datamezua) {
		this.datamezua = datamezua;
	}

	public String getDiruMezu() {
		return diruMezu;
	}

	public void setDiruMezu(String diruMezu) {
		this.diruMezu = diruMezu;
	}

	public String getTyperenMezua() {
		return typerenMezua;
	}

	public void setTyperenMezua(String typerenMezua) {
		this.typerenMezua = typerenMezua;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
	}

	public int getMezutype() {
		return mezutype;
	}

	public void setMezutype(int mezutype) {
		this.mezutype = mezutype;
	}
	

}
