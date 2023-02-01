package frc.robot.Actions;

import frc.robot.Swerve.SwerveDrive;

public interface AutoAction {

    //Auto Actions will return true when complete so the program knows to move on the the next action or path
    //Auto Actions accept an int array in order to specify things that may be needed for certain actions, such as which cone to pick up or what spot to place the game piece on
    public boolean Act();

    // reference to dummy class (gamepieceController)
    public static SwerveDrive swerveDrive = new SwerveDrive();
}