package frc.robot.Sensors;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Constants;

public class ColorSensor {
    static ColorSensorV3 colorSensor;
    static ColorMatch colorMatch;
    static Color cube = new Color(Constants.ColorSensor.kCubeRed, Constants.ColorSensor.kCubeGreen, Constants.ColorSensor.kCubeBlue);
    static Color cone = new Color(Constants.ColorSensor.kConeRed, Constants.ColorSensor.kConeGreen, Constants.ColorSensor.kConeBlue); // not actually the cone

    public ColorSensor() {
        colorSensor = new ColorSensorV3(Port.kOnboard);
        colorMatch = new ColorMatch();
        colorMatch.addColorMatch(cube); 
        colorMatch.addColorMatch(cone); // need to check rgb values for cone
    }

    static public String getColor() {
        // System.out.println("r" + colorSensor.getColor().red); // values from 0 to 1
        // System.out.println("g" + colorSensor.getColor().green);
        // System.out.println("b" + colorSensor.getColor().blue);

        if(colorMatch.matchClosestColor(colorSensor.getColor()).color == cube) {
            // System.out.println("p");
            return "purple";
        } else if(colorMatch.matchClosestColor(colorSensor.getColor()).color == cone) {
            // System.out.println("y");
            return "yellow";
        } else {
            return "yellow";
        }
    }

    static public boolean senseObj() { // boolean if using the if else in the function
        System.out.println(colorSensor.getProximity());
        if(colorSensor.getProximity() > Constants.ColorSensor.kProximityThreshold) {
            return true;
        }
        else {
            return false;
        }
    }
}