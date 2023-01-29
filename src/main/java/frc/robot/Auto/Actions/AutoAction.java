package frc.robot.Auto.Actions;

public abstract class AutoAction {

    //Auto Actions will return true when complete so the program knows to move on the the next action or path
    public abstract boolean Act();

    public double startTime;

    public double endTime;
}