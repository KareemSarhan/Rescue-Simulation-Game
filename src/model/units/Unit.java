package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Disaster;
import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Unit implements Simulatable, SOSResponder {
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private WorldListener worldListener;

	public Unit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		this.unitID = unitID;
		this.location = location;
		this.stepsPerCycle = stepsPerCycle;
		this.state = UnitState.IDLE;
		this.worldListener = worldListener;
	}

	public void setWorldListener(WorldListener listener) {
		this.worldListener = listener;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}

	@Override
	public void respond(Rescuable r) throws CannotTreatException, IncompatibleTargetException {

		if (!this.canTreat(r)) {
			throw new CannotTreatException(this, r, "Cannot treat this target !!!");
		} else {
			if (target != null && state == UnitState.TREATING) 
			{
				
				reactivateDisaster();
				
			}
			finishRespond(r);
		}
	}

	public boolean canTreat(Rescuable x) {
		if (x instanceof ResidentialBuilding) {
			if (((ResidentialBuilding) x).getFireDamage() > 0 || ((ResidentialBuilding) x).getGasLevel() > 0
					|| ((ResidentialBuilding) x).getFoundationDamage() > 0) {
				return true;
			} else {
				return false;
			}
		} else if (x instanceof Citizen) {
			if (((Citizen) x).getBloodLoss() > 0 || ((Citizen) x).getToxicity() > 0)
				return true;
			else
				return false;
		}
		return false;
	}

	public void reactivateDisaster() {
		Disaster curr = target.getDisaster();
		curr.setActive(true);
		curr.setHasaunit(false);
	}

	public void finishRespond(Rescuable r) {
		target = r;
		target.getDisaster().setHasaunit(true);
		state = UnitState.RESPONDING;
		Address t = r.getLocation();
		distanceToTarget = Math.abs(t.getX() - location.getX()) + Math.abs(t.getY() - location.getY());

	}

	public abstract void treat();

	public void cycleStep() {
		if (state == UnitState.IDLE)
			return;
		if (distanceToTarget > 0) {
			distanceToTarget = distanceToTarget - stepsPerCycle;
			if (distanceToTarget <= 0) {
				distanceToTarget = 0;
				Address t = target.getLocation();
				worldListener.assignAddress(this, t.getX(), t.getY());
			}
		} else {
			state = UnitState.TREATING;
			treat();
		}
	}

	public void jobsDone() {
		target = null;
		state = UnitState.IDLE;

	}

	public String displayType() {
		if (this instanceof PoliceUnit) {
			return "Police Unit";
		} else if (this instanceof MedicalUnit) {
			return "Medical Unit";
		} else {
			return "Fire Unit";
		}
	}

	public String display() {
		if (this.getTarget() != null) {
			return "Location :- (" + this.getLocation().getX() + "," + this.getLocation().getY() + ") \n"
					+ "Unit ID :- " + this.getUnitID() + "\n" + "Type :- " + this.displayType() + "\n"
					+ "Steps Per Cycle :- " + this.getStepsPerCycle() + "\n" + "State :- " + this.getState() + "\n"
					+ "Target :- \n {  " + this.getTarget().displayTarget() + "  } \n";
		} else {
			return "Location :- (" + this.getLocation().getX() + "," + this.getLocation().getY() + ") \n"
					+ "Unit ID :- " + this.getUnitID() + "\n" + "Type :- " + this.displayType() + "\n"
					+ "Steps Per Cycle :- " + this.getStepsPerCycle() + "\n" + "Target :- " + "None" + "\n"
					+ "State :- " + this.getState() + "\n";
		}
	}

	public String getName() {
		return "";
	}
	public String getdisastername() {
		return "";
	}
}
