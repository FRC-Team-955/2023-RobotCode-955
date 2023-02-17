package frc.robot.Actions;

import frc.robot.Drivebase;

public class AutoBalance implements AutoAction {
    public AutoBalance() {}

    public boolean Act() {
        Drivebase.autoBalance();
        return Drivebase.isBalanced();
    }
    
}
