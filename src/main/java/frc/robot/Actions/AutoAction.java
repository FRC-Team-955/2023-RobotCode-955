package frc.robot.Actions;

import frc.robot.Swerve.SwerveDrive;

public interface AutoAction {

    //Auto Actions will return true when complete so the program knows to move on the the next action or path
    //Auto Actions should be able to be called repeatedly in a loop even when finished and not cause problems
    public boolean Act();

    public static SwerveDrive swerveDrive = new SwerveDrive();
}