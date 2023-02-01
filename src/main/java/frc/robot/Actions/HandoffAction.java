package frc.robot.Actions;

import edu.wpi.first.wpilibj.Timer;

public class HandoffAction implements AutoAction {
    int speed;
    Timer timer;
    Double time;

    public HandoffAction(int speed, Double time) {
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
            gamepieceManager.moveHandoff(speed);
        }
        return false;
    }
}
