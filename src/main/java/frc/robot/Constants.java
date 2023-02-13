package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Swerve.SwerveSettings;

public class Constants {
    // Auto Balance PID
    // Note from owen: we will have soooo many pids these need to be named better
    public static class Drivebase{

        public static double autoBalancekP = 1;
        public static double autoBalancekI = 0;
        public static double autoBalancekD = 0;

        public static Translation2d autoBalanceStop = new Translation2d(0,0);
        public static Translation2d autoBalanceForward = new Translation2d(0,0.1);
        public static Translation2d autoBalanceBackward = new Translation2d(0,-0.1);
    }
    public static double forwardRateLimiter = 0;
    public static double strafeRateLimiter = 0;

    public static class AutoAlign{
        //final for 1.16 meters out (from center of robot)
        public static final double aprilTagAlignXkP = 0.045; 
        public static final double aprilTagAlignXkI = 0;
        public static final double aprilTagAlignXkD = 0;
        //need to test
        public static double limelightAlignXkP = 0.045;
        public static double limelightAlignXkI = 0;
        public static double limelightAlignXkD = 0;

        public static double odometryAlignXkP = 3;
        public static double odometryAlignXkI = 0;
        public static double odometryAlignXkD = 0;
        
        public static double odometryAlignYkP = 3;
        public static double odometryAlignYkI = 0;
        public static double odometryAlignYkD = 0;
        
        public static double alignTolerance = 0.1;
    }
    public static class Intake{

        public static int flapLeftMotorId = 1232;
        public static int flapRightMotorId = 43532;
        public static int holdMotorId = 201;
        public static int foldMotorId = 423;

        public static double motorStop = 0;

        //CHANGE THE MOTOR NUMBERS TO THEIR ACTUAL ONES
        public static double flapMotorRun = 0.3;
        public static double flapMotorSlow = 0.1;

        public static double foldMotorRun = 0.2;

        public static double holdMotorRun = 0.1;
        public static double holdMotorSlow = 0.05;

        public static double foldedEncoder = 384;
        public static double unfoldedEncoder = 87;
    }

    public static class Claw{
        //CHANGE MOTOR NUMBER TO THE ACTUAL ONE
        public static int motorID = 3;
        public static double run = 0.1;
        public static double runTime = 2000;
    }

    public static class AprilTagCamera{
        //The position of the globalshutter camera in robot cordinate system
        public static double xPosition = 0.46;
        public static double yPosition = 0;
        public static double zPosition = 0;

        public static double alignTolerance= 0.1; // Faris: random number idk actual value
        
        public static class Filter{
            public static double pitch = 2;
            public static double roll = 2;
            public static double distance = 2;
        }
    }
    public static class LimelightCamera {
        public static double alignTolerance= 0.1; // Faris: random number idk actual value
    }
    public static class IO{
        public static class Joy0{
            public static int joy0Id = 0;
            public static int forwardRawAxis = 0;
            public static int strafeRawAxis = 1;
            public static int rotAxis = 4;
            public static int rotationOverrideButton = 5;
            public static int thrustAxis = 3;
            public static int autoAlignAxis = 2;
            public static int autoBalanceButton = 4;
        }
        public static class Joy1{
            public static int joy1Id = 1;
            public static int overrrideEnabledButton = 8;
            public static int elevatorUpButton = 4;
            public static int elevatorDownButton = 1;
            public static int elevatorOverrideAxis = 1;
            public static int armOverrideAxis = 4;
            public static int deployRunIntakeAxis = 3;
            public static int clawDropPieceButton = 2;
        }
    }
    public static final class Arm {
        // Arm Motor Id's
        public static int motorID = 0;
        // Arm Encoder Id
        public static int encoderID = 0;
        // Arm Encoder Values
        public static int upperLimit = 10000;
        public static int lowerLimit = 0 ;
        // Arm Setpoint Values
        public static int retracted = 0;
        
        // public static int kBottomLevel = 1000;
        // public static int kMiddleLevel = 2000;
        // public static int kTopLevel = 3000;

        public static int conePrep = 7000;
        public static int coneReady = 6000;

        public static int cubePrep = 4000;
        public static int cubeReady = 5000;

        // Arm Pid Values
        public static double kP = 1;
        public static double kI = 0;
        public static double kD = 0;
        // Feedfoward Values
        public static double kS = 0; // static gain
        public static double kV = 0; // velocity gain
        public static double kG = 0; // gravity gain
        public static double desiredPos = 0;
    }
    
