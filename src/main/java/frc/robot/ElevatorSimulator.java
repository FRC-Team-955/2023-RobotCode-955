// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotController;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.simulation.BatterySim;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/** This is a sample program to demonstrate the use of elevator simulation with existing code. */
public class ElevatorSimulator{
  Elevator elevator;
  int level = 0;
  double maxVelocity = 0;

  private static final int kMotorPort = 0;
  private static final int kEncoderAChannel = 0;
  private static final int kEncoderBChannel = 1;
  private static final int kJoystickPort = 0;

  private static final double kElevatorGearing = 5.0;
  private static final double kElevatorDrumRadius = Units.inchesToMeters(2.0);
  private static final double kCarriageMass = Units.lbsToKilograms(20); // kg

  private static final double kMinElevatorHeight = Units.inchesToMeters(2);
  private static final double kMaxElevatorHeight = Units.inchesToMeters(50);

  // distance per pulse = (distance per revolution) / (pulses per revolution)
  //  = (Pi * D) / ppr
  private static final double kElevatorEncoderDistPerPulse =
      2.0 * Math.PI * kElevatorDrumRadius / 4096;

  private final DCMotor m_elevatorGearbox = DCMotor.getVex775Pro(4);

  // Standard classes for controlling our elevator
  private final Encoder m_encoder = new Encoder(kEncoderAChannel, kEncoderBChannel);
  private final CANSparkMax m_motor = new CANSparkMax(kMotorPort, MotorType.kBrushless);
  private final Joystick m_joystick = new Joystick(kJoystickPort);

  // Simulation classes help us simulate what's going on, including gravity.
  private final ElevatorSim m_elevatorSim =
      new ElevatorSim(
          m_elevatorGearbox,
          kElevatorGearing,
          kCarriageMass,
          kElevatorDrumRadius,
          kMinElevatorHeight,
          kMaxElevatorHeight,
          true,
          VecBuilder.fill(0.0001));
  private final EncoderSim m_encoderSim = new EncoderSim(m_encoder);

  // Create a Mechanism2d visualization of the elevator
  private final Mechanism2d m_mech2d = new Mechanism2d(20, 50);
  private final MechanismRoot2d m_mech2dRoot = m_mech2d.getRoot("Elevator Root", 10, 0);
  private final MechanismLigament2d m_elevatorMech2d =
      m_mech2dRoot.append(
          new MechanismLigament2d(
              "Elevator", Units.metersToInches(m_elevatorSim.getPositionMeters()), 90));

  public void robotInit() {
    m_encoder.setDistancePerPulse(kElevatorEncoderDistPerPulse);
    elevator = new Elevator(m_motor, m_encoder);
    // Publish Mechanism2d to SmartDashboard
    // To view the Elevator Sim in the simulator, select Network Tables -> SmartDashboard ->
    // Elevator Sim
    SmartDashboard.putData("Elevator Sim", m_mech2d);
  }

  public void simulationPeriodic() {
    // In this method, we update our simulation of what our elevator is doing
    // First, we set our "inputs" (voltages)
    m_elevatorSim.setInput(m_motor.get() * RobotController.getBatteryVoltage());

    // Next, we update it. The standard loop time is 20ms.
    m_elevatorSim.update(0.020);

    // Finally, we set our simulated encoder's readings and simulated battery voltage
    m_encoderSim.setDistance(m_elevatorSim.getPositionMeters());
    // SimBattery estimates loaded battery voltages
    RoboRioSim.setVInVoltage(
      BatterySim.calculateDefaultBatteryLoadedVoltage(m_elevatorSim.getCurrentDrawAmps())
      //12
    );

    // Update elevator visualization with simulated position
    m_elevatorMech2d.setLength(Units.metersToInches(m_elevatorSim.getPositionMeters()));
    SmartDashboard.putNumber("encoder postion", m_encoder.get());
    SmartDashboard.putNumber("Battery voltage", RoboRioSim.getVInVoltage());
  }

  public void teleopPeriodic() {
    // if (m_joystick.getRawButtonPressed(2)) {
    //   level = (level+1)%4;
    // }
    // elevator.setElevator(level);
    elevator.moveElevator(m_joystick.getRawAxis(1));
    SmartDashboard.putNumber("max velocity", maxVelocity);
    if (m_encoder.getRate() > maxVelocity) {
      maxVelocity = m_encoder.getRate();
    }
  }

  public void disabledInit() {
    // This just makes sure that our simulation code knows that the motor's off.
    m_motor.set(0.0);
  }
}