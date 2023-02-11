package frc.robot.Auto;

import java.util.ArrayList;

import frc.robot.Auto.Actions.AutoAction;

public class AutoProfile {
    public String name;
    public String description;

    public ArrayList<AutoAction> Actions;

    public AutoProfile() {
        Actions = new ArrayList<AutoAction>();
    }
}
