package frc.robot;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Translation2d;

public class Drivebase {
    
    public static PIDController pid = new PIDController(Settings.kP, Settings.kI, Settings.kD);
    public static double currentAngle = 34; // get from gyroscope

    public static void driveFieldRelative(){

    }

    public static void driveFieldRelative(double rotation){
        
    }

    public static void driveFieldRelative(Translation2d translation, double rotation){
        
    }

    public static void driveRobotRelative(){

    }

    public static void driveRobotRelative(Translation2d translation, double rotation){
        
    }

    public static double autoBalance() {
       double output = MathUtil.clamp(pid.calculate(currentAngle, 0), -1 ,1);
       return output;
    }
    
    public boolean isBalanced() {
        if (2.5 <= currentAngle && currentAngle >= -2.5) {
            return true;
        } else {
            return false;
        }
    }
}

