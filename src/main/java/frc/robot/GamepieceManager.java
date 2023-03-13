package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.IO.GridArmPosition;
import frc.robot.IO.GridRowPosition;
import frc.robot.Robot.AutoState;
import frc.robot.Subsystems.Arm;
import frc.robot.Subsystems.Claw;
import frc.robot.Subsystems.Elevator;
import frc.robot.Subsystems.IntakeV2;

public class GamepieceManager {

    public static boolean runFlapsAuto(int speed) {
        return IntakeV2.moveIntake(speed * 4);
    }

    // public static boolean foldIntakeAuto(int position) {
    //     if (position == 0) {
    //         return IntakeV2.handOff();
    //     } else if (position == 1) {
    //         return IntakeV2.extend();
    //     } else if (position == -1) {
    //         return IntakeV2.retract();
    //     }
    //     return false;
    // }
    
    public static void moveClawAuto(int speed) {
        if (speed == -1) {
            Claw.outputGamePiece();
        } else if (speed == 1) {
            Claw.intakeGamePiece();
        } else {
            Claw.stopishMotor();
        }
    }

    private static enum loadStates{
        Intake,
        LoadPrep,
        Load,
        Loaded,
        Finish,
    }

    private static loadStates loadState = loadStates.Intake;

    public static void loadResetOverride(boolean reset) {
        loadState = loadStates.Intake;
    }
    private static Timer clawTimer = new Timer();

