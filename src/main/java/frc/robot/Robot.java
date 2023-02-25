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
import frc.robot.Subsystems.IntakeV2;

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

  @Override
  public void autonomousInit() {
    // auto.autoTestInit(AutoProfile.Test());
    // auto.autoInit();

  }

  @Override
  public void autonomousPeriodic() {
    // auto.autoPeriodic();
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
    IO.keyInputSubstationLocation();
    Drivebase.updateSwerveOdometry();
    field2d.setRobotPose(Drivebase.getPose());



  }
  @Override
  public void teleopPeriodic() {
    // IntakeV2.displayInformation();
    // IntakeV2.moveMotor(IO.intakeOverride() * 0.5);
    selectTeleopState();
    teleopAllState();

    System.out.println("AutoAlign.gridAlignState: "+ AutoAlign.gridAlignState);
    System.out.println(" AutoAlign.substationAlignStateSave: "+  AutoAlign.substationAlignStateSave);

    //This is the broken one.
    switch(robotState){
      case AUTO_ALIGN:
        GamepieceManager.autoAlign();
        // Intake.foldInIntake();
        System.out.println("auto");

      case AUTO_BALANCE:
        Drivebase.autoBalance();
        // Drivebase.autoBalanceBangBang();
        GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
        // Intake.foldInIntake();
      default: // DRIVE
        AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
        AutoAlign.substationAlignStateSave = AutoAlign.SubstationAlignState.AlignedToOdometry;
        // GamepieceManager.loadSequence();
        Claw.stopishMotor();
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
