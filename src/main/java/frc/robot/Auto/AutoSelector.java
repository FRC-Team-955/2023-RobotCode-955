package frc.robot.Auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.Robot;
import frc.robot.Robot.AutoLeaveSelection;
import frc.robot.Robot.AutoState;
import frc.robot.Robot.AutoType;
import frc.robot.Robot.NewAutoLeaveSelection;
import frc.robot.Robot.NewAutoState;

public class AutoSelector {
    // autoTypeSelection = AutoType.Old;
    // autoLeaveSelection = AutoLeaveSelection.Left;
    // newAutoLeaveSelection = NewAutoLeaveSelection.Right;
    // autoGridSelection = 2;
    // autoState = AutoState.Setup; //AutoState.Setup for games
    // newAutoState = NewAutoState.Setup;
    
    static SendableChooser<AutoType> type = new SendableChooser<AutoType>();
    static SendableChooser<AutoLeaveSelection> leave = new SendableChooser<AutoLeaveSelection>();
    static SendableChooser<NewAutoLeaveSelection> leaveNew = new SendableChooser<NewAutoLeaveSelection>();
    static SendableChooser<Integer> grid = new SendableChooser<Integer>();
    static SendableChooser<Boolean> enabled = new SendableChooser<Boolean>();

    public static void start() {
        type.setDefaultOption("Old (One piece or balance)", AutoType.Old);
        type.addOption("New (2 piece)", AutoType.New);

        leave.setDefaultOption("Left", AutoLeaveSelection.Left);
        leave.addOption("Charge", AutoLeaveSelection.Charge);
        leave.addOption("Right", AutoLeaveSelection.Right);

        if (DriverStation.getAlliance() == Alliance.Blue) {
            leaveNew.setDefaultOption("Blue - ONLY FOR USE WHEN TESTING", NewAutoLeaveSelection.Left);
            leaveNew.addOption("Red - ONLY FOR USE WHEN TESTING", NewAutoLeaveSelection.Right);

            grid.setDefaultOption("3", 2);
            grid.addOption("1", 0);
            grid.addOption("4", 3);
            grid.addOption("6", 5);
            grid.addOption("7", 6);
            grid.addOption("9", 8);
        }
        else {
            leaveNew.setDefaultOption("Red - ONLY FOR USE WHEN TESTING", NewAutoLeaveSelection.Right);
            leaveNew.addOption("Blue - ONLY FOR USE WHEN TESTING", NewAutoLeaveSelection.Left);

            grid.setDefaultOption("7", 2);
            grid.addOption("1", 0);
            grid.addOption("3", 6);
            grid.addOption("4", 3);
            grid.addOption("6", 5);
            grid.addOption("9", 8);
        }

        enabled.setDefaultOption("Enabled", true);
        enabled.addOption("Disabled", false);
    }

    public static void set() {
        Robot.autoTypeSelection = type.getSelected();
        Robot.autoLeaveSelection = leave.getSelected();
        Robot.newAutoLeaveSelection = leaveNew.getSelected();
        Robot.autoGridSelection = grid.getSelected();
        if (enabled.getSelected()) {
            Robot.autoState = AutoState.Setup;
            Robot.newAutoState = NewAutoState.Setup;
        }
        else {
            Robot.autoState = AutoState.Done;
            Robot.newAutoState = NewAutoState.Done;
        }
    }
}
