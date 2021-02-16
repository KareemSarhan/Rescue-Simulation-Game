package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Fire;
import model.disasters.Injury;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {

	public Ambulance(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getBloodLoss() > 0) {
			target.setBloodLoss(target.getBloodLoss() - getTreatmentAmount());
			if (target.getBloodLoss() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getBloodLoss() == 0)

			heal();

	}

	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException {
		if(r instanceof Citizen) 
		{
			if(r.getDisaster() instanceof Injury) 
			{
				if (r != null && this.getState() == UnitState.TREATING)
					reactivateDisaster();
				finishRespond(r);
			}
			
			


			else if(!(r != null && this.getState() == UnitState.TREATING)|| !this.canTreat(r)) 
					throw new CannotTreatException(this,r);
			

		}
		else if (r.getDisaster() != null || !(r.getDisaster() instanceof Injury) && r.getDisaster() != null)
				throw new IncompatibleTargetException(this, r,"Ambulance can't treat "+r.getDisaster().getName()+" Disaster !!!");
			else 
				throw new IncompatibleTargetException(this, r,"Ambulance can't treat this Target !!!");
	}
	public String getName() {
		return "Ambulance";
	}
	public String getdisastername() {
		return "jury";
	}

}
