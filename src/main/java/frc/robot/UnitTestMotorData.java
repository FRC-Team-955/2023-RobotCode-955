package frc.robot;

public class UnitTestMotorData {
    public int UnitsPerRotation;

    public double RPS;

    public UnitTestMotorData() {
        UnitsPerRotation = 0;
        RPS = 0;
    }

    public UnitTestMotorData(int _UnitsPerRotation, double _RPS) {
        UnitsPerRotation = _UnitsPerRotation;
        RPS = _RPS;
    }
}
