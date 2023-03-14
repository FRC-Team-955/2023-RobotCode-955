package frc.robot.Auto.Actions.Deprecated;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.AutoAlign;
import frc.robot.Constants;
import frc.robot.Drivebase;
import frc.robot.GamepieceManager;
import frc.robot.IO;
import frc.robot.Auto.Actions.AutoAction;
import frc.robot.Auto.Actions.AutoAction.EarlyEndMode;
import frc.robot.Auto.Actions.AutoAction.LateEndMode;
import frc.robot.Auto.AutoProfile.Team;
import frc.robot.IO.GridArmPosition;
import frc.robot.IO.GridRowPosition;
import frc.robot.Subsystems.Claw;

public class ScoreActionOld extends AutoAction {
    public int position;
    public int level;

    Translation2d target;

    @Override
    public void Start() {
        if (profile.team == Team.Red) {
            switch (position) {
                case 0: target = Constants.FieldPositions.AutoAlignPositions.red0; break;
                case 1: target = Constants.FieldPositions.AutoAlignPositions.red1; break;
                case 2: target = Constants.FieldPositions.AutoAlignPositions.red2; break;
                case 3: target = Constants.FieldPositions.AutoAlignPositions.red3; break;
                case 4: target = Constants.FieldPositions.AutoAlignPositions.red4; break;
                case 5: target = Constants.FieldPositions.AutoAlignPositions.red5; break;
                case 6: target = Constants.FieldPositions.AutoAlignPositions.red6; break;
                case 7: target = Constants.FieldPositions.AutoAlignPositions.red7; break;
                case 8: target = Constants.FieldPositions.AutoAlignPositions.red8; break;
            }
        }
        else {
            switch (position) {
                case 0: target = Constants.FieldPositions.AutoAlignPositions.blue0; break;
                case 1: target = Constants.FieldPositions.AutoAlignPositions.blue1; break;
                case 2: target = Constants.FieldPositions.AutoAlignPositions.blue2; break;
                case 3: target = Constants.FieldPositions.AutoAlignPositions.blue3; break;
                case 4: target = Constants.FieldPositions.AutoAlignPositions.blue4; break;
                case 5: target = Constants.FieldPositions.AutoAlignPositions.blue5; break;
                case 6: target = Constants.FieldPositions.AutoAlignPositions.blue6; break;
                case 7: target = Constants.FieldPositions.AutoAlignPositions.blue7; break;
                case 8: target = Constants.FieldPositions.AutoAlignPositions.blue8; break;
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
                extended = GamepieceManager.extention((level == 0) ? GridRowPosition.Low : (level == 1) ? GridRowPosition.Mid : GridRowPosition.High, (position == 1 || position == 4 || position == 7) ? GridArmPosition.CubePrep : GridArmPosition.ConePrep);
            else {
                if (!placed) {
                    placed = GamepieceManager.extention((level == 0) ? GridRowPosition.Low : (level == 1) ? GridRowPosition.Mid : GridRowPosition.High, (position == 1 || position == 4 || position == 7) ? GridArmPosition.CubeReady : GridArmPosition.ConeReady);
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

    public ScoreActionOld(double _startTime, double _endTime, EarlyEndMode earlyEndMode, LateEndMode lateEndMode, int gridLevel, int gridPosition, AutoAction[] _endActions) {
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

    public ScoreActionOld() {

    }
}
