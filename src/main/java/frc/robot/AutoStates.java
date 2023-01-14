package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.DummyClasses.SwerveDrive;

public class AutoStates {
    SwerveDrive swerve;
    Enums.State autoState = Enums.State.STOP;
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
        autoState = autoProfile.AutoState.get(0);
        autoProfile.AutoState.remove(0);
        storedTime = System.currentTimeMillis();
    }

    public void autoPeriodic() {
        switch (autoState) {
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
                if (autoProfile.AutoActions.get(0).Act(autoProfile.AutoActionOptions.get(0))) {
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
                if (System.currentTimeMillis() - storedTime > autoProfile.AutoPauseTime.get(0)) {
                    autoProfile.AutoPauseTime.remove(0);
                    nextState();
                }
            case STOP:
                // set motors to zero, ect.
                System.out.println("stop");
        }
    }
}