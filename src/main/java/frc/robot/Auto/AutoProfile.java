package frc.robot.Auto;

import java.util.ArrayList;

import frc.robot.Constants;
import frc.robot.Auto.Actions.AutoAction;
import frc.robot.Auto.Actions.ScoreAction;
import frc.robot.Auto.Actions.ScoreActionOld;
import frc.robot.Auto.Actions.TestAction;
import frc.robot.Auto.Actions.WaypointAction;
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
            new ScoreActionOld(1, 14, EarlyEndMode.End, LateEndMode.Continue, 1, 4, new AutoAction[] {})
        };

        for (AutoAction action : autoActions) {
            profile.Actions.add(action);
        }

        return profile;
    }

    public static AutoProfile OneGamepieceAndMobility(int position, int level, boolean leaveRight) {
        AutoProfile profile = new AutoProfile();
        profile.Actions = new ArrayList<AutoAction>();

        profile.team = (Constants.isRed()) ? Team.Red : Team.Blue;

        AutoAction[] autoActions = {
            new ScoreAction(0, 5, EarlyEndMode.End, LateEndMode.Wait, level, position, new AutoAction[] {
                new WaypointAction((profile.team == Team.Red) ? ((leaveRight) ? Constants.FieldPositions.AutoAlignPositions.red8 : Constants.FieldPositions.AutoAlignPositions.red0) : 
                ((leaveRight) ? Constants.FieldPositions.AutoAlignPositions.blue8 : Constants.FieldPositions.AutoAlignPositions.blue0), -180, 5, 8, EarlyEndMode.End, LateEndMode.Wait, new AutoAction[] {
                    // new WaypointAction((profile.team == Team.Red) ? () : ())
                })
            })
        };

        for (AutoAction action : autoActions) {
            profile.Actions.add(action);
        }

        return profile;
    }
}