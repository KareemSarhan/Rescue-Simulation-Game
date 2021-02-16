package model.units;
import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;
public class Evacuator extends PoliceUnit {
	public Evacuator(String unitID, Address location, int stepsPerCycle,
			WorldListener worldListener, int maxCapacity) {
		super(unitID, location, stepsPerCycle, worldListener, maxCapacity);
	}
	@Override
	public void treat() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0
				|| target.getOccupants().size() == 0) {
			jobsDone();
			return;
		}
		for (int i = 0; getPassengers().size() != getMaxCapacity()
				&& i < target.getOccupants().size(); i++) {
			getPassengers().add(target.getOccupants().remove(i));
			i--;
		}
		setDistanceToBase(target.getLocation().getX()
				+ target.getLocation().getY());
	}
	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException {

		if(r instanceof ResidentialBuilding) 
		{
			if(r.getDisaster() instanceof Collapse) 
			{
				if (r != null && this.getState() == UnitState.TREATING)
					reactivateDisaster();
				finishRespond(r);
			}
			
			


			else if(!(r != null && this.getState() == UnitState.TREATING)|| !this.canTreat(r)) 
					throw new CannotTreatException(this,r);
			else if (!(r.getDisaster() instanceof Collapse) && r.getDisaster() != null)
				throw new IncompatibleTargetException(this, r,"Evacuator can't treat "+r.getDisaster().getName()+" Disaster !!!");
			else if (!(r.getDisaster() instanceof Collapse) && r.getDisaster() == null)
				throw new IncompatibleTargetException(this, r,"Evacuator can't treat this Target !!!");

		}
		else if (r.getDisaster() != null)
				throw new IncompatibleTargetException(this, r,"Evacuator can't treat "+r.getDisaster().getName()+" Disaster !!!");
			else 
				throw new IncompatibleTargetException(this, r,"Evacuator can't treat this Target !!!");
	
		

	}
	public String getdisastername() {
		return "Collapse";
	}
	public String displayPassengers() {
		String r="";
		for(int i=0;i<this.getPassengers().size();i++) {
			r+=this.getPassengers().get(i).display()+"\n";
		}
		return r;
	}
	public String display() {
		if (this.getPassengers().size() != 0) {
			return super.display()+"Number of Passengers :- "+this.getPassengers().size()+"\n"+
					"Passengers :- "+this.displayPassengers();
		}
		else
			return super.display()+"Number of Passengers :- "+this.getPassengers().size()+"\n"+
			"Passengers :- "+"None";
	}
	public String getName() {
		return "Evacuator";
	}
}
