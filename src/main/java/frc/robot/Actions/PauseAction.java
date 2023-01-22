package frc.robot.Actions;

public class PauseAction extends AutoAction {
    boolean isTimerRunning = false;
    double timer;
    double time;

    public PauseAction(double time) {
        this.time = time;
    }

    public boolean Act() {
        if (!isTimerRunning) {
            timer = System.currentTimeMillis();
            isTimerRunning = true;
        }
        if (System.currentTimeMillis() - timer < time) {
            return true;
        } else {
            return false;
        }
    }
}
