package frc.robot.Auto;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.GamePieceController;
import frc.robot.Auto.Actions.AutoAction;
import frc.robot.Auto.Actions.AutoAction.EarlyEndMode;
import frc.robot.Swerve.SwerveDrive;


public class Auto {
    public AutoProfile profile;

    //See EarlyEndMode and LateEndMode in AutoAction to understand why there are 3 Auto Action arrays

    ArrayList<AutoAction> queuedActions = new ArrayList<AutoAction>(); //Actions which are awaiting their start time

    ArrayList<AutoAction> currentActions = new ArrayList<AutoAction>(); //Actions that are running and have not passed their end time

    ArrayList<AutoAction> lateActions = new ArrayList<AutoAction>(); //Actions that have passed their end time but are still running



    public void autoInit() {
        queuedActions.addAll(profile.Actions);

        ArrayList<AutoAction> finishedActions = new ArrayList<AutoAction>();

        for (int i = 0; i < queuedActions.size(); i++) {
            if (queuedActions.get(i).startTime == 0) {
                currentActions.add(profile.Actions.get(i));
                finishedActions.add(queuedActions.get(i));
            }
        }

        queuedActions.removeAll(finishedActions);
    }

    public void autoPeriodic() {
        ArrayList<AutoAction> finishedActions = new ArrayList<AutoAction>();

        for (int i = 0; i < lateActions.size(); i++) {
            if (lateActions.get(i).Act()) {
                finishedActions.add(lateActions.get(i));

                if (!lateActions.get(i).endDeployed) {
                    for (AutoAction dependent : lateActions.get(i).endActions) {
                        lateActions.add(dependent);
                    }
                    lateActions.get(i).endDeployed = true;
                }
            }
        }

        lateActions.removeAll(finishedActions);

        finishedActions = new ArrayList<AutoAction>();
        
        for (int i = 0; i < queuedActions.size(); i++) {
            if (queuedActions.get(i).startTime < 15 - DriverStation.getMatchTime()) { //getMatchTime returns the time remaining in auto / teleop, not time elapsed
                currentActions.add(profile.Actions.get(i));
                finishedActions.add(queuedActions.get(i));
            }
        }

        queuedActions.removeAll(finishedActions);

        finishedActions = new ArrayList<AutoAction>();

        for (int i = 0; i < currentActions.size(); i++) {
            if (currentActions.get(i).Act()) {
                switch (currentActions.get(i).earlyMode) { //Early modes are explained in AutoAction
                    case Continuous:
                        for (AutoAction dependent : currentActions.get(i).endActions) {
                            queuedActions.add(dependent);
                            dependent.startTime = currentActions.get(i).endTime;
                        }
                        currentActions.get(i).endDeployed = true;
                        break;

                    case Minimumn:
                        for (AutoAction dependent : currentActions.get(i).endActions) {
                            queuedActions.add(dependent);
                            dependent.startTime = currentActions.get(i).endTime;
                        }
                        currentActions.get(i).endDeployed = true;
                        finishedActions.add(currentActions.get(i));
                        break;

                    case End:
                        for (AutoAction dependent : currentActions.get(i).endActions) {
                            currentActions.add(dependent);
                        }
                        currentActions.get(i).endDeployed = true;
                        finishedActions.add(currentActions.get(i));
                        break;
                }
            }
            else if (currentActions.get(i).endTime >= 15 - DriverStation.getMatchTime()) {
                if (currentActions.get(i).earlyMode == EarlyEndMode.Continuous && currentActions.get(i).endDeployed)
                    finishedActions.add(currentActions.get(i));
                else {
                    switch (currentActions.get(i).lateMode) { //Late modes are explained in AutoAction
                        case Continue:
                            for (AutoAction dependent : currentActions.get(i).endActions) {
                                currentActions.add(dependent);
                            }
                            currentActions.get(i).endDeployed = true;
                            break;

                        case Enforce:
                            for (AutoAction dependent : currentActions.get(i).endActions) {
                                currentActions.add(dependent);
                            }
                            currentActions.get(i).endDeployed = true;
                            finishedActions.add(currentActions.get(i));
                            break;

                        case Rely:
                            finishedActions.add(currentActions.get(i));
                            break;

                        default:
                            finishedActions.add(currentActions.get(i));
                            lateActions.add(currentActions.get(i));
                            break;
                    }
                }
            }
        }

        currentActions.removeAll(finishedActions);
    }

    public void displayData() {
        SmartDashboard.putNumber("Step", AutoProfileNew.getOriginalList().size() - AutoProfileNew.AutoActions.size() + 1);
    }

    public static class Utils {
        public static GamePieceController gamepieceController = new GamePieceController();
        public static SwerveDrive swerveDrive = new SwerveDrive();
    }
}