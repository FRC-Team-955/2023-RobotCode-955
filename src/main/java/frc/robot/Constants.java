package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;

public class Constants {
    public static class IO{
        public static class Joy0{
            public static final int joy0Id = 0;
            public static int forwardRawAxis = 0;
            public static int strafeRawAxis = 1;
            public static int rotAxis = 4;
            public static int autoHeadingButton = 6;
            public static int thrustAxis = 2;
            public static int autoAlignAxis = 3;
            public static int autoBalanceButton = 4;
        }
        public static class Joy1{
            public static final int joy1Id = 1;
            public static int overrrideEnabledButton = 8;
            public static int elevatorUpButton = 4;
            public static int elevatorDownButton = 1;
            public static int elevatorOverrideAxis = 1;
            public static int armOverrideAxis = 4;
            public static int deployRunIntakeAxis = 3;
            public static int clawDropPieceAxis = 2;
            public static int clawDropPieceButton = 2;
        }
    }
    // Auto Balance PID
    // Note from owen: we will have soooo many pids these need to be named better
    public static class Drivebase{

        public static double autoBalancekP = 1;
        public static double autoBalancekI = 0;
        public static double autoBalancekD = 0;

        public static Translation2d autoBalanceStop = new Translation2d(0,0);
        public static Translation2d autoBalanceForward = new Translation2d(0,0.1);
        public static Translation2d autoBalanceBackward = new Translation2d(0,-0.1);

        public static double turnRate = 1.3;

        public static double speed = 0.7;
        public static double turnSpeed = 0.7;
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

        //CHANGE THE MOTOR NUMBERS TO THEIR ACTUAL ONES
        public static double flapMotorRun = 0.3;
        public static double flapMotorSlow = 0.1;

        public static double foldMotorRun = 0.2;

        public static double holdMotorRun = 0.1;
        public static double holdMotorSlow = 0.05;

        public static double foldedEncoder = 384;
        public static double unfoldedEncoder = 87;

        public static double ethanWheelsKp = 0.1;
        public static double foldMotorKp = 0.1;
        public static double foldMotorKs = 2;
    }

    public static class Claw{
        //CHANGE MOTOR NUMBER TO THE ACTUAL ONE
        public static int motorID = 21;
        public static double run = -1;
        public static double runTime = 2000;
        public static double stopish = -0.1;
        public static double drop = 0.1;
    }

    public static class AprilTagCamera{
        //The position of the globalshutter camera in robot cordinate system
        public static double xPosition = 0.1016;
        public static double yPosition = -0.1778;
        public static double zPosition = 0.3175;

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
    
    public static final class Arm {
        // Arm Motor Id's
        public static int motorID = 19;
        // Arm Encoder Values
        public static double angleOffset = 109.37;
        // public static double angleOffset = 0;
        public static double upperLimit = 87.5;
        public static double lowerLimit = -111.4;
        // Arm Setpoint Values
        public static int retracted = -100;
        
        // public static int kBottomLevel = 1000;
        // public static int kMiddleLevel = 2000;
        // public static int kTopLevel = 3000;

        public static int level = 0;
        public static int conePrep = 25;
        public static int coneReady = 13;

        public static int cubePrep = 10;
        public static int cubeReady = 0;

        public static int hybrid = -95;

        public static int doubleSubstation = -5;

        // Arm Pid Values
        public static double kP = 0.3;
        public static double kI = 0;
        public static double kD = 0;
        // Feedfoward Values
        public static double kS = 0; // static gain
        public static double kV = 0; // velocity gain
        public static double kG = 0.3; // gravity gain
        public static double desiredPos = 0;
    }
    
    public final static class Elevator {
        // Elevator Motor Id's
        public static int motorID = 20;
        // Elevator Encoder Values
        public static double upperLimit = 28;
        public static double lowerLimit = 5;
        // Elevator Tolerance Values
        public static double tolerance = 500;
        // Extension Level Values
        public static double retracted = 1;
        public static double low = 10;
        public static double mid = 25;
        public static double high = 30;
        public static double doubleSubstationPosition = 30;
        // Elevator PID Values
        public static double kP = 0.8;
        public static double kI = 0;
        public static double kD = 0;
        // Elevator Feedforward Values
        public static double kS = 0;
        public static double kG = 0.4;
        public static double kV = 0;
        // Elevator gear ratio
        public static double gearRatio = 33./34.;
        // Elevator CANCoder IDs
        public static int coder1ID = 9;
        public static int coder2ID = 25;
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

    public static final double bumperWidth = 0.93;
    public static final double bumperLength = 0.81;

    public static class FieldPositions{
        public static final double fieldX = 16.537877;
        public static final double fieldY = 8.016177;
        // public static double fieldY = 8.02;

