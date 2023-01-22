package frc.robot;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Timer;

public class UnitTestCANSparkMax {
    public double position;

    public double velocity;

    public UnitTestCANSparkMaxEncoder Encoder;

    Timer Timer;

    UnitTestMotorData MotorData;

    public void setVoltage(double voltage) {
        set(voltage / 12);
    }

    public void set(double value) {
        UpdatePosition();

        velocity = value;
    }

    void UpdatePosition() {
        position += velocity * Timer.get() * MotorData.RPS * MotorData.UnitsPerRotation;

        Timer.reset();
    }

    public UnitTestCANSparkMaxEncoder getEncoder() {
        return Encoder;
    }

    public UnitTestCANSparkMax(int Port, MotorType Type) {
        MotorData = UnitTestMotorDataInfo.MotorData[Port];

        position = 0;

        Encoder = new UnitTestCANSparkMaxEncoder();

        velocity = 0;

        Timer = new Timer();
        Timer.start();
    }
}
