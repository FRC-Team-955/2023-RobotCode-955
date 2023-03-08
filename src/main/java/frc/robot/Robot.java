package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.net.PortForwarder;
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
    IntakeV2.setup();
    Claw.setup();
    AprilTagCameraWrapper.setUp();
    Drivebase.resetAnglesToAbsolute();
    auto = new Auto(5810);
    IntakeV2.setup();
    Arm.setArm(IO.GridArmPosition.Retract);
    Elevator.setElevator(IO.GridRowPosition.Retract);
    PortForwarder.add(5800, "photonvision.local", 5800);
  }

  @Override
  public void robotPeriodic() {}
  
  Field2d field2d = new Field2d();

  @Override
  public void autonomousInit() {
    SmartDashboard.putData("Field", field2d);
    Drivebase.resetAnglesToAbsolute();
    Arm.setOffset();
    // TO DO: Implement automatic drivebase position detection before auto runs
    // Drivebase.setSwerveOdometry(new Pose2d(autoGridSelection(autoGridSelection).getX(), autoGridSelection(autoGridSelection).getY(),Gyro.getYawR2D()));
    auto.autoInit();
  }


  @Override
  public void autonomousPeriodic() {
    Drivebase.updateSwerveOdometry();
    field2d.setRobotPose(Drivebase.getPose());
    auto.autoPeriodic();
  }

  @Override
  public void teleopInit() {
    // IntakeV2.setup();
    // Arm.setOffset();    
    // Arm.setArm(IO.GridArmPosition.Retract);
    // Elevator.setElevator(IO.GridRowPosition.Retract);
    Drivebase.resetAnglesToAbsolute();
  }

  public void teleopAllState(){
    Drivebase.logData();
    IO.keyInputOdometryMapping();
    IO.keyInputRowPosition();
    IO.keyInputSubstationPosition();
    IO.displayInformation();
    Drivebase.updateSwerveOdometry();
    AutoAlign.displayInformation();
    field2d.setRobotPose(Drivebase.getPose());
    // GamepieceManager.displayInformation();
  }
  @Override
  public void teleopPeriodic() {
    // IntakeV2.displayInformation();
    //IntakeV2.slowIntake();
    // IntakeV2.reverseIntake();
    // IntakeV2.moveMotor(IO.intakeOverride() * 0.5);
    // if (!IO.getTestingButton()) {
    //   IntakeV2.extendNoPid();
    // } else {
    //   IntakeV2.retractNoPid();
    // }
    // if (IO.getTestingTrigger() > 0.5) {
    //   GamepieceManager.loadGamepiece();
    // }
    // GamepieceManager.loadResetOverride(IO.getTestingButton());
    selectTeleopState();
    teleopAllState();

    switch(robotState){
      case AUTO_ALIGN:
        GamepieceManager.autoAlign();
        // IntakeV2.retractNoPid();
        break;
      case AUTO_BALANCE:
        Drivebase.autoBalance();
        // Drivebase.autoBalanceBangBang();
        GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
        //IntakeV2.retractNoPid();
        break;
      default:
        AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
        AutoAlign.substationAlignState = AutoAlign.SubstationAlignState.AlignedToOdometry;
        GamepieceManager.placeState = GamepieceManager.PlaceState.Align;

        GamepieceManager.loadSequence();
        if(IO.resetGyroAngle()){
          Gyro.set(90);
          SwerveDrive.headingSetPoint = -180;
        }
        // if(IO.resetOdometryAngle()){

        // }
        GamepieceManager.manageExtension();
        Drivebase.drive();
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
    // else if (IO.Drivebase.isAutoBalanceActive()){
    //   robotState = RobotState.AUTO_BALANCE;
    // } 
    else {
      robotState = RobotState.DRIVING;
    }
  }
}
