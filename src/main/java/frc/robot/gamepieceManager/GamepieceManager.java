package frc.robot.gamepieceManager;

public class GamepieceManager {
    public static void setLocation(int position) {
        Arm.setArmLocation(position);
        Elevator.setElevatorPosition(position);
    }

    public static boolean moveArmElevator() {
        boolean armAtSetpoint = Arm.setArm();
        return Elevator.setElevator() && armAtSetpoint;
    }

    public static void runEthanWheels(int speed) {
        if (speed == -1) {
            Intake.reverseEthanWheels();
        } else if (speed == 1) {
            Intake.runEthanWheels();
        } else {
            Intake.stopEthanWheels();
        }
    }

    public static boolean foldIntake(int position) {
        if (position == 0) {
            return Intake.foldOutIntake();
        } else if (position == 1) {
            return Intake.foldInIntake();
        }
        return false;
    }
    
    public static void moveHandoff(int speed) {
        if (speed == -1) {
            Handoff.outputGamePiece();
        } else if (speed == 1) {
            Handoff.intakeGamePiece();
        } else {
            Handoff.stopMotor();
        }
    }
    
    public static void loadHandoff() {
        if ((Intake.senseObj() || IO.loadHandoffButton()) && !foldIntake(1)) {
            moveHandoff(1);
        } else {
            moveHandoff(0);
        }
    }
}