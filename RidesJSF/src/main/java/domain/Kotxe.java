package domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity

public class Kotxe implements Serializable{
    
	 
	//Begiratu ondo matrikula.

	@Id

	private String matrikula;
	private String marka;
	private String modelo;
	private int tokiKopurua;
	@OneToOne (fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Driver driver;
	public Kotxe(String marka,String modelo, int tokiKopurua, String matrikula,Driver driver) {
		this.marka=marka;
		this.modelo=modelo;
		this.tokiKopurua=tokiKopurua;
		this.matrikula=matrikula;
		this.driver=driver;
	}
	public Kotxe() {
		
	}
	public String getMatrikula() {
		return matrikula;
	}
	public void setMatrikula(String matrikula) {
		this.matrikula = matrikula;
	}
	public String getMarka() {
		return marka;
	}
	public void setMarka(String marka) {
		this.marka = marka;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public int getTokiKopurua() {
		return tokiKopurua;
	}
	public void setTokiKopurua(int tokiKopurua) {
		this.tokiKopurua = tokiKopurua;
	}
	public String toString() {
		return marka+" " +modelo+ " " + matrikula;
	}
}
