package frc.robot.Auto.Actions;

import frc.robot.Auto.Auto;

public class ClawAction extends AutoAction {
    int speed;
    Double time;
    Double storedTime;
    boolean isTimerRunning;
    public ClawAction(int speed, Double time) {
        this.speed = speed;
        this.time = time;
    }
    public boolean Act() {
        if (!isTimerRunning) {
            isTimerRunning = true;
            storedTime = (double) System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - storedTime > time) {
            return true;
        }
        if (speed == -1) {
            Auto.Utils.gamepieceController.reverseClaw();
        } else if (speed == 0) {
            Auto.Utils.gamepieceController.stopClaw();
        } else if (speed == 1) {
            Auto.Utils.gamepieceController.moveClaw();
        }
        return false;
    }
}
