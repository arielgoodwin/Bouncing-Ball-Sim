// import statements used for assigning color and creating graphics
import java.awt.Color;
import acm.graphics.*;

/**
 * @author arielgoodwin
 * This class is used as a template for creating an arbitrary amount of ball
 * objects. 
 */

// class declaration, ensuring to extend Thread so that our balls can run concurrently with main method
public class bBall extends Thread {

	// constants related to the simulation of the ball
	private static final double g = 9.8;           // MKS acceleration due to gravity near Earth'surface, 9.8m/s^2
	private static final double Pi = 3.141592654;  // to convert degrees to radians
	private static final double TICK = 0.1;        // clock tick duration (sec)
	private static final double ETHR = 0.01;       // when Voy < ETHR * Vo, STOP
	private static final int HEIGHT = 600;         // distance from top of screen to ground plane
	private static final int SCALE = HEIGHT / 100; // ratio of pixels/metre

	// instance variables of bBall class
	private double Xi;       // initial x-position of the ball
	private double Yi;       // initial y-position of the ball
	private double Vo;       // initial speed of the ball (m/s)
	private double theta;    // launch angle of the ball (degrees)
	private double bSize;    // radius of the ball (metres)
	private Color bColor;    // color of the ball
	private double bLoss;    // energy loss factor of the ball
	private GOval ball;      // ball object
	private double Vx;		 // horizontal velocity of the ball
	private double Vy;       // vertical velocity of the ball
	private boolean running; // tells whether ball is still bouncing or not

	/**
	 * The constructor specifies the parameters for simulation. They are
	 * 
	 * @param Xi     double The initial X position of the center of the ball
	 * @param Yi     double The initial Y position of the center of the ball
	 * @param Vo     double The initial velocity of the ball at launch
	 * @param theta  double Launch angle (with the horizontal plane)
	 * @param bSize  double The radius of the ball in simulation units
	 * @param bColor Color The initial color of the ball
	 * @param bLoss  double Fraction [0,1] of the energy lost on each bounce
	 * @param running boolean Tells whether or not the ball is still bouncing
	 */

	public bBall(double Xi, double Yi, double Vo, double theta, double bSize, Color bColor, double bLoss, boolean running) {

		// get simulation parameters
		this.Xi = Xi;
		this.Yi = Yi;
		this.Vo = Vo;
		this.theta = theta;
		this.bSize = bSize;
		this.bColor = bColor;
		this.bLoss = bLoss;
		this.running = running;
		
		// constructing the ball using the given parameters
		this.ball = new GOval(this.Xi, this.Yi, this.bSize * SCALE * 2, this.bSize * SCALE * 2);
		this.ball.setFilled(true);
		this.ball.setFillColor(this.bColor);
	}
	
	/**
	 * getSize
	 * 
	 * Getter method to access a given ball size
	 * @return The size of the ball in simulation units
	 */
	public double getSize() {
		return this.bSize;
	}
	
	/**
	 * getBall
	 * 
	 * Getter method to access a given ball object's graphical representation
	 * @return The GOval representing a given bBall
	 */	
	public GOval getBall() {
		return this.ball;
	}
	
	/**
	 * runStatus
	 * 
	 * Getter method to access a ball's action status
	 * @return Whether or not the ball is still bouncing
	 */
	public boolean runStatus() {
		return this.running;
	}
	
	/**
	 * moveTo
	 * 
	 * Method for setting the location of a given ball
	 * @param x Horizontal coordinate of the ball in screen units
	 * @param y Vertical coordinate of the ball in screen units
	 */
	public void moveTo(double x, double y) {
		ball.setLocation(x, y);
	}
	
	/**
	 * Once the
	 * start method is called on the bBall instance, the code in the run method is
	 * executed concurrently with the main program.
	 * 
	 * @param void
	 * @return void
	 */
	public void run() {

		double time_g = 0; 					          // global time variable
		double time_l = 0; 					          // local time variable

		double ScrX; 						          // x-coordinate of the ball in screen units
		double ScrY; 						          // y-coordinate of the ball in screen units

		Vx = Vo * Math.cos(theta * Pi / 180);  // constant x-component of velocity
		double Voy = Vo * Math.sin(theta * Pi / 180); // initial y-component of velocity
		double X; 							          // x-coordinate of the ball in simulation units
		double Y; 							          // y-coordinate of the ball in simulation units

		while (true) {

			X = this.Xi + Vx * time_g;                              // equation describing horizontal motion of the ball
			Y = this.Yi + Voy * time_l - 0.5 * g * time_l * time_l; // equation describing vertical motion of the ball
			Vy = Voy - g * time_l;                             // equation describing the y-component of the ball's velocity

			// updating the time variables with each iteration of the loop
			time_g = time_g + TICK;
			time_l = time_l + TICK;

			// an if statement to identify collisions with the ground plane
			// activates if ball is traveling downwards and if its height is less than or
			// equal to its radius
			if (Vy < 0 && Y <= this.bSize) {

				// adjusting initial y-velocity after each bounce to account for energy loss
				Voy = Voy * Math.sqrt(1 - this.bLoss);

				// resetting the local time variable
				time_l = 0;

				// re-initializing the ball's initial height as it bounces
				Y = this.bSize;
				Yi = this.bSize;

			}

			// converting from simulation units to screen units
			ScrX = (int) ((X - this.bSize) * SCALE);
			ScrY = (int) (HEIGHT - (Y + this.bSize) * SCALE);

			// updating the ball's location with each iteration of the loop
			ball.setLocation(ScrX, ScrY);

			// an if statement that causes the program to end if the ball's initial
			// y-velocity is less than 1% of its original
			if (Voy <= (this.Vo * ETHR)) {
				this.running = false; // set the ball's running status to false
				break;

			}

			// pausing the loop to create the fluid animation of the ball's motion

			try {
				// pause for 50 milliseconds
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}

	}

}
