package frc.robot;

import edu.wpi.first.wpilibj.Timer;
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

    public static void loadGamepiece() {
        Arm.setArm();
        Elevator.setElevator();
        switch(loadState) {
            case Intake:
                if(IntakeV2.extendNoPid()) {
                    Arm.setArm(IO.GridArmPosition.Retract);
                    Elevator.setElevator(IO.GridRowPosition.Retract);
                    if(IntakeV2.intake()) {
                        loadState = loadStates.LoadPrep;
                    }
                }
                break;
            case LoadPrep:
                if(Arm.setArm() && Elevator.setElevator()) {
                    loadState = loadStates.Load;
                }
                break;
            case Load:
                Claw.intakeGamePiece();
                if(IntakeV2.handOffNoPid()) {
                    loadState = loadStates.Loaded;
                }
                break;
            case Loaded:
                Claw.stopishMotor();
                // IntakeV2.extendNoPid();
                loadState = loadStates.Finish;
                break;
            case Finish:
                break;
        }
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
        else if(IO.intakeSequence()){
            loadGamepiece();
        }
        else{
            // loadSequenceTimer.start();
            // if (!loadSequenceTimer.hasElapsed(Constants.GamepieceManager.intakeRunTime) ){
            //     // IntakeV2.slowIntake();
            //     Claw.intakeGamePiece();
            //     if(runExtention()){
            //         IntakeV2.retractNoPid();
            //     }
            // } else {
            Claw.stopishMotor();
            loadState = loadStates.Intake;

            // }
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
                if(IO.gridArmPosition == IO.GridArmPosition.Hybrid){
                    extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Hybrid);
                }else{
                    extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
                }
                Claw.stopishMotor();
                boolean robotInPosition = AutoAlign.moveToGridPositionOdometryTwoStep();

                if(AutoAlign.gridAlignState == AutoAlign.GridAlignState.InPosition){
                    extention(IO.gridRowPosition, IO.gridArmPosition);
                }
                
                if (robotInPosition){
                    placeState = PlaceState.Place;
                }
                break;
            case Place:
                AutoAlign.moveToGridPositionOdometryTwoStep();
                clawDrop();
                break;
            case Leave:
                Claw.outputGamePiece();
                runExtention();
                if (IO.isConeNodePosition && (IO.gridArmPosition != IO.GridArmPosition.Hybrid)){
                    AutoAlign.alignOdometry(IO.keyInputOdometryPosition, -180);
                }
                break;

        }
        // boolean robotInPosition  = AutoAlign.moveToGridPositionOdometryTwoStep();
        // if (robotInPosition){
        // // if (false){
        //     clawDrop();
        // }
        // else{
        //     extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
        //     Claw.stopishMotor();
        //     //uncomment when intake is mounted
        //     //Intake.unholdItem();
        // }
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

    public static void clawDrop(){
        if (IO.clawDropPiece()){
            // if (extention(IO.gridRowPosition, (IO.isConeNodePosition && (IO.gridArmPosition != IO.GridArmPosition.Hybrid))?IO.GridArmPosition.ConeReady:IO.gridArmPosition)){
            if (extention(IO.gridRowPosition, (IO.isConeNodePosition && (IO.gridArmPosition != IO.GridArmPosition.Hybrid))?IO.GridArmPosition.ConeAlmostReady:IO.gridArmPosition)){
                Claw.outputGamePiece();
                placeState = PlaceState.Leave;
            }else{
                Claw.stopishMotor();
            }
        }else{
            extention(IO.gridRowPosition, IO.gridArmPosition);
            Claw.stopishMotor();
        }
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
            // System.out.println("hello");

            extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
        }else if (IO.elevatorManualRetract()){
            extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);

        }
            // Arm.setpoint = Arm.setpoint + IO.elevatorFineControl()*2;
            // System.out.println("runetentions");
        runExtention();
        wasInCommunityOrLoadingZone = AutoAlign.isInCommunity() || AutoAlign.isInLoadingZone();
    }
    public static void displayInformation(){
        SmartDashboard.putString("PlaceState", placeState.toString());
        SmartDashboard.putString("state", loadState.toString());
    }
}