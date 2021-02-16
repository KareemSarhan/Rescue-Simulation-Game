package controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import exceptions.SimulationException;
import model.disasters.Disaster;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Unit;
import model.units.UnitState;
import simulation.Rescuable;
import simulation.Simulator;
import view.RescueSim;

public class CommandCenter  implements SOSListener, ActionListener {
 
	public Simulator engine;
	public ArrayList<ResidentialBuilding> visibleBuildings;
	public ArrayList<Citizen> visibleCitizens;
	public RescueSim rescueSim;
	ArrayList<Citizen> citizens;
	ArrayList<ResidentialBuilding> buildings;
	ArrayList<Citizen> deadPeople;
	public ArrayList<Unit> emergencyUnits;
	public JButton[][] gridButtons;
	Citizen CurC;
	ResidentialBuilding CurB;
	public ActionEvent Last;
	JButton NC = new JButton();
	public int Causlities;
	public String log = "";
	public int oldCauslities;

	
	public CommandCenter() throws Exception {
		this.rescueSim = new RescueSim();
		
		engine = new Simulator(this);
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		citizens = engine.getCitizens();
		buildings = engine.getBuildings();
		emergencyUnits = engine.getEmergencyUnits();
		rescueSim.Casualties.setText(""+Causlities);
		deadPeople = new ArrayList<>();
		for (int i = 0; i < emergencyUnits.size(); i++) {
			Unit unit = emergencyUnits.get(i);
			JButton btnUnit = new JButton();
			btnUnit.setText(unit.getName());
			// System.err.println(unit.getName());
			// RescueSim.resizeImage();
			if (unit.getName() == "Ambulance") {
				btnUnit.setIcon(new ImageIcon("c:/Java_Dev/Units/Ambulance.png"));
			} else if (unit.getName() == "Disease Control Unit") {
				btnUnit.setIcon(new ImageIcon("c:/Java_Dev/Units/Disease Control Unit.png"));
			} else if (unit.getName() == "Fire Truck") {
				btnUnit.setIcon(new ImageIcon("c:/Java_Dev/Units/Fire Truck.png"));
			} else if (unit.getName() == "Gas Control Unit") {
				btnUnit.setIcon(new ImageIcon("c:/Java_Dev/Units/Gas Control Unit.png"));
			} else if (unit.getName() == "Evacuator") {
				btnUnit.setIcon(new ImageIcon("c:/Java_Dev/Units/Evacuator.png"));
			}

			btnUnit.addActionListener(this);
			btnUnit.setActionCommand(emergencyUnits.get(i).toString());
			rescueSim.AvilableUnitPanel.add(btnUnit);
		}

		gridButtons = new JButton[10][10];
		for (int i = 9; i >= 0; i--) {
			for (int j = 0; j < 10; j++) {
				JButton h = new JButton(i + " " + j);
				h.setForeground(Color.BLUE);
				// h.setOpaque(false);
				// h.setContentAreaFilled(false);
				// h.setBorderPainted(false);
				rescueSim.RescuePanel.add(h);
				gridButtons[i][j] = h;
			}
		}
		gridButtons[0][0].addActionListener(this);
		gridButtons[0][0].setActionCommand("Base");

		for (int i = 0; i < citizens.size(); i++) {

			JButton l = gridButtons[citizens.get(i).getLocation().getX()][citizens.get(i).getLocation().getY()];
			// l.setBounds(x_pos, y_pos, 30, 25);
			l.setForeground(Color.BLUE);
			l.setOpaque(false);
			l.setContentAreaFilled(false);
			// l.setBorderPainted(false);

			if (!inbil(citizens.get(i))) {
				l.setText("");
				l.setIcon(RescueSim.CitAdder());
				l.addActionListener(this);
			}

			l.setActionCommand(citizens.get(i).toString());
		}

		for (int i = 0; i < buildings.size(); i++) {
			JButton l = gridButtons[buildings.get(i).getLocation().getX()][buildings.get(i).getLocation().getY()];
			l.setForeground(Color.BLUE);
			l.setOpaque(false);
			l.setContentAreaFilled(false);
			l.setText("");

			ImageIcon Temp = new ImageIcon(RescueSim.BilCitNumAdder(buildings.get(i).getOccupants().size()));
			l.setIcon(Temp);
			Temp.getImage().flush();
			// File tempf =new File("c:/Java_Dev/joined"+i+".png");
			// tempf.delete();

			l.addActionListener(this);
			// System.out.println(buildings.get(i).toString());
			l.setActionCommand(buildings.get(i).toString());
			// rescueSim.RescuePanel.getComponentAt((buildings.get(i).getLocation().getX(),buildings.get(i).getLocation().getY()));
		}
		rescueSim.HINT.setText("HINT");
		rescueSim.HINT.setPreferredSize(new Dimension(70, 70));
		rescueSim.HINT.setActionCommand("Jarvis");
		rescueSim.HINT.addActionListener(this);
		NC.addActionListener(this);
		NC.setActionCommand("next");
		NC.setText("START");
		
		rescueSim.GenralInfoPanel.add(NC);

		// rescueSim.Re
	}

