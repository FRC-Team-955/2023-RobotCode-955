package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.TimedRobot;

public class AutoStates {
    private SwerveDrive swerve;
    enum State {
        CURVE,
        ACTION,
        TRANSITION,
        STOP,
        LOADTRAJECTORY
    }
    State autoState = State.STOP;

    public void robotInit() {
        swerve = new SwerveDrive();
        swerve.resetAnglesToAbsolute();
        swerve.resetOdometry(new Pose2d(0,0, new Rotation2d(0)));
    }

    public void autoPeriodic() {
        switch (autoState) {
            case LOADTRAJECTORY:
                System.out.println("Loading");
                swerve.loadTrajectory("trajectory");
                autoState = State.CURVE;
            
            case CURVE:
                System.out.println("moving");
                if(swerve.followTrajectory(180)){//change number to read from something
                    autoState = State.ACTION;
                }
            
            case ACTION:
                System.out.println("action");
                if(subsystem.getfinished()) {
                    autoState = State.TRANSITION;
                } else {
                    subsystem.doaction();
                }
            
            case TRANSITION:
                System.out.println("transition");
                // not sure whether we'll need this based on autoalign, ect.
                autoState = getnextstate();

            case STOP:
                // set motors to zero, ect.
        }
    }
}