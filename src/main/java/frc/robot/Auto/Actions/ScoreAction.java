package frc.robot.Auto.Actions;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.AutoAlign;
import frc.robot.Constants;
import frc.robot.Drivebase;
import frc.robot.GamepieceManager;
import frc.robot.IO;
import frc.robot.Auto.AutoProfile.Team;
import frc.robot.IO.GridArmPosition;
import frc.robot.IO.GridRowPosition;
import frc.robot.Subsystems.Claw;

public class ScoreAction extends AutoAction {
    public int position;
    public int level;

    Translation2d target;

    @Override
    public void Start() {
        if (profile.team == Team.Red) {
            switch (position) {
                case 0: target = Constants.FieldPositions.AutoAlignPositions.redGrid0; break;
                case 1: target = Constants.FieldPositions.AutoAlignPositions.redGrid1; break;
                case 2: target = Constants.FieldPositions.AutoAlignPositions.redGrid2; break;
                case 3: target = Constants.FieldPositions.AutoAlignPositions.redGrid3; break;
                case 4: target = Constants.FieldPositions.AutoAlignPositions.redGrid4; break;
                case 5: target = Constants.FieldPositions.AutoAlignPositions.redGrid5; break;
                case 6: target = Constants.FieldPositions.AutoAlignPositions.redGrid6; break;
                case 7: target = Constants.FieldPositions.AutoAlignPositions.redGrid7; break;
                case 8: target = Constants.FieldPositions.AutoAlignPositions.redGrid8; break;
            }
        }
        else {
            switch (position) {
                case 0: target = Constants.FieldPositions.AutoAlignPositions.blueGrid0; break;
                case 1: target = Constants.FieldPositions.AutoAlignPositions.blueGrid1; break;
                case 2: target = Constants.FieldPositions.AutoAlignPositions.blueGrid2; break;
                case 3: target = Constants.FieldPositions.AutoAlignPositions.blueGrid3; break;
                case 4: target = Constants.FieldPositions.AutoAlignPositions.blueGrid4; break;
                case 5: target = Constants.FieldPositions.AutoAlignPositions.blueGrid5; break;
                case 6: target = Constants.FieldPositions.AutoAlignPositions.blueGrid6; break;
                case 7: target = Constants.FieldPositions.AutoAlignPositions.blueGrid7; break;
                case 8: target = Constants.FieldPositions.AutoAlignPositions.blueGrid8; break;
            }
        }
    }

    boolean aligned = false;
    boolean extended = false;
    boolean placed = false;
    boolean retreated = false;
    Pose2d scorePosition;

    public boolean Act() {
        if (!aligned) {
            aligned = AutoAlign.moveToGridPositionOdometryTwoStep();
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            Claw.stopishMotor();
        }
        else {
            if (!extended)
                extended = GamepieceManager.extention((level == 0) ? GridRowPosition.Low : (level == 1) ? GridRowPosition.MidCone : GridRowPosition.High, (position == 1 || position == 4 || position == 7) ? GridArmPosition.CubePrep : GridArmPosition.ConePrepHigh);
            else {
                if (!placed) {
                    placed = GamepieceManager.extention((level == 0) ? GridRowPosition.Low : (level == 1) ? GridRowPosition.MidCone : GridRowPosition.High, (position == 1 || position == 4 || position == 7) ? GridArmPosition.CubeReady : GridArmPosition.ConeReadyHigh);
                    scorePosition = Drivebase.getPose();
                }
                else {
                    if (!retreated) {
                        Claw.outputGamePiece();
                        Drivebase.drive.drive(new Translation2d(-0.3, 0), 0, false, false, false, 0);
                        retreated = (profile.team == Team.Red) ? (Drivebase.getPose().getX() < scorePosition.getX() - 0.5) : (Drivebase.getPose().getX() > scorePosition.getX() + 0.5);
                    }
                    else
                        return GamepieceManager.extention(GridRowPosition.Retract, GridArmPosition.Retract);
                }
            }
        }
        return false;
    }

    public void Finish() {
        Drivebase.drive.drive(new Translation2d(0, 0), 0, false, false, false, 0);
        Claw.stopishMotor();
        GamepieceManager.extention(GridRowPosition.Retract, GridArmPosition.Retract);
    }

    public ScoreAction(double _startTime, double _endTime, EarlyEndMode earlyEndMode, LateEndMode lateEndMode, int gridLevel, int gridPosition, AutoAction[] _endActions) {
        startTime = _startTime;
        endTime = _endTime;
        earlyMode = earlyEndMode;
        lateMode = lateEndMode;
        level = gridLevel;
        position = gridPosition;
        endActions = new ArrayList<AutoAction>();
        for (AutoAction action : _endActions) {
            endActions.add(action);
        }
    }

    public ScoreAction() {

    }
}
