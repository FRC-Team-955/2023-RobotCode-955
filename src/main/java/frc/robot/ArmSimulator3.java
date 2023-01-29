package frc.robot;



import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.simulation.BatterySim;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;

public class ArmSimulator3 {
    private static final int kMotorPort = 0;
    private static final int kEncoderAChannel = 0;
    private static final int kEncoderBChannel = 1;
    private static final int kJoystickPort = 0;
  
    public static final String kArmPositionKey = "ArmPosition";
    public static final String kArmPKey = "ArmP";
    PIDController pid = new PIDController(Constants.Arm.kP, 
                                Constants.Arm.kI, 
                                Constants.Arm.kD); 
  
    // distance per pulse = (angle per revolution) / (pulses per revolution)
    //  = (2 * PI rads) / (4096 pulses)pid
    private static final double kArmEncoderDistPerPulse = 2.0 * Math.PI / 4096;
  
    // The arm gearbox represents a gearbox containing two Vex 775pro motors.
    private final DCMotor m_armGearbox = DCMotor.getVex775Pro(2);
  
    // Standard classes for controlling our arm
    private final Encoder m_encoder = new Encoder(kEncoderAChannel, kEncoderBChannel);
    private final CANSparkMax m_motor = new CANSparkMax(kMotorPort, MotorType.kBrushless);
    private final Joystick m_joystick = new Joystick(kJoystickPort);
  
    // Simulation classes help us simulate what's going on, including gravity.
    private static final double m_armReduction = 800;
    private static double m_armMass = 8.0; // Kilograms
    private static final double m_armLength = Units.inchesToMeters(30);
    // This arm sim represents an arm that can travel from -75 degrees (rotated down front)
    // to 255 degrees (rotated down in the back).
    private SingleJointedArmSim m_armSim =
        new SingleJointedArmSim(
            m_armGearbox,
            m_armReduction,
            SingleJointedArmSim.estimateMOI(m_armLength, m_armMass),
            m_armLength,
            Units.degreesToRadians(-75),
            Units.degreesToRadians(75),
            m_armMass,
            true,
            VecBuilder.fill(kArmEncoderDistPerPulse) // Add noise with a std-dev of 1 tick
            );
    private final EncoderSim m_encoderSim = new EncoderSim(m_encoder);

    ArmRobotCode armRobotCode;
    Timer timer;
    double lastVelocity = 0;
    
  
    // Create a Mechanism2d display of an Arm with a fixed ArmTower and moving Arm.
    private final Mechanism2d m_mech2d = new Mechanism2d(60, 60);
    private final MechanismRoot2d m_armPivot = m_mech2d.getRoot("ArmPivot", 30, 30);
    private final MechanismLigament2d m_armTower =
        m_armPivot.append(new MechanismLigament2d("ArmTower", 30, -90));
    private final MechanismLigament2d m_arm =
        m_armPivot.append(
            new MechanismLigament2d(
                "Arm",
                30,
                Units.radiansToDegrees(m_armSim.getAngleRads()),
                6,
                new Color8Bit(Color.kYellow)));
  
    public void robotInit() {
      m_encoder.setDistancePerPulse(kArmEncoderDistPerPulse);
  
      // Put Mechanism 2d to SmartDashboard
      SmartDashboard.putData("Arm Sim", m_mech2d);
      m_armTower.setColor(new Color8Bit(Color.kBlue));
    }
  
    public void simulationPeriodic() {
      // In this method, we update our simulation of what our arm is doing
      // First, we set our "inputs" (voltages)
      m_armSim.setInput(m_motor.get() * RobotController.getBatteryVoltage());
  
      // Next, we update it. The standard loop time is 20ms.
      m_armSim.update(0.020);
  
      // Finally, we set our simulated encoder's readings and simulated battery voltage
      m_encoderSim.setDistance(m_armSim.getAngleRads());
      // SimBattery estimates loaded battery voltages
      RoboRioSim.setVInVoltage(
          BatterySim.calculateDefaultBatteryLoadedVoltage(m_armSim.getCurrentDrawAmps()));
  
      // Update the Mechanism Arm angle based on the simulated arm angle
      m_arm.setAngle(Units.radiansToDegrees(m_armSim.getAngleRads()));
  
      SmartDashboard.putNumber("Encoder Postion", m_encoder.get());
      SmartDashboard.putNumber("Encoder Postion Modified", m_encoder.get()/11.3505);
      SmartDashboard.putNumber("Encoder scaling factor", m_encoder.get()/Units.radiansToDegrees(m_armSim.getAngleRads()) /11.3505);
    }
  
    public void teleopInit() {
        armRobotCode = new ArmRobotCode(m_motor, m_encoder, m_joystick);
    }
  
    public void teleopPeriodic() {
      armRobotCode.teleopPeriodic();
      if (m_joystick.getRawButton(5)) {
        moveArmManual();
      }
      if(m_joystick.getRawButtonPressed(4)) {
        if (m_armMass == 8) {
            m_armMass = 15;
        } else {
            m_armMass = 8;
        }
        m_armSim =
        new SingleJointedArmSim(
            m_armGearbox,
            m_armReduction,
            SingleJointedArmSim.estimateMOI(m_armLength, m_armMass),
            m_armLength,
            Units.degreesToRadians(-75),
            Units.degreesToRadians(75),
            m_armMass,
            true,
            VecBuilder.fill(kArmEncoderDistPerPulse) // Add noise with a std-dev of 1 tick
            );
      }
      SmartDashboard.putNumber("arm mass", m_armMass);

    }
    public void moveArmManual() {
      m_motor.set(m_joystick.getRawAxis(0));
    }
    public void disabledInit() {
      // This just makes sure that our simulation code knows that the motor's off.
      m_motor.set(0.0);
    }
}
