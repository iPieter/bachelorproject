package controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class IndexController implements Serializable{

	private static final long serialVersionUID = 426047598496441124L;
	
	//Returns how many actions the user contributed to: #solved issues, #assigned tasks
	public Map<String,String> getUserHistoryStatistics(){
		HashMap result=new HashMap<String,String>();
		//TODO manier om statistics door te geven
		return result;
	}
	
	//Returns recent activity of the user
	public Map<String,String> getUserRecentStatistics(){
		HashMap result=new HashMap<String,String>();
		//TODO manier om statistics door te geven
		return result;
	}

}
