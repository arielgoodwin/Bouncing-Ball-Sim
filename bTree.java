// import statement used for creating/editing graphics
import acm.program.*;

/**
 * Implements a B-Tree class using a non-recursive algorithm.
 * 
 * @author arielgoodwin
 * 
 * This class outlines a data structure, a binary tree, that is
 * useful in storing and sorting data. One such method for retrieving
 * the data organized within the tree is to sort in order from least
 * to greatest. This method will be implemented here to organize the
 * bBall objects stored in the tree by their size.
 *
 */

// class declaration, ensuring to extend GraphicsProgram
public class bTree extends GraphicsProgram{
	
	// instance variables
	private bNode root = null;						  // sets the root of the tree as null
	private int count = 0;							  // counter used to keep track of running balls
	private int lastSize = 0;						  // stores the size of the last ball checked in the sortOrder method
	private double x = 0;							  // initializing location variable for the sortOrder method
	private static final int HEIGHT = 600;            // distance from top of screen to ground plane
	private static final double SCALE = HEIGHT / 100; // scale factor between screen coordinates and pixel coordinates
	private boolean isEmpty = true;                   // whether or not the tree has any nodes

	/**
	 * addNode method - adds a new node by descending to the leaf node using a while
	 * loop in place of recursion.
	 * 
	 * 
	 * Edited to store bBall objects in the nodes, sorting according to the size
	 * of each ball.
	 * 
	 * @param iBall A bBall object
	 * @return void
	 */	
	public void addNode(bBall iBall) {
		
		this.isEmpty = false;
		
		// initialize new node as current
		bNode current;
		
		// empty tree case
		if (root == null) {
			root = makeNode(iBall);
		}

		// if not empty, descend to the leaf node according to
		// the input data.
		else {
			current = root;
			while (true) {
				
				if (iBall.getSize() < current.data.getSize()) {

					// new data < data at node, branch left

					if (current.left == null) { // empty leaf node case
						current.left = makeNode(iBall); // attach new node here
						break;
						
					} else { // otherwise
						current = current.left; // keep traversing
					}
					
				} else {
					
					// new data >= data at node, branch right

					if (current.right == null) { // empty leaf node case
						current.right = makeNode(iBall); // attach new node here
						break;
						
					} else { // otherwise
						current = current.right; // keep traversing
					}
				}
			}
		}
	}
	
	/**
	 * sortOrder
	 * 
	 * This method is based on the in-order traversal routine, and serves to
	 * organize the contents (i.e. bBalls) of the tree with respect to their size.
	 * It follows the LEFT-ROOT-RIGHT pattern, and operates recursively. 
	 * 
	 * When it encounters balls of a unique size (based on the truncated integer portion of their bSize
	 * characteristic) it moves them to the left side of the screen, to the right of any
	 * previously moved balls, and removes all balls of the same truncated size from the display. 
	 * It also stops the thread of every ball that it encounters.
	 * 
	 * @param root The root node of the binary tree
	 * @return void
	 */	
	public void sortOrder(bNode root) {
		
		// if the node to the left of the root is not empty
		if (root.left != null)
			
			// traverse left
			sortOrder(root.left);
			
		// if the encountered ball is of a unique size, different from that of the previous ball
		if ((int) root.data.getSize() != lastSize) {
			
			// move to left side of screen
			root.data.stop();
			root.data.moveTo(x, (HEIGHT - (root.data.getSize() * SCALE * 2)));
			
			// set location variable for subsequent balls
			x += root.data.getSize() * SCALE * 2;
			
			// store the most recent ball size
			lastSize =  (int) root.data.getSize();
			
		} else { // ball is the same size as the balls before it
			
			// remove the ball from the display
			root.data.getBall().setVisible(false);
			root.data.stop();
		}
		
		// if the node to the right of the root is not empty
		if (root.right != null)
			
			// traverse right
			sortOrder(root.right);	
	} 
	
	/**
	 * moveSort
	 * 
	 * Simply calls the sortOrder method, hiding recursion from
	 * the user. Resets x-position of sorted balls to zero, and
	 * the lastSize variable to zero.
	 * 
	 * @param void
	 * @return void
	 * 
	 */	
	public void moveSort() {
		x = 0;
		sortOrder(root);
		lastSize = 0;
	} 
	
	/**
	 * isEmpty
	 * 
	 * getter method to determine if the tree has any nodes
	 * 
	 * @return whether the tree contains any nodes
	 */
	public boolean isEmpty() {
		return this.isEmpty;
	}
	
	/**
	 * haltScan
	 * 
	 * traverses the tree in the post-order traversal routine (LEFT-RIGHT-ROOT)
	 * stops the thread of any ball that it encounters
	 * 
	 * @param root
	 * @return void
	 */
	public void haltScan(bNode root) {
		
		// if the node to the left of the root is not empty
		if (root.left != null)
					
			// traverse left
			haltScan(root.left);
				
		// if the node to the right of the root is not empty
		if (root.right != null)
					
			// traverse right
			haltScan(root.right);
		
		root.data.stop();
	}
	
	/**
	 * stopSim
	 * 
	 * simply calls the haltScan method on the root of a given tree
	 * hides recursion from the user
	 * 
	 * @param void
	 * @return void
	 */
	public void stopSim() {
		haltScan(root);
	}
	
	/**
	 * makeNode
	 *
	 * Creates a single instance of a bNode.
	 *
	 * @param iBall bBall object to be added
	 * @return bNode node Node created
	 */
	private bNode makeNode(bBall iBall) {
		
		bNode node = new bNode(); // create new object
		node.data = iBall;        // initialize data field
		node.left = null;         // set left successor to null
		node.right = null;        // set right successor to null
		return node;              // return handle to new object
	}
}

/**
 * A simple bNode class for use by bTree. The "payload" can be modified
 * accordingly to support any object type.
 * 
 * In this case the "payload" has been modified to support bBall objects.
 *
 */
class bNode {
	
	// instance variables
	bBall data; 
	bNode left;
	bNode right;
}
