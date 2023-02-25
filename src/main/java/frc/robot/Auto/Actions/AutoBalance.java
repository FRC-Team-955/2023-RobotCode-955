package frc.robot.Auto.Actions;

import frc.robot.Drivebase;

public class AutoBalance extends AutoAction {
    public AutoBalance() {}

    public boolean Act() {
        Drivebase.autoBalance();
        return Drivebase.isBalanced();
    }
    
    public void Finish() {
        
    }
}
