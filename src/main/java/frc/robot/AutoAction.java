package frc.robot;

public interface AutoAction {

    //Auto Actions will return true when complete so the program knows to move on the the next action or path
    //Auto Actions accept an int array in order to specify things that may be needed for certain actions, such as which cone to pick up or what spot to place the game piece on
    public boolean Act(int[] Options);

}