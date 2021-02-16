package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.GasLeak;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class GasControlUnit extends FireUnit {

	public GasControlUnit(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getGasLevel() > 0) 
			target.setGasLevel(target.getGasLevel() - 10);

		if (target.getGasLevel() == 0)
			jobsDone();

	}
	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException {


		if(r instanceof ResidentialBuilding) 
		{
			if(r.getDisaster() instanceof GasLeak) 
			{
				if (r != null && this.getState() == UnitState.TREATING)
					reactivateDisaster();
				finishRespond(r);
			}
			
			


			else if(!(r != null && this.getState() == UnitState.TREATING)|| !this.canTreat(r)) 
					throw new CannotTreatException(this,r);
			else if (!(r.getDisaster() instanceof GasLeak) && r.getDisaster() != null)
				throw new IncompatibleTargetException(this, r,"Gas Control Unit can't treat "+r.getDisaster().getName()+" Disaster !!!");
			else if (!(r.getDisaster() instanceof GasLeak) && r.getDisaster() == null)
				throw new IncompatibleTargetException(this, r,"Gas Control Unit can't treat this Target !!!");

		}
		else if (r.getDisaster() != null)
				throw new IncompatibleTargetException(this, r,"Gas Control Unit can't treat "+r.getDisaster().getName()+" Disaster !!!");
			else 
				throw new IncompatibleTargetException(this, r,"Gas Control Unit can't treat this Target !!!");
	
		

	
	}
	public String getdisastername() {
		return "GasLeak";
	}
	public String getName() {
		return "Gas Control Unit";
	}

}
