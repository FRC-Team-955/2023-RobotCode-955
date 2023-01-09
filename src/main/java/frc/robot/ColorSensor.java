package frc.robot;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;

public class ColorSensor {
    ColorSensorV3 colorSensor;
    ColorMatch colorMatch;

    public ColorSensor() {
        colorSensor = new ColorSensorV3(Port.kOnboard);
        colorMatch = new ColorMatch();
        colorMatch.addColorMatch(Color.kViolet);
        colorMatch.addColorMatch(Color.kYellow);
    }

    public void senseColor() {
        System.out.println("r" + colorSensor.getRed());
        System.out.println("g" + colorSensor.getGreen());
        System.out.println("b" + colorSensor.getBlue());
        if(colorMatch.matchClosestColor(colorSensor.getColor()).color == Color.kViolet) {
            System.out.println("violet");
        }
        else if(colorMatch.matchClosestColor(colorSensor.getColor()).color == Color.kYellow) {
            System.out.println("yellow");
        }
        else {
            System.out.println(" ");
        }
    }

    public void senseObject() {
        colorSensor.getProximity();
    }
}
