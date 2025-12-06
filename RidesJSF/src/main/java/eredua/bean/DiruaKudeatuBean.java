package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import domain.Driver;
import domain.Profile;
import domain.Ride;
import domain.Traveller;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.annotation.ManagedProperty;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
@Named("diruaKudeatu")
@RequestScoped
public class DiruaKudeatuBean implements Serializable{
	private BLFacade blfacade;
	private float diruKop;
	private float userDiruKop;
	private String mota="sartu";
	private Profile user;
	 @Inject
	private LoginBean loginBean;
	private String diruaGood="";
	private String diruaFail="";


	public DiruaKudeatuBean() {

	}
	 @PostConstruct
	    public void init() {
	        this.user = loginBean.getOraingoUser();
	        
	        if (this.user == null) {
	            System.out.println("¡Acceso no autorizado!");
	    	    

	        }
	        
	    }


	public float getDiruKop() {
		return diruKop;
	}


	public void setDiruKop(float diruKop) {
		this.diruKop = diruKop;
	}


	public float getUserDiruKop() {
		blfacade=FacadeBean.getBusinessLogic();
		userDiruKop=blfacade.getMoney(user);
		return userDiruKop;
	}


	public void setUserDiruKop(float userDiruKop) {
		this.userDiruKop = userDiruKop;
	}


	public String getMota() {
		return mota;
	}


	public void setMota(String mota) {
		this.mota = mota;
	}
	public void diruaKudeatu() {
		blfacade=FacadeBean.getBusinessLogic();
		boolean emaitza;
		if(mota.equals("sartu")) {
			
			emaitza=blfacade.gehituDirua(diruKop, user);
			
		}else {
			if(diruKop>userDiruKop) {
				diruaFail="ezin duzu atera duzun dirua baina gehiago";
			}
			emaitza=blfacade.kenduDirua(diruKop, user);	
		}
		if(emaitza) {
			diruaGood="ondo "+ mota+" dira"+ diruKop+"€-ak";
			diruaFail="";
			this.getUserDiruKop();
		}else {
			diruaGood="";
			if(diruaFail.equals("")) diruaFail="errorea dirua"+mota+"egitean";
		}
	}
	public String exit() {
		if (user==null){
			return "menu";
		}else {
			if (user instanceof Traveller) {
				return "menuTraveller";
			}else {
				return "menuDriver";
			}
		}
	}
	public String getDiruaGood() {
		return diruaGood;
	}
	public void setDiruaGood(String diruaGood) {
		this.diruaGood = diruaGood;
	}
	public String getDiruaFail() {
		return diruaFail;
	}
	public void setDiruaFail(String diruaFail) {
		this.diruaFail = diruaFail;
	}
	
	

}
