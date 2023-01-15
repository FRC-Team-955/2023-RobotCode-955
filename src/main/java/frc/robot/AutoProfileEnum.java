package frc.robot;

import java.util.ArrayList;
import java.util.List;
import frc.robot.Enums.State;

import frc.robot.Actions.*;

public class AutoProfileEnum {
    ArrayList<State[]> AutoState = new ArrayList<>();
    ArrayList<String> AutoTrajectory = new ArrayList<>();
    ArrayList<AutoAction[]> AutoActions = new ArrayList<>();
    ArrayList<int[][]> AutoActionOptions = new ArrayList<>();
    ArrayList<Double[]> AutoPauseTime = new ArrayList<>();

    ArrayList<State[]> Auto1State = new ArrayList<>(List.of(
        new State[]{State.ACTION},
        new State[]{State.ACTION},
        new State[]{State.LOADTRAJECTORY},
        new State[]{State.CURVE},
        new State[]{State.ACTION},
        new State[]{State.LOADTRAJECTORY},
        new State[]{State.ACTION, State.CURVE, State.ACTION},
        new State[]{State.ACTION}
    ));
    ArrayList<String> Auto1Trajectory = new ArrayList<>(List.of(
        "Grid4-Piece2.wpilib.json",
        "Piece2-Grid5.wpilib.json"
    ));
    ArrayList<AutoAction[]> Auto1Actions = new ArrayList<>(List.of(
        new AutoAction[]{new ElevatorAction()},
        new AutoAction[]{new ClawAction()}, 
        new AutoAction[]{new ClawAction()},
        new AutoAction[]{new ClawAction(), new ElevatorAction()},
        new AutoAction[]{new ClawAction()}
    ));
    ArrayList<int[][]> Auto1ActionOptions = new ArrayList<>(List.of(
        new int[][]{{0}},
        new int[][]{{-1}},
        new int[][]{{1}}, 
        new int[][]{{1}, {1}},
        new int[][]{{-1}}
    ));
    ArrayList<Double[]> Auto1PauseTime = new ArrayList<>(List.of(
        new Double[]{1000000.},
        new Double[]{5000.},
        new Double[]{5000.},
        new Double[]{5000., 100000.},
        new Double[]{10000.}
    ));

    public AutoProfileEnum(String autoVersion) {
        if (autoVersion == "Auto1") {
            AutoState = Auto1State;
            AutoTrajectory = Auto1Trajectory;
            AutoActions = Auto1Actions;
            AutoPauseTime = Auto1PauseTime;
        }
    }
}
