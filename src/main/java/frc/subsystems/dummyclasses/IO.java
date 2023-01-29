package frc.subsystems.dummyclasses;

import edu.wpi.first.wpilibj.Joystick;

public final class IO {
    public static Joystick joy0 = new Joystick(0);
    private static Joystick joy1 = new Joystick(1);
    
    private static Joystick key0 = new Joystick(2);
    private static Joystick key1 = new Joystick(3);

    public static class Drivebase{
        public static boolean isAutoAlignActive() {
            return joy0.getRawButton(Constants.IO.joy0.autoAlignButton);
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
          return joy1.getRawAxis(Constants.IO.joy1.elevatorOverrideAxis);
        }

        public static boolean rotationOverrideEnabled(){
            return true;
        }
        public static boolean thrustEnabled(){
            return true;
        }
    }
    public static void rumbleJoy0(){    }

    public static boolean isConeNodePosition = true;
    
    private static enum GridRowPosition{
        Low,
        Mid,
        High
    }
    public static GridRowPosition gridRowPosition = GridRowPosition.Low;

    public static void keyInputRowPosition(){}

    public static void keyInputOdometryMapping() {}
}