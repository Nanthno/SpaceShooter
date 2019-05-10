// Code from https://stackoverflow.com/questions/18037576/how-do-i-check-if-the-user-is-pressing-a-key
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
public class IsKeyPressed {
    // KeyEvents for controlling ship1
    private static int up1ke = KeyEvent.VK_W;
    private static int down1ke = KeyEvent.VK_S;
    private static int left1ke = KeyEvent.VK_A;
    private static int right1ke = KeyEvent.VK_D;
    // booleans for storing if a key is pressed
    static volatile boolean up1 = false;
    static volatile boolean down1 = false;
    static volatile boolean left1 = false;
    static volatile boolean right1 = false;
     // KeyEvents for controlling ship2
    private static int up2ke = KeyEvent.VK_UP;
    private static int down2ke = KeyEvent.VK_DOWN;
    private static int left2ke = KeyEvent.VK_LEFT;
    private static int right2ke = KeyEvent.VK_RIGHT;
    // booleans for storing if a key is pressed
    static volatile boolean up2 = false;
    static volatile boolean down2 = false;
    static volatile boolean left2 = false;
    static volatile boolean right2 = false;
     /*
    private static volatile boolean wPressed = false;
    public static boolean isWPressed() {
        synchronized (IsKeyPressed.class) {
            return wPressed;
        }
	}*/
    public static boolean getUp1() {
	synchronized (IsKeyPressed.class) {
	    return up1;
	}
    }
    public static boolean getDown1() {
	synchronized (IsKeyPressed.class) {
	    return down1;
	}
    }
    public static boolean getLeft1() {
	synchronized (IsKeyPressed.class) {
	    return left1;
	}
    }
    public static boolean getRight1() {
	synchronized (IsKeyPressed.class) {
	    return right1;
	}
    }
     public static boolean getUp2() {
	synchronized (IsKeyPressed.class) {
	    return up2;
	}
    }
    public static boolean getDown2() {
	synchronized (IsKeyPressed.class) {
	    return down2;
	}
    }
    public static boolean getLeft2() {
	synchronized (IsKeyPressed.class) {
	    return left2;
	}
    }
    public static boolean getRight2() {
	synchronized (IsKeyPressed.class) {
	    return right2;
	}
    }
     public static void makeKeyboardManager() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
             @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                synchronized (IsKeyPressed.class) {
                    switch (ke.getID()) {
                    case KeyEvent.KEY_PRESSED:
                        if (ke.getKeyCode() == up1ke) {
                            up1 = true;
                        }
			else if (ke.getKeyCode() == down1ke) {
			    down1 = true;
			}
			else if (ke.getKeyCode() == left1ke) {
			    left1 = true;
			}
			else if (ke.getKeyCode() == right1ke) {
			    right1 = true;
			}
 			else if (ke.getKeyCode() == up2ke) {
                            up2 = true;
                        }
			else if (ke.getKeyCode() == down2ke) {
			    down2 = true;
			}
			else if (ke.getKeyCode() == left2ke) {
			    left2 = true;
			}
			else if (ke.getKeyCode() == right2ke) {
			    right2 = true;
			}
                        break;
                     case KeyEvent.KEY_RELEASED:
                        if (ke.getKeyCode() == up1ke) {
                            up1 = false;
                        }
			else if (ke.getKeyCode() == down1ke) {
			    down1 = false;
			}
			else if (ke.getKeyCode() == left1ke) {
			    left1 = false;
			}
			else if (ke.getKeyCode() == right1ke) {
			    right1 = false;
			}
 			else if (ke.getKeyCode() == up2ke) {
                            up2 = false;
                        }
			else if (ke.getKeyCode() == down2ke) {
			    down2 = false;
			}
			else if (ke.getKeyCode() == left2ke) {
			    left2 = false;
			}
			else if (ke.getKeyCode() == right2ke) {
			    right2 = false;
			}
                        break;
                    }
                    return false;
                }
            }
        });
    }
}
