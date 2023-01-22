package frc.robot;

public class UnitTestCANSparkMaxEncoder {
    UnitTestCANSparkMax UnitTestMotor;

    public double getPosition() {
        return UnitTestMotor.position;
    }

    public double getVelocity() {
        return UnitTestMotor.velocity * UnitTestMotor.MotorData.UnitsPerRotation * UnitTestMotor.MotorData.RPS;
    }
}
