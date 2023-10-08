package frc.robot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot.AutoSide;

public class AutoSelector {
    
    static SendableChooser<AutoSide> side = new SendableChooser<AutoSide>();
    static SendableChooser<Integer> node = new SendableChooser<Integer>();

    static SendableChooser<Boolean> enabled = new SendableChooser<Boolean>();

    public static void start() {
        side.setDefaultOption("Left", AutoSide.Left);
        side.addOption("Charge", AutoSide.Charge);
        side.addOption("Right", AutoSide.Right);

        node.setDefaultOption("1", 0);
        node.addOption("3", 2);
        node.addOption("4", 3);
        node.addOption("6", 5);
        node.addOption("7", 6);
        node.addOption("9", 8);

        enabled.setDefaultOption("Disabled", false);
        enabled.addOption("Enabled", true);

        SmartDashboard.putData("Auto Side", side);
        SmartDashboard.putData("Auto Node (1 - 9)", node);

        SmartDashboard.putData("Auto Enabled", enabled);
    }

    public static void set() {
        Robot.autoEnabled = enabled.getSelected();

        Robot.autoSide = side.getSelected();

        Robot.firstGamepiece = node.getSelected();
    }
}
