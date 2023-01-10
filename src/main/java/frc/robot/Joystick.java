package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;

public final class Joystick {

    public static boolean isAutoAlignActive() {
        return true;
    }

    public static boolean isAutoBalanceActive(){
        return false;
    }

    public static double getSwerveRotation(){
        return 0;
    }

    public static Translation2d getSwerveTranslation(){
        return new Translation2d();
    }
}
