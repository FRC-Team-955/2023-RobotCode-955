package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Sensors.ColorSensor;

public class Robot extends TimedRobot {
  // Robot States
  public enum RobotState {
    DRIVING,
    AUTO_ALIGN,
    AUTO_BALANCE
  }
  // Objects
  static Arm arm = new Arm();
  static ColorSensor colorSensor = new ColorSensor();
  static RobotState robotState = RobotState.DRIVING;

  AutoAlign autoAlign = new AutoAlign();
  
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

  public void teleopAllState(){
    Drivebase.logData();
    IO.keyInputOdometryMapping();
    IO.keyInputRowPosition();
  }

  @Override
  public void teleopPeriodic() {

    selectTeleopState();
    teleopAllState();
    switch(robotState){
      case AUTO_ALIGN:
        if (autoAlign.moveToGridPosition()){
          //run auto arm code
        }
      case AUTO_BALANCE:

      default: // DRIVE
        AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
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

    if (IO.Drivebase.isAutoAlignActive()) {
      robotState = RobotState.AUTO_ALIGN;
    } 
    //once auto balance is added into false
    else if (IO.Drivebase.isAutoBalanceActive()){
      robotState = RobotState.AUTO_BALANCE;
    } else {
      robotState = RobotState.DRIVING;
    }
  }
}
