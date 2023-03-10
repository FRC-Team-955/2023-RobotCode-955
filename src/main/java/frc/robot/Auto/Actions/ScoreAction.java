package frc.robot.Auto.Actions;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.IO;
import frc.robot.IO.GridArmPosition;
import frc.robot.IO.GridRowPosition;
import frc.robot.AutoAlign;
import frc.robot.Constants;
import frc.robot.Drivebase;
import frc.robot.GamepieceManager;
import frc.robot.Subsystems.Claw;

public class ScoreAction extends AutoAction {

    public int nodePosition;
    public int nodeLevel;

    public AutoState autoState = AutoState.AlignNode;

    @Override
    public boolean Act() {
        switch (autoState) {
            case AlignNode:
                GamepieceManager.runExtention();
                Claw.intakeGamePiece();
                if(AutoAlign.alignOdometry(autoGridSelection(nodePosition), -180)){
                    if (isConePosition(nodePosition)) {
                        autoState = AutoState.AlignPosition;
                    } else {
                        autoState = AutoState.Place;
                    }
                }
                break;
            case AlignPosition:
                GamepieceManager.runExtention();
                Claw.intakeGamePiece();
                if (AutoAlign.alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, 
                    autoGridSelection(nodePosition).getY()), -180.0)) {
                    autoState = AutoState.Extend;
                }
                break;
            case Extend:
                Claw.stopishMotor();
                if (GamepieceManager.extention(autoRowSelection(nodeLevel), (nodeLevel == 0) ? GridArmPosition.Hybrid : IO.GridArmPosition.ConePrep)){
                    autoState = AutoState.Place;
                }
                break;
            case Place:
                if(GamepieceManager.extention(autoRowSelection(nodeLevel), (nodeLevel == 0) ? GridArmPosition.Hybrid : IO.GridArmPosition.ConePrep)){
                    Claw.outputGamePiece();
                    autoState = AutoState.LeaveNode;
                }else{
                    Claw.stopishMotor();
                }
                break;
            case LeaveNode:
                GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
                Claw.outputGamePiece();

                return AutoAlign.alignOdometry(autoGridSelection(nodePosition), -180);
        }
        return false;
    }

    @Override
    public void Finish() {
        Claw.stopishMotor();        
    }

    public static Translation2d autoGridSelection(int position){
        switch (position) {
            case 0: return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue0:Constants.FieldPositions.AutoAlignPositions.red0;
            case 1: return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue1:Constants.FieldPositions.AutoAlignPositions.red1;
            case 2: return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue2:Constants.FieldPositions.AutoAlignPositions.red2;
            case 3: return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue3:Constants.FieldPositions.AutoAlignPositions.red3;
            case 4: return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue4:Constants.FieldPositions.AutoAlignPositions.red4;
            case 5: return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue5:Constants.FieldPositions.AutoAlignPositions.red5;
            case 6: return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue6:Constants.FieldPositions.AutoAlignPositions.red6;
            case 7: return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue7:Constants.FieldPositions.AutoAlignPositions.red7;
            case 8: return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue8:Constants.FieldPositions.AutoAlignPositions.red8;
            default: return Drivebase.getPose().getTranslation();
        }
    }

    public static boolean isConePosition(int position) {
        switch (position) {
            case 0: return true;
            case 2: return true;
            case 3: return true;
            case 5: return true;
            case 6: return true;
            case 8: return true;
            default: return false;
        }
    }

    public static GridRowPosition autoRowSelection(int position) {
        switch (position) {
            case 0: return GridRowPosition.Retract;
            case 1: return GridRowPosition.Retract;
            case 2: return GridRowPosition.High;
            default: return GridRowPosition.Retract;
        }
    }

    public static enum AutoState {
        AlignNode,
        AlignPosition,
        Extend,
        Place,
        LeaveNode
    }

    public ScoreAction() {

    }
    public ScoreAction(double _startTime, double _endTime, EarlyEndMode earlyEndMode, LateEndMode lateEndMode, int gridLevel, int gridPosition, AutoAction[] _endActions) {
        startTime = _startTime;
        endTime = _endTime;
        earlyMode = earlyEndMode;
        lateMode = lateEndMode;
        nodeLevel = gridLevel;
        nodePosition = gridPosition;
        endActions = new ArrayList<AutoAction>();
        for (AutoAction action : _endActions) {
            endActions.add(action);
        }
    }
}
