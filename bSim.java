// import statements used for assigning color, creating graphics, and randomizing parameters
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import acm.graphics.*;
import acm.gui.TableLayout;
import acm.program.*;
import acm.util.RandomGenerator;

/**
 * @author arielgoodwin
 * This class is used to create a preset number of balls with random
 * parameters. 
 * 
 */

// class declaration, ensuring to extend GraphicsProgram 
// and implement the two types of listeners used
public class bSim extends GraphicsProgram implements ChangeListener, ItemListener {

	// constants related to screen and ball parameters
	private static final int WIDTH = 1440;            // defines the width of the screen in pixels
	private static final int HEIGHT = 600;            // distance from top of screen to ground plane
	private static final int OFFSET = 200;            // distance from bottom of screen to ground plane
	private static final double SCALE = HEIGHT / 100; // scale factor between screen coordinates and pixel coordinates
	
	// instantiating sliderBox objects
	private sliderBox ballNumber;
	private sliderBox ballMinSpeed;
	private sliderBox ballMaxSpeed;
	private sliderBox ballMinSize;
	private sliderBox ballMaxSize;
	private sliderBox ballMinAngle;
	private sliderBox ballMaxAngle;
	private sliderBox ballMinLoss;
	private sliderBox ballMaxLoss;
	
	// instantiating ComboBox objects
	private JComboBox menu;
	private JComboBox file;
	private JComboBox edit;
	private JComboBox help;
	
	// instantiating the side panel that houses the sliderBoxes
	private JPanel sidePanel;
	
	// instantiating the RandomGenerator object
	private RandomGenerator rndm = RandomGenerator.getInstance();
	
	// instantiating the binary tree used for sorting balls
	private bTree myTree = new bTree();
	
	// variable to prevent/allow the simulation from running
	private boolean simEnable;
	
	/**
	 * init
	 * 
	 * initializes the applet GUI
	 * 
	 * @param void
	 * @return void
	 */
	public void init() {
		
		// instantiate the bTree 
		myTree = new bTree();
		
		// by default, simulation should not run
		simEnable = false;
		
		// initializing the window
		this.resize(WIDTH, HEIGHT + OFFSET);

		// creating the line that the ball bounces on
		GRect floor = new GRect(0, HEIGHT, WIDTH, 3);
		floor.setFilled(true);
		floor.setFillColor(Color.BLACK);
		add(floor);
		
		// constructing sliderBoxes for each parameter
		ballNumber = new sliderBox("NUMBALLS:", 1, 255, 10);
		ballMinSize = new sliderBox("MIN SIZE:", 1.0, 25.0, 4.0);
		ballMaxSize = new sliderBox("MAX SIZE:", 1.0, 25.0, 10.0);
		ballMinSpeed = new sliderBox("MIN SPEED:", 1.0, 200.0, 25.0);
		ballMaxSpeed = new sliderBox("MAX SPEED:", 1.0, 200.0, 45.0);
		ballMinAngle = new sliderBox("TH MIN:", 1.0, 180.0, 80.0);
		ballMaxAngle = new sliderBox("TH MAX:", 1.0, 180.0, 100.0);
		ballMinLoss = new sliderBox("LOSS MIN:", 0.0, 1.0, 0.4);
		ballMaxLoss = new sliderBox("LOSS MAX:", 0.0, 1.0, 0.6);
		
		// constructing ComboBoxes at top of the screen
		// this first ComboBox is functional, while the next three after it are purely aesthetic
		menu = new JComboBox<String>();
		menu.addItem("bSim");
		menu.addItem("Run");
		menu.addItem("Hist");
		menu.addItem("Clear");
		menu.addItem("Stop");
		menu.addItem("Quit");
		add(menu, NORTH);
		
		// aesthetic
		file = new JComboBox<String>();
		file.addItem("File");
		add(file, NORTH);
		
		// aesthetic
		edit = new JComboBox<String>();
		edit.addItem("Edit");
		add(edit, NORTH);
		
		// aesthetic
		help = new JComboBox<String>();
		help.addItem("Help");
		add(help, NORTH);
		
		// constructing the sidePanel that contains the sliderBoxes 
		// and adding it to the right side of the screen
		sidePanel = new JPanel(new TableLayout(10, 1));
		sidePanel.add(ballNumber);
		sidePanel.add(ballMinSize);
		sidePanel.add(ballMaxSize);
		sidePanel.add(ballMinSpeed);
		sidePanel.add(ballMaxSpeed);
		sidePanel.add(ballMinAngle);
		sidePanel.add(ballMaxAngle);
		sidePanel.add(ballMinLoss);
		sidePanel.add(ballMaxLoss);
		add(sidePanel, EAST);
		
		// adding listeners for all of the sliderBoxes so that they can 
		// respond to user-generated events
		ballNumber.getSlider().addChangeListener((ChangeListener) this);
		ballMinSize.getSlider().addChangeListener((ChangeListener) this);
		ballMaxSize.getSlider().addChangeListener((ChangeListener) this);
		ballMinSpeed.getSlider().addChangeListener((ChangeListener) this);
		ballMaxSpeed.getSlider().addChangeListener((ChangeListener) this);
		ballMinAngle.getSlider().addChangeListener((ChangeListener) this);
		ballMaxAngle.getSlider().addChangeListener((ChangeListener) this);
		ballMinLoss.getSlider().addChangeListener((ChangeListener) this);
		ballMaxLoss.getSlider().addChangeListener((ChangeListener) this);
		
		// adding listeners to the functional ComboBox so that
		// it can respond to user-generated events
		menu.addItemListener((ItemListener) this);	
	}
	