        public static final double centerLine = 8.272113;

        // public static double inBlueCommunityX = 3-(bumperWidth/2);
        //The edge of the charge station - the width of the robot
        public static double inBlueCommunityX = 2.919396-(bumperWidth/2);
        public static final double inRedCommunityX = fieldX-inBlueCommunityX;
        public static final double inCommunityY = 5.5;

        public static double inRedLoadingZoneX = 6.710363;
        public static double inBlueLoadingZoneX = fieldX - inRedLoadingZoneX;
        public static double inLoadingZoneY = inCommunityY + (bumperLength/2);

        // public static double atGridBlueX = 1.8;
        // public static double atGridBlueX = 1.377950 + (bumperWidth/2);
        public static double atGridOffset =-0;
        public static double atGridOffsetCube = 1;
        //offset
        public static double atGridBlueX =1.377950 + atGridOffset +(bumperWidth/2);
        public static double atGridRedX = fieldX-atGridBlueX;

        public static double atSubstationRedX = 1.64;
        public static double atSubstationBlueX =fieldX - 1.64;

        public static class AutoAlignPositions{
            public static Translation2d blue0 = new Translation2d(2.16,4.983099);
            public static Translation2d blue1 = new Translation2d(2.16,4.424426);
            public static Translation2d blue2 = new Translation2d(2.16,3.865499);
            public static Translation2d blue3 = new Translation2d(2.16,3.306699);
            public static Translation2d blue4 = new Translation2d(2.16,2.747899);
            public static Translation2d blue5 = new Translation2d(2.16,2.189099);
            public static Translation2d blue6 = new Translation2d(2.16,1.630299);
            public static Translation2d blue7 = new Translation2d(2.16,1.071499);
            public static Translation2d blue8 = new Translation2d(2.16,0.512699);
            public static Translation2d blueAvoidChargerUppper = new Translation2d(5.5,8.02-3);
            public static Translation2d blueOutCommunityUppper = new Translation2d(4.0, 8.02-3);
            public static Translation2d blueInCommunityUppper = new Translation2d(2.0, 8.02-3);
            public static Translation2d blueAvoidChargerLower = new Translation2d(5.5,8.02-7.5);
            public static Translation2d blueOutCommunityLower = new Translation2d(4.0, 8.02-7.5);
            public static Translation2d blueInCommunityLower = new Translation2d(2.0, 8.02-7.5);

            public static Translation2d red0 = new Translation2d(fieldX - 2.16, blue8.getY());
            public static Translation2d red1 = new Translation2d(fieldX - 2.16, blue7.getY());
            public static Translation2d red2 = new Translation2d(fieldX - 2.16, blue6.getY());
            public static Translation2d red3 = new Translation2d(fieldX - 2.16, blue5.getY());
            public static Translation2d red4 = new Translation2d(fieldX - 2.16, blue4.getY());
            public static Translation2d red5 = new Translation2d(fieldX - 2.16, blue3.getY());
            public static Translation2d red6 = new Translation2d(fieldX - 2.16, blue2.getY());
            // public static Translation2d red7 = new Translation2d(fieldX - 2.16, blue1.getY());
            public static Translation2d red7 = new Translation2d(fieldX - 2.16, blue1.getY());
            public static Translation2d red8 = new Translation2d(fieldX - 2.16, blue0.getY());
            //6.9375
            public static Translation2d redAvoidChargerUppper = new Translation2d(11,8.02-3);
            public static Translation2d redOutCommunityUppper = new Translation2d(12.5, 8.02-3);
            public static Translation2d redInCommunityUppper = new Translation2d(14, 8.02-3);
            public static Translation2d redAvoidChargerLower = new Translation2d(11,8.02-7.5);
            public static Translation2d redOutCommunityLower = new Translation2d(12.5, 8.02-7.5);
            public static Translation2d redInCommunityLower = new Translation2d(14, 8.02-7.5);



            public static Translation2d redLeftDoubleSubstation = new Translation2d(2.1, 6.028182);
            public static Translation2d redRightDoubleSubstation = new Translation2d(2.1, 7.471220);
            public static Translation2d blueLeftDoubleSubstation = new Translation2d(fieldX-redLeftDoubleSubstation.getX(), 7.471220);
            public static Translation2d blueRightDoubleSubstation = new Translation2d(fieldX-redRightDoubleSubstation.getX(), 6.028182);
        }   
    }

    // public static DriverStation.Alliance color = DriverStation.getAlliance();
    public static DriverStation.Alliance color = DriverStation.Alliance.Blue;
    public static boolean isBlue(){
        return false;
        // return color == DriverStation.Alliance.Blue;
    }
    public static boolean isRed(){
        return true;
        // return color == DriverStation.Alliance.Red;
    }
}