package frc.robot;

import java.util.ArrayList;
import java.util.List;

public class AutoProfileEnum {
    ArrayList<Enums.State> AutoState = new ArrayList<>();
    ArrayList<String> AutoTrajectory = new ArrayList<>();
    ArrayList<AutoAction> AutoActions = new ArrayList<>();

    ArrayList<Enums.State> Auto1State = new ArrayList<>(List.of(Enums.State.ACTION, Enums.State.ACTION, Enums.State.ACTION, Enums.State.ACTION, Enums.State.LOADTRAJECTORY, Enums.State.CURVE, Enums.State.STOP));
    ArrayList<String> Auto1Trajectory = new ArrayList<>(List.of("Grid4-Piece2.wpilib.json"));
    ArrayList<AutoAction> Auto1Actions = new ArrayList<>(List.of(new ScoreAction(), new ObtainAction()));

    public AutoProfileEnum(String autoVersion) {
        if (autoVersion == "Auto1") {
            AutoState = Auto1State;
            AutoTrajectory = Auto1Trajectory;
            AutoActions = Auto1Actions;
        }
    }
}
