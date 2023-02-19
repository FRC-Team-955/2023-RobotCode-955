package frc.robot.Auto.Actions;

import java.util.ArrayList;

public abstract class AutoAction {

    //Auto Actions will return true when complete so the program knows to move on the the next action or path
    public abstract boolean Act();

    public double startTime; //Time to start the action

    public double endTime; //Target time to finish the action

    public EarlyEndMode earlyMode; //See below
    public LateEndMode lateMode; //See below

    public ArrayList<AutoAction> endActions; //Actions to complete once the required end state is reached
    public boolean endDeployed; //Have the dependent actions been put in the queue or started

    public enum LateEndMode {
        Wait, //Wait to start the dependents until the action returns true
        Continue, //Start the dependents at end time, but keep calling the action until it returns true
        Enforce, //Stop the action at end time and start the dependents
        Rely //Stop the action at end time and don't call dependents if not completed in time
    }
    public enum EarlyEndMode {
        Continuous, //Keep calling the action until end time and start the dependents then
        Minimumn, //Stop calling the action once it returns true, but wait until end time to start dependents
        End //Stop the action once it return true and start the dependents
    }
}