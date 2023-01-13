package frc.robot;

import java.util.ArrayList;
import java.util.List;

import frc.robot.Actions.*;

public class AutoProfileEnum {
    ArrayList<Enums.State> AutoState = new ArrayList<>();
    ArrayList<String> AutoTrajectory = new ArrayList<>();
    ArrayList<AutoAction> AutoActions = new ArrayList<>();
    ArrayList<int[]> AutoActionOptions = new ArrayList<>();

    ArrayList<Enums.State> Auto1State = new ArrayList<>(List.of(Enums.State.ACTION, Enums.State.ACTION, Enums.State.ACTION, Enums.State.ACTION, Enums.State.LOADTRAJECTORY, Enums.State.CURVE, Enums.State.STOP));
    ArrayList<String> Auto1Trajectory = new ArrayList<>(List.of("Grid4-Piece2.wpilib.json"));
    ArrayList<AutoAction> Auto1Actions = new ArrayList<>(List.of(new ScoreAction(), new ObtainAction(), new ClawAction(), new ElevatorAction()));
    ArrayList<int[]> Auto1ActionOptions = new ArrayList<>(List.of(new int[]{}, new int[]{}, new int[]{0}, new int[]{2}));

    public AutoProfileEnum(String autoVersion) {
        if (autoVersion == "Auto1") {
            AutoState = Auto1State;
            AutoTrajectory = Auto1Trajectory;
            AutoActions = Auto1Actions;
        }
    }
}
