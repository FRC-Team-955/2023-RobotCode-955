package frc.robot.Actions;

public class ClawAction implements AutoAction {
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
            gamepieceController.reverseClaw();
        } else if (speed == 0) {
            gamepieceController.stopClaw();
        } else if (speed == 1) {
            gamepieceController.moveClaw();
        }
        return false;
    }
}
