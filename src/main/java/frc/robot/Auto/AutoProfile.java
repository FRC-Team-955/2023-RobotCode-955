package frc.robot.Auto;

import java.util.ArrayList;

import frc.robot.Auto.Actions.AutoAction;
import frc.robot.Auto.Actions.ScoreAction;
import frc.robot.Auto.Actions.TestAction;
import frc.robot.Auto.Actions.AutoAction.EarlyEndMode;
import frc.robot.Auto.Actions.AutoAction.LateEndMode;

public class AutoProfile {
    public String name;
    public String description;

    public enum Team {
        Red,
        Blue
    }

    public Team team;

    public ArrayList<AutoAction> Actions;

    public AutoProfile() {
        Actions = new ArrayList<AutoAction>();
        AutoAction.profile = this;
    }

    public static AutoProfile Test() {
        AutoProfile profile = new AutoProfile();
        profile.Actions = new ArrayList<AutoAction>();

        profile.team = Team.Red;

        AutoAction[] autoActions = {
            new ScoreAction(1, 14, EarlyEndMode.End, LateEndMode.Continue, 1, 4, new AutoAction[] {})
        };

        for (AutoAction action : autoActions) {
            profile.Actions.add(action);
        }

        return profile;
    }
}