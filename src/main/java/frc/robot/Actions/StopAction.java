package frc.robot.Actions;

import frc.robot.GamepieceManager;
import frc.robot.IO;

public class StopAction implements AutoAction {
    public boolean Act() {
        GamepieceManager.setExtention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
        GamepieceManager.moveClawAuto(0);
        GamepieceManager.foldIntakeAuto(0);
        GamepieceManager.runFlapsAuto(0);
        return false;
    }  
}
