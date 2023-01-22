package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public final class Joystick {
    static XboxController xboxController;
    public Joystick() {
        xboxController = new XboxController(0);
    }

    public static boolean isOverrideEnabled() {
        if (xboxController.getBButton() == false) {
            return false;
        } else {
            return true;
        }
    }
}
