package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Disaster implements Simulatable{
	private int startCycle;
	private Rescuable target;
	private boolean active;
	private boolean hasaunit;
	
	
	
	
	
	public Disaster(int startCycle, Rescuable target) {
		this.startCycle = startCycle;
		this.target = target;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getStartCycle() {
		return startCycle;
	}
	public Rescuable getTarget() {
		return target;
	}
	public void strike ()throws CitizenAlreadyDeadException, BuildingAlreadyCollapsedException
	{
		if(this.getTarget() instanceof ResidentialBuilding) {
			if(checkCollapsed(((ResidentialBuilding)this.getTarget()))) {
				throw new BuildingAlreadyCollapsedException(this,"This Building is already Collapsed !!!!!");
				
			}
		}else {
			if(this.getTarget() instanceof Citizen) {
				if(checkDead(((Citizen)this.getTarget()))) {
					throw new CitizenAlreadyDeadException(this,"This Citizen is already Dead !!!!!");
					
				}
			}
		}
		target.struckBy(this);
		active=true;
	}
	public boolean checkDead(Citizen c) {
		if(c.getState()==CitizenState.DECEASED)
			return true;
		else 
			return false;
	}
	public boolean checkCollapsed(ResidentialBuilding b) {
		if(b.getStructuralIntegrity()==0)
			return true;
		else
			return false;
	}
	public String getName() {
		return "";
	}
	public boolean getHasaunit() {
		return hasaunit;
	}
	public void setHasaunit(boolean hasaunit) {
		this.hasaunit = hasaunit;
	}
}
