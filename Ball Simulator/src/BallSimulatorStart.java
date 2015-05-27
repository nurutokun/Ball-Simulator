import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.rawad.ballsimulator.input.KeyboardInput;
import com.rawad.ballsimulator.input.MouseInput;
import com.rawad.ballsimulator.log.Logger;

public class BallSimulatorStart {
	
	private static JFrame frame;
	private static JPanel panel;
	
	private static EventHandler l;
	
	public static void main(String[] args) {
		
		frame = new JFrame();
		panel = new JPanel();
		
		l = new EventHandler();
		
		frame.addKeyListener(l);
		
		panel.addMouseListener(l);
		panel.addMouseMotionListener(l);
		panel.addMouseWheelListener(l);
		
	}
	
	private static class EventHandler implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener {
		
		@Override
		public void keyPressed(KeyEvent e) {
			KeyboardInput.setKeyDown(e.getKeyCode(), true);
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			KeyboardInput.setKeyDown(e.getKeyCode(), false);
		}
		
		@Override
		public void keyTyped(KeyEvent e) {}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			MouseInput.setMouseWheelPosition(e.getUnitsToScroll());
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {}
		
		@Override
		public void mouseExited(MouseEvent e) {}
		
		@Override
		public void mousePressed(MouseEvent e) {
			
			if(e.isAltDown()) {
				
				Logger.log(Logger.DEBUG, "alt mouse pressed");
				
			} else if(e.isMetaDown()) {
				MouseInput.setButtonDown(MouseInput.RIGHT_MOUSE_BUTTON, true);
				
			} else {
				MouseInput.setButtonDown(MouseInput.LEFT_MOUSE_BUTTON, true);
				
			}
			
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			
			if(e.isAltDown()) {
				
				Logger.log(Logger.DEBUG, "alt mouse released");
				
			} else if(e.isMetaDown()) {
				MouseInput.setButtonDown(MouseInput.RIGHT_MOUSE_BUTTON, false);
				
			} else {
				MouseInput.setButtonDown(MouseInput.LEFT_MOUSE_BUTTON, false);
				
			}
			
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			mouseMoved(e);
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			
			MouseInput.setX(e.getX());
			MouseInput.setY(e.getY());
			
		}
		
	}
	
}
