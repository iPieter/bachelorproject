package controllers;

import java.io.Serializable;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class AuthenticationController implements Serializable
{

	/**
	 * The AuthenticationController is used by the authentication.(x)html page.
	 */
	
	private static final long serialVersionUID = -8396156684143995442L;
	
	public void doLogin()
	{
		
		System.out.println("-------- DO LOGIN --------");
		
	}
	

}
