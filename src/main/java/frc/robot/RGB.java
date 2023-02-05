package frc.robot;

import edu.wpi.first.wpilibj.motorcontrol.Spark;

public class RGB {
    public static Spark RGB;

    public RGB() { 
        RGB = new Spark(0); // roborio PWM port number
    }

    public static void signalCube() {
        RGB.set(0.91); // violet
    }

    public static void signalCone() {
        RGB.set(0.69); // yellow
    }

    public static void turnOffSignal(){
        RGB.stopMotor();
    }
}
