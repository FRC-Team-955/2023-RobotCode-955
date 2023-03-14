package frc.robot.Auto.Actions;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.GamepieceManager;
import frc.robot.IO.GridArmPosition;
import frc.robot.IO.GridRowPosition;
import frc.robot.Subsystems.Claw;

public class CubeIntake extends AutoAction {
    Timer timer = new Timer();

    public double time;

    boolean extended;

    public boolean Act() {
        if (!extended) {
            extended = GamepieceManager.extention(GridRowPosition.CubeIntake, GridArmPosition.CubeIntake);
            if (extended)
                timer.start();
        }
        else {
            Claw.intakeGamePiece();
            return (timer.get() >= time);
        }

        return false;
    }

    @Override
    public void Finish() {
        Claw.stopishMotor();
    }
}
