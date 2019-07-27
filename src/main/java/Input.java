package src.main.java;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Input {

    // KeyEvents for controlling ship1
    private static int upke = KeyEvent.VK_UP;
    private static int downke = KeyEvent.VK_DOWN;
    private static int leftke = KeyEvent.VK_LEFT;
    private static int rightke = KeyEvent.VK_RIGHT;
    private static int fireke = KeyEvent.VK_SPACE;
    private static int special1ke = KeyEvent.VK_Q;
    private static int special2ke = KeyEvent.VK_W;
    private static int special3ke = KeyEvent.VK_E;
    // booleans for storing if a key is pressed
    static volatile boolean up = false;
    static volatile boolean down = false;
    static volatile boolean left = false;
    static volatile boolean right = false;
    static volatile boolean fire = false;
    static volatile boolean special1 = false;
    static volatile boolean special2 = false;
    static volatile boolean special3 = false;
    static volatile boolean isMouse1Pressed = false;
    static volatile boolean isMouse1Released = false;

    MouseListener mouseListener;

    public Input() {
	IsKeyPressed.makeKeyboardManager();

		mouseListener = new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {

			}

			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				isMouse1Pressed = true;
			}

			@Override
			public void mouseReleased(MouseEvent mouseEvent) {
				isMouse1Pressed = false;
				isMouse1Released = true;
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent) {

			}

			@Override
			public void mouseExited(MouseEvent mouseEvent) {

			}
		};

	    // Code from https://stackoverflow.com/questions/18037576/how-do-i-check-if-the-user-is-pressing-a-key
	    // creates input updater
	KeyboardFocusManager.getCurrentKeyboardFocusManager()
	    .addKeyEventDispatcher(new KeyEventDispatcher() {

		    @Override
		    public boolean dispatchKeyEvent(KeyEvent ke) {
			synchronized (IsKeyPressed.class) {

			    // switch-case like a cond!
			    switch (ke.getID()) {
			    case KeyEvent.KEY_PRESSED:
				// movement
				if (ke.getKeyCode() == upke) {
				    up = true;
				}
				else if (ke.getKeyCode() == downke) {
				    down = true;
				}
				else if (ke.getKeyCode() == leftke) {
				    left = true;
				}
				else if (ke.getKeyCode() == rightke) {
				    right = true;
				}
				// firing
				else if (ke.getKeyCode() == fireke){
				    fire = true;
				}
				else if (ke.getKeyCode() == special1ke) {
				    special1 = true;
				}
				else if (ke.getKeyCode() == special2ke) {
					special2 = true;
				}
				else if (ke.getKeyCode() == special3ke) {
					special3 = true;
				}

				break;
			    

			    case KeyEvent.KEY_RELEASED:
				// movement
				if (ke.getKeyCode() == upke) {
				    up = false;
				}
				else if (ke.getKeyCode() == downke) {
				    down = false;
				}
				else if (ke.getKeyCode() == leftke) {
				    left = false;
				}
				else if (ke.getKeyCode() == rightke) {
				    right = false;
				}
				// firing
				else if (ke.getKeyCode() == fireke){
				    fire = false;
				}
				else if (ke.getKeyCode() == special1ke) {
				    special1 = false;
				}
				else if (ke.getKeyCode() == special2ke) {
					special2 = false;
				}
				else if (ke.getKeyCode() == special3ke) {
					special3 = false;
				}

				break;
			    }
			}
			return false;
		    }
		}
		);
	
	
    }

    public boolean getIsMouse1Pressed() {
    	return isMouse1Pressed;
	}
	public boolean getIsMouse1Released() {
    	return isMouse1Released;
	}
	public void resetIsMouse1Released() {
    	isMouse1Released = false;
	}

	public MouseListener getMouseListener() {
		return mouseListener;
	}

}

