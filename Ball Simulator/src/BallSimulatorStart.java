import com.rawad.ballsimulator.displaymanager.DisplayManager;
import com.rawad.ballsimulator.main.BallSimulator;

public class BallSimulatorStart {
	
	public static void main(String[] args) {
		
		BallSimulator.init();
		
		DisplayManager.setDisplayMode(DisplayManager.Mode.WINDOWED);
		
	}
	
}
