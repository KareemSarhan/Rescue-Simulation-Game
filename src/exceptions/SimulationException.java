package exceptions;

import model.units.Unit;
import simulation.Rescuable;

public abstract class SimulationException extends Exception {

	public SimulationException() {
	super();	
	}
	public SimulationException(String message) {
		super(message);
	}
	
}
