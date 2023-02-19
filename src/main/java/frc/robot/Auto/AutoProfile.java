package frc.robot.Auto;

import java.util.ArrayList;

import frc.robot.Auto.Actions.AutoAction;
import frc.robot.Auto.Actions.TestAction;
import frc.robot.Auto.Actions.AutoAction.EarlyEndMode;
import frc.robot.Auto.Actions.AutoAction.LateEndMode;

public class AutoProfile {
    public String name;
    public String description;

    public ArrayList<AutoAction> Actions;

    public AutoProfile() {
        Actions = new ArrayList<AutoAction>();
    }

    public static AutoProfile Test() {
        AutoProfile profile = new AutoProfile();
        profile.Actions = new ArrayList<AutoAction>();

        AutoAction[] autoActions = {new TestAction(1, 3, EarlyEndMode.End, LateEndMode.Enforce, 21, new AutoAction[] {})};

        for (AutoAction action : autoActions) {
            profile.Actions.add(action);
        }

        return profile;
    }
}