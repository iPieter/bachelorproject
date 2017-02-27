package primefaces;
 
import controllers.*;
import model.Workplace;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
 
@ManagedBean
public class MenuView {
     
    private MenuModel model;
    
    @Inject
    private TraincoachDepotController controller;
    
    @PostConstruct
    public void init() {
        model = new DefaultMenuModel();
        //controller=new TraincoachDepotController();
        		
        //Fetch Data
        List<Workplace> datalist=controller.getAllTraincoachDepots();
        
        //First submenu
        DefaultSubMenu firstSubmenu = new DefaultSubMenu("Traincoach Depots");
         
        DefaultMenuItem item;
        for(Workplace w: datalist){
        	item = new DefaultMenuItem(w.getName());
	        item.setUrl("http://www.primefaces.org");
	        item.setIcon("ui-icon-home");
	        firstSubmenu.addElement(item);
        }
         
        model.addElement(firstSubmenu);
    }
 
    public MenuModel getModel() {
        return model;
    }   
     
    public void save() {
        addMessage("Success", "Data saved");
    }
     
    public void update() {
        addMessage("Success", "Data updated");
    }
     
    public void delete() {
        addMessage("Success", "Data deleted");
    }
     
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}