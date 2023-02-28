package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Auto.Auto;
import frc.robot.Sensors.AprilTagCameraWrapper;
import frc.robot.Sensors.Gyro;
// import frc.robot.Sensors.ColorSensor;
import frc.robot.Subsystems.Arm;
import frc.robot.Subsystems.Claw;
import frc.robot.Subsystems.Elevator;
import frc.robot.Subsystems.IntakeV2;
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
    // auto = new Auto();
    IntakeV2.setup();
  }

  @Override
  public void robotPeriodic() {}

  public static enum AutoState {
        Setup,
        Up,
        Align,
        Extend,
        Place,
        Leave,
        Done
  }
  public static AutoState autoState = AutoState.Setup;

  @Override
  public void autonomousInit() {
    Drivebase.resetAnglesToAbsolute();
    Arm.setOffset();
  }

  @Override
  public void autonomousPeriodic() {
    Drivebase.updateSwerveOdometry();
    switch(autoState){
      case Setup:
        Drivebase.setSwerveOdometry(new Pose2d(14,Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue0.getY():Constants.FieldPositions.AutoAlignPositions.red8.getY(),Gyro.getYawR2D()));
        Claw.intakeGamePiece();
        if (GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract)){
          autoState = AutoState.Up;
        }
        break;
      case Up:
        Claw.intakeGamePiece();
        if (GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up)){
          autoState = AutoState.Align;
        }
        break;
      case Align:
        Claw.intakeGamePiece();
        if(AutoAlign.alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, 
                                                    Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue0.getY():Constants.FieldPositions.AutoAlignPositions.red8.getY()), -180)){
          autoState = AutoState.Extend;
        }
        break;
      case Extend:
        Claw.stopishMotor();
        if (GamepieceManager.extention(IO.GridRowPosition.High, IO.GridArmPosition.ConePrep)){
          autoState = AutoState.Place;
        }
        break;
      case Place:
        if(GamepieceManager.extention(IO.GridRowPosition.High, IO.GridArmPosition.ConeReady)){
          Claw.outputGamePiece();
          autoState = AutoState.Leave;
        }else{
          Claw.stopishMotor();
        }
        break;
      case Leave:
        if(AutoAlign.alignOdometry(new Translation2d(Constants.isBlue()?4:12, Constants.FieldPositions.AutoAlignPositions.red8.getY()), -180)){
          autoState = AutoState.Done;
        }
        break;
      case Done:
        Drivebase.driveRobotRelativeRotation(new Translation2d(0,0), 0);
        break;
    }
    
  }
  Field2d field2d = new Field2d();

  @Override
  public void teleopInit() {
    SmartDashboard.putData("Field", field2d);
    // IntakeV2.setup();
    Drivebase.resetAnglesToAbsolute();
    Arm.setOffset();    
    Arm.setArm(IO.GridArmPosition.Retract);
    Elevator.setElevator(IO.GridRowPosition.Retract);
  }

  public void teleopAllState(){
    // Drivebase.logData();
    IO.keyInputOdometryMapping();
    IO.keyInputRowPosition();
    IO.keyInputSubstationPosition();
    IO.displayInformation();
    Drivebase.updateSwerveOdometry();
    AutoAlign.displayInformation();
    field2d.setRobotPose(Drivebase.getPose());
    System.out.println(Drivebase.getPose());
    // GamepieceManager.displayInformation();
  }
  @Override
  public void teleopPeriodic() {
    // IntakeV2.displayInformation();
    // IntakeV2.moveMotor(IO.intakeOverride() * 0.5);
    selectTeleopState();
    teleopAllState();

    switch(robotState){
      case AUTO_ALIGN:
        GamepieceManager.autoAlign();
        // Intake.foldInIntake();
        break;
      case AUTO_BALANCE:
        Drivebase.autoBalance();
        // Drivebase.autoBalanceBangBang();
        GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
        // Intake.foldInIntake();
        break;
      default: // DRIVE
        AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
        AutoAlign.substationAlignState = AutoAlign.SubstationAlignState.AlignedToOdometry;
        GamepieceManager.placeState = GamepieceManager.PlaceState.Align;

        GamepieceManager.loadSequence();
        if(IO.resetGyroAngle()){
          Gyro.set(90);
          SwerveDrive.headingSetPoint = -180;
        }
        GamepieceManager.manageExtension();
        Drivebase.drive();
    }



    // if(IO.elevatorManualUp()){
    //   Elevator.setElevator(IO.gridRowPosition);
    //   Arm.setArm(IO.gridArmPosition);
    //   if(Constants.isBlue()? Drivebase.getPose().getX() > Constants.FieldPositions.centerLine : Drivebase.getPose().getX() < Constants.FieldPositions.centerLine){
    //     Elevator.setElevator(IO.GridRowPosition.DoubleSubstation);
    //     Arm.setArm(IO.GridArmPosition.DoubleSubstation);
    //   }
    // }else if (IO.elevatorManualDown()){
    //   Arm.setArm(IO.GridArmPosition.Retract);
    //   Elevator.setElevator(IO.GridRowPosition.Retract);
    // }
    // Elevator.setElevator();
    // // Arm.setpoint = Arm.setpoint + IO.elevatorFineControl()*2;
    // Arm.setArm();


    //Working??
    // GamepieceManager.manageExtension();

    // if(IO.intakeSequence()){
    //   Claw.intakeGamePiece();
    // }
    // else if(IO.clawDropPiece()){
    //   Claw.outputGamePiece();
    // }else{
    //   Claw.stopishMotor();
    // }

    // if (IO.Drivebase.thrustEnabled()){
    //   AutoAlign.moveToGridPositionOdometryTwoStep();
    // }else if(IO.Drivebase.isAutoAlignActive()){
    //   AutoAlign.moveToSubstationPosition();
    // }
    // else {
    //   Drivebase.drive();
    //   AutoAlign.substationAlignStateSave = AutoAlign.SubstationAlignState.AlignedToOdometry;
    //   AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
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
    // else if (IO.Drivebase.isAutoBalanceActive()){
    //   robotState = RobotState.AUTO_BALANCE;
    // } 
    else {
      robotState = RobotState.DRIVING;
    }
  }
}
