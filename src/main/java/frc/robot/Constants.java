package frc.robot;


public final class Constants {
   
    public final static class ElevatorConstants {
        // Elevator Motor Id's
        public static int kElevatorMotorId = 0;
        // Elevator Encoder Values
        public static double kElevatorUpperLimit = 5000;
        public static double kElevatorLowerLimit = 0;
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
   
}
