package frc.robot;

public class Constants {
    public static class Intake{
        //CHANGE THE MOTOR NUMBERS TO THEIR ACTUAL ONES
        static double motorOutput = 0.3;
        static int motorOneNum = 1;
        static int motorTwoNum = 2;
        static int intakeFoldMotorNum = 4;
        static double intakeFoldMotorOutput = 0.1;
        static int intakeMotorStop = 0;
    }
    public static class Handoff{
        //CHANGE MOTOR NUMBER TO THE ACTUAL ONE
        static int handoffMotorOneNum = 3;
        static double handoffGamepieceHoldMotorOutput = 0.02;
        static double handoffMotorOutput = 0.2;
        static int handoffMotorStop = 0;
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
    public static class IO{
        public static class Joy1{
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
}