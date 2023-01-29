package frc.robot;
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
        }
        public static class joy1{
            public static int joy1Id = 1;
        }
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