    public static void loadGamepieceCone() {
        Arm.setArm();
        Elevator.setElevator();
        switch(loadState) {
            case Intake:
                Arm.setArm(IO.GridArmPosition.Retract);
                Elevator.setElevator(IO.GridRowPosition.Low);
                IntakeV2.slowIntake();
                SmartDashboard.putBoolean("intake extended", IntakeV2.extendNoPid());
                if(IntakeV2.extendNoPid()) {
                    if(IntakeV2.intake()) {
                        loadState = loadStates.LoadPrep;
                    }
                }
                break;
            case LoadPrep:
                IntakeV2.extendNoPid();
                IntakeV2.slowIntake();
                // if(runExtention()) {
                    loadState = loadStates.Load;
                // }
                break;
            case Load:
                Claw.intakeGamePiece();
                if(IntakeV2.handOffNoPid()) {
                    loadState = loadStates.Loaded;
                    clawTimer.reset();
                    clawTimer.start();
                }
                break;
            case Loaded:
                IntakeV2.handOffNoPid();
                if (!loadSequenceTimer.hasElapsed(Constants.GamepieceManager.intakeRunTime) ){
                    Claw.intakeGamePiece();
                }else{
                    Claw.stopishMotor();
                }
                loadState = loadStates.Finish;
                break;
            case Finish:
                IntakeV2.handOffNoPid();
                if (!loadSequenceTimer.hasElapsed(Constants.GamepieceManager.intakeRunTime) ){
                    Claw.intakeGamePiece();
                }else{
                    Claw.stopishMotor();
                }
                break;
        }
    }
    public static void loadGamepieceCube() {
        Claw.intakeGamePiece();
        IntakeV2.retractNoPid();
        extention(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
    }


    private static Timer loadSequenceTimer = new Timer();

    //DO NOT USE UNTIL INTAKE IS MOUNTED
    //Assumes runExtension is constantly called
    public static void loadSequence(){
        if(IO.runIntakeIn()){
            Claw.intakeGamePiece();
        }else if(IO.clawDropPiece() && Arm.atConePrepPosition() && IO.gridArmPosition == IO.GridArmPosition.ConePrep){
        //   if (GamepieceManager.extention(IO.gridRowPosition, IO.GridArmPosition.ConeReady)){
        //     Claw.outputGamePiece();
        //   }
          if (GamepieceManager.extention(IO.gridRowPosition, IO.GridArmPosition.ConeAlmostReady)){
            Claw.outputGamePiece();
          }
        }else if(IO.clawDropPiece()){
            Claw.outputGamePiece();
        }
        else if(IO.intakeSequenceCone()){
            loadGamepieceCone();
            loadSequenceTimer.start();
            loadSequenceTimer.reset();
        }
        else if(IO.intakeSequenceCube()){
            loadGamepieceCube();
            loadSequenceTimer.start();
            loadSequenceTimer.reset();
        }
        else{
            loadSequenceTimer.start();
            if (!loadSequenceTimer.hasElapsed(Constants.GamepieceManager.intakeRunTime) ){
                Claw.intakeGamePiece();
            }else{
                Claw.stopishMotor();
            }
            IntakeV2.handOffNoPid();
            loadState = loadStates.Intake;
        }
    }
    private static boolean elevatorInPosition = false;
    private static boolean armInPosition = false;
    public static void setExtention(IO.GridRowPosition gridRowPosition, IO.GridArmPosition armRowPosition){
        Elevator.setElevator(gridRowPosition);
        Arm.setArm(armRowPosition);
    }
    public static boolean runExtention(){
        elevatorInPosition = Elevator.setElevator();
        armInPosition = Arm.setArm();
        // return true;
        return elevatorInPosition && armInPosition;
    }
    public static boolean extention(IO.GridRowPosition gridRowPosition, IO.GridArmPosition armRowPosition){
        // //Elevator then arm for normal, arm then elevator for retract
        // if(gridRowPosition == GridRowPosition.Retract && armRowPosition == GridArmPosition.Retract){
        //     Arm.setArm(armRowPosition);
        //     armInPosition = Arm.setArm();
        //     if (armInPosition){
        //         Elevator.setElevator(gridRowPosition);
        //     }

        // }else{
        //     Elevator.setElevator(gridRowPosition);
        //     elevatorInPosition = Elevator.setElevator();
        //     if (elevatorInPosition){
        //         Arm.setArm(armRowPosition);
        //     }
        // }
        Arm.setArm(armRowPosition);
        Elevator.setElevator(gridRowPosition);
        return runExtention();
        // elevatorInPosition = Elevator.setElevator();
        // armInPosition = Arm.setArm();

        // return elevatorInPosition && armInPosition;

    }
    public static void autoAlign(){
        if (AutoAlign.isInCommunity()){
            autoPlace();
        }else if (AutoAlign.isInLoadingZone()){
            autoGrab();
        }
    }
    public static boolean clawDropped = false;

    public static enum PlaceState {
        Align,
        Place,
        Leave
    }
    public static PlaceState placeState = PlaceState.Align;
    
    public static void autoPlace(){
        switch(placeState){
            case Align:
                Claw.stopishMotor();
                boolean robotInPosition = AutoAlign.moveToGridPositionOdometryTwoStep();
                //If your left right is correct but not your forward back yet, move elevator and arm into position
                if(AutoAlign.gridAlignState == AutoAlign.GridAlignState.InPosition){
                    extention(IO.gridRowPosition, IO.gridArmPosition);
                }
                //If Hybrid than Hybrid, however must do this before entering anyways
                else if(IO.gridArmPosition == IO.GridArmPosition.Hybrid){
                    extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Hybrid);
                //if your a cube position and your left right is almost correct, then move elevator and arm into position and allow 
                }else if(IO.gridArmPosition == IO.GridArmPosition.CubePrep){
                    if(Math.abs(IO.keyInputOdometryPosition.getX() - Drivebase.getPose().getX()) < Constants.AutoAlign.cubePreemptiveExtension){
                        extention(IO.gridRowPosition, IO.GridArmPosition.CubePrep);
                    }else if(Math.abs(IO.keyInputOdometryPosition.getX() - Drivebase.getPose().getX()) < Constants.AutoAlign.cubePreemptiveDrop){
                        placeState = PlaceState.Place;
                    }
                }else if(IO.gridArmPosition == IO.GridArmPosition.ConePrep && 
                        Math.abs(IO.keyInputOdometryPosition.getX() - Drivebase.getPose().getX()) < Constants.AutoAlign.conePreemptiveExtension){
                        extention(IO.gridRowPosition, IO.GridArmPosition.ConePrep);
                }
                //else just move arm into position
                else{
                    extention(IO.GridRowPosition.Retract, IO.GridArmPosition.ConePrep);
                }
                if (robotInPosition){
                    placeState = PlaceState.Place;
                }
                break;
            case Place:
                AutoAlign.moveToGridPositionOdometryTwoStep();
                if(clawDrop()){
                    placeState = PlaceState.Leave;
                }
                break;
            case Leave:
                Claw.outputGamePiece();
                runExtention();
                if(IO.gridArmPosition == IO.GridArmPosition.ConePrep){
                    AutoAlign.alignOdometry(IO.keyInputOdometryPosition, -180);
                }else if(IO.gridArmPosition == IO.GridArmPosition.CubePrep){
                    AutoAlign.alignOdometry(IO.keyInputOdometryPosition.plus(new Translation2d(Constants.isBlue()?Constants.Auto.notHitGridOffset:-Constants.Auto.notHitGridOffset,0)), -180);
                }
                break;

        }
    }

