package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

  public enum RobotState {
    DRIVING,
    AUTO_ALIGN,
    AUTO_BALANCE
  }

  RobotState robotState = RobotState.DRIVING;

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

  public void teleopAllState(){}

  @Override
  public void teleopPeriodic() {
    selectTeleopState();
    teleopAllState();
    switch(robotState){
      case AUTO_ALIGN:
      default: // DRIVE
        Drivebase.driveFieldRelative();
    }
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

    if (Joystick.isAutoAlignActive()) {
      robotState = RobotState.AUTO_ALIGN;
    } else if (Joystick.isAutoBalanceActive()){
      robotState = RobotState.AUTO_BALANCE;
    } else {
      robotState = RobotState.DRIVING;
    }
  }
}
