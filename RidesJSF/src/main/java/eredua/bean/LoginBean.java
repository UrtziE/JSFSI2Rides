package eredua.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.primefaces.event.SelectEvent;

import businessLogic.BLFacade;
import domain.Driver;
import domain.Profile;
import domain.Traveller;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;


@Named("login")
@SessionScoped 
public class LoginBean implements Serializable {
	private String user;
	private String pasahitza;
	private String mota;

	private BLFacade blfacade;

	public LoginBean() {
	}


	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPasahitza() {
		return pasahitza;
	}

	public void setPasahitza(String pasahitza) {
		this.pasahitza = pasahitza;
	}





	public String egiaztatu() {
		blfacade=FacadeBean.getBusinessLogic();
		Profile emaitza=blfacade.login(user, pasahitza);
		if(emaitza!=null) {
			if(emaitza instanceof Traveller) {
				mota="Traveller";
				return "menuaTraveller";
			}else {
				if(emaitza instanceof Driver) {
					mota="Driver";
					return "menuaDriver";
				}
			}
		}else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Usuarioa edo pasahitza gaizki dago jarrita."));
			mota=null;
			return "error";
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Usuarioa edo pasahitza gaizki dago jarrita."));
		mota=null;
		return "error";
	}




}