package controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import be.kuleuven.cs.gent.projectweek.model.Workplace;
import be.kuleuven.cs.gent.projectweek.services.InternalDatafetchService;
import java.io.Serializable;

@Named
@ConversationScoped
public class WorkplaceController implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Inject
	private InternalDatafetchService internalDatafetchService;

	private Workplace currentWorkplace = new Workplace();
	
	private int workplaceId = -1;
	
	public List<Workplace> getAllWorkplaces()
	{
		return internalDatafetchService.getAllWorkplaces();
	}
	
	@PostConstruct
	public void initialiseWorkplace()
	{
		if (this.workplaceId >= 0)
		{
			this.currentWorkplace = internalDatafetchService.findWorkplaceByWorkplaceId(workplaceId);
			
			System.out.println(currentWorkplace.getName());
		}
		
		//currentWorkplace = internalDatafetchService.findWorkplaceByWorkplaceId( currentWorkplace.getId() );
		//System.out.println("ID:"+currentWorkplace.getId());
	}
	
	public int getWorkplaceId()
	{
		return this.workplaceId;
	}
	
	public void setWorkplaceId(int workplaceId)
	{
		this.workplaceId = workplaceId;
	}

	// GETTERS & SETTERS
	public Workplace getCurrentWorkplace()
	{
		return currentWorkplace;
	}

}
