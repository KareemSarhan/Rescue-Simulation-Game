package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;

import model.disasters.Fire;
import model.disasters.GasLeak;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getFireDamage() > 0)

			target.setFireDamage(target.getFireDamage() - 10);

		if (target.getFireDamage() == 0)

			jobsDone();

	}
	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException {
		if(r instanceof ResidentialBuilding) 
		{
			if(r.getDisaster() instanceof Fire) 
			{
				if (r != null && this.getState() == UnitState.TREATING)
					reactivateDisaster();
				finishRespond(r);
			}
			
			


			else if(!(r != null && this.getState() == UnitState.TREATING)|| !this.canTreat(r)) 
					throw new CannotTreatException(this,r);
			else if (!(r.getDisaster() instanceof Fire) && r.getDisaster() != null)
				throw new IncompatibleTargetException(this, r,"Fire Truck can't treat "+r.getDisaster().getName()+" Disaster !!!");
			else if (!(r.getDisaster() instanceof Fire) && r.getDisaster() == null)
				throw new IncompatibleTargetException(this, r,"Fire Truck can't treat this Target !!!");

		}
		else if (r.getDisaster() != null)
				throw new IncompatibleTargetException(this, r,"Fire Truck can't treat "+r.getDisaster().getName()+" Disaster !!!");
			else 
				throw new IncompatibleTargetException(this, r,"Fire Truck can't treat this Target !!!");
	}
	public String getName() {
		return "Fire Truck";
	}
	public String getdisastername() {
		return "Fire";
	}
}
