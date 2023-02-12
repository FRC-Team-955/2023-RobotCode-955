package frc.robot.Subsystems;

import frc.robot.Constants;

public class ElevatorPosition {
    //Calculate position of elevator based on two encoders, one measuring the shaft of rotation, and the other one measuring the same thing at a 31/30 gear ratio
    //firstPos = actualPos - 360*rotations, secondPos = actualPos * a/b - 360 * rotations2, rotations2 = rotations (+1)
    //b/a secondPos = actualpos - b/a * 360 * rotations2
    //actualPos = firstPos (mod 360), actualPos = b/a * secondPos (mod 360*b/a)
    //firstPos + 360rot = b/a * secondPos +360*b/a rot
    //firstPos - b/a secondPos = (b/a-1)*360 rot
    //https://docs.google.com/spreadsheets/d/1vmimM9WY0oizPk8pCv3PD8BcboQV6MVU4hQiDylCEdw/edit?usp=sharing
    //google sheet by ethan (edited by me a bit) w/ simple calculations

    public static double calculate(double firstPos, double secondPos) {
        double rotations = (double) Math.round(((firstPos - 1/Constants.Elevator.kGearRatio * secondPos) / ((1/Constants.Elevator.kGearRatio - 1) * 360)) % 34);
        if (rotations < 0) {rotations = rotations + 34;}
        return 5*rotations + 5*firstPos/360;
    }
}
