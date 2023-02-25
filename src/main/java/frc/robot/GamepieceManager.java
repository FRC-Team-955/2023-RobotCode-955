package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.IO.GridArmPosition;
import frc.robot.IO.GridRowPosition;
import frc.robot.Subsystems.Arm;
import frc.robot.Subsystems.Claw;
import frc.robot.Subsystems.Elevator;
import frc.robot.Subsystems.IntakeV2;

public class GamepieceManager {

    public static boolean runFlapsAuto(int speed) {
        return IntakeV2.moveIntake(speed * 4);
    }

    public static boolean foldIntakeAuto(int position) {
        if (position == 0) {
            return IntakeV2.handOff();
        } else if (position == 1) {
            return IntakeV2.extend();
        } else if (position == -1) {
            return IntakeV2.retract();
        }
        return false;
    }
    
    public static void moveClawAuto(int speed) {
        if (speed == -1) {
            Claw.outputGamePiece();
        } else if (speed == 1) {
            Claw.intakeGamePiece();
        } else {
            Claw.stopishMotor();
        }
    }

    private static boolean runClaw = false;
    private static long startTime = System.currentTimeMillis();
    private static Timer loadSequenceTimer = new Timer();

    //DO NOT USE UNTIL INTAKE IS MOUNTED
    //Assumes runExtension is constantly called
    public static void loadSequence(){
        if(IO.intakeSequence()){
            IntakeV2.extend();
            IntakeV2.intake();
            setExtention(GridRowPosition.Retract, GridArmPosition.Retract);
            loadSequenceTimer.reset();
            startTime = System.currentTimeMillis();
            runClaw = true;
        }
        else{
            loadSequenceTimer.start();
            if(runClaw && runExtention() && IntakeV2.handOff()) {
                Claw.intakeGamePiece();
            } else {
                Claw.stopishMotor();
            }
            if (loadSequenceTimer.hasElapsed(Constants.Claw.runTime)){
                runClaw = false;
                IntakeV2.extend();
                IntakeV2.reverseIntake();
            } else {
                IntakeV2.slowIntake();
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
    public static void autoGrab(){
        robotInPosition = AutoAlign.moveToSubstationPosition();
        extentionInPosition = extention(IO.GridRowPosition.DoubleSubstation, IO.GridArmPosition.DoubleSubstation);
        Claw.intakeGamePiece();
    }
    public static void autoAlign(){
        if (AutoAlign.isInCommunity()){
            autoPlace();
        }else if (AutoAlign.isInLoadingZone()){
            autoGrab();
        }
    }

    public static void clawDrop(){
        if (IO.clawDropPiece()){
            Claw.outputGamePiece();
        }else{
            Claw.stopishMotor();
        }
    }
    private static boolean robotInPosition = false;
    private static boolean extentionInPosition = false;
    public static void autoPlace(){
        // robotInPosition = AutoAlign.moveToGridPosition();
        robotInPosition = AutoAlign.moveToGridPositionOdometryTwoStep();
        extentionInPosition = AutoAlign.isInCommunity() && extention(IO.gridRowPosition, IO.gridArmPosition);
        if (robotInPosition && extentionInPosition){
            IO.rumbleJoy1();
            clawDrop();
        }else{
            Claw.stopishMotor();
            //uncomment when intake is mounted
            //Intake.unholdItem();
        }
    }
    public static boolean wasInCommunityOrLoadingZone = AutoAlign.isInCommunity() || AutoAlign.isInLoadingZone();
    public static void manageExtension(){
        if(wasInCommunityOrLoadingZone && (!AutoAlign.isInCommunity() || !AutoAlign.isInLoadingZone())){
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
        wasInCommunityOrLoadingZone = AutoAlign.isInCommunity() || AutoAlign.isInLoadingZone();
    }
}