    public final static class Elevator {
        // Elevator Motor Id's
        public static int motorID = 20;
        // Elevator Encoder Values
        public static double upperLimit = 5000;
        public static double lowerLimit = 0;
        // Elevator Tolerance Values
        public static double tolerance = 500;
        // Extension Level Values
        public static double retracted = 0;
        public static double bottomLevel = 1000;
        public static double mediumLevel = 2000;
        public static double topLevel = 3000;
        // Elevator PID Values
        public static double kP = 1;
        public static double kI = 0;
        public static double kD = 0;
        // Elevator Feedforward Values
        public static double kS = 1;
        public static double kG = 0;
        public static double kV = 0;
        // Elevator gear ratio
        public static double gearRatio = 33./34.;
        // Elevator CANCoder IDs
        public static int coder1ID = 9;
        public static int coder2ID = 46;
    }

    public static class ColorSensor {
        // Proximity Threshold
        public static double proximityThreshold = 1700;
        // Cube RGB Values
        public static double cubeRed = 0.211181640625;
        public static double cubeGreen = 0.317626953125;
        public static double cubeBlue = 0.471435546875;
        // Cone RGB Values (not the cone)
        public static double coneRed = 0.3955078125;
        public static double coneGreen = 0.51025390625;
        public static double coneBlue = 0.0947265625;
    }
    public static class FieldPositions{
        public static double centerLine = 8.27;

        public static double inBlueCommunityX = 3-(SwerveSettings.SwerveConstants.trackWidth/2);
        public static double inRedCommunityX = 16.54-inBlueCommunityX;
        public static double inCommunityY = 8.02-2.5;

        // public static double atGridBlueX = 1.8;
        public static double atGridBlueX = 1.4 + (SwerveSettings.SwerveConstants.trackWidth/2);
        public static double atGridRedX = 16.54-atGridBlueX;

        public static class AutoAlignPositions{
            public static Translation2d blue0 = new Translation2d(2.2,8.02-3);
            public static Translation2d blue1 = new Translation2d(2.2,4.42);
            public static Translation2d blue2 = new Translation2d(2.2,8.02-4.125);
            public static Translation2d blue3 = new Translation2d(2.2,8.02-4.6875);
            public static Translation2d blue4 = new Translation2d(2.2, 8.02-5.25);
            public static Translation2d blue5 = new Translation2d(2.2, 8.02-5.8125);
            public static Translation2d blue6 = new Translation2d(2.2,8.02-6.375);
            public static Translation2d blue7 = new Translation2d(2.2,8.02-6.9375);
            public static Translation2d blue8 = new Translation2d(2.2,8.02-7.5);
            public static Translation2d blueAvoidChargerUppper = new Translation2d(5.5,8.02-3);
            public static Translation2d blueOutCommunityUppper = new Translation2d(4.0, 8.02-3);
            public static Translation2d blueInCommunityUppper = new Translation2d(2.0, 8.02-3);
            public static Translation2d blueAvoidChargerLower = new Translation2d(5.5,8.02-7.5);
            public static Translation2d blueOutCommunityLower = new Translation2d(4.0, 8.02-7.5);
            public static Translation2d blueInCommunityLower = new Translation2d(2.0, 8.02-7.5);

            public static Translation2d red0 = new Translation2d(14.34,8.02-3);
            public static Translation2d red1 = new Translation2d(14.34,8.02-3.5625);
            public static Translation2d red2 = new Translation2d(14.34,8.02-4.125);
            public static Translation2d red3 = new Translation2d(14.34,8.02-4.6875);
            public static Translation2d red4 = new Translation2d(14.34, 8.02-5.25);
            public static Translation2d red5 = new Translation2d(14.34, 8.02-5.8125);
            public static Translation2d red6 = new Translation2d(14.34,8.02-6.375);
            public static Translation2d red7 = new Translation2d(14.34,8.02-6.9375);
            public static Translation2d red8 = new Translation2d(14.34,8.02-7.5);
            public static Translation2d redAvoidChargerUppper = new Translation2d(11,8.02-3);
            public static Translation2d redOutCommunityUppper = new Translation2d(12.5, 8.02-3);
            public static Translation2d redInCommunityUppper = new Translation2d(14, 8.02-3);
            public static Translation2d redAvoidChargerLower = new Translation2d(11,8.02-7.5);
            public static Translation2d redOutCommunityLower = new Translation2d(12.5, 8.02-7.5);
            public static Translation2d redInCommunityLower = new Translation2d(14, 8.02-7.5);
        }   
    }

    public static DriverStation.Alliance color = DriverStation.getAlliance();
    public static boolean isBlue(){
        return color == DriverStation.Alliance.Blue;
    }
    public static boolean isRed(){
        return color == DriverStation.Alliance.Red;
    }
}