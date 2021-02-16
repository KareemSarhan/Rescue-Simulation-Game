package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class DiseaseControlUnit extends MedicalUnit {

	public DiseaseControlUnit(String unitID, Address location,
			int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);
		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getToxicity() > 0) {
			target.setToxicity(target.getToxicity() - getTreatmentAmount());
			if (target.getToxicity() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getToxicity() == 0)
			heal();

	}
	public String getdisastername() {
		return "fection";
	}
	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException {
		if(r instanceof Citizen) 
		{
			if(r.getDisaster() instanceof Infection) 
			{
				if (r != null && this.getState() == UnitState.TREATING)
					reactivateDisaster();
				finishRespond(r);
			}
			
			


			else if(!(r != null && this.getState() == UnitState.TREATING)|| !this.canTreat(r)) 
					throw new CannotTreatException(this,r);
			

		}
		else if (r.getDisaster() != null || !(r.getDisaster() instanceof Infection) && r.getDisaster() != null)
				throw new IncompatibleTargetException(this, r,"Disease Control Unit can't treat "+r.getDisaster().getName()+" Disaster !!!");
			else 
				throw new IncompatibleTargetException(this, r,"Disease Control Unit can't treat this Target !!!");
	}
	public String getName() {
		return "Disease Control Unit";
	}

}
