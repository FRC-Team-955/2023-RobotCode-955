package frc.robot.Auto.Actions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Auto.Auto;

//REWRITE

public class ClawAction extends AutoAction {
    int speed;
    Double time;
    Timer timer;

    public ClawAction(int speed, Double time) {
        this.speed = speed;
        this.time = time;
        timer = new Timer();
    }
    public boolean Act() {
        timer.start();
        if (timer.hasElapsed(time)) {
            
        }
        if (speed == -1) {
            Auto.Utils.gamepieceController.reverseClaw();
        } else if (speed == 0) {
            Auto.Utils.gamepieceController.stopClaw();
        } else if (speed == 1) {
            Auto.Utils.gamepieceController.moveClaw();
        }
    }
}
