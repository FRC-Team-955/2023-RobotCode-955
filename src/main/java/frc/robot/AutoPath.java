package frc.robot;

import java.util.ArrayList;

import frc.robot.Actions.AutoAction;

public class AutoPath {
    //Path Object
    public String Path;

    //Stores the starting position of the path in field relative space, with 0, 0 being the bottom left corner, with the length of the field being x and width being y, blue on the left side
    public double x;
    public double y;

    //Actions to execute at the end of the path (Ex: pick up a cube)
    public ArrayList<AutoAction> Actions;

    public ArrayList<int[]> ActionOptions;
}
