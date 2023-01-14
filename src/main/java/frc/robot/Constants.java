package frc.robot;

public final class Constants {
    
    public final static class Elevator {
        // Motor Id's
        public static int kElevatorMotorId = 0;
        // Motor Encoder Values
        public static double kTopLimit = 5000;
        public static double kBotLimit = 0;
        // Extension Level Values
        public static double kRetracted = 0;
        public static double kBottomLevel = 1000;
        public static double kMediumLevel = 2000;
        public static double kTopLevel = 3000;
        // PID Values
        public static double kP = 1;
        public static double kI = 0;
        public static double kD = 0;
    }
    
}

