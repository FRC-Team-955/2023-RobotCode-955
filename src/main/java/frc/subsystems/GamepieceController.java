package frc.subsystems;
import frc.subsystems.dummyclasses.*;

public class GamepieceController {
    Arm arm = new Arm();
    Elevator elevator = new Elevator();
    
    public void setLocation(int position) {
        Arm.setArm(position);
        Elevator.setElevator(position);
    }
    public boolean elevatorAtSetpoint() {
        return Elevator.elevatorAtSetpoint() && Arm.armAtSetpoint();
    }//not currently implemented

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
