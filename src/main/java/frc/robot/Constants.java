package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class Constants {
    public static class IO{
        public static class Joy0{
            public static final int joy0Id = 0;
            public static final double swerveDeadband = 0.15;
            public static int forwardRawAxis = 0;
            public static int strafeRawAxis = 1;
            public static int rotAxis = 4;
            public static int robotRelativeButton = 5;
            public static int autoHeadingButton = 8;
            public static int thrustAxis = 2;
            public static int autoAlignAxis = 3;
            public static int autoBalanceButton = 4;
            public static int resetAngleButton =6;
            public static int robotForwardMoveButton = 2;
        }
        public static class Joy1{
            public static final int joy1Id = 1;
            public static int overrrideEnabledButton = 8;
            public static int elevatorUpButton = 4;
            public static int elevatorDownButton = 1;
            public static int elevatorOverrideAxis = 1;
            public static int armOverrideAxis = 5;
            public static int deployRunIntakeAxis = 2;
            public static int clawDropPieceAxis = 3;
        }
        public static class Key2{
            public static int clawDropPieceButton = 3;
            public static int intakeSequenceConeButton = 4;
            public static int runIntakeInButton = 7;
            public static int manualCubeRetractButton = 9;
            public static int isPowerSavingButton = 10;
            public static int intakeSequenceCubeButton = 11;
            public static int doubleSubstationExtension = 12;

        }
    }
    public static class Drivebase{
        public static double autoBalancekP = 0.6;
        public static double autoBalancekI = 0;
        // public static double autoBalancekD = 0.6;
        public static double autoBalancekD = 0.4;

        public static Translation2d autoBalanceStop = new Translation2d(0,0);
        public static Translation2d autoBalanceForward = new Translation2d(0,0.5);
        public static Translation2d autoBalanceBackward = new Translation2d(0,-0.5);
        //Aiden
        // public static double turnRate = 2;
        //Alex
        // public static double turnRate = 1;

        public static double turnRate = 0.5;

        public static double slowSpeed = 0.7;
    }
    public static double forwardRateLimiter = 0;
    public static double strafeRateLimiter = 0;

    public static class Auto{
        public static double prepLeaveOffset = 1;
        public static double communitySideOut = 2;
        public static double intakeRunTime = 0;
        public static double visionWaitTime = 0;
        public static double noHitGridOffset = 0.32 + 0.1778 - 0.05;
        // public static double noHitGridOffset = 0;
        // public static double notHitSideWall = 0.2;
        public static double notHitSideWall = 0.3;
        public static double notHitSideWallRedLeft = 0.21;
        public static double stopBeforeGamepieceOffset = 1;

        //are driver relative changes
        public static double blueGamePieceOffset0X =0.4;
        public static double blueGamePieceOffset0Y =1.4;
        public static double blueGamePieceOffset1X =0;
        public static double blueGamePieceOffset1Y =0;
        public static double blueGamePieceOffset2X =0;
        public static double blueGamePieceOffset2Y =0;
        public static double blueGamePieceOffset3X =0.6;
        public static double blueGamePieceOffset3Y =1;

        public static double redGamePieceOffset0X =0.6;
        public static double redGamePieceOffset0Y =0.6;
        // sorry this needs to be dec by 0.1
        public static double redGamePieceOffset1X =0;
        public static double redGamePieceOffset1Y =0;
        public static double redGamePieceOffset2X =0;
        public static double redGamePieceOffset2Y =0;
        public static double redGamePieceOffset3X =0.4;
        public static double redGamePieceOffset3Y =0.6;
        public static double autoXkP = 2;
        public static double autoYkP = 2;

    }

    public static class AutoAlign{
        //final for 1.16 meters out (from center of robot)
        public static final double aprilTagAlignXkP = 0.045; 
        public static final double aprilTagAlignXkI = 0;
        public static final double aprilTagAlignXkD = 0;
        //need to test
        public static double limelightAlignXkP = 0.05;
        public static double limelightAlignXkI = 0;
        public static double limelightAlignXkD = 0;

        public static final double gamePieceAutoAlignXkP = 0.05;
        public static final double gamePieceAutoAlignXkI = 0;
        public static final double gamePieceAutoAlignXkD = 0;

        public static double gamePieceTelopAlignXkP = 0.05;
        public static double gamePieceTelopAlignXkI = 0;
        public static double gamePieceTelopAlignXkD = 0;

        public static final double odometryAlignXkP = 4;
        public static final double odometryAlignXkI = 0;
        public static final double odometryAlignXkD = 0;
        
        public static final double odometryAlignYkP = 4;
        public static final double odometryAlignYkI = 0;
        public static final double odometryAlignYkD = 0;

        public static double translationAlignXkP = 3;
        public static double translationAlignXkI = 0;
        public static double translationAlignXkD = 0;
        
        public static double translationAlignYkP = 3;
        public static double translationAlignYkI = 0;
        public static double translationAlignYkD = 0;

        //not sure was one 0.1 before
        public static double alignTolerance = 0.025;
        public static double alignTranslationY = 0.025;
        public static double alignTranslationX = 0.17;

        public static double cubePreemptiveExtension = 0.7;
        public static double cubePreemptiveDrop = 0.1;
        public static double conePreemptiveExtension = 0.4;

        public static double alignedGamePiecePitch = 6.32; //fake value need to make a xy plane
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

    public static class IntakeV2{
        public static int handOffMotorID = 18;
        public static int motorLeftID = 60;
        public static int motorRightID = 61;

        public static double Kp = 0.2;
        public static double Ki = 0.01;
        public static double Kd = 0.01;

        public static double Ks = 0.4;

        public static double intakeAmpThreshhold = 30;
        public static double handOffMotorRunFullSpeed = 0.5;//.5
        public static double handOffMotorRun = 0.5;//.5
        public static double handOffMotorSlow = 0.2;//.4
        // public static double handoffMotorReverse = -0.15; //-.15
        public static double handoffMotorReverse = -0.4; //-.15

        public static double ExtendPosition = 1.2;
        public static double RetractPosition = 2.1;
        public static double HandoffPosition = 1.7;
    }

    public static class Claw{
        //CHANGE MOTOR NUMBER TO THE ACTUAL ONE
        public static int motorID = 21;
        public static double run = -1;
        public static double stopish = -0.3;
        public static double drop = 0.7;
        public static double dropFast = 1;
    }

    public static class AprilTagCamera{
        //The position of the globalshutter camera in robot cordinate system
        public static double xPosition = 0.1016;
        public static double yPosition = -0.1778;
        public static double zPosition = 0.3175;

        public static double alignTolerance= 0.1; // Faris: random number idk actual value
        
        public static class Filter{
            public static double pitch = 5;
            public static double roll = 5;
            public static double distance = 2;
        }
    }
    public static class LimelightCamera {
        public static double coneAlignTolerance= 1; // Faris: random number idk actual value
        public static double gamePieceTolerance = 2.5;

        public static double gamePieceVerticalToHorizontalA = 2.5677;
        public static double gamePieceVerticalToHorizontalB = 1.0672;
        public static double gamePieceVerticalToDistanceA = -1.36;
        public static double gamePieceVerticalToDistanceB = 5.36;
    }
    
    public static final class Arm {
        // Arm Motor Id's
        public static int motorID = 19;
        //
        public static int tolerance = 5;
        // Arm Encoder Values
        public static double angleOffset = -124.2786;
        public static double upperAngleOffset = 138.803417;
        // public static double angleOffset = 0;
        public static double upperLimit = 87.5;
        public static double lowerLimit = -111.4;
        // Arm Setpoint Values
        public static int retracted = -125;
        // public static int fullRetract = -125;
        
        // public static int kBottomLevel = 1000;
        // public static int kMiddleLevel = 2000;
        // public static int kTopLevel = 3000;

        public static int level = 0;
        
        public static int coneClosePrepMid = 50;
        public static int coneCloseReadyMid = 25;
        public static int coneFarPrepMid = 23;
        public static int coneFarReadyMid = 0;

        public static final int coneClosePrepHigh = 40;
        public static final int coneCloseReadyHigh = 20;

        public static final int coneFarPrepHigh = 42;
        public static final int coneFarReadyHigh = 22;
        public static int coneFarPrepHighAuto = 35;
        public static int coneFarReadyHighAuto = 15;
        // public static int conePrepHigh = 42;
        // public static int coneReadyHigh = 22;


        public static final int cubePrep = 25;
        public static final int cubeReady = 25;

        public static int hybrid = -85;
        public static double newHybrid = -30;

        public static int singleSubstation = 30;
        public static final double doubleSubstation = 50; //50.5

        public static int up = 120;

        public static int cubeIntake = -95;
        public static double cubeRetract = -130;

        public static double coneIntake = -120;

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
        public static double elevatorOffset = 1.04761993;
        // Elevator Encoder Values
        public static double upperLimit = 28;
        public static double lowerLimit = 5;
        // Elevator Tolerance Values
        public static double tolerance = 1.5;
        public static double upRetract = 3.5;
        // Extension Level Values
        public static double retracted = 4;
        public static double low = 10.4;
        // For Mid Cone
        public static double coneCloseMid = 0.4;
        public static double coneFarMid =18;
        public static final double coneCloseHigh = 28.5;
        public static final double highFarConeAndCube = 30; //edit
        public static double highFarConeAuto = 26;
        // public static double high = 28;
        // public static double doubleSubstationPosition = 30;
        public static double singleSubstation = 1;
        public static final double doubleSubstation = 4;
        
        public static double cubeIntake = 0.5;
        public static double cubeRetract = 4.5;

        public static double coneIntake = 10.4;

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

    public static class GamepieceManager{
        public static double clawExtraRunTime = 2;
    }
    public static final double bumperWidth = 0.93;
    public static final double bumperLength = 0.81;

    public static class FieldPositions{;
        public static final double fieldX = 16.537877 - 0.065 + 0.1778 + 0.05 - 0.17;
        public static final double fieldY = 8.016177;
        // public static double fieldY = 8.02;

        public static final double centerLine = 8.272113;

        //The edge of the charge station - the width of the robot
        public static double inBlueCommunityX = 2.919396-(bumperWidth/2);
        public static final double inRedCommunityX = fieldX-inBlueCommunityX;
        public static final double inCommunityY = 5.5;

        public static double inRedLoadingZoneX = 6.710363;
        public static double inBlueLoadingZoneX = fieldX - inRedLoadingZoneX;
        
        public static double inLoadingZoneY = inCommunityY;
        public static double noHitCubeOffset = 0;
        public static double nearGridOffset =-0.07;
        // public static double atAtGridOffset = -0.115;
        public static double atAtGridOffset = -0.17;
        // public static double atAtGridOffset = -0.2;
        // public static double atAtGridOffset = -0.2;
        // public static double atGridOffset =-0.08;
        //offset
        public static double nearGridBlueX =1.377950 +(bumperWidth/2) + nearGridOffset;
        public static double nearGridRedX = fieldX-nearGridBlueX;

        public static double atGridBlueX =1.377950 +(bumperWidth/2) + atAtGridOffset;
        public static double atGridRedX = fieldX-atGridBlueX;


        public static double atSubstationRedX = 1.17 - 0.07; //edit
        public static double atSubstationBlueX =fieldX - atSubstationRedX;

        public static double outSubstationShortBlue = 4;
        // public static double outSubstationShortBlue = 8;
        public static double outSubstationShortRed = fieldX - outSubstationShortBlue;
        public static double outSubstationLongBlue = 6;
        public static double outSubstationLongRed = fieldX - outSubstationLongBlue;

        public static class AutoAlignPositions{

            public static Translation2d blueGrid0 = new Translation2d(2.16 - 0.1778 + 0.05,4.983099);
            public static Translation2d blueGrid1 = new Translation2d(2.16 - 0.1778+ 0.05,4.424426);
            public static Translation2d blueGrid2 = new Translation2d(2.16- 0.1778+ 0.05,3.865499);
            public static Translation2d blueGrid3 = new Translation2d(2.16- 0.1778+ 0.05,3.306699);
            public static Translation2d blueGrid4 = new Translation2d(2.16- 0.1778+ 0.05,2.747899);
            public static Translation2d blueGrid5 = new Translation2d(2.16- 0.1778+ 0.05,2.189099);
            public static Translation2d blueGrid6 = new Translation2d(2.16- 0.1778+ 0.05,1.630299);
            public static Translation2d blueGrid7 = new Translation2d(2.16- 0.1778+ 0.05,1.071499);
            public static Translation2d blueGrid8 = new Translation2d(2.16- 0.1778+ 0.05,0.512699);
            public static Translation2d blueAvoidChargerUppper = new Translation2d(5.5,8.02-3);
            public static Translation2d blueOutCommunityUppper = new Translation2d(4.0, 8.02-3);
            public static Translation2d blueInCommunityUppper = new Translation2d(2.0, 8.02-3);
            public static Translation2d blueAvoidChargerLower = new Translation2d(5.5,8.02-7.5);
            public static Translation2d blueOutCommunityLower = new Translation2d(4.0, 8.02-7.5);
            public static Translation2d blueInCommunityLower = new Translation2d(2.0, 8.02-7.5);

            public static Translation2d redGrid0 = new Translation2d(fieldX - blueGrid8.getX(), blueGrid8.getY());
            public static Translation2d redGrid1 = new Translation2d(fieldX - blueGrid7.getX(), blueGrid7.getY());
            public static Translation2d redGrid2 = new Translation2d(fieldX - blueGrid6.getX(), blueGrid6.getY());
            public static Translation2d redGrid3 = new Translation2d(fieldX - blueGrid5.getX(), blueGrid5.getY());
            public static Translation2d redGrid4 = new Translation2d(fieldX - blueGrid4.getX(), blueGrid4.getY());
            public static Translation2d redGrid5 = new Translation2d(fieldX - blueGrid3.getX(), blueGrid3.getY());
            public static Translation2d redGrid6 = new Translation2d(fieldX - blueGrid2.getX(), blueGrid2.getY());
            public static Translation2d redGrid7 = new Translation2d(fieldX - blueGrid1.getX(), blueGrid1.getY());
            public static Translation2d redGrid8 = new Translation2d(fieldX - blueGrid0.getX(), blueGrid0.getY());

            // public static Translation2d blueGamePiece0 = new Translation2d(7.056,4.521+1.22);
            public static Translation2d blueGamePiece0 = new Translation2d(7.05837,4.583049);
            // public static Translation2d blueGamePiece1 = new Translation2d(7.056,3.328);
            public static Translation2d blueGamePiece1 = new Translation2d(7.05837,3.363849);
            public static Translation2d blueGamePiece2 = new Translation2d(7.058374,2.144649);
            // public static Translation2d blueGamePiece2 = new Translation2d(7.056,2.135);
            // public static Translation2d blueGamePiece3 = new Translation2d(7.056,0.92075);
            public static Translation2d blueGamePiece3 = new Translation2d(7.058374,0.925449);
            public static Translation2d redGamePiece0 = new Translation2d(fieldX-blueGamePiece0.getX(),blueGamePiece3.getY());
            public static Translation2d redGamePiece1 = new Translation2d(fieldX-blueGamePiece0.getX(),blueGamePiece2.getY());
            public static Translation2d redGamePiece2 = new Translation2d(fieldX-blueGamePiece0.getX(),blueGamePiece1.getY());
            public static Translation2d redGamePiece3 = new Translation2d(fieldX-blueGamePiece0.getX(),blueGamePiece0.getY());


            //6.9375
            public static Translation2d redAvoidChargerUppper = new Translation2d(11,8.02-3);
            public static Translation2d redOutCommunityUppper = new Translation2d(12.5, 8.02-3);
            public static Translation2d redInCommunityUppper = new Translation2d(14, 8.02-3);
            public static Translation2d redAvoidChargerLower = new Translation2d(11,8.02-7.5);
            public static Translation2d redOutCommunityLower = new Translation2d(12.5, 8.02-7.5);
            public static Translation2d redInCommunityLower = new Translation2d(14, 8.02-7.5);

            //4.9 For old
            //7 if okay
            //7.4 def
            public static double mobilityBlue = 7;
            public static double gamePieceBlue = 6.9;
            // public static double mobilityBlue = 6.6;
            // public static double mobilityBlue = 4.9;
            public static double mobilityRed =fieldX-mobilityBlue;
            public static double gamePieceRed = fieldX-gamePieceBlue;
            public static Translation2d chargeStationBlue = new Translation2d(4.9, 2.747899);
            public static Translation2d chargeStationRed = new Translation2d(fieldX-chargeStationBlue.getX(), 2.747899);

            public static Translation2d redLeftDoubleSubstation = new Translation2d(1.9, 6.028182);
            public static Translation2d redRightDoubleSubstation = new Translation2d(1.9, 7.471220);
            public static Translation2d redSingleSubstation = new Translation2d(2.315069, fieldY -1.17);
            public static Translation2d blueLeftDoubleSubstation = new Translation2d(fieldX-redLeftDoubleSubstation.getX(), redRightDoubleSubstation.getY());
            public static Translation2d blueRightDoubleSubstation = new Translation2d(fieldX-redRightDoubleSubstation.getX(), redLeftDoubleSubstation.getY());
            public static Translation2d blueSingleSubstation = new Translation2d(fieldX-redSingleSubstation.getX(),redSingleSubstation.getY());

            public static double oneStepDoubleSubstationSlope = 0.557851739352;
        }
    }

    // public static DriverStation.Alliance color = DriverStation.getAlliance();
    public static DriverStation.Alliance color = DriverStation.Alliance.Blue;
    public static boolean isBlue(){
        return DriverStation.getAlliance() == Alliance.Blue;
        // return true;
        // return color == DriverStation.Alliance.Blue;
    }
    public static boolean isRed(){
        return !isBlue();
        // return color == DriverStation.Alliance.Red;
    }
}