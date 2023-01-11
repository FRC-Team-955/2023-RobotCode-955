package frc.robot;
<<<<<<< Updated upstream
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

=======

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;
>>>>>>> Stashed changes
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class Claw {
    TalonSRX motorOne = new TalonSRX(1);
    TalonSRX motorTwo = new TalonSRX(2);
<<<<<<< Updated upstream
    JoystickButton buttonOne = new JoystickButton(null, 0)
    buttonOne.getRawButton(5, 1);
=======
    JoystickButton buttonOne = new JoystickButton(null, 0);
}

public boolean getRawButton(int button) {
    
>>>>>>> Stashed changes
}