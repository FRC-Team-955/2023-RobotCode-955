package frc.robot;
import edu.wpi.first.wpilibj.Joystick;

public class IO {
    private static Joystick joy0 = new Joystick(0);
    private static Joystick joy1 = new Joystick(1);
    public static boolean isAutoAlignActive() {
        return true;
    }

    public static boolean isAutoBalanceActive(){
        return false;
    }
    public static boolean isOverrrideEnabled() {
        return false;
    } 
};
