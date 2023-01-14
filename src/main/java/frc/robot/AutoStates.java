package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.DummyClasses.SwerveDrive;

public class AutoStates {
    SwerveDrive swerve;
    Enums.State autoState = Enums.State.STOP;
    AutoProfile Profile;
    AutoPath Path;

    public void autoPeriodic() {
        switch (autoState) {
            case LOADTRAJECTORY:
                System.out.println("Loading");
                swerve.loadTrajectory(Path.Path);
                autoState = Enums.State.CURVE;
            case CURVE:
                System.out.println("moving");
                if(swerve.followTrajectory(180)){
                    autoState = Enums.State.ACTION;
                }
            
            case ACTION:
                System.out.println("action");
                if (Path.Actions.get(0).Act(Path.ActionOptions.get(0))) {
                    Path.Actions.remove(0);
                    Path.ActionOptions.remove(0);
                }

                if (Path.Actions.isEmpty())
                    autoState = Enums.State.TRANSITION;
            case TRANSITION:
                System.out.println("transition");
                Profile.Paths.remove(0);
                if (Profile.Paths.isEmpty()) {
                    autoState = Enums.State.STOP;
                    return;
                }

                Path = Profile.Paths.get(0);
                autoState = Enums.State.LOADTRAJECTORY;
                // not sure whether we'll need this based on autoalign, ect.

            case STOP:
                // set motors to zero, ect.
                System.out.println("stop");
        }
    }

    public AutoStates() {
        swerve = new SwerveDrive();
        swerve.resetAnglesToAbsolute();
        swerve.resetOdometry(new Pose2d(0,0, new Rotation2d(0)));
        //Load Auto Profile Line Here
        Path = Profile.Paths.get(0);
        autoState = Enums.State.LOADTRAJECTORY;
    }
}