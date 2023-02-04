package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.Sensors.ColorSensor;
import frc.robot.Subsystems.*;

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
  
  @Override
  public void robotInit() {
    Arm.setup();
    Elevator.setup();
    Intake.setup();
    Claw.setup();
  }

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
<<<<<<< Updated upstream

    selectTeleopState();
    teleopAllState();
    switch(robotState){
      case AUTO_ALIGN:
      //move the auto align into game pience along with the arm code
        if (AutoAlign.moveToGridPosition()){
          //run drop function
        }
      case AUTO_BALANCE:
        Drivebase.autoBalance();
      default: // DRIVE
        AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
        Drivebase.driveFieldRelative();
    }
=======
    Drivebase.driveFieldRelativeHeading(IO.Drivebase.getSwerveTranslation(), 180);
    Drivebase.updateSwerveOdometry();
    System.out.println("HL" + AprilTagCameraWrapper.getHorizontalOffset());

    
    // selectTeleopState();
    // teleopAllState();
    // switch(robotState){
    //   case AUTO_ALIGN:
    //     autoAlign.moveToGridPosition();
    //   default: // DRIVE
    //     Drivebase.driveFieldRelative();
    // }
>>>>>>> Stashed changes
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
