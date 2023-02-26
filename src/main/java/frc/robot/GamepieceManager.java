package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
        System.out.println("extention function");
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

    public static void autoPlace(){
        boolean robotInPosition = AutoAlign.moveToGridPositionOdometryTwoStep();
        if (robotInPosition){
        // if (false){
            clawDrop();
        }else{
            extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
            Claw.stopishMotor();
            //uncomment when intake is mounted
            //Intake.unholdItem();
        }
    }

    public static void autoGrab(){
        AutoAlign.moveToSubstationPosition();
        extention(IO.GridRowPosition.DoubleSubstation, IO.GridArmPosition.DoubleSubstation);
        Claw.intakeGamePiece();
    }

    public static void clawDrop(){
        if (IO.clawDropPiece()){
            if (extention(IO.gridRowPosition, IO.isConeNodePosition?IO.GridArmPosition.ConeReady:IO.gridArmPosition)){
                Claw.outputGamePiece();
            }else{
                Claw.stopishMotor();
            }
                // Arm.setpoint = Arm.setpoint + IO.elevatorFineControl()*2;
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
            if(Constants.isBlue()? Drivebase.getPose().getX() < Constants.FieldPositions.centerLine : Drivebase.getPose().getX() > Constants.FieldPositions.centerLine){
                extention(IO.gridRowPosition, IO.gridArmPosition);
            }
            else{
                extention(IO.GridRowPosition.DoubleSubstation, IO.GridArmPosition.DoubleSubstation);
            }

        }else if (IO.elevatorManualDown()){
            // extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
            // System.out.println("hello");

            extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
        }
            // Arm.setpoint = Arm.setpoint + IO.elevatorFineControl()*2;
            // System.out.println("runetentions");
        runExtention();
        wasInCommunityOrLoadingZone = AutoAlign.isInCommunity() || AutoAlign.isInLoadingZone();
    }
    public static void displayInformation(){
        SmartDashboard.putBoolean("clawDropPiece()", IO.clawDropPiece());
    }
}