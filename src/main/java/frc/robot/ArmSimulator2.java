
package frc.robot;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.simulation.BatterySim;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class ArmSimulator2 {
  private static final int kMotorPort = 0;
  private static final int kEncoderAChannel = 0;
  private static final int kEncoderBChannel = 1;
  private static final int kJoystickPort = 0;

  // The P gain for the PID controller that drives this arm.
  private static double kArmKp = 10.0;

  // distance per pulse = (angle per revolution) / (pulses per revolution)
  //  = (2 * PI rads) / (4096 pulses)
  private static final double kArmEncoderDistPerPulse = 2.0 * Math.PI / 4096;

  // Standard classes for controlling our arm
  private final PIDController m_controller = new PIDController(kArmKp, 0, 0);
  private final Encoder m_encoder = new Encoder(kEncoderAChannel, kEncoderBChannel);
  private final PWMSparkMax m_motor = new PWMSparkMax(kMotorPort);
  private final Joystick m_joystick = new Joystick(kJoystickPort);

  // This arm sim represents an arm that can travel from -75 degrees (rotated down front)
  // to 255 degrees (rotated down in the back).
  private final EncoderSim m_encoderSim = new EncoderSim(m_encoder);


  public void robotInit() {
    m_encoder.setDistancePerPulse(kArmEncoderDistPerPulse);
    SmartDashboard.putData("Arm Sim", CreateArmSim.m_mech2d);

    CreateArmSim.setupDisplay();
  }

  public void simulationPeriodic() {
    // In this method, we update our simulation of what our arm is doing
    // First, we set our "inputs" (voltages)
    CreateArmSim.m_armSim.setInput(m_motor.get() * RobotController.getBatteryVoltage());

    // Next, we update it. The standard loop time is 20ms.
    CreateArmSim.m_armSim.update(0.020);;

    // Finally, we set our simulated encoder's readings and simulated battery voltage
    m_encoderSim.setDistance(CreateArmSim.m_armSim.getAngleRads());
    // SimBattery estimates loaded battery voltages
    RoboRioSim.setVInVoltage(
        BatterySim.calculateDefaultBatteryLoadedVoltage(CreateArmSim.m_armSim.getCurrentDrawAmps()));

    // Update the Mechanism Arm angle based on the simulated arm angle
    CreateArmSim.setupDisplay();

    SmartDashboard.putNumber("Encoder Postion", m_encoder.get()/11.3505);
    SmartDashboard.putNumber("Encoder scaling factor", m_encoder.get()/Units.radiansToDegrees(CreateArmSim.m_armSim.getAngleRads()) /11.3505);
  }

  public void teleopInit() {}

  public void teleopPeriodic() {
    /*if (m_joystick.getRawAxis(0)>0.9) {
      // Here, we run PID control like normal, with a constant setpoint of 75 degrees.
      var pidOutput =
          m_controller.calculate(m_encoder.get(), armPositionDeg+75);
      m_motor.setVoltage(pidOutput);
    } else {
      // Otherwise, we disable the motor.
      m_motor.set(0.0);
    }*/
    var pidOutput = m_controller.calculate(m_encoder.get(), m_encoder.get() + m_joystick.getRawAxis(0));
    m_motor.setVoltage(pidOutput);
  }

  public void disabledInit() {
    m_motor.set(0.0);
  }
}
