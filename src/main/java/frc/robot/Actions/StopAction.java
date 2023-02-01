package frc.robot.Actions;

import frc.robot.gamepieceManager.GamepieceManager;

public class StopAction implements AutoAction {
    public boolean Act() {
        GamepieceManager.setLocation(0);
        GamepieceManager.moveHandoff(0);
        GamepieceManager.foldIntake(0);
        GamepieceManager.runEthanWheels(0);
        return false;
    }  
}
