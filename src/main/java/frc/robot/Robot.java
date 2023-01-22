package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Sensors.ColorSensor;

public class Robot extends TimedRobot {
  // Objects
  //static Arm arm = new Arm();
  //static ColorSensor colorSensor = new ColorSensor();
  //static RobotState robotState;

  static Elevator Elevator = new Elevator();

  Joystick joystick = new Joystick(0);
  
  // Robot States
  public enum RobotState {
    DRIVING,
    AUTO_ALIGN,
    AUTO_BALANCE
  }

  @Override
  public void robotInit() {}

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
    //selectTeleopState();

    if (joystick.getRawButton(0))
      Elevator.setElevator(0);
    if (joystick.getRawButton(1))
      Elevator.setElevator(1);
    if (joystick.getRawButton(2))
      Elevator.setElevator(2);
    if (joystick.getRawButton(3))
      Elevator.setElevator(3);
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}

  public void selectTeleopState(){

    if (IO.isAutoAlignActive()) {
      //robotState = RobotState.AUTO_ALIGN;
    } else if (IO.isAutoBalanceActive()){
      //robotState = RobotState.AUTO_BALANCE;
    } else {
      //robotState = RobotState.DRIVING;
    }
  }
}
