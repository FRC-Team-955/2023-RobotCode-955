package frc.robot.Auto.Actions;

import frc.robot.GamepieceManager;
import frc.robot.IO.GridArmPosition;
import frc.robot.IO.GridRowPosition;

public class RetractAction extends AutoAction {
    public boolean up;

    public boolean Act() {
        return GamepieceManager.extention(GridRowPosition.Retract, (up) ? GridArmPosition.Up : GridArmPosition.Retract);
    }
}
