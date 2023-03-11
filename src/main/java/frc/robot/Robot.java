package frc.robot;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
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
    POWER_SAVING,
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
    // auto = new Auto();
    IntakeV2.setup();
    Arm.setArm(IO.GridArmPosition.Retract);
    Elevator.setElevator(IO.GridRowPosition.Retract);
    PortForwarder.add(5800, "photonvision.local", 5800);
    SmartDashboard.putString("Alliance Color",  DriverStation.getAlliance().toString());
  }

  @Override
  public void robotPeriodic() {}

  public static enum AutoType{
    Old,
    New
  }
  public static AutoType autoTypeSelection = AutoType.Old;

  public static enum AutoLeaveSelection{
    Left,
    Right,
    Charge,
    None
  }
  public static AutoLeaveSelection autoLeaveSelection = AutoLeaveSelection.Left;

  public static enum NewAutoLeaveSelection{
    Left,
    Right,
    None
  }
  public static NewAutoLeaveSelection newAutoLeaveSelection = NewAutoLeaveSelection.Left;
  public static boolean isCharge = false;

  public static int autoGridSelection = 2; //zero is left most, eight is right most
  public static class GamePiecePosition{
    int gridSelectionPosition;
    IO.GridRowPosition gridRowPosition;
    public GamePiecePosition(int gridSelectionPosition, IO.GridRowPosition gridRowPosition){
      this.gridSelectionPosition = gridSelectionPosition;
      this.gridRowPosition = gridRowPosition;
    }
  }
  public static GamePiecePosition[] gamePiecePositionArray = new GamePiecePosition[]{
    new GamePiecePosition(2,IO.GridRowPosition.High),
    new GamePiecePosition(3,IO.GridRowPosition.High),
    new GamePiecePosition(3,IO.GridRowPosition.Retract)
  };

  public static enum AutoState {
        Setup,
        Up,
        AlignNode,
        AlignPosition,
        Extend,
        Place,
        LeaveNode,
        OutCommunity,
        LeaveCommunity,
        Done,
        AutoBalance
  }
  public static AutoState autoState = AutoState.Done;

  public static enum NewAutoState{
    Setup,
    AlignNode,
    AlignPosition,
    Extend,
    Place,
    LeaveNode,
    PrepLeaveCommunity,
    LeaveCommunity,
    MoveToGamePiece,
    GetGamePiece,
    PrepEnterCommunity,
    EnterCommunity,
    Done,
    PrepCharge,
    OnToCharge,
    AutoBalance
  }
  public static NewAutoState newAutoState = NewAutoState.Done;

  // public static int numberOfGamepieces = 3;
  public static int numberOfGamepieces = gamePiecePositionArray.length;
  public static int numberOfGamepiecesCounter = numberOfGamepieces;

  public static boolean isAutoConeNodePosition = false;

  private static Timer intakeTimer = new Timer();

  public static Translation2d autoGridSelectionTranslation2d(int position){
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
  public static Translation2d autoGamePieceTranslation2d(){
    if(Constants.isBlue()){
      if(numberOfGamepieces-numberOfGamepiecesCounter == 1){
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.blueGamePiece0:Constants.FieldPositions.AutoAlignPositions.blueGamePiece3;
      }
      else if(numberOfGamepieces-numberOfGamepiecesCounter == 2){
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.blueGamePiece1:Constants.FieldPositions.AutoAlignPositions.blueGamePiece2;
      }
      else if(numberOfGamepieces-numberOfGamepiecesCounter == 3){
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.blueGamePiece2:Constants.FieldPositions.AutoAlignPositions.blueGamePiece1;
      }
      else{
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.blueGamePiece3:Constants.FieldPositions.AutoAlignPositions.blueGamePiece0;
      }
    }else{
      if(numberOfGamepieces-numberOfGamepiecesCounter == 1){
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.redGamePiece0:Constants.FieldPositions.AutoAlignPositions.redGamePiece3;
      }
      else if(numberOfGamepieces-numberOfGamepiecesCounter == 2){
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.redGamePiece1:Constants.FieldPositions.AutoAlignPositions.redGamePiece2;
      }
      else if(numberOfGamepieces-numberOfGamepiecesCounter == 3){
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.redGamePiece2:Constants.FieldPositions.AutoAlignPositions.redGamePiece1;
      }
      else{
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.redGamePiece3:Constants.FieldPositions.AutoAlignPositions.redGamePiece0;
      }
    }
  }
  public static double autoGamePieceHeading(){
    //if first piece rotation 0, else depend on left and right
    return (numberOfGamepieces-1 == numberOfGamepiecesCounter)?0:
                                                              (newAutoLeaveSelection ==  NewAutoLeaveSelection.Left)?270:90;
  }
  public static Translation2d autoCommunitySideTranslation2d(){
    switch(newAutoLeaveSelection){
      case Left:
        return Constants.isBlue()?new Translation2d(Constants.FieldPositions.AutoAlignPositions.blue0.getX() + Constants.FieldPositions.notHitGridOffset,Constants.FieldPositions.AutoAlignPositions.blue0.getY()):
                                  new Translation2d(Constants.FieldPositions.AutoAlignPositions.red0.getX() - Constants.FieldPositions.notHitGridOffset,Constants.FieldPositions.AutoAlignPositions.red0.getY());
      case Right:
        return Constants.isBlue()?new Translation2d(Constants.FieldPositions.AutoAlignPositions.blue8.getX() + Constants.FieldPositions.notHitGridOffset,Constants.FieldPositions.AutoAlignPositions.blue8.getY()):
                                  new Translation2d(Constants.FieldPositions.AutoAlignPositions.red8.getX() - Constants.FieldPositions.notHitGridOffset,Constants.FieldPositions.AutoAlignPositions.red8.getY());
      default:
        return Drivebase.getPose().getTranslation();
    }
  }
  public static Translation2d autoGamePieceSideTranslation2d(){
    switch(newAutoLeaveSelection){
      case Left:
        return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGamePiece0:Constants.FieldPositions.AutoAlignPositions.redGamePiece0;
      case Right:
        return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGamePiece3:Constants.FieldPositions.AutoAlignPositions.redGamePiece3;
      default:
        return Drivebase.getPose().getTranslation();
    }
  }
  public static Translation2d autoChargePrepTranslation2d(){
    switch(newAutoLeaveSelection){
      case Left:
        return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue3:Constants.FieldPositions.AutoAlignPositions.red3;
      case Right:
        return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue5:Constants.FieldPositions.AutoAlignPositions.red5;
      default:
        return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue4:Constants.FieldPositions.AutoAlignPositions.red4;
    }
  }
  Field2d field2d = new Field2d();

  @Override
  public void autonomousInit() {
    SmartDashboard.putData("Field", field2d);
    Drivebase.resetAnglesToAbsolute();
    Arm.setOffset();
    IntakeV2.setOffset();
    Drivebase.setSwerveOdometry(new Pose2d(autoGridSelectionTranslation2d(autoGridSelection).getX(), autoGridSelectionTranslation2d(autoGridSelection).getY(),Gyro.getYawR2D()));
    SmartDashboard.putString("Alliance Color",  DriverStation.getAlliance().toString());
  }

  public void autoAllState(){
    Drivebase.logData();
    Claw.logData();
    field2d.setRobotPose(Drivebase.getPose());
    SmartDashboard.putString("Alliance Color",  DriverStation.getAlliance().toString());
    SmartDashboard.putString("Auto State", autoState.toString());
    SmartDashboard.putBoolean("isAutoConeNode", isAutoConeNodePosition);
    AutoAlign.displayInformation();
    Drivebase.displayInformation();
    Drivebase.updateSwerveOdometry();
  }

  @Override
  public void autonomousPeriodic() {
    autoAllState();
    switch(autoTypeSelection){
      case Old:
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
            if(AutoAlign.alignOdometry(autoGridSelectionTranslation2d(autoGridSelection), -180)){
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
              autoGridSelectionTranslation2d(autoGridSelection).getY()), -180)){
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
    
            if(AutoAlign.alignOdometry(autoGridSelectionTranslation2d(autoGridSelection), -180)){
              autoState = AutoState.OutCommunity;
            }
            break;
          case OutCommunity:
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
              case Charge:
                if(AutoAlign.alignOdometry(Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blue4:Constants.FieldPositions.AutoAlignPositions.red4, -180)){
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
              case Charge:
                if(Constants.isBlue()?Drivebase.getPose().getX()>Constants.FieldPositions.AutoAlignPositions.chargeStationBlue.getX():
                  Drivebase.getPose().getX()<Constants.FieldPositions.AutoAlignPositions.chargeStationRed.getX() ){
                  // autoState = AutoState.Done;
                  autoState = AutoState.AutoBalance;
                }else{
                  // Drivebase.driveFieldRelativeHeading(new Translation2d(0,-2), -180);
                  Drivebase.driveRobotRelativeRotation(new Translation2d(2,0), 0);
                }
                break;
              case None:
                autoState = AutoState.Done;
                break;
            }
            break;
          case Done:
            // GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            // Drivebase.driveRobotRelativeRotation(new Translation2d(0,0), 0);
            // System.out.println("Odometry X: "+ Drivebase.getPose().getX());
            break;
          case AutoBalance:
            Drivebase.autoBalance();
            break;
            
        }    
        case New:
        switch(newAutoState){
          //Get actual staging marking positions!!!
          //Also get the actual driver relative headings!
          case Setup:
            Claw.intakeGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.reverseIntake();
            if (GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up)){
              newAutoState = NewAutoState.AlignNode;
            }
            break;
          case AlignNode:
            //also get arm into position if it is a cube node, idk if we can pre-extend for cones
            if (isAutoConeNodePosition){
              GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            }else{
              GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.CubePrep);
            }
            Claw.intakeGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            // if(AutoAlign.alignOdometry(autoGridSelectionTranslation2d(autoGridSelection), -180)){
              // if(AutoAlign.alignOdometry(autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepieces-numberOfGamepiecesCounter].gridSelectionPosition), 
              //                           -180)){
            if(AutoAlign.alignOdometry(new Translation2d(autoCommunitySideTranslation2d().getX(), 
                                                        autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepieces-numberOfGamepiecesCounter].gridSelectionPosition).getY()), 
                                      -180)){
              if (isAutoConeNodePosition){
                newAutoState = NewAutoState.AlignPosition;
              }else{
                newAutoState = NewAutoState.Place;
              }
            }
            break;
          case AlignPosition:
            Claw.intakeGamePiece();
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            if(AutoAlign.alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:
                                                                            Constants.FieldPositions.atGridRedX, 
                                                        autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepieces-numberOfGamepiecesCounter].gridSelectionPosition).getY()), 
                                      -180)){
              newAutoState = NewAutoState.Extend;
            }
            break;
          case Extend:
            Claw.stopishMotor();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            // if (GamepieceManager.extention(IO.GridRowPosition.High, IO.GridArmPosition.ConePrep)){
            if (GamepieceManager.extention(gamePiecePositionArray[numberOfGamepieces-numberOfGamepiecesCounter].gridRowPosition, IO.GridArmPosition.ConePrep)){
              newAutoState = NewAutoState.Place;
            }
            break;
          case Place:
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            // if(GamepieceManager.extention(IO.GridRowPosition.High, IO.GridArmPosition.ConeAlmostReady)){
            if(GamepieceManager.extention(gamePiecePositionArray[numberOfGamepieces-numberOfGamepiecesCounter].gridRowPosition, IO.GridArmPosition.ConeAlmostReady)){
              Claw.outputGamePiece();
              numberOfGamepiecesCounter--;
              newAutoState = NewAutoState.LeaveNode;
            }else{
              Claw.stopishMotor();
            }
            break;
          case LeaveNode:
            Claw.outputGamePiece();
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            if(AutoAlign.alignOdometry(new Translation2d(autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepieces-numberOfGamepiecesCounter].gridSelectionPosition).getX() + (Constants.isBlue()?Constants.FieldPositions.notHitGridOffset:
                                                                                                                                        -Constants.FieldPositions.notHitGridOffset),
                                                        autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepieces-numberOfGamepiecesCounter].gridSelectionPosition).getY()), 
                                      -180)){
              //if we have placed all pieces and we want to charge, then go charge
              if(numberOfGamepiecesCounter == 0 && isCharge){
                newAutoState = NewAutoState.PrepCharge;
              }
              //if we have placed all pieces, then finish
              else if(numberOfGamepiecesCounter == 0){
                newAutoState = NewAutoState.Done;
              }
              else{
                newAutoState = NewAutoState.PrepLeaveCommunity;
              }
            }
            break;
          case PrepLeaveCommunity:
            // GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            Claw.stopMotor();
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            if(AutoAlign.alignOdometry(autoCommunitySideTranslation2d(), -180)){
              newAutoState = NewAutoState.LeaveCommunity;
            }
            if(newAutoLeaveSelection ==  NewAutoLeaveSelection.None){
              newAutoState = NewAutoState.Done;
            }
            break;
          case LeaveCommunity:
            Claw.intakeGamePiece();
            GamepieceManager.extention(IO.GridRowPosition.Low, IO.GridArmPosition.Retract);
            IntakeV2.extendNoPid();
            IntakeV2.slowIntake();
            if(AutoAlign.alignOdometry(autoGamePieceSideTranslation2d(), autoGamePieceHeading())){
                  newAutoState = NewAutoState.MoveToGamePiece;
            }
            break;
          case MoveToGamePiece:
            Claw.intakeGamePiece();
            GamepieceManager.extention(IO.GridRowPosition.Low, IO.GridArmPosition.Retract);
            IntakeV2.extendNoPid();
            IntakeV2.slowIntake();
            if(AutoAlign.alignOdometry(autoGamePieceTranslation2d(), autoGamePieceHeading())){
              intakeTimer.reset();
              intakeTimer.start();
              newAutoState = NewAutoState.GetGamePiece;
            }
            break;
          case GetGamePiece:
            Claw.intakeGamePiece();
            GamepieceManager.extention(IO.GridRowPosition.Low, IO.GridArmPosition.Retract);
            IntakeV2.extendNoPid();
            IntakeV2.slowIntake();
            AutoAlign.alignOdometry(autoGamePieceTranslation2d(), autoGamePieceHeading());
            if (intakeTimer.hasElapsed(Constants.Auto.intakeRunTime) ){
              newAutoState = NewAutoState.PrepEnterCommunity;
            }
            break;
          case PrepEnterCommunity:
            GamepieceManager.extention(IO.GridRowPosition.Low, IO.GridArmPosition.Retract);
            IntakeV2.handOffNoPid();
            Claw.intakeGamePiece();
            if(AutoAlign.alignOdometry(autoGamePieceSideTranslation2d(), -180)){
              newAutoState = NewAutoState.EnterCommunity;
            }
            break;
          case EnterCommunity:
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            IntakeV2.retractNoPid();
            Claw.intakeGamePiece();
            if(AutoAlign.alignOdometry(autoCommunitySideTranslation2d(), -180)){
              newAutoState = NewAutoState.AlignNode;
            }
            break;
          case Done:
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            Drivebase.driveFieldRelativeRotation(new Translation2d(0, 0), 0);
            break;
          case PrepCharge:
            if(AutoAlign.alignOdometry(autoChargePrepTranslation2d(), -180)){
              newAutoState = NewAutoState.OnToCharge;
            }
            break;
          case OnToCharge:
            if(Constants.isBlue()?Drivebase.getPose().getX()>Constants.FieldPositions.AutoAlignPositions.chargeStationBlue.getX():
              Drivebase.getPose().getX()<Constants.FieldPositions.AutoAlignPositions.chargeStationRed.getX() ){
              // newAutoState = NewAutoState.Done;
              newAutoState = NewAutoState.AutoBalance;
            }else{
              // Drivebase.driveFieldRelativeHeading(new Translation2d(0,-2), -180);
              Drivebase.driveRobotRelativeRotation(new Translation2d(2,0), 0);
            }
            break;
          case AutoBalance:
            Drivebase.autoBalance();
            break;
            
        } 

      }

  }

  @Override
  public void teleopInit() {
    // IntakeV2.setup();
    // Arm.setOffset();    
    // Arm.setArm(IO.GridArmPosition.Retract);
    // Elevator.setElevator(IO.GridRowPosition.Retract);
    IntakeV2.stopIntake();
    Drivebase.resetAnglesToAbsolute();
  }

  public void teleopAllState(){
    Drivebase.logData();
    Claw.logData();
    IO.keyInputOdometryMapping();
    IO.keyInputRowPosition();
    IO.keyInputSubstationPosition();
    IO.displayInformation();
    Drivebase.updateSwerveOdometry();
    Drivebase.displayInformation();
    AutoAlign.displayInformation();
    field2d.setRobotPose(Drivebase.getPose());
    GamepieceManager.displayInformation();
    IntakeV2.displayInformation();
    // Gyro.displayInformation();
  }

  private static boolean resetAngle = true;

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
      case POWER_SAVING:
        Claw.intakeFineControl(0);
        Arm.disableArm();
        Elevator.disableElevator();
        IntakeV2.stopIntake();
        if(resetAngle){
          SwerveDrive.headingSetPoint = Gyro.getAngle()-90;
        }
        resetAngle = false;
        Drivebase.driveFieldRelativeRotation(new Translation2d(IO.Drivebase.getSwerveTranslation().getX()*0.25,IO.Drivebase.getSwerveTranslation().getY()*0.25), IO.Drivebase.getSwerveRotation()*0.25);
        break;
      default:
        AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
        AutoAlign.substationAlignState = AutoAlign.SubstationAlignState.AlignedToOdometry;
        GamepieceManager.placeState = GamepieceManager.PlaceState.Align;

        GamepieceManager.loadSequence();
        // if (IO.getTestingButton()){
        //   IntakeV2.extendNoPid();
        // }
        // else if (IO.getTestingButtonTwo()){
        //   IntakeV2.handOffNoPid();
        // }else{
        //   IntakeV2.stopHandoff();
        // }
        // if(IO.resetAngle()){
        //   Gyro.set(90);
        //   SwerveDrive.headingSetPoint = -180;
        // }
        // if(IO.resetOdometryAngle()){

        // }
        // GamepieceManager.manageExtension();
        // Drivebase.drive();
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
    else if (IO.Drivebase.isPowerSaving()){
      robotState = RobotState.POWER_SAVING;
    }
    else {
      resetAngle = true;
      robotState = RobotState.DRIVING;
    }
  }
}
