// import statements used for assigning color and building GUI elements
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import acm.gui.TableLayout;

/**
 * A sliderBox class to facilitate the creation of sliders for the parameters
 * of our simulation. 
 * 
 * @author arielgoodwin
 * 
 * This class creates two constructors for building "sliderBoxes" which encapsulate a slider
 * with minimum, maximum, and default labels. sliderBoxes are employed in the bSim class to
 * allow the user to control various parameters of the generated balls.
 * It also has a few getter methods to pass along information from the private instance variables.
 * 
 */

// class declaration, ensuring to extend JPanel
public class sliderBox extends JPanel{
	
	// instance variables
	private JLabel nameLabel;
	private JLabel minLabel;
	private JLabel maxLabel;
	private JLabel defLabel;
	private JSlider mySlider;
	
	// constructor for integer parameters
	public sliderBox(String name, int min, int max, int dValue) {
		
		// establish TableLayout and add label for the type of slider
		this.setLayout(new TableLayout(1,5));
		nameLabel = new JLabel(name);
		this.add(nameLabel, "width = 100");
		
		// add label for minimum value
		minLabel = new JLabel(min +"");
		this.add(minLabel, "width = 25");
		
		// add slider with given min, max, and default values
		mySlider = new JSlider(min, max, dValue);
		this.add(mySlider, "width = 150");
		
		// add label for maximum value
		maxLabel = new JLabel(max +"");
		this.add(maxLabel, "width = 80");
		
		// add label for current (default) value
		defLabel = new JLabel(dValue + "");
		defLabel.setForeground(Color.blue);
		this.add(defLabel, "width = 40");		
	}
	
	// constructor for double parameters
	public sliderBox(String name, double min, double max, double dValue) {
		
		// establish TableLayout and add label for the type of slider
		this.setLayout(new TableLayout(1,5));
		nameLabel = new JLabel(name);
		this.add(nameLabel, "width = 100");
		
		// add label for minimum value
		minLabel = new JLabel(min +"");
		this.add(minLabel, "width = 25");
		
		// add slider with given min, max, and default values
		mySlider = new JSlider((int) (min * 10), (int) (max * 10), (int) (dValue * 10));
		this.add(mySlider, "width = 150");
		
		// add label for maximum value
		maxLabel = new JLabel(max +"");
		this.add(maxLabel, "width = 80");
		
		// add label for current (default) value
		defLabel = new JLabel(dValue + "");
		defLabel.setForeground(Color.blue);
		this.add(defLabel, "width = 40");
			
	}
	
	/**
	 * getSlider
	 * 
	 * getter method to access the slider component
	 * 
	 * @return the slider of a given sliderBox
	 */
	public JSlider getSlider() {
		return this.mySlider;
	}
	
	/**
	 * getLabel
	 * 
	 * getter method to access the current value label of a sliderBox
	 * 
	 * @return the current value label of a given sliderBox
	 */
	public JLabel getLabel() {
		return this.defLabel;
	}
	
	/**
	 * getValue
	 * 
	 * getter method to access the current value of a slider
	 * 
	 * @return the current value of the slider of a given sliderBox
	 */
	public int getValue() {
		return mySlider.getValue();
	}
}
