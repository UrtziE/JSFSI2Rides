package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.persistence.CascadeType;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String info;

	private float kantitatea;

	@OneToOne(fetch = FetchType.LAZY)
	private RideRequest erreserba;

	@OneToOne(fetch = FetchType.LAZY)
	private Ride ride;

	@ManyToOne(fetch = FetchType.LAZY)
	private Profile p;
	private int type = 1;
	private int mezutype;

	private String datamezua;
	private String diruMezu;
	private String typerenMezua;
	private Date when;
	private boolean irakurrita = false;
	@OneToOne(fetch = FetchType.EAGER)
	private Alerta alerta;
	@OneToOne(fetch = FetchType.EAGER)
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
		info = zerena + "__" + datamezua + "_ " + mezua;
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
			//sortuMezua("Mezuak.Requested","Mezuak.DataRequest","-",true);
			

			break;
		case 1:
			//sortuMezua("Mezuak.Rejected","Mezuak.DataRejected","+",true);
			break;
		case 2:
			//sortuMezua("Mezuak.Canceled","Mezuak.DataCanceled","+",EZDAERRESERBA);
			break;
		case 3:
			//sortuMezua("Mezuak.Deposite","Mezuak.DataDeposite","+",EZDAERRESERBA);
			break;
		// Driver
		case 4:
			//sortuMezua("Mezuak.NotDone","Mezuak.DataNotDone","+",EZDAERRESERBA);
			break;
		case 5:
			//sortuMezua("Mezuak.Canceled","Mezuak.DataCanceled","+",EZDAERRESERBA);
			break;
		case 6:
			//sortuMezua("Mezuak.Withdraw","Mezuak.DataWithdraw","-",EZDAERRESERBA);
			break;
		case 7:
			//sortuMezua("Mezuak.Done",DATADONE,"+",EZDAERRESERBA);
			break;
		case 8:
			//sortuMezua("Mezuak.NewErreklamazioa",DATADONE,"+",EZDAERRESERBA);
			break;
		case 9:
			//sortuMezua("Mezuak.ErreklamazioaAccepted",DATADONE,"+",EZDAERRESERBA);
			break;
		case 10:
			//sortuMezua("Mezuak.ErreklamazioaRejected",DATADONE,"",EZDAERRESERBA);
			break;
		case 11:
			//sortuMezua("Mezuak.Erreklamatuta",DATADONE,"-",EZDAERRESERBA);
			break;
		}

	}

	public void zeinErreklamazio(int i) {
		switch (i) {
		case 0:
			//sortuMezua("Mezuak.ErreklamazioaAccepted",DATADONE,"+",EZDAERRESERBA);
			break;
		case 1:
			//sortuMezua("Mezuak.ErreklamazioaRejected",DATADONE,"",EZDAERRESERBA);
			break;
		case 2:
			//sortuMezua("Mezuak.ErreklamazioBukatuta",DATADONE,"",EZDAERRESERBA);
			//typerenMezua = typerenMezua+"  Accepted";
			break;
		case 3:
			//sortuMezua("Mezuak.ErreklamazioBukatuta",DATADONE,"",EZDAERRESERBA);
			//typerenMezua =typerenMezua +"  Rejected";
		
			break;
		}
	}

	public void zeinAlerta() {
		//sortuMezua("Mezuak.EskatutakoAlerta",DATADONE,"",EZDAERRESERBA);
		//typerenMezua = typerenMezua + alerta.toString();
	}

	private void sortuMezua(String mezua, String data, String gehituEdoKendu, boolean erreserbaDa) {
		if (erreserbaDa) {
			typerenMezua = ResourceBundle.getBundle("Etiquetas").getString(mezua) + ":  "+ erreserba.mezua() + "__" + ride.mezua();
		} else {
			if (ride != null) {
				typerenMezua = ResourceBundle.getBundle("Etiquetas").getString(mezua) + ":   " + ride.mezua();
			} else {
				typerenMezua = ResourceBundle.getBundle("Etiquetas").getString(mezua) + ": ";
			}
			datamezua = ResourceBundle.getBundle("Etiquetas").getString(data) + " " + when;
			diruMezu = gehituEdoKendu + kantitatea + "€";
		}
	}
	

}
