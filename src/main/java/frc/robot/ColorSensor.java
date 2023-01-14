package frc.robot;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;

public class ColorSensor {
    // not sensing yellow right now for some reason (might need to rescan the cube)
    ColorSensorV3 colorSensor;
    ColorMatch colorMatch;
    Color cube = new Color(0.211181640625, 0.317626953125, 0.471435546875);

    public ColorSensor() {
        colorSensor = new ColorSensorV3(Port.kOnboard);
        colorMatch = new ColorMatch();
        colorMatch.addColorMatch(cube); 
        colorMatch.addColorMatch(Color.kYellow); // need to check rgb values for cone
    }

    public void senseColor() {
        System.out.println("r" + colorSensor.getColor().red); // values from 0 to 1
        System.out.println("g" + colorSensor.getColor().green);
        System.out.println("b" + colorSensor.getColor().blue);
        if(colorMatch.matchClosestColor(colorSensor.getColor()).color == cube) {
            System.out.println("violet");
        }
        else if(colorMatch.matchClosestColor(colorSensor.getColor()).color == Color.kYellow) {
            System.out.println("yellow");
        }
        else {
            System.out.println(" ");
        }
    }

    public void senseObj() { // boolean if using the if else in the function
        System.out.println(colorSensor.getProximity());
        // if(colorSensor.getProximity() > 1700) {
        //     return true;
        // }
        // else {
        //     return false;
        // }
    }
}
