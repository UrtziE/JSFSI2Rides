package domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Embeddable

public class Geltoki implements Serializable{
	
	
	
	private String tokiIzen;
	private float prezioa;
	private int seatKop;
	public Geltoki(String tokiIzen, float prezioa, int seatkop) {
		this.tokiIzen=tokiIzen;
		this.prezioa=prezioa;
		this.seatKop=seatkop;
	}
	public Geltoki()
	{}
	public void kenduSeatKop(int seat) {
		seatKop=seatKop-seat;
	}
	public void gehituSeatKop(int seat) {
		seatKop=seatKop+seat;
	}
	public String getTokiIzen() {
		return tokiIzen;
	}
	public float getPrezioa() {
		return prezioa;
	}
	public int getEserleku() {
		return seatKop;
	}
	
	
}
