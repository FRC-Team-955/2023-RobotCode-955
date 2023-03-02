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
    // Intake.setup();
    Claw.setup();
    AprilTagCameraWrapper.setUp();
    Drivebase.resetAnglesToAbsolute();
    // auto = new Auto();
    IntakeV2.setup();
    PortForwarder.add(5800, "photonvision.local", 5800);
  }

  @Override
  public void robotPeriodic() {}
  public static enum AutoLeaveSelection{
    Left,
    Right,
    None
  }
  public static int autoGridSelection = 5; //zero is left most, eight is right most
  public static AutoLeaveSelection autoLeaveSelection = AutoLeaveSelection.None;
  public static enum AutoState {
        Setup,
        Up,
        AlignNode,
        AlignPosition,
        Extend,
        Place,
        LeaveNode,
        Out,
        LeaveCommunity,
        Done
  }
  public static AutoState autoState = AutoState.Setup;
  public static boolean isAutoConeNodePosition = false;
  public static Translation2d autoGridSelection(int position){
    if(position == 0){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue0:Constants.FieldPositions.AutoAlignPositions.red0;
    }
    else if(position == 1){
      isAutoConeNodePosition = false;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue1:Constants.FieldPositions.AutoAlignPositions.red1;
    }
    else if(position == 2){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue2:Constants.FieldPositions.AutoAlignPositions.red2;
    }
    else if(position == 3){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue3:Constants.FieldPositions.AutoAlignPositions.red3;
    }
    else if(position == 4){
      isAutoConeNodePosition = false;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue4:Constants.FieldPositions.AutoAlignPositions.red4;
    }
    else if(position == 5){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue5:Constants.FieldPositions.AutoAlignPositions.red5;
    }
    else if(position == 6){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue6:Constants.FieldPositions.AutoAlignPositions.red6;
    }
    else if(position == 7){
      isAutoConeNodePosition = false;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue7:Constants.FieldPositions.AutoAlignPositions.red7;
    }
    else if(position == 8){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue8:Constants.FieldPositions.AutoAlignPositions.red8;
    }else{
      return Drivebase.getPose().getTranslation();
    }
  }
  
  Field2d field2d = new Field2d();

  @Override
  public void autonomousInit() {
    SmartDashboard.putData("Field", field2d);
    Drivebase.resetAnglesToAbsolute();
    Arm.setOffset();
    // Drivebase.setSwerveOdometry(new Pose2d(Constants.isBlue()?Constants.FieldPositions.fieldX-14:14, Constants.FieldPositions.fieldY/2,Gyro.getYawR2D()));
    Drivebase.setSwerveOdometry(new Pose2d(autoGridSelection(autoGridSelection).getX(), autoGridSelection(autoGridSelection).getY(),Gyro.getYawR2D()));
  }


  @Override
  public void autonomousPeriodic() {
    Drivebase.updateSwerveOdometry();
    field2d.setRobotPose(Drivebase.getPose());
    SmartDashboard.putString("Auto State", autoState.toString());
    // teleopAllState();
        switch(autoState){
          case Setup:
            IntakeV2.reverseIntake();
            // Drivebase.setSwerveOdometry(new Pose2d(Constants.isBlue()?Constants.FieldPositions.fieldX-14:14, autoGridSelection(autoGridSelection).getY(),Gyro.getYawR2D()));
            Claw.intakeGamePiece();
            if (GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract)){
              autoState = AutoState.Up;
            }
            break;
          case Up:
            Claw.intakeGamePiece();
            IntakeV2.reverseIntake(); 
            if (GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up)){
              autoState = AutoState.AlignNode;
            }
            break;
          case AlignNode:
            GamepieceManager.runExtention();
            Claw.intakeGamePiece();
            if(AutoAlign.alignOdometry(autoGridSelection(autoGridSelection), -180)){
              if (isAutoConeNodePosition){
                autoState = AutoState.AlignPosition;
              }else{
                autoState = AutoState.Place;
              }
            }
            break;
          case AlignPosition:
            GamepieceManager.runExtention();
            Claw.intakeGamePiece();
            if(AutoAlign.alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, 
              autoGridSelection(autoGridSelection).getY()), -180)){
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
            if(GamepieceManager.extention(IO.GridRowPosition.High, IO.GridArmPosition.ConeAlmostReady)){
              Claw.outputGamePiece();
              autoState = AutoState.LeaveNode;
            }else{
              Claw.stopishMotor();
            }
            break;
          case LeaveNode:
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            Claw.outputGamePiece();

            if(AutoAlign.alignOdometry(autoGridSelection(autoGridSelection), -180)){
              autoState = AutoState.Out;
            }
            break;
          case Out:
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            switch(autoLeaveSelection){
              case Left:
                if(AutoAlign.alignOdometry(Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue0:Constants.FieldPositions.AutoAlignPositions.red0, -180)){
                  autoState = AutoState.LeaveCommunity;
                }
                break;
              case Right:
                if(AutoAlign.alignOdometry(Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue8:Constants.FieldPositions.AutoAlignPositions.red8, -180)){
                  autoState = AutoState.LeaveCommunity;
                }

                break;
              case None:
                autoState = AutoState.Done;
                break;
                
            }
            break;
          case LeaveCommunity:
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);

            switch(autoLeaveSelection){
              case Left:
                // if(AutoAlign.alignOdometry(Constants.isBlue()?new Translation2d(Constants.FieldPositions.outSubstationShortBlue ,Constants.FieldPositions.AutoAlignPositions.blue0.getY()):
                //                                               new Translation2d(Constants.FieldPositions.outSubstationLongRed ,Constants.FieldPositions.AutoAlignPositions.red0.getY()), -180)){
                //   autoState = AutoState.Done;
                // }
                if(Constants.isBlue()?Drivebase.getPose().getX()>Constants.FieldPositions.outSubstationShortBlue:
                                      Drivebase.getPose().getX()<Constants.FieldPositions.outSubstationLongRed){
                  autoState = AutoState.Done;
                }else{
                  Drivebase.driveFieldRelativeHeading(new Translation2d(0,-1), -180);
                }
                break;
              case Right:
                // if(AutoAlign.alignOdometry(Constants.isBlue()?new Translation2d(Constants.FieldPositions.outSubstationLongBlue ,Constants.FieldPositions.AutoAlignPositions.blue8.getY()):
                //                                               new Translation2d(Constants.FieldPositions.outSubstationShortRed ,Constants.FieldPositions.AutoAlignPositions.red8.getY()), -180)){
                //   autoState = AutoState.Done;
                // }
                if(Constants.isBlue()?Drivebase.getPose().getX()>Constants.FieldPositions.outSubstationLongBlue:
                                      Drivebase.getPose().getX()<Constants.FieldPositions.outSubstationShortRed ){
                  autoState = AutoState.Done;
                }else{
                  Drivebase.driveFieldRelativeHeading(new Translation2d(0,-1), -180);
                }
                break;
              case None:
                autoState = AutoState.Done;
                break;
            }
            break;
          case Done:
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            Drivebase.driveRobotRelativeRotation(new Translation2d(0,0), 0);
            break;
        }    
  }

  @Override
  public void teleopInit() {
    SmartDashboard.putData("Field", field2d);
    IntakeV2.setup();
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
    Drivebase.logData();
    IntakeV2.displayInformation();
    SmartDashboard.putBoolean("amp limit hit", IntakeV2.intake());
    GamepieceManager.displayInformation();
    //IntakeV2.reverseIntake();
    // IntakeV2.moveMotor(IO.intakeOverride() * 0.5);
    // if (!IO.getTestingButton()) {
    //   IntakeV2.extendNoPid();
    // } else {
    //   IntakeV2.retractNoPid();
    // }
    if (IO.getTestingTrigger() > 0.5) {
      GamepieceManager.loadGamepiece();
    }
    GamepieceManager.loadResetOverride(IO.getTestingButton());
    // selectTeleopState();
    // teleopAllState();

    // switch(robotState){
    //   case AUTO_ALIGN:
    //     GamepieceManager.autoAlign();
    //     // Intake.foldInIntake();
    //     break;
    //   case AUTO_BALANCE:
    //     Drivebase.autoBalance();
    //     // Drivebase.autoBalanceBangBang();
    //     GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
    //     // Intake.foldInIntake();
    //     break;
    //   default: // DRIVE
    //     AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
    //     AutoAlign.substationAlignState = AutoAlign.SubstationAlignState.AlignedToOdometry;
    //     GamepieceManager.placeState = GamepieceManager.PlaceState.Align;

    //     GamepieceManager.loadSequence();
    //     if(IO.resetGyroAngle()){
    //       Gyro.set(90);
    //       SwerveDrive.headingSetPoint = -180;
    //     }
    //     GamepieceManager.manageExtension();
    //     Drivebase.drive();
    // }



    // // if(IO.elevatorManualUp()){
    // //   Elevator.setElevator(IO.gridRowPosition);
    // //   Arm.setArm(IO.gridArmPosition);
    // //   if(Constants.isBlue()? Drivebase.getPose().getX() > Constants.FieldPositions.centerLine : Drivebase.getPose().getX() < Constants.FieldPositions.centerLine){
    // //     Elevator.setElevator(IO.GridRowPosition.DoubleSubstation);
    // //     Arm.setArm(IO.GridArmPosition.DoubleSubstation);
    // //   }
    // // }else if (IO.elevatorManualDown()){
    // //   Arm.setArm(IO.GridArmPosition.Retract);
    // //   Elevator.setElevator(IO.GridRowPosition.Retract);
    // // }
    // // Elevator.setElevator();
    // // // Arm.setpoint = Arm.setpoint + IO.elevatorFineControl()*2;
    // // Arm.setArm();


    // //Working??
    // // GamepieceManager.manageExtension();

    // // if(IO.intakeSequence()){
    // //   Claw.intakeGamePiece();
    // // }
    // // else if(IO.clawDropPiece()){
    // //   Claw.outputGamePiece();
    // // }else{
    // //   Claw.stopishMotor();
    // // }

    // // if (IO.Drivebase.thrustEnabled()){
    // //   AutoAlign.moveToGridPositionOdometryTwoStep();
    // // }else if(IO.Drivebase.isAutoAlignActive()){
    // //   AutoAlign.moveToSubstationPosition();
    // // }
    // // else {
    // //   Drivebase.drive();
    // //   AutoAlign.substationAlignStateSave = AutoAlign.SubstationAlignState.AlignedToOdometry;
    // //   AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
    // // }
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