	/**
	 * run
	 * 
	 * checks if simEnable is true, allowing the simulation to run if it is
	 * 
	 * @param void
	 * @return void
	 */
	public void run() {
		
		while(true) {
			pause(200);
			if(simEnable) {
				
				// run simulation
				doSim();
				
				// reset ComboBox and simEnable status
				menu.setSelectedIndex(0);
				simEnable = false;
			}
		}
	}

	@Override
	/**
	 * itemStateChanged
	 * 
	 * listens for events (exceptions) to trigger specific responses from the program
	 * this method deals with events for the ComboBox
	 * 
	 * @param e
	 * @return void
	 */
	public void itemStateChanged(ItemEvent e) {
		
		// if event is from the menu ComboBox
		if (e.getSource() == menu) {
			
			// if menu has been selected to "Run"
			if (menu.getSelectedIndex() == 1) {
				
				// run simulation
				simEnable = true;
				
			// if menu has been selected to "Hist" and the tree is not empty
			} else if (menu.getSelectedIndex() == 2 && !myTree.isEmpty()) {
				
				// sort balls
				myTree.moveSort();
				
			// if menu has been selected to "Clear" and the tree is not empty
			} else if (menu.getSelectedIndex() == 3 && !myTree.isEmpty()) {
				
				// stop the ball threads
				myTree.stopSim();
				
				// re-initialize bTree
				myTree = new bTree();
				
				// remove the balls from the display
				removeAll();
				
				// recreate the line that the balls bounce on
				GRect floor = new GRect(0, HEIGHT, WIDTH, 3);
				floor.setFilled(true);
				floor.setFillColor(Color.BLACK);
				add(floor);
			
			// if menu has been selected to "Stop" and the tree is not empty
			} else if (menu.getSelectedIndex() == 4 && !myTree.isEmpty()) {
				
				// stop the ball threads
				myTree.stopSim();
			
			// if menu has been selected to "Quit"
			} else if (menu.getSelectedIndex() == 5) {
				
				// stop the program
				System.exit(0);	
			}		
		} 	
	}

	
		
	/**
	 * doSim
	 * 
	 * carries out the simulation of the ball motion and
	 * stores the balls in a binary tree for sorting/manipulation.
	 * uses the RandomGenerator to assign random characteristics to each ball
	 * the bounds on these random numbers are assigned by the user interacting
	 * with the sliderBoxes
	 * 
	 * @param void
	 * @return void
	 */
	public void doSim() {
		
		// initializing the window
		this.resize(WIDTH, HEIGHT + OFFSET);

		// creating the line that the balls bounce on
		GRect floor = new GRect(0, HEIGHT, WIDTH, 3);
		floor.setFilled(true);
		floor.setFillColor(Color.BLACK);
		add(floor);

		// variable to keep track of the number of balls generated
		int counter = 0;

		// loop to generate randomized balls
		while (true) {

			// setting randomized variables for all the necessary parameters of bBall constructor
			// and setting its run status to 'true'
			// the bounds on the bBall parameters are taken from the current value of
			// their respective sliderBox
			int NUMBALLS = ballNumber.getSlider().getValue();
			double iSize = rndm.nextDouble(ballMinSize.getSlider().getValue() / 10.0, ballMaxSize.getSlider().getValue() / 10.0);
			double iLoss = rndm.nextDouble(ballMinLoss.getSlider().getValue() / 10.0, ballMaxLoss.getSlider().getValue() / 10.0);
			double iVelo = rndm.nextDouble(ballMinSpeed.getSlider().getValue() / 10.0, ballMaxSpeed.getSlider().getValue() / 10.0);
			double iTheta = rndm.nextDouble(ballMinAngle.getSlider().getValue() / 10.0, ballMaxAngle.getSlider().getValue() / 10.0);
			Color iColor = rndm.nextColor();
			double Xi = (WIDTH - sidePanel.getWidth())/ (2 * SCALE); // accounts for the width of the sidePanel
			double Yi = iSize;
			boolean iRunning = true;

			// instantiating bBall object
			bBall myBall = new bBall(Xi, Yi, iVelo, iTheta, iSize, iColor, iLoss, iRunning);
			add(myBall.getBall());
			
			// adding the bBall object to the binary tree
			myTree.addNode(myBall);
			
			// start the thread
			myBall.start();
			
			// incrementing the counter
			counter++;

			// breaks the while loop when the program has generated the specified number of balls
			if (counter == NUMBALLS)
				break;	
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}	
}
