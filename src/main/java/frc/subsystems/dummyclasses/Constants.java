package frc.subsystems.dummyclasses;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;

public class Constants {
    // Auto Balance PID
    // Note from owen: we will have soooo many pids these need to be named better
    public static double kP = 1;
    public static double kI = 0;
    public static double kD = 0;
    public static double forwardRateLimiter = 0;
    public static double strafeRateLimiter = 0;

    public static class AprilTagCamera{
        //The position of the globalshutter camera in robot cordinate system
        public static double xPosition = 0.5;
        public static double yPosition = 0;
        public static double zPosition = 0.5;

        public static double kAlignTolerance= 0.1; // Faris: random number idk actual value
        
        public static class Filter{
            public static double pitch = 2;
            public static double roll = 2;
            public static double distance = 2;
        }
    }
    public static class LimelightCamera {
        public static double kAlignTolerance= 0.1; // Faris: random number idk actual value
    }
    public static class IO{
        public static class joy0{
            public static int joy0Id = 0;
            public static int forwardRawAxis = 0;
            public static int strafeRawAxis = 1;
            public static int rotAxis = 4;
            public static int rotationOverrideButton = 0;
            public static int thrustAxis = 2;
            public static int autoAlignButton = 3;
        }
        public static class joy1{
            public static int joy1Id = 1;
            public static int elevatorOverrideAxis = 1;
        }
    }
    public static final class Arm {
        // Arm Motor Id's
        public static int kArmMotorId = 0;
        // Arm Encoder Id
        public static int kArmEncoderId = 0;
        // Arm Encoder Values
        public static int kArmUpperLimit = 1000;
        public static int kArmLowerLimit = 0 ;
        // Arm Setpoint Values
        public static int kRetracted = 0;
        public static int kBottomLevel = 1000;
        public static int kMiddleLevel = 2000;
        public static int kTopLevel = 3000;
        // Arm Pid Values
        public static double kP = 1;
        public static double kI = 0;
        public static double kD = 0;
        // Feedfoward Values
        public static double kS = 0; // static gain
        public static double kV = 0; // velocity gain
        public static double kG = 0; // gravity gain
        public static double desiredArmPos = 0;
    }
    
    public final static class Elevator {
        // Elevator Motor Id's
        public static int kElevatorMotorId = 0;
        // Elevator Encoder Values
        public static double kElevatorUpperLimit = 5000;
        public static double kElevatorLowerLimit = 0;
        // Elevator Tolerance Values
        public static double kElevatorTolerance = 500;
        // Extension Level Values
        public static double kRetracted = 0;
        public static double kBottomLevel = 1000;
        public static double kMediumLevel = 2000;
        public static double kTopLevel = 3000;
        // Elevator PID Values
        public static double kPElevator = 1;
        public static double kIElevator = 0;
        public static double kDElevator = 0;
        // Elevator Feedforward Values
        public static double kSElevator = 1;
        public static double kGElevator = 0;
        public static double kVElevator = 0;
        // Elevator gear ratio
        public static double kGearRatio = 33./34.;
    }

    public static class ColorSensor {
        // Proximity Threshold
        public static double kProximityThreshold = 1700;
        // Cube RGB Values
        public static double kCubeRed = 0.211181640625;
        public static double kCubeGreen = 0.317626953125;
        public static double kCubeBlue = 0.471435546875;
        // Cone RGB Values (not the cone)
        public static double kConeRed = 0.3955078125;
        public static double kConeGreen = 0.51025390625;
        public static double kConeBlue = 0.0947265625;
    }
    public static class FieldPositions{
        public static double centerLine = 4.01;
        public static double inBlueCommunityX = 2;
        public static double inRedCommunityX = 6;
        public static double inCommunityY = 2;

        public static double atGridBlueX = 1;
        public static double atGridRedX = 7;

        public static class AutoAlignPositions{
            public static Translation2d blue0 = new Translation2d(2.0,3);
            public static Translation2d blue1 = new Translation2d(2.0,3.5625);
            public static Translation2d blue2 = new Translation2d(2.0,4.125);
            public static Translation2d blue3 = new Translation2d(2.0,4.6875);
            public static Translation2d blue4 = new Translation2d(2.0, 5.25);
            public static Translation2d blue5 = new Translation2d(2.0, 5.8125);
            public static Translation2d blue6 = new Translation2d(2.0,6.375);
            public static Translation2d blue7 = new Translation2d(2.0,6.9375);
            public static Translation2d blue8 = new Translation2d(2.0,7.5);
            public static Translation2d blueAvoidChargerUppper = new Translation2d(5.5,3);
            public static Translation2d blueOutCommunityUppper = new Translation2d(4.0, 3);
            public static Translation2d blueInCommunityUppper = new Translation2d(2.0, 3);
            public static Translation2d blueAvoidChargerLower = new Translation2d(5.5,7.5);
            public static Translation2d blueOutCommunityLower = new Translation2d(4.0, 7.5);
            public static Translation2d blueInCommunityLower = new Translation2d(2.0, 7.5);

            public static Translation2d red0 = new Translation2d(14.0,3);
            public static Translation2d red1 = new Translation2d(14.0,3.5625);
            public static Translation2d red2 = new Translation2d(14.0,4.125);
            public static Translation2d red3 = new Translation2d(14.0,4.6875);
            public static Translation2d red4 = new Translation2d(14.0, 5.25);
            public static Translation2d red5 = new Translation2d(14.0, 5.8125);
            public static Translation2d red6 = new Translation2d(14.0,6.375);
            public static Translation2d red7 = new Translation2d(14.0,6.9375);
            public static Translation2d red8 = new Translation2d(14.0,7.5);
            public static Translation2d redAvoidChargerUppper = new Translation2d(11,3);
            public static Translation2d redOutCommunityUppper = new Translation2d(12.5, 3);
            public static Translation2d redInCommunityUppper = new Translation2d(14, 3);
            public static Translation2d redAvoidChargerLower = new Translation2d(11,7.5);
            public static Translation2d redOutCommunityLower = new Translation2d(12.5, 7.5);
            public static Translation2d redInCommunityLower = new Translation2d(14, 7.5);
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