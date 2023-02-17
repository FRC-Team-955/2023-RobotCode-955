package frc.robot.Auto.Actions;

import edu.wpi.first.wpilibj.Timer;

public class PauseAction extends AutoAction {
    double time;
    Timer timer;

    public PauseAction(double time) {
        this.time = time;
        timer = new Timer();
    }

    public boolean Act() {
        timer.start();
        if (timer.hasElapsed(time)) {
            return true;
        } else {
            return false;
        }
    }
}
