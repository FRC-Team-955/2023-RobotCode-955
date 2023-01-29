package frc.robot.Auto;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.GamePieceController;
import frc.robot.Auto.Actions.AutoAction;
import frc.robot.Swerve.SwerveDrive;


public class Auto {
    AutoAction[] Actions = new AutoAction[0];

    public Auto() {
        AutoProfileNew.autoVersion("Auto1");
        Actions = AutoProfileNew.AutoActions.get(0);
    }

    public void autoPeriodic() {
        for (int i = 0; i < Actions.length; i++) {
            if (DriverStation.getMatchTime() > Actions[i].startTime && DriverStation.getMatchTime() < Actions[i].endTime)
                Actions[i].Act();
        }
    }

    public void displayData() {
        SmartDashboard.putNumber("Step", AutoProfileNew.getOriginalList().size() - AutoProfileNew.AutoActions.size() + 1);
    }

    public static class Utils {
        public static GamePieceController gamepieceController = new GamePieceController();
        public static SwerveDrive swerveDrive = new SwerveDrive();
    }
}