package frc.robot;

public class GamepieceManager {
    public void setLocation(int position) {
        Arm.setArmLocation(position);
        Elevator.setElevatorPosition(position);
    }

    public boolean moveArmElevator() {
        boolean armAtSetpoint = Arm.setArm();
        return Elevator.setElevator() && armAtSetpoint;
    }

    public void runEthanWheels(int speed) {
        if (speed == -1) {
            Intake.reverseEthanWheels();
        } else if (speed == 1) {
            Intake.runEthanWheels();
        } else {
            Intake.stopEthanWheels();
        }
    }

    public void foldIntake(int position) {
        if (position == 0) {
            Intake.foldOutIntake();
        } else if (position == 1) {
            Intake.foldInIntake();
        }
    }
    
    public void moveHandoff(int speed) {
        if (speed == -1) {
            Handoff.outputGamePiece();
        } else if (speed == 1) {
            Handoff.intakeGamePiece();
        } else {
            Handoff.stopMotor();
        }
    }
    
    public void loadHandoff() {
        if (Intake.senseObj() || IO.loadHandoffButton()) {
            moveHandoff(1);
            foldIntake(1);
        } else {
            moveHandoff(0);
        }
    }

    private boolean runHandOff = false;
    private long startTime = System.currentTimeMillis();

    public void loadHandoffv2(){
        if(IO.deployRunIntake()){
            Intake.foldOutIntake();
            Intake.runEthanWheels();
            startTime = System.currentTimeMillis();
            runHandOff = true;
        }
        else{
            Intake.foldInIntake();
            if (runHandOff){
                Handoff.intakeGamePiece();
            }
            if (System.currentTimeMillis() - (startTime + Constants.Handoff.runTime) < 0){
                runHandOff = false;
            }
        }
    }
    IO.GridRowPosition extentionState = IO.GridRowPosition.Retract;
    IO.GridArmPosition armState = IO.GridArmPosition.Retract;
    public void extention(){
        if (IO.manualDown()){
            extentionState = IO.GridRowPosition.Retract;
        }else if(IO.manualUp()){
            extentionState = IO.gridRowPosition;
        }
        Elevator.setElevator(extentionState);
    }


}