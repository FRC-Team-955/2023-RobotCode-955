package frc.robot;

import frc.robot.Subsystems.Arm;
import frc.robot.Subsystems.Claw;
import frc.robot.Subsystems.Elevator;
import frc.robot.Subsystems.Intake;

public class GamepieceManager {

    // public static boolean runFlapsAuto(int speed) {
    //     if (speed == -1) {
    //         Intake.reverseFlaps();
    //     } else if (speed == 1) {
    //         Intake.runFlaps();
    //     } else {
    //         Intake.stopFlaps();
    //     }
    //     return Intake.senseObj();
    // }

    // public static boolean foldIntakeAuto(int position) {
    //     if (position == 0) {
    //         return Intake.foldOutIntake();
    //     } else if (position == 1) {
    //         return Intake.foldInIntake();
    //     }
    //     return false;
    // }
    
    // public static void moveClawAuto(int speed) {
    //     if (speed == -1) {
    //         Claw.outputGamePiece();
    //     } else if (speed == 1) {
    //         Claw.intakeGamePiece();
    //     } else {
    //         Claw.stopishMotor();
    //     }
    // }

    private static boolean runClaw = false;
    private static long startTime = System.currentTimeMillis();

    //DO NOT USE UNTIL INTAKE IS MOUNTED
    public static void loadSequence(){
        if(IO.intakeSequence()){
            Intake.foldOutIntake();
            Intake.runFlaps();
            startTime = System.currentTimeMillis();
            runClaw = true;
        }
        else{
            Intake.foldInIntake();
            if(Intake.senseObj()){
                Intake.holdItemUntilFolded();
            }
            if (runClaw){
                Claw.intakeGamePiece();
            } else{
                Claw.stopishMotor();
            }
            if (System.currentTimeMillis() - (startTime + Constants.Claw.runTime) > 0){
                runClaw = false;
            }
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
        
        return elevatorInPosition && armInPosition;
    }
    public static boolean extention(IO.GridRowPosition gridRowPosition, IO.GridArmPosition armRowPosition){
        Elevator.setElevator(gridRowPosition);
        elevatorInPosition = Elevator.setElevator();
        if (elevatorInPosition){
            Arm.setArm(armRowPosition);
        }
        return runExtention();
        // elevatorInPosition = Elevator.setElevator();
        // armInPosition = Arm.setArm();

        // return elevatorInPosition && armInPosition;

    }
    public static void autoGrab(){
        robotInPosition = AutoAlign.moveToSubstationPosition();
        extentionInPosition = extention(IO.GridRowPosition.DoubleSubstation, IO.GridArmPosition.DoubleSubstation);
        Claw.intakeGamePiece();
    }
    public static void autoAlign(){
        if (AutoAlign.isInCommunity()){
            autoPlace();
        }else if (AutoAlign.isInLoadingZong()){
            autoGrab();
        }
    }

    public static void clawDrop(){
        if (IO.clawDropPiece()){
            Claw.outputGamePiece();
        }else{
            Claw.intakeGamePiece();
        }
    }
    private static boolean robotInPosition = false;
    private static boolean extentionInPosition = false;
    public static void autoPlace(){
        robotInPosition = AutoAlign.moveToGridPosition();
        extentionInPosition = AutoAlign.isInCommunity() && extention(IO.gridRowPosition, IO.gridArmPosition);
        if (robotInPosition && extentionInPosition){
            IO.rumbleJoy1();
            clawDrop();
        }else{
            Claw.intakeGamePiece();
            //uncomment when intake is mounted
            //Intake.unholdItem();
        }
    }
    public static boolean wasInCommunityOrLoadingZone = AutoAlign.isInCommunity() || AutoAlign.isInLoadingZong();
    public static void manageExtension(){
        if(wasInCommunityOrLoadingZone && (!AutoAlign.isInCommunity() || !AutoAlign.isInLoadingZong())){
            extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
        }
        else if(IO.elevatorManualUp()){
            extention(IO.gridRowPosition, IO.gridArmPosition);
        }
        else if (IO.elevatorManualDown()){
            extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
        }else{
            runExtention();
        }
        wasInCommunityOrLoadingZone = AutoAlign.isInCommunity() || AutoAlign.isInLoadingZong();
    }
}