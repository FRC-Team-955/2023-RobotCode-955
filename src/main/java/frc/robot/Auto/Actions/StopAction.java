package frc.robot.Auto.Actions;

import frc.robot.Auto.Auto;

public class StopAction extends AutoAction {
    public boolean Act() {
        Auto.Utils.gamepieceController.setLocation(0);
        Auto.Utils.gamepieceController.stopClaw();
        return false;
    }  
}
