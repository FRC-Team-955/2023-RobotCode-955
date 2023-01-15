package frc.robot;

import frc.robot.Actions.AutoAction;
import frc.robot.Actions.StopAction;

public class AutoStates {
    AutoAction[] autoActionList = new AutoAction[]{new StopAction()};

    public AutoStates() {
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
}