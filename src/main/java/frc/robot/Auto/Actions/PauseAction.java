package frc.robot.Auto.Actions;

import edu.wpi.first.wpilibj.Timer;

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
