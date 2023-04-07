package frc.robot.Auto;

import java.math.BigInteger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    // static SendableChooser<Integer> firstGrid = new SendableChooser<Integer>();
    // static SendableChooser<Integer> secondGrid = new SendableChooser<Integer>();
    static SendableChooser<Boolean> enabled = new SendableChooser<Boolean>();

    public static void start() {
        type.setDefaultOption("Old (One piece or balance)", AutoType.Old);
        type.addOption("New (2 piece)", AutoType.New);

        leave.setDefaultOption("None", AutoLeaveSelection.None);
        leave.addOption("Left", AutoLeaveSelection.Left);
        leave.addOption("Charge", AutoLeaveSelection.Charge);
        leave.addOption("Right", AutoLeaveSelection.Right);

        if (DriverStation.getAlliance() == Alliance.Blue) {
            leaveNew.setDefaultOption("Left", NewAutoLeaveSelection.Left);
            leaveNew.addOption("Right", NewAutoLeaveSelection.Right);
            
            // firstGrid.setDefaultOption("1", 0);
            // firstGrid.addOption("3", 2);
            // firstGrid.addOption("4", 3);
            // firstGrid.addOption("6", 5);
            // firstGrid.addOption("7", 6);
            // firstGrid.addOption("9", 8);
            
            // secondGrid.setDefaultOption("2", 1);
            // secondGrid.addOption("5", 4);
            // secondGrid.addOption("8", 7);
            
            grid.addOption("1", 0);
            grid.addOption("3", 2);
            grid.addOption("4", 3);
            grid.addOption("6", 5);
            grid.setDefaultOption("7", 6);
            grid.addOption("9", 8);
        }
        else {
            leaveNew.setDefaultOption("Right", NewAutoLeaveSelection.Right);
            leaveNew.addOption("Left", NewAutoLeaveSelection.Left);

            // firstGrid.addOption("1", 0);
            // firstGrid.addOption("3", 2);
            // firstGrid.addOption("4", 3);
            // firstGrid.addOption("6", 5);
            // firstGrid.addOption("7", 6);
            // firstGrid.setDefaultOption("9", 8);
            
            // secondGrid.addOption("2", 1);
            // secondGrid.addOption("5", 4);
            // secondGrid.setDefaultOption("8", 7);

            grid.addOption("1", 0);
            grid.setDefaultOption("3", 2);
            grid.addOption("4", 3);
            grid.addOption("6", 5);
            grid.addOption("7", 6);
            grid.addOption("9", 8);
        }

        enabled.setDefaultOption("Enabled", true);
        enabled.addOption("Disabled", false);

        SmartDashboard.putData("Type", type);
        SmartDashboard.putData("Leave", leave);
        SmartDashboard.putData("2 Piece Side", leaveNew);
        SmartDashboard.putData("Grid Position", grid);
        // SmartDashboard.putData("First Node", firstGrid);
        // SmartDashboard.putData("Second Node", secondGrid);
        SmartDashboard.putData("Enabled", enabled);
    }

    public static void set() {
        Robot.autoTypeSelection = AutoType.Old;
        // Robot.autoTypeSelection = type.getSelected();
        Robot.autoLeaveSelection = AutoLeaveSelection.Charge;
        // Robot.autoLeaveSelection = leave.getSelected();
        Robot.newAutoLeaveSelection = NewAutoLeaveSelection.Right;
        // Robot.newAutoLeaveSelection = leaveNew.getSelected();
        // Robot.autoGridSelection = grid.getSelected();
        Robot.autoGridSelection = 3;

        Robot.getGamePieceInBalance =true;
        
        if(Robot.newAutoLeaveSelection == NewAutoLeaveSelection.Left){
            Robot.firstPiece = 0;
            Robot.secondPiece = 1;
        }else if(Robot.newAutoLeaveSelection == NewAutoLeaveSelection.Right){
            Robot.firstPiece = 8;
            Robot.secondPiece = 7;
        }else{
            Robot.firstPiece = 0;
            Robot.secondPiece = 1;
        }
        if (enabled.getSelected()) {
            Robot.autoState = AutoState.Place;
            Robot.newAutoState = NewAutoState.Place;
        }
        else {
            Robot.autoState = AutoState.Done;
            Robot.newAutoState = NewAutoState.Done;
        }
    }
}
