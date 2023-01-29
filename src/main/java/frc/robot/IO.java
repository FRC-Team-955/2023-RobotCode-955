package frc.robot;
import edu.wpi.first.wpilibj.Joystick;

public class IO {
    public static Joystick joy0 = new Joystick(0);
    public static Joystick joy1 = new Joystick(1);

    public static boolean isAutoAlignActive() {
        return true;
    }

    public static boolean isAutoBalanceActive(){
        return false;
    }
    public static boolean isOverrrideEnabled() {
        if (joy0.getRawButton(0) == false) {
            return false;
        } else {
            return true;
        }
    } 
    public static double elevatorOverride(){
        return joy1.getRawAxis(Constants.IO.Joy1.elevatorOverrideAxis);
    }
};