	@Override
	public void receiveSOSCall(Rescuable r) {
		if (r instanceof ResidentialBuilding) {
			if (!visibleBuildings.contains(r))
				visibleBuildings.add((ResidentialBuilding) r);
		} else {

			if (!visibleCitizens.contains(r))
				visibleCitizens.add((Citizen) r);
		}
	}

	public boolean inbil(Citizen X) {
		for (int i = 0; i < buildings.size(); i++) {
			if (buildings.get(i).getOccupants().contains(X)) {
				return true;
			}

		}
		return false;
	}

	public void actionPerformed(ActionEvent e) {
		// get the JButton that was clicked
		//System.out.println(e.getActionCommand());
		this.rescueSim.revalidate();
		JButton buttonap = (JButton) e.getSource();
		if (e.getActionCommand().equals("Jarvis")) 
		{
			infoBox(JARVISLOGIC(), "HINTS");
		}
		else if (e.getActionCommand().equals("next")) {
			if (!engine.checkGameOver()) {
				try {
					engine.nextCycle();
					
					this.rescueSim.revalidate();
					this.rescueSim.repaint();
					updatter();
					updateAvailable();
					try {
						updatethisthing();
						updateDis();
						rescueSim.RescuePanel.removeAll();
						for (int i = 9; i >= 0; i--) {
							for (int j = 0; j < 10; j++) {
								JButton h = gridButtons[i][j];
								h.addActionListener(this);
								rescueSim.RescuePanel.add(h);

							}
						}
						// gridButtons[0][0].addActionListener(this);
						gridButtons[0][0].setActionCommand("Base");
					} catch (IOException e1) {

					}

					NC.setText("Cycle :- " + engine.getCurrentCycle());
					this.displayCasulities(Causlities);
					rescueSim.Casualties.setText("" + Causlities);
					log(oldCauslities);
					rescueSim.LogsPanelMaker(rescueSim.LogsPanel, log);
					this.rescueSim.revalidate();
					this.rescueSim.repaint();
				} catch (SimulationException e1) {
					infoBox(e1.getMessage(), "Execption");
				}
			} else {
				infoBox("Game Over \n Casuilties : " + Causlities, "End Game ");
			}
		} else if ((rescueSim.RescuePanel).isAncestorOf(buttonap)) {
			if (buttonap.getActionCommand().contains("Citizen")) {
				for (int i = 0; i < citizens.size(); i++) {
					if ((citizens.get(i).toString()).equals(buttonap.getActionCommand())) {
						rescueSim.TextInfoPanel.setText(citizens.get(i).display());
						CurC = citizens.get(i);
					}
				}
				// CITIZENS RESPONDING
				if (Last != null) {
					if (Last.getActionCommand().toString().contains("units")) {
						for (int i = 0; i < emergencyUnits.size(); i++) {
							if ((emergencyUnits.get(i).toString()).equals(Last.getActionCommand())) {
								Unit CurU = emergencyUnits.get(i);
								try {
									CurU.respond(CurC);
									rescueSim.RespondingUnitPanel.add((JButton) Last.getSource());
									rescueSim.AvilableUnitPanel.remove((JButton) Last.getSource());
								} catch (CannotTreatException e1) {
								//	System.out.println("ythtyytnhtntntythn  ththn tn");
									infoBox(e1.getMessage(), "Execption");
									// System.out.println(e1.getMessage());
								} catch (IncompatibleTargetException e1) {
									//System.out.println("ythtyytnhtntntythn  ththn tn");
									infoBox(e1.getMessage(), "Execption");
									// System.out.println(e1.getMessage());
								}
							}
						}
					}
				}
			} else if (buttonap.getActionCommand().contains("ResidentialBuilding")) {
				for (int i = 0; i < buildings.size(); i++) {
					if (buildings.get(i).toString().equals(buttonap.getActionCommand())) {
						rescueSim.TextInfoPanel.setText(buildings.get(i).display());
						CurB = buildings.get(i);
					}
				}
				// BUILDING RESPONDING
				if (Last != null) {
					if (Last.getActionCommand().toString().contains("units")) {
						for (int i = 0; i < emergencyUnits.size(); i++) {
							if ((emergencyUnits.get(i).toString()).equals(Last.getActionCommand())) {
								Unit CurU = emergencyUnits.get(i);
								try {
									CurU.respond(CurB);
									rescueSim.RespondingUnitPanel.add((JButton) Last.getSource());
									rescueSim.AvilableUnitPanel.remove((JButton) Last.getSource());
								} catch (CannotTreatException e1) {
									//System.out.println("ythtyytnhtntntythn  ththn tn");
									infoBox(e1.getMessage(), "Execption");
									//System.out.println(e1.getMessage());
								} catch (IncompatibleTargetException e1) {
									//System.out.println(e1.getMessage());
									infoBox(e1.getMessage(), "Execption");
								}
							}
						}
					}
				}
			} else if (e.getActionCommand().toString().equals("Base")) {
				//System.out.println(e.getActionCommand() + "    " + displayBase());
				rescueSim.TextInfoPanel.setText(displayBase());
			}
			// rescueSim.TextInfoPanel.setText(buttonap.getActionCommand());
		} else if ((rescueSim.UnitPanel).isAncestorOf(buttonap)) {
			for (int i = 0; i < emergencyUnits.size(); i++) {
				if (emergencyUnits.get(i).toString().equals(buttonap.getActionCommand())) {
					rescueSim.TextInfoPanel.setText(emergencyUnits.get(i).display());
				}
			}
		}

		Last = e;
		this.rescueSim.revalidate();
	}

