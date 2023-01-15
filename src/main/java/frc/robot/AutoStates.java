package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Actions.AutoAction;
import frc.robot.DummyClasses.SwerveDrive;

public class AutoStates {
    SwerveDrive swerve;
    Enums.State[] autoStateList = new Enums.State[]{Enums.State.STOP};
    AutoProfileEnum autoProfile;
    private double storedTime;

    public AutoStates() {
        swerve = new SwerveDrive();
        swerve.resetAnglesToAbsolute();
        swerve.resetOdometry(new Pose2d(0,0, new Rotation2d(0)));
        autoProfile = new AutoProfileEnum("Auto1");
        nextState();
    }

    public void nextState() {
        autoStateList = autoProfile.AutoState.get(0);
        autoProfile.AutoState.remove(0);
        storedTime = System.currentTimeMillis();
    }

    public void autoPeriodic() {
        for (int i = 0; i < autoStateList.length; i++) {
            switch (autoStateList[i]) {
                case LOADTRAJECTORY:
                    System.out.println("Loading");
                    swerve.loadTrajectory("trajectory");
                    nextState();
                case CURVE:
                    System.out.println("moving");
                    if(swerve.followTrajectory(180)){
                        nextState();
                    }
                case ACTION:
                    System.out.println("action");
                    int[][] storedOption = autoProfile.AutoActionOptions.get(0);
                    AutoAction[] storedAction = autoProfile.AutoActions.get(0);
                    if (storedAction[0].Act(storedOption[0])) {
                        autoProfile.AutoActions.remove(0);
                        autoProfile.AutoActionOptions.remove(0);
                        nextState();
                    }
                case TRANSITION:
                    System.out.println("transition");
                    // not sure whether we'll need this based on autoalign, ect.
                    nextState();
                case PAUSE:
                    System.out.println("pause");
                    Double[] storedPauseTime = autoProfile.AutoPauseTime.get(0);
                    if (System.currentTimeMillis() - storedTime > storedPauseTime[0]) {
                        autoProfile.AutoPauseTime.remove(0);
                        nextState();
                    }
                case STOP:
                    // set motors to zero, ect.
                    System.out.println("stop");
            }
        }
    }
}