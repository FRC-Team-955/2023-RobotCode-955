package frc.robot;

public class GamepieceManager {
    Elevator elevator;

    public boolean CollectFromIntake() {
        Intake.foldInIntake();
        elevator.setElevator(0);
        Arm.setArm(0, 0);
        Handoff.intakeGamePiece();

        return (Intake.isIntakeFolded() && !Intake.senseObj() /* If Handoff has gamepiece */);
    }
}
