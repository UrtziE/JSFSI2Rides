package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import domain.Driver;
import domain.Kotxe;
import domain.Profile;
import domain.Ride;
import domain.Traveller;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
@Named("createCar")
@ViewScoped
public class CreateCarBean implements Serializable{
	@Inject
    private LoginBean loginBean;
	private String matrikula;
	private String marka;
	private String modelo;
	private int tokiKopurua;
	List<Kotxe> kotxeak;
	private String carGood="";
	private String carExists="";
	private Profile user;
	private BLFacade blfacade;
	public CreateCarBean() {
		
	}
	 @PostConstruct
	    public void init() {
	        this.user = loginBean.getOraingoUser();
	        
	        if (this.user == null) {
	            System.out.println("¡Acceso no autorizado!");
	    	    

	        }
	        
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
	 public String getCarGood() {
		 return carGood;
	 }
	 public void setCarGood(String carGood) {
		 this.carGood = carGood;
	 }
	 public String getCarExists() {
		 return carExists;
	 }
	 public void setCarExists(String carExists) {
		 this.carExists = carExists;
	 }
	 public List<Kotxe> getKotxeak(){
		
		 blfacade=FacadeBean.getBusinessLogic();
		 kotxeak=blfacade.getKotxeGuztiak((Driver)user);
		 return kotxeak;
	 }
	 
	 
	 public void createCar() {
		 blfacade=FacadeBean.getBusinessLogic();
		 if(user!=null) {
			boolean sortuta=blfacade.createCar(marka, modelo, matrikula, tokiKopurua, (Driver)user);
			if(sortuta) {
				this.getKotxeak();
				carGood="Ondo sortu da kotxea!!!";
				carExists="";
			}else {
				carGood="";
				carExists="Errorea kotxea sortzean!!!";
			}
		 }else {
			 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Ez zara ondo indetifikatu"));
		 }
	 }
	    
	   
}
