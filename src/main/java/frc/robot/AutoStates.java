package frc.robot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.DummyClasses.SwerveDrive;

public class AutoStates {
    SwerveDrive swerve;
    Enums.State autoState = Enums.State.STOP;
    AutoProfileEnum autoProfile;

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
                if (autoProfile.AutoActions.get(0).Act(new int[]{})) {
                    autoProfile.AutoActions.remove(0);
                    nextState();
                }
            case TRANSITION:
                System.out.println("transition");
                // not sure whether we'll need this based on autoalign, ect.
                nextState();

            case STOP:
                // set motors to zero, ect.
                System.out.println("stop");
        }
    }
}