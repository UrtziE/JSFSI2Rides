package eredua.bean;

import java.io.IOException;
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
	private Profile oraingoUser;

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
	
	public String autologin(Profile userProfile) {
	    this.oraingoUser = userProfile;
	    
	    if (this.oraingoUser instanceof Traveller) {
	    	mota="Traveller";
	        return "menuTraveller";
	    } else if (this.oraingoUser instanceof Driver) {
	    	mota="Driver";
	        return "menuDriver";
	    }
	    mota=null;
	    
	    return "error";
	}
	public String logout() {
	    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	     
	     mota=null;
		 oraingoUser=null;
		 
	    return "menu";
	}
	public void redirect() {
		 try {		    		     
		        if (this.oraingoUser!=null) {		            
		            FacesContext.getCurrentInstance()
		                .getExternalContext()
		                .redirect("Menu.xhtml"); 
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	}
	public void redirectUser() {
		 try {		    		     
			 if (this.oraingoUser==null) {	            
		            FacesContext.getCurrentInstance()
		                .getExternalContext()
		                .redirect("Menu.xhtml"); 
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	}
	public void redirectDriver() {
		 try {		    		     
			 if (! (this.oraingoUser instanceof Driver)) {	            
		            FacesContext.getCurrentInstance()
		                .getExternalContext()
		                .redirect("Menu.xhtml"); 
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	}
	public void redirectTraveller() {
		 try {		    		     
		        if (! (this.oraingoUser instanceof Traveller)) {		            
		            FacesContext.getCurrentInstance()
		                .getExternalContext()
		                .redirect("Menu.xhtml"); 
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	}






	public String egiaztatu() {
		blfacade=FacadeBean.getBusinessLogic();
		Profile emaitza=blfacade.login(user, pasahitza);
		if(emaitza!=null) {
			oraingoUser=emaitza;
			if(emaitza instanceof Traveller) {
				mota="Traveller";
				return "menu";
			}else {
				if(emaitza instanceof Driver) {
					mota="Driver";
					return "menu";
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


	public String getMota() {
		return mota;
	}


	public void setMota(String mota) {
		this.mota = mota;
	}


	public Profile getOraingoUser() {
		return oraingoUser;
	}


	public void setOraingoUser(Profile oraingoUser) {
		if(oraingoUser instanceof Traveller) {
			mota="Traveller";
		}else {
			mota="Driver";
		}
		user= oraingoUser.getUser();
		this.oraingoUser = oraingoUser;
	}




}