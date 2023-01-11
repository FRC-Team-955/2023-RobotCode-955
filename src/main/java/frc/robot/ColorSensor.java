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
        System.out.println("r" + colorSensor.getColor().red); // values from 0 to 1
        System.out.println("g" + colorSensor.getColor().green);
        System.out.println("b" + colorSensor.getColor().blue);
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

    public void senseDist() {
        System.out.println(colorSensor.getProximity());
    }
}
