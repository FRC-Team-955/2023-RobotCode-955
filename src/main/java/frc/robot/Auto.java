package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Actions.AutoAction;
import frc.robot.DummyClasses.GamePieceController;
import frc.robot.DummyClasses.SwerveDrive;

public class Auto {
    AutoAction[] autoActionList = new AutoAction[0];

    public Auto() {
        AutoProfileNew.autoVersion("Auto1");
        autoActionList = AutoProfileNew.AutoActions.get(0);
    }

    public void autoPeriodic() {
        boolean stepFinished = true;
        for (int i = 0; i < autoActionList.length; i++) {
            if (!autoActionList[i].Act()) {
                stepFinished = false;
            }
        }
        if (stepFinished = true) {
            AutoProfileNew.AutoActions.remove(0);
            autoActionList = AutoProfileNew.AutoActions.get(0);
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