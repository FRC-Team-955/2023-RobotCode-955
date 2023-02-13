package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.robot.Sensors.ColorSensor;
import frc.robot.Subsystems.*;
import frc.robot.Sensors.*;

public class Robot extends TimedRobot {
  // Robot States
  public enum RobotState {
    DRIVING,
    AUTO_ALIGN,
    AUTO_BALANCE
  }
  
  static RobotState robotState = RobotState.DRIVING;
  
  @Override
  public void robotInit() {
    // Arm.setup();
    // Elevator.setup();
    // Intake.setup();
    // Claw.setup();
    AprilTagCameraWrapper.setUp();
    Drivebase.resetAnglesToAbsolute();
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    Drivebase.resetAnglesToAbsolute();
  }

  public void teleopAllState(){
    Drivebase.logData();
    IO.keyInputOdometryMapping();
    IO.keyInputRowPosition();
  }
  private final Field2d m_field = new Field2d();
  @Override
  public void teleopPeriodic() {
    selectTeleopState();
    teleopAllState();

    switch(robotState){
      case AUTO_ALIGN:
        GamepieceManager.autoPlace();
        Intake.foldInIntake();
      case AUTO_BALANCE:
        Drivebase.autoBalance();
        // Drivebase.autoBalanceBangBang();
        GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
        Intake.foldInIntake();
      default: // DRIVE
        AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
        GamepieceManager.loadSequence();
        GamepieceManager.manageExtension();
        Drivebase.drive();
    }

    System.out.println(Constants.isBlue());
    System.out.println("x" + Drivebase.getPose().getX());
    System.out.println("y" + Drivebase.getPose().getY());
    if (IO.Drivebase.thrustEnabled()){

      // AutoAlign.alignAprilTag();
      AutoAlign.moveToGridPosition();
    }
    // else if (IO.Drivebase.isAutoAlignActive()) {
    //   AutoAlign.alignOdometry(Constants.FieldPositions.AutoAlignPositions.blue1);
    // } else if (IO.Drivebase.rotationOverrideEnabled()){
    //   AutoAlign.moveIntoPosition();
    // }
    else {
      Drivebase.driveFieldRelativeHeading(IO.Drivebase.getSwerveTranslation(), 180);
      // Drivebase.driveFieldRelativeRotation(IO.Drivebase.getSwerveTranslation(), IO.Drivebase.getSwerveRotation());
    }
    Drivebase.updateSwerveOdometry();
    // Drivebase.driveFieldRelativeHeading(new Translation2d(0, 0), 180);
  
    //System.out.println("HL" + AprilTagCameraWrapper.getHorizontalOffset());

    
    // selectTeleopState();
    // teleopAllState();
    // switch(robotState){
    //   case AUTO_ALIGN:
    //     autoAlign.moveToGridPosition();
    //   default: // DRIVE
    //     Drivebase.driveFieldRelative();
    // }
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