	public void updatethisthing() {
		for (int i = 0; i < buildings.size(); i++) {
			if (buildings.get(i).getStructuralIntegrity() == 0) {
				JButton X = new JButton();
				X.setForeground(Color.BLUE);
				X.setOpaque(false);
				X.setContentAreaFilled(false);
				X.setActionCommand(
						gridButtons[buildings.get(i).getLocation().getX()][buildings.get(i).getLocation().getY()]
								.getActionCommand());
				X.addActionListener(this);
				X.setIcon(new ImageIcon("c:/Java_Dev/GridShit/BilDis.png"));
				gridButtons[buildings.get(i).getLocation().getX()][buildings.get(i).getLocation().getY()] = X;
			}
		}
		for (int i = 0; i < citizens.size(); i++) {
			if (citizens.get(i).getHp() == 0 && !inbil(citizens.get(i))) {
				JButton X = new JButton();
				X.setForeground(Color.BLUE);
				X.setOpaque(false);
				X.setContentAreaFilled(false);
				X.setActionCommand(
						gridButtons[citizens.get(i).getLocation().getX()][citizens.get(i).getLocation().getY()]
								.getActionCommand());
				X.addActionListener(this);
				X.setIcon(new ImageIcon("c:/Java_Dev/GridShit/DeadCit.png"));
				gridButtons[citizens.get(i).getLocation().getX()][citizens.get(i).getLocation().getY()] = X;
			}
		}
	}

	public void re() {
		this.rescueSim.revalidate();
		this.rescueSim.repaint();
		this.rescueSim.revalidate();
	}

	public String displayBase() {
		String s = "";
		for (int i = 0; i < citizens.size(); i++) {
			if (citizens.get(i).getLocation().getX() == 0 && citizens.get(i).getLocation().getY() == 0) {
				s += "Occupants :  " + citizens.get(i).display() + "\n \n";
			}
		}
		for (int i = 0; i < emergencyUnits.size(); i++) {
			if (emergencyUnits.get(i).getLocation().getX() == 0 && emergencyUnits.get(i).getLocation().getY() == 0) {
				s += emergencyUnits.get(i).display() + "\n \n";
			}
		}
		return s;
	}

	public void log(int c) {
		if (this.deadPeople.size() != c) {
			log = "Cycle : " + this.engine.getCurrentCycle() + "\n" + "Disaster : "
					+ engine.getExecutedDisasters().get(engine.getExecutedDisasters().size() - 1).getName() + "\n"
					+ "Target : \n---------- \n"
					+ engine.getExecutedDisasters().get(engine.getExecutedDisasters().size() - 1).getTarget()
							.displayTarget()
					+ "\n" + "Dead People : " + "\n \n" + this.displayDeadInfo() + "============================== \n"
					+ log;
		} else if (this.engine.getExecutedDisasters().size() != 0)
			log = "Cycle : " + this.engine.getCurrentCycle() + "\n" + "Disaster : "
					+ engine.getExecutedDisasters().get(engine.getExecutedDisasters().size() - 1).getName() + "\n"
					+ "Target : \n---------- \n" + engine.getExecutedDisasters()
							.get(engine.getExecutedDisasters().size() - 1).getTarget().displayTarget()
					+ "\n" + "============================== \n" + log;

		else
			log = "Cycle : " + this.engine.getCurrentCycle() + "\n" + "No Disaster"
					+ "\n============================== \n" + log;
		this.rescueSim.LogsPanel.setText(log);
	}

