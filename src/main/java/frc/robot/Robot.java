package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.net.PortForwarder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Auto.Auto;
import frc.robot.Auto.AutoSelector;
import frc.robot.Sensors.AprilTagCameraWrapper;
import frc.robot.Sensors.Gyro;
import frc.robot.Sensors.LimelightCameraWrapper;
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
    CUBE_ALIGN
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
    AutoSelector.start();
    // Arm.setOffset();
  }

  @Override
  public void robotPeriodic() {}

  public static enum AutoType{
    Old,
    New
  }
  public static AutoType autoTypeSelection;

  public static enum AutoLeaveSelection{
    Left,
    Right,
    Charge,
    None
  }
  public static AutoLeaveSelection autoLeaveSelection;

  public static enum NewAutoLeaveSelection{
    Left,
    Right,
    None
  }
  public static NewAutoLeaveSelection newAutoLeaveSelection;
  public static boolean isCharge = false;

  public static int autoGridSelection; //zero is left most, eight is right most
  public static class GamePiecePosition{
    int gridSelectionPosition;
    IO.GridRowPosition gridRowPosition;
    public GamePiecePosition(int gridSelectionPosition, IO.GridRowPosition gridRowPosition){
      this.gridSelectionPosition = gridSelectionPosition;
      this.gridRowPosition = gridRowPosition;
    }
  }
  public static GamePiecePosition[] gamePiecePositionArray;

  public static enum AutoState {
        Place,
        LeaveNode,
        OutCommunity,
        LeaveCommunity,
        Done,
        Turn,
        LimelightTranslation,
        GetGamePiece,
        OnToCharge,
        AutoBalance
  }
  public static AutoState autoState;

  public static enum NewAutoState{
    AlignPosition,
    Place,
    LeaveNode,
    OutCommunity,
    InFrontOfGamePiece,
    MoveToGamePiece,
    LimelightTranslation,
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
  private static Timer cubeAlignTimer = new Timer();
  private static Timer intakeTimer = new Timer();
  private static Timer visionTimer = new Timer();
  private static Timer cubePlaceTimer = new Timer();

  public static Translation2d autoGridSelectionTranslation2d(int position){
    if(position == 0){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid0:Constants.FieldPositions.AutoAlignPositions.redGrid0;
    }
    else if(position == 1){
      isAutoConeNodePosition = false;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid1:Constants.FieldPositions.AutoAlignPositions.redGrid1;
    }
    else if(position == 2){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid2:Constants.FieldPositions.AutoAlignPositions.redGrid2;
    }
    else if(position == 3){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid3:Constants.FieldPositions.AutoAlignPositions.redGrid3;
    }
    else if(position == 4){
      isAutoConeNodePosition = false;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid4:Constants.FieldPositions.AutoAlignPositions.redGrid4;
    }
    else if(position == 5){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid5:Constants.FieldPositions.AutoAlignPositions.redGrid5;
    }
    else if(position == 6){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid6:Constants.FieldPositions.AutoAlignPositions.redGrid6;
    }
    else if(position == 7){
      isAutoConeNodePosition = false;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid7:Constants.FieldPositions.AutoAlignPositions.redGrid7;
    }
    else if(position == 8){
      isAutoConeNodePosition = true;
      return Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid8:Constants.FieldPositions.AutoAlignPositions.redGrid8;
    }else{
      return Drivebase.getPose().getTranslation();
    }
  }
  public static Translation2d autoGamePieceTranslation2d(boolean useOffset){
    if(Constants.isBlue()){
      if(numberOfGamepiecesPlaced == 1){
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.blueGamePiece0.plus(useOffset?new Translation2d(Constants.Auto.blueGamePieceOffset0X,-Constants.Auto.blueGamePieceOffset0Y): new Translation2d(0,0)):Constants.FieldPositions.AutoAlignPositions.blueGamePiece3.plus(useOffset?new Translation2d(Constants.Auto.blueGamePieceOffset3X,-Constants.Auto.blueGamePieceOffset3Y): new Translation2d(0,0));
      }
      else if(numberOfGamepiecesPlaced == 2){
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.blueGamePiece1.plus(useOffset?new Translation2d(Constants.Auto.blueGamePieceOffset1X,-Constants.Auto.blueGamePieceOffset1Y):new Translation2d(0,0)):Constants.FieldPositions.AutoAlignPositions.blueGamePiece2.plus(useOffset?new Translation2d(Constants.Auto.blueGamePieceOffset2X,-Constants.Auto.blueGamePieceOffset2Y): new Translation2d(0,0));
      }
      else{
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.blueGamePiece0:Constants.FieldPositions.AutoAlignPositions.blueGamePiece3;
      }
    }
    else{
      if(numberOfGamepiecesPlaced == 1){
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.redGamePiece0.minus(useOffset?new Translation2d(Constants.Auto.redGamePieceOffset0X,-Constants.Auto.redGamePieceOffset0Y): new Translation2d(0,0)):Constants.FieldPositions.AutoAlignPositions.redGamePiece3.minus(useOffset?new Translation2d(Constants.Auto.redGamePieceOffset3X,-Constants.Auto.redGamePieceOffset3Y):new Translation2d(0,0));
      }
      else if(numberOfGamepiecesPlaced == 2){
        return (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?Constants.FieldPositions.AutoAlignPositions.redGamePiece1.minus(useOffset?new Translation2d(Constants.Auto.redGamePieceOffset1X,-Constants.Auto.redGamePieceOffset1Y): new Translation2d(0,0)):Constants.FieldPositions.AutoAlignPositions.redGamePiece2.minus(useOffset?new Translation2d(Constants.Auto.redGamePieceOffset2X,-Constants.Auto.redGamePieceOffset2Y): new Translation2d(0,0));
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
  Field2d field2dVision = new Field2d();

  public static int firstPiece;
  public static int secondPiece;
  public static boolean getGamePiece;

  @Override
  public void autonomousInit() {
    AutoSelector.set();
    // autoTypeSelection = AutoType.New;
    // autoLeaveSelection = AutoLeaveSelection.Right;
    // newAutoLeaveSelection = NewAutoLeaveSelection.Right;
    // autoGridSelection = 6;
    // autoState = AutoState.Place;
    gamePiecePositionArray = new GamePiecePosition[]{
      new GamePiecePosition(firstPiece,IO.GridRowPosition.HighFarConeAuto),
      new GamePiecePosition(secondPiece,IO.GridRowPosition.HighFarConeAndCube)
      // new GamePiecePosition(8,IO.GridRowPosition.HighFarConeAuto),
      // new GamePiecePosition(7,IO.GridRowPosition.HighFarConeAndCube)
      // new GamePiecePosition(7,IO.GridRowPosition.Retract)
      
    };
    getGamePiece = true;
    // newAutoState = NewAutoState.Place;
    numberOfGamepieces = gamePiecePositionArray.length;
    numberOfGamepiecesPlaced = 0;
    isAutoConeNodePosition = true;
    SmartDashboard.putData("Field", field2d);
    Drivebase.resetAnglesToAbsolute();
    IntakeV2.setOffset();
    // Arm.setOffset();
    Arm.setUpperOffset();
    Elevator.setOffset();
    switch(autoTypeSelection){
      case Old:
        Drivebase.setSwerveOdometry(new Pose2d(autoGridSelectionTranslation2d(autoGridSelection),Gyro.getYawR2D()));
        break;
      case New:
        Drivebase.setSwerveOdometry(new Pose2d(autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced].gridSelectionPosition),Gyro.getYawR2D()));
        break;
    }
    SmartDashboard.putString("Alliance Color",  DriverStation.getAlliance().toString());

  }

  public void autoAllState(){
    Drivebase.logData();
    Claw.logData();
    field2d.setRobotPose(Drivebase.getPose());
    SmartDashboard.putBoolean("isAlignedToGamePiece()", LimelightCameraWrapper.isAlignedToGamePiece());
    SmartDashboard.putBoolean("hasTargets", LimelightCameraWrapper.hasTargets());
    SmartDashboard.putString("Auto State New or Old", autoTypeSelection == AutoType.New?newAutoState.toString():autoState.toString());
    SmartDashboard.putBoolean("isAutoConeNode", isAutoConeNodePosition);
    AutoAlign.displayInformation();
    Drivebase.displayInformation();
    Arm.displayInformation();
  }
  double yAlign = autoGamePieceTranslation2d(true).getY();
  @Override
  public void autonomousPeriodic() {
    autoAllState();
    switch(autoTypeSelection){
      case Old:
        switch(autoState){
          case Place:
          Drivebase.updateSwerveOdometry();
          if(GamepieceManager.extention(IO.GridRowPosition.HighFarConeAuto, isAutoConeNodePosition?IO.GridArmPosition.ConeFarReadyHighAuto:IO.GridArmPosition.coneFarPrepHighAuto)){
              Claw.outputGamePiece();
              autoState = AutoState.LeaveNode;
            }else{
              Claw.stopishMotor();
            }
            break;
          case LeaveNode:
          Drivebase.updateSwerveOdometry();
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            Claw.outputGamePiece();
            if(AutoAlign.alignOdometry(autoGridSelectionTranslation2d(autoGridSelection), -180)){
              autoState = AutoState.OutCommunity;
            }
            break;
          case OutCommunity:
          Drivebase.updateSwerveOdometry();
            switch(autoLeaveSelection){
              case Left:
                GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
                if(AutoAlign.alignOdometry(Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid0:Constants.FieldPositions.AutoAlignPositions.redGrid0, -180)){
                  autoState = AutoState.LeaveCommunity;
                }
                break;
              case Right:
                GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
              if(AutoAlign.alignOdometry(Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid8:Constants.FieldPositions.AutoAlignPositions.redGrid8, -180)){
                  autoState = AutoState.LeaveCommunity;
                }
                break;
              case Charge:
                autoState = AutoState.LeaveCommunity;
                // GamepieceManager.extention(IO.GridRowPosition.CubeRetract, IO.GridArmPosition.CubeRetract);
                // if(AutoAlign.alignOdometry(Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueGrid4:Constants.FieldPositions.AutoAlignPositions.redGrid4, -180)){
                //   autoState = AutoState.LeaveCommunity;
                // }
                break;
              case None:
                GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
                autoState = AutoState.Done;
                break;
                
            }
            break;
          case LeaveCommunity:
          Drivebase.updateSwerveOdometry();
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            switch(autoLeaveSelection){
              case Left:
              GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
                if(Constants.isBlue()?Drivebase.getPose().getX()>Constants.FieldPositions.outSubstationShortBlue:
                                      Drivebase.getPose().getX()<Constants.FieldPositions.outSubstationLongRed){
                  autoState = AutoState.Done;
                }else{
                  Drivebase.driveFieldRelativeHeading(new Translation2d(0,-1), -180, true);
                }
                break;
              case Right:
                GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
                if(Constants.isBlue()?Drivebase.getPose().getX()>Constants.FieldPositions.outSubstationLongBlue:
                                      Drivebase.getPose().getX()<Constants.FieldPositions.outSubstationShortRed ){
                  autoState = AutoState.Done;
                }else{
                  Drivebase.driveFieldRelativeHeading(new Translation2d(0,-1), -180, true);
                }
                break;
              case Charge:
                GamepieceManager.extention(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
                if(Constants.isBlue()?Drivebase.getPose().getX()>Constants.FieldPositions.AutoAlignPositions.mobilityBlue:
                  Drivebase.getPose().getX()<Constants.FieldPositions.AutoAlignPositions.mobilityRed ){
                  // autoState = AutoState.Done;
                  if (getGamePiece){
                    autoState = AutoState.Turn;
                  }else{
                    autoState = AutoState.OnToCharge;
                  }
                }else{
                  Drivebase.driveFieldRelativeHeading(new Translation2d(0,-2), -180, true);
                  // Drivebase.driveRobotRelativeRotation(new Translation2d(0,2), 0);
                }
                break;
              case None:
                GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
                autoState = AutoState.Done;
                break;
            }
            break;
          case Done:
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            Drivebase.driveRobotRelativeRotation(new Translation2d(0,0), 0);
            IntakeV2.reverseIntake();
            Claw.intakeGamePiece();
            break;
          case Turn:
            Drivebase.updateSwerveOdometryNoVision();
            GamepieceManager.extention(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
            Claw.intakeGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            Drivebase.driveFieldRelativeHeading(new Translation2d(0,0), 0, true);
            if (Math.abs(Gyro.getYaw()-90)< 3  && (turnTimer.get() >1.1)){
              autoState = AutoState.LimelightTranslation;
            }
            break;
          case LimelightTranslation:
            Drivebase.updateSwerveOdometryNoVision();
            GamepieceManager.extention(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
            Claw.intakeGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            if(LimelightCameraWrapper.hasTargets()?(AutoAlign.alignToPiece(true) && cubeAlignTimer.hasElapsed(0.2)):true){
              yAlign = Drivebase.getPose().getY();
              newAutoState = NewAutoState.GetGamePiece;
            }
            break;
          case GetGamePiece:
            Drivebase.updateSwerveOdometryNoVision();
            GamepieceManager.extention(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
            Claw.intakeGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            if(AutoAlign.alignOdometry(new Translation2d(Drivebase.getPose().getX()+(Constants.isBlue()?0.5:-0.5), yAlign), 0)){
              newAutoState = NewAutoState.OnToCharge;
            }
            
            break;
          case OnToCharge:
            Drivebase.updateSwerveOdometryNoVision();
            if(Constants.isBlue()?Drivebase.getPose().getX()<Constants.FieldPositions.AutoAlignPositions.chargeStationBlue.getX():
                Drivebase.getPose().getX()>Constants.FieldPositions.AutoAlignPositions.chargeStationRed.getX() ){
                autoState = AutoState.AutoBalance;

            }else{
              // Drivebase.driveRobotRelativeRotation(new Translation2d(0,-2), 0);
              Drivebase.driveFieldRelativeHeading(new Translation2d(0,2), 0, true);
            }
            break;
          case AutoBalance:
            Drivebase.updateSwerveOdometryNoVision();
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
          case AlignPosition:
            Drivebase.updateSwerveOdometry();
            Claw.intakeGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            //have to run it and check it
            GamepieceManager.extention(gamePiecePositionArray[numberOfGamepiecesPlaced].gridRowPosition, IO.GridArmPosition.coneFarPrepHighAuto);
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            if(AutoAlign.alignOdometrykP(isAutoConeNodePosition?new Translation2d(Constants.isBlue()?Constants.FieldPositions.nearGridBlueX:
                                                                                                  Constants.FieldPositions.nearGridRedX, 
                                                                                autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced].gridSelectionPosition).getY()):
                                                              autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced].gridSelectionPosition), 
                                      -180, 2, 2,0.1, false) &&
              GamepieceManager.extention(gamePiecePositionArray[numberOfGamepiecesPlaced].gridRowPosition, IO.GridArmPosition.coneFarPrepHighAuto)){
              cubePlaceTimer.reset();
              cubePlaceTimer.start();
              newAutoState = NewAutoState.Place;
            }
            break;
          case Place:
            if (numberOfGamepiecesPlaced !=0){
              AutoAlign.alignOdometrykP(isAutoConeNodePosition?new Translation2d(Constants.isBlue()?Constants.FieldPositions.nearGridBlueX:
                                                                                                    Constants.FieldPositions.nearGridRedX, 
                                                                              autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced].gridSelectionPosition).getY()):
                                                            autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced].gridSelectionPosition), 
                                        -180, 1, 1,0.05, false);
            }
            Drivebase.updateSwerveOdometry();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            if(GamepieceManager.extention(gamePiecePositionArray[numberOfGamepiecesPlaced].gridRowPosition, isAutoConeNodePosition?IO.GridArmPosition.ConeFarReadyHighAuto:IO.GridArmPosition.coneFarPrepHighAuto)){
              Claw.outputGamePiece();
              if(isAutoConeNodePosition?true:cubePlaceTimer.hasElapsed(0.5)){
                numberOfGamepiecesPlaced++;
                newAutoState = NewAutoState.LeaveNode;
              }
            }else{
              Claw.stopishMotor();
            }
            break;
          case LeaveNode:
            Drivebase.updateSwerveOdometry();
            Claw.outputGamePiece();
            GamepieceManager.extention(gamePiecePositionArray[numberOfGamepiecesPlaced-1].gridRowPosition, IO.GridArmPosition.ConeFarReadyHighAuto);
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
            if(AutoAlign.alignOdometrykP(autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced-1].gridSelectionPosition), 
                                      -180, Constants.AutoAlign.odometryAlignXkP, Constants.AutoAlign.odometryAlignYkP, 0.15, false)){
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
            // GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.CubeIntake);
            GamepieceManager.extention(IO.GridRowPosition.CubeRetract, IO.GridArmPosition.CubeRetract);
            Claw.outputGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            if(AutoAlign.alignOdometry(autoCommunityPrepLeaveTranslation2d(), (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?-179:-181)){
              // Drivebase.headingSetPointSave = -180;
              Drivebase.headingSetPointSave = (newAutoLeaveSelection == NewAutoLeaveSelection.Left)?-179:-181;
              if (numberOfGamepiecesPlaced == 1){
                newAutoState = NewAutoState.Turn;
                turnTimer.reset();
                turnTimer.start();
              }else{
                newAutoState = NewAutoState.Turn;
                turnTimer.reset();
                turnTimer.start();
              }
            }
            if(newAutoLeaveSelection ==  NewAutoLeaveSelection.None){
              newAutoState = NewAutoState.Done;
            }
            break;
          case Turn:
            Drivebase.updateSwerveOdometryNoVision();
            GamepieceManager.extention(IO.GridRowPosition.CubeRetract, IO.GridArmPosition.CubeRetract);
            Claw.outputGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            Drivebase.driveFieldRelativeHeading(new Translation2d(0,0), 0, true);
            if (Math.abs(Gyro.getYaw()-90)< 3  && (turnTimer.get() >1.1)){
              if (numberOfGamepiecesPlaced == 1){
                newAutoState = NewAutoState.InFrontOfGamePiece;
              }else{
                newAutoState = NewAutoState.OutCommunity;
              }
              
            }
            break;
          case OutCommunity:
            Drivebase.updateSwerveOdometryNoVision();
            GamepieceManager.extentionArmFirst(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
            Claw.outputGamePiece();
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            if(AutoAlign.alignOdometrykP(autoCommunityOutTranslation2d(), 0, Constants.Auto.autoXkP, Constants.Auto.autoYkP ,0.1, false)){
              Drivebase.headingSetPointSave = 0;
              newAutoState = NewAutoState.InFrontOfGamePiece;
            }
            if(newAutoLeaveSelection ==  NewAutoLeaveSelection.None){
              newAutoState = NewAutoState.Done;
            }
            break;
          case InFrontOfGamePiece:
            Drivebase.updateSwerveOdometryNoVision();
            Claw.intakeGamePiece();
            GamepieceManager.extentionArmFirst(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            if(AutoAlign.alignOdometrykP(Constants.isBlue()?autoGamePieceTranslation2d(true).minus(new Translation2d(Math.cos(Math.toRadians(autoGamePieceHeading()))*Constants.Auto.stopBeforeGamepieceOffset,
                                                                                                              Math.sin(Math.toRadians(autoGamePieceHeading()))*Constants.Auto.stopBeforeGamepieceOffset)):
                                                          autoGamePieceTranslation2d(true).plus(new Translation2d(Math.cos(Math.toRadians(autoGamePieceHeading()))*Constants.Auto.stopBeforeGamepieceOffset,
                                                                                                              Math.sin(Math.toRadians(autoGamePieceHeading()))*Constants.Auto.stopBeforeGamepieceOffset)), 
                                      autoGamePieceHeading(), Constants.Auto.autoXkP, Constants.Auto.autoYkP, 0.1, false)){
                  cubeAlignTimer.reset();
                  cubeAlignTimer.start();
                  newAutoState = NewAutoState.LimelightTranslation;
            }
            break;
          case LimelightTranslation:
          Drivebase.updateSwerveOdometryNoVision();
          Claw.intakeGamePiece();
          GamepieceManager.extentionArmFirst(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
          IntakeV2.retractNoPid();
          IntakeV2.stopIntake();
            if(LimelightCameraWrapper.hasTargets()?(AutoAlign.alignToPiece(true) && cubeAlignTimer.hasElapsed(0.2)):true){
              yAlign = Drivebase.getPose().getY();
              newAutoState = NewAutoState.MoveToGamePiece;
            }
            break;
          case MoveToGamePiece:
            Drivebase.updateSwerveOdometryNoVision();
            Claw.intakeGamePiece();
            GamepieceManager.extentionArmFirst(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            // if(AutoAlign.alignOdometrykP(autoGamePieceTranslation2d(true), autoGamePieceHeading(), Constants.Auto.autoXkP, Constants.Auto.autoYkP ,0.05)){
            if(AutoAlign.alignOdometrykP(new Translation2d(autoGamePieceTranslation2d(true).getX(),yAlign), autoGamePieceHeading(), 1, 1 ,0.1, false)){
            // Drivebase.driveRobotRelativeRotation(new Translation2d(0,-1.5), 0);
            // if(Constants.isBlue()?Drivebase.getPose().getX()> autoGamePieceTranslation2d(true).getX():Drivebase.getPose().getX()< autoGamePieceTranslation2d(true).getX()){
              intakeTimer.reset();
              intakeTimer.start();
              newAutoState = NewAutoState.GetGamePiece;
            }
            break;
          case GetGamePiece:
            Drivebase.updateSwerveOdometryNoVision();
            Claw.intakeGamePiece();
            GamepieceManager.extentionArmFirst(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            // AutoAlign.alignOdometrykP(autoGamePieceTranslation2d(true), autoGamePieceHeading(), Constants.Auto.autoXkP, Constants.Auto.autoYkP,0.05);
            Drivebase.driveRobotRelativeRotation(new Translation2d(0,-0.2), 0);
            if (intakeTimer.hasElapsed(Constants.Auto.intakeRunTime) ){
              Drivebase.headingSetPointSave = SwerveDrive.headingSetPoint;
              // Drivebase.setSwerveOdometry(new Pose2d(autoGamePieceTranslation2d(false),Gyro.getYawR2D()));
              // newAutoState = NewAutoState.Done;
              newAutoState = NewAutoState.PrepEnterCommunity;

            }
            break;
          case PrepEnterCommunity:
            Drivebase.updateSwerveOdometryNoVision();
            //to update the isAutoConeNodePosition
            autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced].gridSelectionPosition);
            // GamepieceManager.extention(IO.GridRowPosition.Retract, isAutoConeNodePosition?IO.GridArmPosition.ConePrep:IO.GridArmPosition.CubePrep);
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            IntakeV2.retractNoPid();
            IntakeV2.stopIntake();
            Claw.stopishMotor();
            if(AutoAlign.alignOdometrykP(autoCommunityOutTranslation2d().plus(new Translation2d(0, Constants.isBlue()?((newAutoLeaveSelection == NewAutoLeaveSelection.Left)?-0.6:0):(newAutoLeaveSelection == NewAutoLeaveSelection.Left)?0.6:0.1)), -180, Constants.Auto.autoXkP, Constants.Auto.autoYkP ,0.15, false)){
              // newAutoState = NewAutoState.EnterCommunity;
              visionTimer.reset();
              visionTimer.start();
              // newAutoState = NewAutoState.EnterCommunity; skipping cause don't need it rn
              if (isAutoConeNodePosition){
                newAutoState = NewAutoState.AlignPosition;
              }else{
                newAutoState = NewAutoState.AlignPosition;
              }
            }
            break;
          // case EnterCommunity:
          //   Drivebase.updateSwerveOdometry();
          //   //to update the isAutoConeNodePosition
          //   autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced].gridSelectionPosition);
          //   // GamepieceManager.extention(IO.GridRowPosition.Retract, isAutoConeNodePosition?IO.GridArmPosition.ConePrep:IO.GridArmPosition.CubePrep);
          //   GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.ConeFarPrepHigh);
          //   IntakeV2.retractNoPid();
          //   Claw.stopishMotor();
          //   AutoAlign.alignOdometry(autoCommunityOutTranslation2d(), -180);
          //   if (visionTimer.hasElapsed(Constants.Auto.visionWaitTime) ){
          //     if (isAutoConeNodePosition){
          //       newAutoState = NewAutoState.AlignPosition;
          //     }else{
          //       newAutoState = NewAutoState.AlignPosition;
          //     }
          //   }
          //   break;
          case Done:
            Drivebase.updateSwerveOdometry();
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            IntakeV2.stopIntake();
            IntakeV2.retractNoPid();
            Claw.stopishMotor();
            Drivebase.driveFieldRelativeRotation(new Translation2d(0, 0), 0 ,false, false);
            // AutoAlign.alignOdometry(autoGridSelectionTranslation2d(gamePiecePositionArray[numberOfGamepiecesPlaced-1].gridSelectionPosition), -180);
            break;
          case PrepCharge:
            Drivebase.updateSwerveOdometry();
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            IntakeV2.stopIntake();
            IntakeV2.retractNoPid();
            Claw.stopishMotor();
            if(AutoAlign.alignOdometry(autoChargePrepTranslation2d(), -180)){
              newAutoState = NewAutoState.OnToCharge;
            }
            break;
          case OnToCharge:
            Drivebase.updateSwerveOdometry();
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            IntakeV2.stopIntake();
            IntakeV2.retractNoPid();
            Claw.stopishMotor();
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
            GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            IntakeV2.stopIntake();
            IntakeV2.retractNoPid();
            Claw.stopishMotor();
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
    GamepieceManager.clawTimer.start();
  }

  public void teleopAllState(){
    Drivebase.logData();
    Claw.logData();
    IO.keyInputOdometryMapping();
    IO.keyInputRowPosition();
    IO.keyInputSubstationPosition();
    IO.keyGridPosition();
    IO.displayInformation();
    Drivebase.updateSwerveOdometry();
    Drivebase.displayInformation();
    AutoAlign.displayInformation();
    field2d.setRobotPose(Drivebase.getPose());
    field2dVision.setRobotPose(Drivebase.getEstimatedVisionPose());

    GamepieceManager.displayInformation();
    // IntakeV2.displayInformation();
    Elevator.displayInformation();
    Arm.displayInformation();
    // Gyro.displayInformation();
    // Gyro.displayInformation();
  }

  private static boolean resetAngle = true;

  @Override
  public void teleopPeriodic() {
    selectTeleopState();
    teleopAllState();
    // SmartDashboard.putNumber("getHorizontalOffset()", LimelightCameraWrapper.getHorizontalOffset());
    // SmartDashboard.putNumber("getVerticalOffset()", LimelightCameraWrapper.getVerticalOffset());
    SmartDashboard.putBoolean("isAlignedToGamePiece()", LimelightCameraWrapper.isAlignedToGamePiece());
    SmartDashboard.putBoolean("hasTargets", LimelightCameraWrapper.hasTargets());
    SmartDashboard.putNumber("offset",  LimelightCameraWrapper.getHorizontalOffset() - Constants.LimelightCamera.gamePieceVerticalToHorizontalA * Math.pow(Constants.LimelightCamera.gamePieceVerticalToHorizontalB, LimelightCameraWrapper.getVerticalOffset()));


    switch(robotState){
      case AUTO_ALIGN:
        // GamepieceManager.manageExtension();

        GamepieceManager.autoAlign();
        // AutoAlign.alignToPiece();

        IntakeV2.retractNoPid();
        if(IO.resetAngle()){
          Gyro.set(90);
          SwerveDrive.headingSetPoint = -180;
          // AutoAlign.alignRotation = -180;
          // Drivebase.headingSetPointSave = -180;
        }

        break;
      case CUBE_ALIGN:
        GamepieceManager.autoCubeIntake();
        // // Drivebase.autoBalance();
        // GamepieceManager.extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
        // IntakeV2.retractNoPid();
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
        Drivebase.driveFieldRelativeRotation(new Translation2d(IO.Drivebase.getSwerveTranslation().getX()*0.25,IO.Drivebase.getSwerveTranslation().getY()*0.25), IO.Drivebase.getSwerveRotation()*0.25 ,true, true);
        break;
      default:
        AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
        AutoAlign.substationAlignState = AutoAlign.SubstationAlignState.AlignedToOdometry;
        GamepieceManager.placeState = GamepieceManager.PlaceState.Align;
        GamepieceManager.cubeIntakeState = GamepieceManager.CubeIntakeState.Align;
        resetAngle = true;

        if(IO.resetAngle()){
          Gyro.set(90);
          SwerveDrive.headingSetPoint = -180;
        }
        IO.rumbleStopJoy0();
        
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
    else if (IO.Drivebase.isCubeAlignActive()){
      robotState = RobotState.CUBE_ALIGN;
    } 
    else if (IO.Drivebase.isPowerSaving()){
      robotState = RobotState.POWER_SAVING;
    }
    else {
      resetAngle = true;
      robotState = RobotState.DRIVING;
    }
  }
}
