import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

class GraphicsManager {


    // size of the screen
    int WIDTH = 800;
    int HEIGHT = 640;

    JFrame frame;

    public GraphicsManager() {
	frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setTitle("Space Shooter");
	frame.setSize(WIDTH, HEIGHT);
	


	// makes the frame visible
	frame.setVisible(true);
    }
}