	public String displayDeadInfo() {
		String l = "";
		for (int L = oldCauslities; L < deadPeople.size(); L++) {
			l += this.deadPeople.get(L).getName() + "\n" + "Location :- (" + this.deadPeople.get(L).getLocation().getX()
					+ "," + this.deadPeople.get(L).getLocation().getY() + ") \n";
		}
		return l;
	}

	public void displayCasulities(int c) {
		Causlities = c;
		oldCauslities = c;
		for (int i = 0; i < citizens.size(); i++) {
			if (citizens.get(i).getState() == CitizenState.DECEASED && !this.deadPeople.contains(citizens.get(i))) {
				Causlities++;
				this.deadPeople.add(citizens.get(i));
			}
		}
	}

	public void updatter() {
		for (int i = 0; rescueSim.RespondingUnitPanel.getComponents().length > i; i++) {
			for (int j = 0; j < emergencyUnits.size(); j++) {
				if (emergencyUnits.get(j).getState().equals(UnitState.TREATING)) {
					if ((rescueSim.RespondingUnitPanel.getComponents().length) > 0
							&& ((JButton) rescueSim.RespondingUnitPanel.getComponent(i)).getActionCommand()
									.equals(emergencyUnits.get(j).toString())) {
						JButton c = (((JButton) rescueSim.RespondingUnitPanel.getComponent(i)));
						rescueSim.TreatingUnitPanel.add(c);
						rescueSim.RespondingUnitPanel.remove(c);
						// i--;
						this.rescueSim.RespondingUnitPanel.revalidate();
						this.rescueSim.RespondingUnitPanel.repaint();
					}
				} else if (emergencyUnits.get(j).getState().equals(UnitState.IDLE)) {
					if ((rescueSim.RespondingUnitPanel.getComponents().length) > 0
							&& ((JButton) rescueSim.RespondingUnitPanel.getComponent(i)).getActionCommand()
									.equals(emergencyUnits.get(j).toString())) {
						JButton c = (((JButton) rescueSim.RespondingUnitPanel.getComponent(i)));
						rescueSim.AvilableUnitPanel.add(c);
						rescueSim.RespondingUnitPanel.remove(c);
						// i--;
						this.rescueSim.RespondingUnitPanel.revalidate();
						this.rescueSim.RespondingUnitPanel.repaint();
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		new CommandCenter();

	}

	public void updateDis() throws IOException {
		ArrayList<Disaster> RET = cycledisasters();
		for (int i = 0; i < RET.size(); i++) {
			if (RET.get(i).getTarget() instanceof ResidentialBuilding) {
				JButton tete = new JButton();
				tete.setActionCommand(gridButtons[RET.get(i).getTarget().getLocation().getX()][RET.get(i).getTarget()
						.getLocation().getY()].getActionCommand());
				tete.addActionListener(this);
				gridButtons[RET.get(i).getTarget().getLocation().getX()][RET.get(i).getTarget().getLocation()
						.getY()] = tete;
				rebuildbil((ResidentialBuilding) RET.get(i).getTarget(),
						gridButtons[RET.get(i).getTarget().getLocation().getX()][RET.get(i).getTarget().getLocation()
								.getY()]);
			} else if (RET.get(i).getTarget() instanceof Citizen) {
				JButton tete = new JButton();
				tete.setActionCommand(gridButtons[RET.get(i).getTarget().getLocation().getX()][RET.get(i).getTarget()
						.getLocation().getY()].getActionCommand());
				tete.addActionListener(this);
				gridButtons[RET.get(i).getTarget().getLocation().getX()][RET.get(i).getTarget().getLocation()
						.getY()] = tete;
				rebuildbil((Citizen) RET.get(i).getTarget(),
						gridButtons[RET.get(i).getTarget().getLocation().getX()][RET.get(i).getTarget().getLocation()
								.getY()]);
			}
		}

	}

	public void updateAvailable() {
		for (int i = 0; rescueSim.TreatingUnitPanel.getComponents().length > i; i++) {
			for (int j = 0; j < emergencyUnits.size(); j++) {
				if (emergencyUnits.get(j).getState().equals(UnitState.IDLE)) {
					if ((rescueSim.TreatingUnitPanel.getComponents().length) > 0
							&& ((JButton) rescueSim.TreatingUnitPanel.getComponent(i)).getActionCommand()
									.equals(emergencyUnits.get(j).toString())) {
						JButton c = (((JButton) rescueSim.TreatingUnitPanel.getComponent(i)));
						rescueSim.AvilableUnitPanel.add(c);
						rescueSim.TreatingUnitPanel.remove(c);
						i--;
						this.rescueSim.TreatingUnitPanel.revalidate();
						this.rescueSim.TreatingUnitPanel.repaint();
					}
				}
			}
		}
	}

	public void rebuildbil(Rescuable X, JButton K) throws IOException {

		if (X instanceof ResidentialBuilding) {
			if (X.getDisaster().toString().contains("Leak")) {
				//System.out.println("leak");

				K.setIcon(new ImageIcon(RescueSim.rebuildbil(((ResidentialBuilding) X).getOccupants().size(), "leak")));
			} else if (X.getDisaster().toString().contains("Coll")) {
				//System.out.println("Col");

				K.setIcon(new ImageIcon(
						RescueSim.rebuildbil(((ResidentialBuilding) X).getOccupants().size(), "colapse")));
			} else if (X.getDisaster().toString().contains("Fire")) {
				//System.out.println("Fire");

				K.setIcon(new ImageIcon(RescueSim.rebuildbil(((ResidentialBuilding) X).getOccupants().size(), "fire")));
			}
		} else if (X instanceof Citizen) {
			if (X.getDisaster().toString().contains("Inj")) {
				//System.out.println("Inj");

				K.setIcon(new ImageIcon(RescueSim.rebuildcit("inj")));
			} else if (X.getDisaster().toString().contains("Inf")) {
				//System.out.println("Inf");

				K.setIcon(new ImageIcon(RescueSim.rebuildcit("inf")));
			}
		}
	}

	public ArrayList<Disaster> cycledisasters() {
		ArrayList<Disaster> RET = new ArrayList<Disaster>();
		for (int j = 0; j < engine.getExecutedDisasters().size() && !engine.getExecutedDisasters().isEmpty(); j++) {
			// System.out.println(engine.getExecutedDisasters()+" "+j);
			if (engine.getExecutedDisasters().get(j).getStartCycle() == engine.getCurrentCycle()) {
				RET.add(engine.getExecutedDisasters().get(j));
			}
		}
		return RET;
	}

	public static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.WARNING_MESSAGE);

	}

	
	
	
	
	
	public String JARVISLOGIC() 
	{
		String Suggestions = "";
		String TEMP = "";
		int min =100;
		for (int i = 0; i < engine.emergencyUnits.size() ; i++) 
		{
			if(engine.emergencyUnits.get(i).getState().equals(UnitState.IDLE))
					{
						//System.out.println(engine.getExecutedDisasters().toString());
						for (int j = 0; j < engine.getExecutedDisasters().size(); j++) 
						{
							//System.out.println(engine.getExecutedDisasters().get(j)+" "+engine.getExecutedDisasters().get(j).toString()+" "+(engine.emergencyUnits.get(i).getdisastername().toString()));
							if(!engine.getExecutedDisasters().get(j).getHasaunit() && engine.getExecutedDisasters().get(j).toString().contains(engine.emergencyUnits.get(i).getdisastername().toString()) && engine.getExecutedDisasters().get(j).getTarget().isRespondable()&&engine.getExecutedDisasters().get(j).isActive()) 
							{
								//System.err.println(engine.getExecutedDisasters().get(j)+"   " + engine.getExecutedDisasters().get(j));
								//TEMP = "";
								

								if(min>engine.getExecutedDisasters().get(j).getTarget().getLocation().getX() +engine.getExecutedDisasters().get(j).getTarget().getLocation().getY()) 
								{

									TEMP=engine.emergencyUnits.get(i).getName().toString() +" better treat the "+ engine.getExecutedDisasters().get(j).getName().toString().toLowerCase()+" at " + engine.getExecutedDisasters().get(j).getTarget().getLocation().getX() +" and "+engine.getExecutedDisasters().get(j).getTarget().getLocation().getY();
									min=engine.getExecutedDisasters().get(j).getTarget().getLocation().getX() +engine.getExecutedDisasters().get(j).getTarget().getLocation().getY();
								}
								
							}
						}
						//System.out.println("min")
						//System.out.println("AsdasdasdsadAS");
						if (TEMP.toString().length()>20)
							Suggestions= Suggestions+"\n"+TEMP ;
						TEMP="";
						min =100;
						
					}

		}
		if (Suggestions.equals(""))
		{
			Suggestions="No actions needed go to the next cycle";
		}
		return  Suggestions;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
