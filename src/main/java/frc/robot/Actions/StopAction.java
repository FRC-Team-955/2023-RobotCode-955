package frc.robot.Actions;

import frc.robot.Auto;

public class StopAction extends AutoAction {
    public boolean Act() {
        Auto.Utils.gamepieceController.setLocation(0);
        Auto.Utils.gamepieceController.stopClaw();
        return false;
    }  
}
