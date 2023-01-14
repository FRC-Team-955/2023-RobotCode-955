package frc.subsystems;
import frc.subsystems.dummyclasses.*;

public class GamepieceController {
    Arm arm = new Arm();
    Elevator elevator = new Elevator();
    
    public void setLocation(int position) {
        arm.setArm(position);
        elevator.setElevator(position);
    }
    public boolean elevatorAtSetpoint() {
        return elevator.elevatorAtSetpoint() && arm.armAtSetpoint();
    }//not currently implemented

    public void reverseClaw() {
        Claw.outputGamePiece();
    }
    public void stopClaw() {
        Claw.stopIntake();
    }
    public void moveClaw() {
        Claw.intakeGamePiece();
    }
}
