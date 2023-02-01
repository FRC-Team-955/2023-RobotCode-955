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

    public boolean foldIntake(int position) {
        if (position == 0) {
            return Intake.foldOutIntake();
        } else if (position == 1) {
            return Intake.foldInIntake();
        }
        return false;
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
        if ((Intake.senseObj() || IO.loadHandoffButton()) && !foldIntake(1)) {
            moveHandoff(1);
        } else {
            moveHandoff(0);
        }
    }
}