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

        public static double atGridBlue = 1;
        public static double atGridRed = 7;

        public static class AutoAlignPositions{

            public static Translation2d blue0 = new Translation2d(2.0,2.0);
            public static Translation2d red0 = new Translation2d(6,2);
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
