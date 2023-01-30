package frc.robot;

public class GamepieceManager {
    Arm arm = new Arm();
    Elevator elevator = new Elevator();
    
    public boolean setLocation(int position) {
        boolean atSetpoint = Elevator.setElevator(position);
        return Arm.setArm(position, 0) && atSetpoint;
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
        if (position == -1) {
            Intake.foldInIntake();
        } else if (position == 1) {
            Intake.foldOutIntake();
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
}