package frc.robot.Actions;

import edu.wpi.first.wpilibj.Timer;

public class PauseAction implements AutoAction {
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
}
