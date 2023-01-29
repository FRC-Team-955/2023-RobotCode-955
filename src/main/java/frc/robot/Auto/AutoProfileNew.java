package frc.robot.Auto;

import java.util.ArrayList;
import java.util.List;

import frc.robot.Auto.Actions.*;

public class AutoProfileNew {
    static String autoVersion;

    static ArrayList<AutoAction[]> AutoActions = new ArrayList<>();

    static ArrayList<AutoAction[]> Auto1Actions = new ArrayList<>(List.of(
        new AutoAction[]{new ElevatorAction(0)},
        new AutoAction[]{new ClawAction(-1, 5000.), new LoadTrajectoryAction("Grid4-Piece2.wpilib.json")},
        new AutoAction[]{new CurveAction()}, 
        new AutoAction[]{new ClawAction(1, 5000.), new LoadTrajectoryAction("Piece2-Grid5.wpilib.json")},
        new AutoAction[]{new ClawAction(1, 5000.), new ElevatorAction(1), new CurveAction()},
        new AutoAction[]{new ClawAction(1, 5000.)},
        new AutoAction[]{new StopAction()}
    ));

    public static void autoVersion(String autoVersion) {
        AutoProfileNew.autoVersion = autoVersion;
        if (autoVersion == "Auto1") {
            AutoActions = Auto1Actions;
        }
    }

    public static ArrayList<AutoAction[]> getOriginalList() {
        if (autoVersion == "Auto1") {
            return Auto1Actions;
        } else {
            return null;
        }
    }
}
