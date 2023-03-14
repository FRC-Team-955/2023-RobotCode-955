package frc.robot.Auto.Actions.Deprecated;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Auto.Actions.AutoAction;

public class PauseAction extends AutoAction {
    Timer timer;
    double time;

    public PauseAction(double time) {
        this.time = time;
        timer = new Timer();
    }

    public boolean Act() {
        timer.start();
        return timer.hasElapsed(time);
    }
    
    public void Finish() {
        
    }
}
