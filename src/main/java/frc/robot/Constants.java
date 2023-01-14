package frc.robot;

public final class Constants {
    public static final class ArmConstants {
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
        public static double kPArm = 1;
        public static double kIArm = 0;
        public static double kDArm = 0;
    }
}

