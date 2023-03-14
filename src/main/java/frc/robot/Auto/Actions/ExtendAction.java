package frc.robot.Auto.Actions;

import java.util.ArrayList;

import frc.robot.GamepieceManager;
import frc.robot.IO.GridArmPosition;
import frc.robot.IO.GridRowPosition;

public class ExtendAction extends AutoAction {
    public double level;
    public boolean cube;

    public boolean Act() {
        return GamepieceManager.extention((level == 2) ? GridRowPosition.High : GridRowPosition.Low, (cube) ? GridArmPosition.CubePrep : GridArmPosition.ConePrep);
    }

    public ExtendAction() {}
    public ExtendAction(double _startTime, double _endTime, EarlyEndMode earlyEndMode, LateEndMode lateEndMode, double gridLevel, boolean isCube, AutoAction[] _endActions) {
        startTime = _startTime;
        endTime = _endTime;
        earlyMode = earlyEndMode;
        lateMode = lateEndMode;
        level = gridLevel;
        cube = isCube;
        endActions = new ArrayList<AutoAction>();
        for (AutoAction action : _endActions) {
            endActions.add(action);
        }
    }
}
