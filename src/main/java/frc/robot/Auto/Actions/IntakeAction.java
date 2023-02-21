package frc.robot.Auto.Actions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.GamepieceManager;

public class IntakeAction extends AutoAction{
    double time;
    Timer timer;
    int speed;

    public IntakeAction(double time, int speed) {
        this.time = time;
        this.speed = speed;
        timer = new Timer();
    }

    public boolean Act() {
        timer.start();
        if (timer.hasElapsed(time)) {
            GamepieceManager.runFlapsAuto(speed);
            return true;
        }
        return false;
    }
    
    public void Finish() {
        
    }
}
