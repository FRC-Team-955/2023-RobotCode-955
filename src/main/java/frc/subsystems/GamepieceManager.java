package frc.subsystems;
import frc.subsystems.dummyclasses.*;

public class GamepieceManager {
    Arm arm = new Arm();
    Elevator elevator = new Elevator();
    
    public boolean setLocation(int position) {
        Elevator.setElevator(position);
        return Arm.setArm(position, 0) && Elevator.elevatorAtSetpoint();
    }

    public void intakeClaw() {
        Intake.runEthanWheels(); 
    }

    public void resetClawTimer() {
        Intake.startIntake();
    }
    
    public void reverseEthanWheels() {
        Intake.reverseEthanWheels();
    }
}
