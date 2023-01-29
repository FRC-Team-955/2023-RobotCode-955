package frc.robot;

public class GamePieceController {    
    public boolean setLocation(int position) {
        Arm.setArm(position, 0);
        return Elevator.setElevator(position) && Arm.setArm(position, 0);
    }

    public void intakeClaw() {
        Intake.intakeGamePiece(); 
    }

    public void resetClawTimer() {
        Intake.startIntake();
    }
    
    public void reverseEthanWheels() {
        Intake.reverseEthanWheels();
    }
}
