package frc.robot;

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
    Arm.setOffset();
  }

  @Override
  public void robotPeriodic() {}

  public static enum AutoType{
    Old,
    New
  }
  public static AutoType autoTypeSelection = AutoType.New;

  public static enum AutoLeaveSelection{
    Left,
    Right,
    Charge,
    None
  }
  public static AutoLeaveSelection autoLeaveSelection = AutoLeaveSelection.Right;

  public static enum NewAutoLeaveSelection{
    Left,
    Right,
    None
  }
  public static NewAutoLeaveSelection newAutoLeaveSelection = NewAutoLeaveSelection.Right;
  public static boolean isCharge = false;

  public static int autoGridSelection = 8; //zero is left most, eight is right most
  public static class GamePiecePosition{
    int gridSelectionPosition;
    IO.GridRowPosition gridRowPosition;
    public GamePiecePosition(int gridSelectionPosition, IO.GridRowPosition gridRowPosition){
      this.gridSelectionPosition = gridSelectionPosition;
      this.gridRowPosition = gridRowPosition;
    }
  }
  public static GamePiecePosition[] gamePiecePositionArray = new GamePiecePosition[]{
    new GamePiecePosition(8,IO.GridRowPosition.High),
    new GamePiecePosition(7,IO.GridRowPosition.High)
    // new GamePiecePosition(7,IO.GridRowPosition.Retract)
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
    OutCommunity,
    InFrontOfGamePiece,
    MoveToGamePiece,
    GetGamePiece,
    PrepEnterCommunity,
    EnterCommunity,
    Done,
    PrepCharge,
    OnToCharge,
    AutoBalance, PrepLeaveCommunity, Turn
  }
  public static NewAutoState newAutoState;

  // public static int numberOfGamepieces = 3;
  public static int numberOfGamepieces;
  public static int numberOfGamepiecesPlaced;

  public static boolean isAutoConeNodePosition;

  private static Timer turnTimer = new Timer();
  private static Timer intakeTimer = new Timer();
  private static Timer visionTimer = new Timer();

  public static Translation2d autoGridSelectionTranslation2d(int position){
    if(position == 0){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid0.plus(new Translation2d(Constants.Auto.noHitGridOffset,0)):Constants.FieldPositions.AutoAlignPositions.redGrid0.minus(new Translation2d(Constants.Auto.noHitGridOffset,0));
    }
    else if(position == 1){
      isAutoConeNodePosition = false;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid1.plus(new Translation2d(Constants.Auto.noHitGridOffset,0)):Constants.FieldPositions.AutoAlignPositions.redGrid1.minus(new Translation2d(Constants.Auto.noHitGridOffset,0));
    }
    else if(position == 2){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid2.plus(new Translation2d(Constants.Auto.noHitGridOffset,0)):Constants.FieldPositions.AutoAlignPositions.redGrid2.minus(new Translation2d(Constants.Auto.noHitGridOffset,0));
    }
    else if(position == 3){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid3.plus(new Translation2d(Constants.Auto.noHitGridOffset,0)):Constants.FieldPositions.AutoAlignPositions.redGrid3.minus(new Translation2d(Constants.Auto.noHitGridOffset,0));
    }
    else if(position == 4){
      isAutoConeNodePosition = false;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid4.plus(new Translation2d(Constants.Auto.noHitGridOffset,0)):Constants.FieldPositions.AutoAlignPositions.redGrid4.minus(new Translation2d(Constants.Auto.noHitGridOffset,0));
    }
    else if(position == 5){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid5.plus(new Translation2d(Constants.Auto.noHitGridOffset,0)):Constants.FieldPositions.AutoAlignPositions.redGrid5.minus(new Translation2d(Constants.Auto.noHitGridOffset,0));
    }
    else if(position == 6){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid6.plus(new Translation2d(Constants.Auto.noHitGridOffset,0)):Constants.FieldPositions.AutoAlignPositions.redGrid6.minus(new Translation2d(Constants.Auto.noHitGridOffset,0));
    }
    else if(position == 7){
      isAutoConeNodePosition = false;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid7.plus(new Translation2d(Constants.Auto.noHitGridOffset,0)):Constants.FieldPositions.AutoAlignPositions.redGrid7.minus(new Translation2d(Constants.Auto.noHitGridOffset,0));
    }
    else if(position == 8){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid8.plus(new Translation2d(Constants.Auto.noHitGridOffset,0)):Constants.FieldPositions.AutoAlignPositions.redGrid8.minus(new Translation2d(Constants.Auto.noHitGridOffset,0));
    }else{
      return Drivebase.getPose().getTranslation();
    }
  }
  public static Translation2d autoGamePieceTranslation2d(){
    if(Constants.isBlue()){
      if(numberOfGamepiecesPlaced == 1){
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.blueGamePiece0.plus(new Translation2d(Constants.Auto.blueGamePieceOffset0X,Constants.Auto.blueGamePieceOffset0Y)):Constants.FieldPositions.AutoAlignPositions.blueGamePiece3.plus(new Translation2d(Constants.Auto.blueGamePieceOffset3X,Constants.Auto.blueGamePieceOffset3Y));
      }
      else if(numberOfGamepiecesPlaced == 2){
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.blueGamePiece1.plus(new Translation2d(Constants.Auto.blueGamePieceOffset1X,Constants.Auto.blueGamePieceOffset1Y)):Constants.FieldPositions.AutoAlignPositions.blueGamePiece2.plus(new Translation2d(Constants.Auto.blueGamePieceOffset2X,Constants.Auto.blueGamePieceOffset2Y));
      }
      else{
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.blueGamePiece0:Constants.FieldPositions.AutoAlignPositions.blueGamePiece3;
      }
    }
    else{
      if(numberOfGamepiecesPlaced == 1){
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.redGamePiece0.minus(new Translation2d(Constants.Auto.redGamePieceOffset0X,-Constants.Auto.redGamePieceOffset0Y)):Constants.FieldPositions.AutoAlignPositions.redGamePiece3.minus(new Translation2d(Constants.Auto.redGamePieceOffset3X,-Constants.Auto.redGamePieceOffset3Y));
      }
      else if(numberOfGamepiecesPlaced == 2){
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.redGamePiece1.minus(new Translation2d(Constants.Auto.redGamePieceOffset1X,-Constants.Auto.redGamePieceOffset1Y)):Constants.FieldPositions.AutoAlignPositions.redGamePiece2.minus(new Translation2d(Constants.Auto.redGamePieceOffset2X,-Constants.Auto.redGamePieceOffset2Y));
      }
      else{
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.redGamePiece0:Constants.FieldPositions.AutoAlignPositions.redGamePiece3;
      }
    }
  }
  public static double autoGamePieceHeading(){
    //if first piece rotation 0, else depend on left and right
    // return (numberOfGamepiecesPlaced == 1)?0:
    //                                         (newAutoLeaveSelection ==  NewAutoLeaveSelection.Left)?90:270;
    // return ;
    if(Constants.isBlue()){
      if(numberOfGamepiecesPlaced == 1){
        return 0;
        // return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Math.atan((Constants.FieldPositions.AutoAlignPositions.blueGamePiece0.getY()-autoCommunityOutTranslation2d().getY())/(Constants.FieldPositions.AutoAlignPositions.blueGamePiece0.getX()-autoCommunityOutTranslation2d().getX())):
        //                                                           Math.atan((Constants.FieldPositions.AutoAlignPositions.blueGamePiece3.getY()-autoCommunityOutTranslation2d().getY())/(Constants.FieldPositions.AutoAlignPositions.blueGamePiece3.getX()-autoCommunityOutTranslation2d().getX()));
      }
      //Roughtly -25 for Left and 25 for Right
      else if(numberOfGamepiecesPlaced == 2){ 
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Math.atan((Constants.FieldPositions.AutoAlignPositions.blueGamePiece1.getY()-autoCommunityOutTranslation2d().getY())/(Constants.FieldPositions.AutoAlignPositions.blueGamePiece1.getX()-autoCommunityOutTranslation2d().getX())):
                                                                  Math.atan((Constants.FieldPositions.AutoAlignPositions.blueGamePiece2.getY()-autoCommunityOutTranslation2d().getY())/(Constants.FieldPositions.AutoAlignPositions.blueGamePiece2.getX()-autoCommunityOutTranslation2d().getX()));
        }
      else{
        return -180;
      }
    }
    else{
      if(numberOfGamepiecesPlaced == 1){
        return 0;
        // return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Math.atan((Constants.FieldPositions.AutoAlignPositions.redGamePiece0.getY()-autoCommunityOutTranslation2d().getY())/(Constants.FieldPositions.AutoAlignPositions.redGamePiece0.getX()-autoCommunityOutTranslation2d().getX())):
        //                                                           Math.atan((Constants.FieldPositions.AutoAlignPositions.redGamePiece3.getY()-autoCommunityOutTranslation2d().getY())/(Constants.FieldPositions.AutoAlignPositions.redGamePiece3.getX()-autoCommunityOutTranslation2d().getX()));
      }
      else if(numberOfGamepiecesPlaced == 2){
        //Roughtly -25 for Left and 25 for Right
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Math.atan((Constants.FieldPositions.AutoAlignPositions.redGamePiece1.getY()-autoCommunityOutTranslation2d().getY())/(Constants.FieldPositions.AutoAlignPositions.redGamePiece1.getX()-autoCommunityOutTranslation2d().getX())):
                                                                  Math.atan((Constants.FieldPositions.AutoAlignPositions.redGamePiece2.getY()-autoCommunityOutTranslation2d().getY())/(Constants.FieldPositions.AutoAlignPositions.redGamePiece2.getX()-autoCommunityOutTranslation2d().getX()));
      }
      else{
        return -180;
      }
    }
    // return (numberOfGamepiecesPlaced == 1)?0:
    //                                         (newAutoLeaveSelection ==  NewAutoLeaveSelection.Left)?90:270;
  }
  public static Translation2d autoCommunityOutTranslation2d(){
    switch(newAutoLeaveSelection){
      case Left:
        return Constants.isBlue()?new Translation2d(Constants.FieldPositions.AutoAlignPositions.blueGrid0.getX() + Constants.Auto.communitySideOut ,Constants.FieldPositions.AutoAlignPositions.blueGrid0.getY() - Constants.Auto.notHitSideWall):
                                  new Translation2d(Constants.FieldPositions.AutoAlignPositions.redGrid0.getX() - Constants.Auto.communitySideOut ,Constants.FieldPositions.AutoAlignPositions.redGrid0.getY() + Constants.Auto.notHitSideWall);
      case Right:
        return Constants.isBlue()?new Translation2d(Constants.FieldPositions.AutoAlignPositions.blueGrid8.getX()  + Constants.Auto.communitySideOut,Constants.FieldPositions.AutoAlignPositions.blueGrid8.getY() + Constants.Auto.notHitSideWall):
                                  // new Translation2d(Constants.FieldPositions.AutoAlignPositions.red8.getX() - Constants.Auto.communitySideOut,4.583049);
                                  new Translation2d(Constants.FieldPositions.AutoAlignPositions.redGrid8.getX() - Constants.Auto.communitySideOut,Constants.FieldPositions.AutoAlignPositions.redGrid8.getY() - Constants.Auto.notHitSideWall);
      default:
        return Drivebase.getPose().getTranslation();
    }
  }
  public static Translation2d autoCommunityPrepLeaveTranslation2d(){
    switch(newAutoLeaveSelection){
      case Left:
        return Constants.isBlue()?new Translation2d(Constants.FieldPositions.AutoAlignPositions.blueGrid0.getX() + Constants.Auto.prepLeaveOffset ,Constants.FieldPositions.AutoAlignPositions.blueGrid0.getY() - Constants.Auto.notHitSideWall):
                                  new Translation2d(Constants.FieldPositions.AutoAlignPositions.redGrid0.getX() - Constants.Auto.prepLeaveOffset ,Constants.FieldPositions.AutoAlignPositions.redGrid0.getY() + Constants.Auto.notHitSideWall);
      case Right:
        return Constants.isBlue()?new Translation2d(Constants.FieldPositions.AutoAlignPositions.blueGrid8.getX()  + Constants.Auto.prepLeaveOffset,Constants.FieldPositions.AutoAlignPositions.blueGrid8.getY() + Constants.Auto.notHitSideWall):
                                  // new Translation2d(Constants.FieldPositions.AutoAlignPositions.red8.getX() - Constants.Auto.prepLeaveOffset,4.583049);
                                  new Translation2d(Constants.FieldPositions.AutoAlignPositions.redGrid8.getX() - Constants.Auto.prepLeaveOffset,Constants.FieldPositions.AutoAlignPositions.redGrid8.getY() - Constants.Auto.notHitSideWall);
      default:
        return Drivebase.getPose().getTranslation();
    }
  }
  public static Translation2d autoGamePieceSideTranslation2d(){
    switch(newAutoLeaveSelection){
      case Left:
        if(numberOfGamepiecesPlaced == 1){
          return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGamePiece0:Constants.FieldPositions.AutoAlignPositions.redGamePiece0;
        }
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
        return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid3:Constants.FieldPositions.AutoAlignPositions.redGrid3;
      case Right:
        return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid5:Constants.FieldPositions.AutoAlignPositions.redGrid5;
      default:
        return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid4:Constants.FieldPositions.AutoAlignPositions.redGrid4;
    }
  }
  Field2d field2d = new Field2d();

  @Override
  public void autonomousInit() {
    newAutoState = NewAutoState.Done;
    numberOfGamepieces = gamePiecePositionArray.length;
    numberOfGamepiecesPlaced = 0;
    isAutoConeNodePosition = false;
    SmartDashboard.putData("Field", field2d);
    Drivebase.resetAnglesToAbsolute();
    IntakeV2.setOffset();
    // Drivebase.setSwerveOdometry(new Pose2d(autoGridSelectionTranslation2d(autoGridSelection).getX(), autoGridSelectionTranslation2d(autoGridSelection).getY(),Gyro.getYawR2D()));
    Drivebase.setSwerveOdometry(new Pose2d(autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced].gridSelectionPosition).getX(), autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced].gridSelectionPosition).getY(),Gyro.getYawR2D()));
    // Drivebase.setSwerveOdometry(new Pose2d(autoGridSelectionTranslation2d(gamePiecePositionArray[0].gridSelectionPosition).getX(), autoGridSelectionTranslation2d(gamePiecePositionArray[0].gridSelectionPosition).getY(),Gyro.getYawR2D()));
    SmartDashboard.putString("Alliance Color",  DriverStation.getAlliance().toString());

  }

  public void autoAllState(){
    Drivebase.logData();
    Claw.logData();
    field2d.setRobotPose(Drivebase.getPose());
    SmartDashboard.putString("Alliance Color",  DriverStation.getAlliance().toString());
    SmartDashboard.putString("Auto State", newAutoState.toString());
    SmartDashboard.putBoolean("isAutoConeNode", isAutoConeNodePosition);
    AutoAlign.displayInformation();
    Drivebase.displayInformation();
    if(autoTypeSelection == AutoType.Old){
      Drivebase.updateSwerveOdometry();
    }
    // Arm.displayInformation();
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
            if (GamepieceManager.extention(IO.GridRowPosition.High, IO.GridArmPosition.ConePrepHigh)){
              autoState = AutoState.Place;
            }
            break;
          case Place:
            if(GamepieceManager.extention(IO.GridRowPosition.High, IO.GridArmPosition.ConeReadyHigh)){
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
                if(AutoAlign.alignOdometry(Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid0:Constants.FieldPositions.AutoAlignPositions.redGrid0, -180)){
                  autoState = AutoState.LeaveCommunity;
                }
                break;
              case Right:
              if(AutoAlign.alignOdometry(Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid8:Constants.FieldPositions.AutoAlignPositions.redGrid8, -180)){
                  autoState = AutoState.LeaveCommunity;
                }
                break;
              case Charge:
                if(AutoAlign.alignOdometry(Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid4:Constants.FieldPositions.AutoAlignPositions.redGrid4, -180)){
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
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            Drivebase.driveRobotRelativeRotation(new Translation2d(0,0), 0);
            IntakeV2.reverseIntake();
            Claw.intakeGamePiece();
            // System.out.println("Odometry X: "+ Drivebase.getPose().getX());
            break;
          case AutoBalance:
            Drivebase.autoBalance();
            break;            
        }
      break;
      case New:
        switch(newAutoState){
          //Get actual staging marking positions!!!
          //Also get the actual driver relative headings!
          //get offset for staging marks
          //slight pre-turn for prepcommunity out
          case Setup:
            Drivebase.updateSwerveOdometry();
            Claw.intakeGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.reverseIntake();
            // if (GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.ConePrep)){
            // if (GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.ConePrep)){
              newAutoState = NewAutoState.AlignNode;
            // }
            break;
          case AlignNode:
            Drivebase.updateSwerveOdometry();
            Claw.intakeGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.reverseIntake();
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.ConePrepHigh);
            if(AutoAlign.alignOdometry(autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced].gridSelectionPosition),
                                      -180) && GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.ConePrepHigh)){
              if (isAutoConeNodePosition){
                newAutoState = NewAutoState.AlignPosition;
              }else{
                newAutoState = NewAutoState.Place;
              }
            }
            //also get arm into position if it is a cube node, idk if we can pre-extend for cones
            // if (isAutoConeNodePosition){
            //   GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.ConePrep);
            // }else{
            //   GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.CubePrep);
            // }
            break;
          case AlignPosition:
            Drivebase.updateSwerveOdometry();
            Claw.intakeGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            GamepieceManager.extention(gamePiecePositionArray[numberOfGamepiecesPlaced].gridRowPosition, IO.GridArmPosition.ConePrepHigh);
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            if(AutoAlign.alignOdometry(isAutoConeNodePosition?new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:
                                                                            Constants.FieldPositions.atGridRedX, 
                                                                            autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced].gridSelectionPosition).getY()):
                                                              autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced].gridSelectionPosition).plus(Constants.isBlue()?new Translation2d(-Constants.Auto.noHitGridOffset,0):new Translation2d(Constants.Auto.noHitGridOffset,0)), 
                                      -180) &&
              GamepieceManager.extention(gamePiecePositionArray[numberOfGamepiecesPlaced].gridRowPosition, IO.GridArmPosition.ConePrepHigh)){
              // newAutoState = NewAutoState.Extend;
              newAutoState = NewAutoState.Place;
            }
            break;
          case Extend:
            //This is skipped
            Drivebase.updateSwerveOdometry();
            Claw.stopishMotor();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            if (GamepieceManager.extention(gamePiecePositionArray[numberOfGamepiecesPlaced].gridRowPosition, IO.GridArmPosition.ConePrepHigh)){
              newAutoState = NewAutoState.Place;
            }
            break;
          case Place:
            Drivebase.updateSwerveOdometry();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            if(GamepieceManager.extention(gamePiecePositionArray[numberOfGamepiecesPlaced].gridRowPosition, IO.GridArmPosition.ConeReadyHigh)){
              Claw.outputGamePiece();
              numberOfGamepiecesPlaced++;
              newAutoState = NewAutoState.LeaveNode;
            }else{
              Claw.stopishMotor();
            }
            break;
          case LeaveNode:
            Drivebase.updateSwerveOdometry();
            Claw.outputGamePiece();
            GamepieceManager.extention(gamePiecePositionArray[numberOfGamepiecesPlaced-1].gridRowPosition, IO.GridArmPosition.ConeReadyHigh);
            // GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.ConePrep);
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            // //if we have placed all pieces and we want to charge, then go charge
            // if(numberOfGamepiecesPlaced == numberOfGamepieces && isCharge){
            //   newAutoState = NewAutoState.PrepCharge;
            // }
            // //if we have placed all pieces, then finish
            // else if(numberOfGamepiecesPlaced == numberOfGamepieces){
            //   newAutoState = NewAutoState.Done;
            // }
            // else{
            //   newAutoState = NewAutoState.OutCommunity;
            // }
            if(AutoAlign.alignOdometry(new Translation2d(autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced-1].gridSelectionPosition).getX(),
                                                        autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced-1].gridSelectionPosition).getY()), 
                                      -180)){
              //if we have placed all pieces and we want to charge, then go charge
              if(numberOfGamepiecesPlaced == numberOfGamepieces && isCharge){
                newAutoState = NewAutoState.PrepCharge;
              }
              //if we have placed all pieces, then finish
              else if(numberOfGamepiecesPlaced == numberOfGamepieces){
                newAutoState = NewAutoState.Done;
              }
              else{
                newAutoState = NewAutoState.PrepLeaveCommunity;
              }
            }
            break;
          case PrepLeaveCommunity:
            Drivebase.updateSwerveOdometry();
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.CubeIntake);
            Claw.outputGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            if(AutoAlign.alignOdometry(autoCommunityPrepLeaveTranslation2d(), -180)){
              // Drivebase.headingSetPointSave = -180;
              if (numberOfGamepiecesPlaced == 1){
                // newAutoState = NewAutoState.InFrontOfGamePiece;
                newAutoState = NewAutoState.Turn;
                turnTimer.reset();
                turnTimer.start();
              }else{
                newAutoState = NewAutoState.Turn;
                turnTimer.reset();
                turnTimer.start();
                // newAutoState = NewAutoState.OutCommunity;
              }
            }
            if(newAutoLeaveSelection ==  NewAutoLeaveSelection.None){
              newAutoState = NewAutoState.Done;
            }
            break;
          case Turn:
            Drivebase.updateSwerveOdometryNoVision();
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.CubeIntake);
            Claw.outputGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            Drivebase.driveFieldRelativeHeading(new Translation2d(0,0), 0);
            // if(AutoAlign.alignOdometry(autoCommunityPrepLeaveTranslation2d(), 0)){
            SmartDashboard.putNumber("Turn Gryo", Math.abs(Gyro.getYaw()));
            if (Math.abs(Gyro.getYaw()-90)< 3  && (turnTimer.get() >1.2)){
              // newAutoState = NewAutoState.OutCommunity;
              newAutoState = NewAutoState.InFrontOfGamePiece;
              
            }
            break;
          case OutCommunity:
            // Drivebase.updateSwerveOdometry();
            Drivebase.updateSwerveOdometryNoVision();
            GamepieceManager.extention(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
            Claw.outputGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            // if(AutoAlign.alignOdometry(autoCommunityOutTranslation2d(), -179)){
            if(AutoAlign.alignOdometry(autoCommunityOutTranslation2d(), 0)){
              Drivebase.headingSetPointSave = 0;
              // Drivebase.headingSetPointSave = -179;
              newAutoState = NewAutoState.InFrontOfGamePiece;
            }
            if(newAutoLeaveSelection ==  NewAutoLeaveSelection.None){
              newAutoState = NewAutoState.Done;
            }
            break;
          case InFrontOfGamePiece:
            Drivebase.updateSwerveOdometryNoVision();
            Claw.intakeGamePiece();
            GamepieceManager.extention(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
            IntakeV2.retractNoPid();
            IntakeV2.slowIntake();
            if(AutoAlign.alignOdometry(Constants.isBlue()?autoGamePieceTranslation2d().minus(new Translation2d(Math.cos(Math.toRadians(autoGamePieceHeading()))*Constants.Auto.stopBeforeGamepieceOffset,
                                                                                                              Math.sin(Math.toRadians(autoGamePieceHeading()))*Constants.Auto.stopBeforeGamepieceOffset)):
                                                          autoGamePieceTranslation2d().plus(new Translation2d(Math.cos(Math.toRadians(autoGamePieceHeading()))*Constants.Auto.stopBeforeGamepieceOffset,
                                                                                                              Math.sin(Math.toRadians(autoGamePieceHeading()))*Constants.Auto.stopBeforeGamepieceOffset)), 
                                      autoGamePieceHeading())){
                  newAutoState = NewAutoState.MoveToGamePiece;
            }
            break;
          case MoveToGamePiece:
            Drivebase.updateSwerveOdometryNoVision();
            Claw.intakeGamePiece();
            GamepieceManager.extention(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
            IntakeV2.retractNoPid();
            IntakeV2.slowIntake();
            // if(AutoAlign.alignOdometry(autoGamePieceTranslation2d(),-180)){
            if(AutoAlign.alignOdometry(autoGamePieceTranslation2d(), autoGamePieceHeading())){
              intakeTimer.reset();
              intakeTimer.start();
              newAutoState = NewAutoState.GetGamePiece;
            }
            break;
          case GetGamePiece:
            Drivebase.updateSwerveOdometryNoVision();
            Claw.intakeGamePiece();
            GamepieceManager.extention(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
            IntakeV2.retractNoPid();
            IntakeV2.slowIntake();
            AutoAlign.alignOdometry(autoGamePieceTranslation2d(), autoGamePieceHeading());
            // AutoAlign.alignOdometry(autoGamePieceTranslation2d(), -180);
            if (intakeTimer.hasElapsed(Constants.Auto.intakeRunTime) ){
              Drivebase.headingSetPointSave = SwerveDrive.headingSetPoint;
              Drivebase.setSwerveOdometry(new Pose2d(autoGamePieceTranslation2d(),Gyro.getYawR2D()));
              newAutoState = NewAutoState.PrepEnterCommunity;
            }
            break;
          case PrepEnterCommunity:
            Drivebase.updateSwerveOdometryNoVision();
            //to update the isAutoConeNodePosition
            autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced].gridSelectionPosition);
            // GamepieceManager.extention(IO.GridRowPosition.Retract, isAutoConeNodePosition?IO.GridArmPosition.ConePrep:IO.GridArmPosition.CubePrep);
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.ConePrepHigh);
            IntakeV2.retractNoPid();
            Claw.stopishMotor();
            if(AutoAlign.alignOdometry(autoCommunityOutTranslation2d(), -180)){
              // newAutoState = NewAutoState.EnterCommunity;
              visionTimer.reset();
              visionTimer.start();
              newAutoState = NewAutoState.EnterCommunity;
            }
            break;
          case EnterCommunity:
            Drivebase.updateSwerveOdometry();
            //to update the isAutoConeNodePosition
            autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced].gridSelectionPosition);
            // GamepieceManager.extention(IO.GridRowPosition.Retract, isAutoConeNodePosition?IO.GridArmPosition.ConePrep:IO.GridArmPosition.CubePrep);
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.ConePrepHigh);
            IntakeV2.retractNoPid();
            Claw.stopishMotor();
            AutoAlign.alignOdometry(autoCommunityOutTranslation2d(), -180);
            if (visionTimer.hasElapsed(Constants.Auto.visionWaitTime) ){
              // newAutoState = NewAutoState.AlignNode;
              newAutoState = NewAutoState.AlignPosition;
            }
            break;
          case Done:
            Drivebase.updateSwerveOdometry();
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            Drivebase.driveFieldRelativeRotation(new Translation2d(0, 0), 0);
            // AutoAlign.alignOdometry(autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced-1].gridSelectionPosition), -180);
            break;
          case PrepCharge:
            Drivebase.updateSwerveOdometry();
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
        break;
      }

  }

  @Override
  public void teleopInit() {
    // Arm.setOffset();
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
    // Drivebase.displayInformation();
    AutoAlign.displayInformation();
    field2d.setRobotPose(Drivebase.getPose());
    GamepieceManager.displayInformation();
    // IntakeV2.displayInformation();
    // Elevator.displayInformation();
    // Arm.displayInformation();
    // Gyro.displayInformation();
    // Gyro.displayInformation();
  }

  private static boolean resetAngle = true;

  @Override
  public void teleopPeriodic() {
    selectTeleopState();
    teleopAllState();

    switch(robotState){
      case AUTO_ALIGN:
        GamepieceManager.autoAlign();
        IntakeV2.retractNoPid();
        break;
      case AUTO_BALANCE:
        Drivebase.autoBalance();
        GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
        IntakeV2.retractNoPid();
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

        if(IO.resetAngle()){
          Gyro.set(90);
          SwerveDrive.headingSetPoint = -180;
        }
        GamepieceManager.loadSequence();
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
    if (IO.Drivebase.isAutoAlignActive() && (AutoAlign.isInLoadingZone()|| AutoAlign.isInCommunity())) {
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
