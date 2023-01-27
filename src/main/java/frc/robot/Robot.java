package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
  ElevatorSimulator elevatorSim;

  @Override
  public void robotInit() {
    elevatorSim = new ElevatorSimulator();
    elevatorSim.robotInit();
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    elevatorSim.teleopPeriodic();
  }

  @Override
  public void disabledInit() {
    elevatorSim.disabledInit();
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {
    elevatorSim.simulationPeriodic();
  }
}