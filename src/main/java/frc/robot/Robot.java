package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Auto.Auto;
import frc.robot.Auto.AutoProfile;
import frc.robot.Sensors.AprilTagCameraWrapper;
// import frc.robot.Sensors.ColorSensor;
import frc.robot.Subsystems.Arm;
import frc.robot.Subsystems.Claw;
import frc.robot.Subsystems.Elevator;
import frc.robot.Swerve.SwerveDrive;

public class Robot extends TimedRobot {
  Auto auto;
  // Robot States
  public enum RobotState {
    DRIVING,
    AUTO_ALIGN,
    AUTO_BALANCE
  }
  
  static RobotState robotState = RobotState.DRIVING;
  
  @Override
  public void robotInit() {
    Arm.setup();
    Elevator.setup();
    // Intake.setup();
    Claw.setup();
    AprilTagCameraWrapper.setUp();
    Drivebase.resetAnglesToAbsolute();
    auto = new Auto();
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    auto.autoTestInit(AutoProfile.Test());
    auto.autoInit();

  }

  @Override
  public void autonomousPeriodic() {
    auto.autoPeriodic();
  }
  Field2d field2d = new Field2d();

  @Override
  public void teleopInit() {
    SmartDashboard.putData("Field", field2d);
    // Drivebase.resetAnglesToAbsolute();
    Arm.setOffset();
  }

  public void teleopAllState(){
    // Drivebase.logData();
    IO.keyInputOdometryMapping();
    IO.keyInputRowPosition();
    IO.keyInputSubstationLocation();
    Drivebase.updateSwerveOdometry();
    field2d.setRobotPose(Drivebase.getPose());
  }
  @Override
  public void teleopPeriodic() {
    // selectTeleopState();
    teleopAllState();

    // switch(robotState){
    //   case AUTO_ALIGN:
    //     GamepieceManager.autoAlign();
    //     Intake.foldInIntake();
    //   case AUTO_BALANCE:
    //     Drivebase.autoBalance();
    //     // Drivebase.autoBalanceBangBang();
    //     GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
    //     Intake.foldInIntake();
    //   default: // DRIVE
    //     AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
    //     GamepieceManager.loadSequence();
    //     GamepieceManager.manageExtension();
    //     Drivebase.drive();
    // }


    // if (IO.elevatorManualDown()){
      
    // }
    // Drivebase.drive();
    // Drivebase.driveRobotRelativeRotation(IO.Drivebase.getSwerveTranslation(), IO.Drivebase.getSwerveRotation());
    System.out.println(Drivebase.getPose());
    // if(IO.intakeSequence()){
    //   // Claw.intakeGamePiece();
    //   Claw.intakeFineControl(-1);

    // }
    // else if(IO.clawDropPiece()){
    //   // Claw.outputGamePiece();
    //   Claw.intakeFineControl(1);

    // }else{
    //   Claw.intakeFineControl(-0.3);
    // }
    Claw.intakeFineControl(IO.armFineControl()-0.3);
    // Claw.intakeFineControl(IO.elevatorFineControl());
    // Arm.moveArm(IO.armFineControl());
    
    // Elevator.moveElevator(IO.elevatorFineControl());
    if(IO.elevatorManualUp()){
      // Elevator.setElevator(IO.gridRowPosition);
      Elevator.setElevator(IO.gridRowPosition);
      Arm.setArm(IO.gridArmPosition);

    }else if (IO.elevatorManualDown()){
      Arm.setArm(IO.GridArmPosition.Retract);
      Elevator.setElevator(IO.GridRowPosition.Retract);
    }
    // Elevator.setElevator(IO.GridRowPosition.Retract);
    System.out.println(Arm.getOffsetPosition());
    Elevator.setElevator();
    // Arm.moveArm(IO.armFineControl());
    Arm.setArm();

    // System.out.println(Constants.isBlue());
    // System.out.println("x" + Drivebase.getPose().getX());
    // System.out.println("y" + Drivebase.getPose().getY());
    if (IO.Drivebase.thrustEnabled()){
      // AutoAlign.alignOdometry(Constants.FieldPositions.AutoAlignPositions.red7, -180);
      AutoAlign.moveToGridPositionOdometryTwoStep();
      // AutoAlign.alignAprilTag();
      // AutoAlign.moveToGridPosition();
    }else if(IO.Drivebase.isAutoAlignActive()){
      AutoAlign.moveToSubstationPosition();
    }
    // else if (IO.Drivebase.isAutoAlignActive()) {
    //   // AutoAlign.alignOdometry(Constants.FieldPositions.AutoAlignPositions.blue1, 180);
    // } 
    // else if (IO.Drivebase.autoHeadingEnabled()){
    //   AutoAlign.moveIntoPosition();
    // }
    else {
      // Drivebase.driveFieldRelativeHeading(IO.Drivebase.getSwerveTranslation(), 180);
      Drivebase.drive();
      
      AutoAlign.substationAlignStateSave = AutoAlign.SubstationAlignState.AlignedToOdometry;
      AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;


      // Drivebase.driveFieldRelativeRotation(IO.Drivebase.getSwerveTranslation(), IO.Drivebase.getSwerveRotation());
    }

    // // Drivebase.driveFieldRelativeHeading(new Translation2d(0, 0), 180);
    // //System.out.println("HL" + AprilTagCameraWrapper.getHorizontalOffset());
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
    else if (IO.Drivebase.isAutoBalanceActive()){
      robotState = RobotState.AUTO_BALANCE;
    } else {
      robotState = RobotState.DRIVING;
    }
  }
}