    public static void autoGrab(){
        AutoAlign.moveToSubstationPosition();
        if (IO.keyInputSubstationPosition == Constants.FieldPositions.AutoAlignPositions.blueSingleSubstation || 
            IO.keyInputSubstationPosition == Constants.FieldPositions.AutoAlignPositions.redSingleSubstation){
            extention(IO.GridRowPosition.SingleSubstation, IO.GridArmPosition.SingleSubstation);
        }else{
            extention(IO.GridRowPosition.DoubleSubstation, IO.GridArmPosition.DoubleSubstation);
        }
        Claw.intakeGamePiece();
    }

    public static boolean clawDrop(){
        if (IO.clawDropPiece()){
            //If its cone node then move down, else don't, then output the game piece
            if (extention(IO.gridRowPosition, (IO.gridArmPosition == IO.GridArmPosition.ConePrep)?IO.GridArmPosition.ConeAlmostReady:IO.gridArmPosition)){
                Claw.outputGamePiece();
                return true;
            }else{
                Claw.stopishMotor();
            }
        }else{
            extention(IO.gridRowPosition, IO.gridArmPosition);
            Claw.stopishMotor();
        }
        return false;
    }
    public static boolean wasInCommunityOrLoadingZone = AutoAlign.isInCommunity() || AutoAlign.isInLoadingZone();
    public static void manageExtension(){
        // if(wasInCommunityOrLoadingZone && (!AutoAlign.isInCommunity() || !AutoAlign.isInLoadingZone())){
        //     extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
        // }
        if(IO.elevatorManualUp()){
            extention(IO.gridRowPosition, IO.gridArmPosition);
            // if(Constants.isBlue()? Drivebase.getPose().getX() < Constants.FieldPositions.centerLine : Drivebase.getPose().getX() > Constants.FieldPositions.centerLine){
            // }
            // else{
            //     extention(IO.GridRowPosition.DoubleSubstation, IO.GridArmPosition.DoubleSubstation);
            // }

        }else if (IO.elevatorManualDown()){
            // extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
            extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
        }else if (IO.manualCubeRetract()){
            extention(IO.GridRowPosition.CubeRetract, IO.GridArmPosition.CubeRetract);
        }
        runExtention();
        // wasInCommunityOrLoadingZone = AutoAlign.isInCommunity() || AutoAlign.isInLoadingZone();
    }
    public static void displayInformation(){
        SmartDashboard.putString("PlaceState", placeState.toString());
        SmartDashboard.putString("loadState", loadState.toString());
        SmartDashboard.putNumber("loadSequenceTimer", loadSequenceTimer.get());
        SmartDashboard.putBoolean("advanceIfElapsed", loadSequenceTimer.advanceIfElapsed(Constants.GamepieceManager.intakeRunTime));
        // SmartDashboard.putString("Intake Loading state", loadState.toString());
    }
}