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

            case STOP:
                // set motors to zero, ect.
        }
        if(autoState == State.LOADTRAJECTORY){
            System.out.println("stop");
            swerve.loadTrajectory("New.wpilib.json");
            autoState == ;
        }
        if(autoState ==1){
            System.out.println("autostate1");
            if(swerve.followTrajectory(180)){
            autoState++;
            }
        }
        if(autoState ==2){
            System.out.println("autostate2");
            swerve.loadTrajectory("2nd.wpilib.json");
            autoState++;
        }
        if(autoState ==3){
            System.out.println("autostate3");
            if(swerve.followTrajectory(0)){
            autoState++;
            }
        }
        if(autoState ==4){
            System.out.println("autostate4");
            swerve.drive(new Translation2d(0,0), 0, false, true);
        }
    }
}
