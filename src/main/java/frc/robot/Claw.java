package frc.robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class Claw {
    TalonSRX motorOne = new TalonSRX(1);
    TalonSRX motorTwo = new TalonSRX(2);
    JoystickButton buttonOne = new JoystickButton(null, 0)
    buttonOne.getRawButton(5, 1);
}