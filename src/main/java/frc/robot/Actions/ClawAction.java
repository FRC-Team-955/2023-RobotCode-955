package frc.robot.Actions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.GamepieceManager;

public class ClawAction implements AutoAction {
    int speed;
    Timer timer;
    Double time;

    public ClawAction(int speed, Double time) {
        this.speed = speed;
        this.time = time;
        timer = new Timer();
        timer.reset();
    }

    public boolean Act() {
        timer.start();
        if (timer.hasElapsed(time)) {
            return true;
        } else {
            GamepieceManager.moveClawAuto(speed);
        }
        return false;
    }
}
