package frc.robot;

public final class Constants {

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
    public static class ColorSensor {
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
}

