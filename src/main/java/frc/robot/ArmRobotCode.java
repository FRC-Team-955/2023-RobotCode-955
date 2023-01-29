package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;

public class ArmRobotCode {
    public Arm arm;
    public Joystick joystick;
    public int level = 0;

    public ArmRobotCode(CANSparkMax motor, Encoder encoder, Joystick joystick) {
        arm = new Arm(motor, encoder);
        this.joystick = joystick;
    }
    public void teleopPeriodic() {
        if (joystick.getRawButtonPressed(3)) {
            level = (level + 1) % 4;
        }
        arm.setArm(level, joystick.getRawAxis(0));
    } 
}
