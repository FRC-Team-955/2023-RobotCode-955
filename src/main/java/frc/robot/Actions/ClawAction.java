package frc.robot.Actions;

public class ClawAction implements AutoAction {
    public boolean Act(int[] Options) {
        if (Options[0] == -1) {
            gamepieceController.reverseClaw();
        } else if (Options[0] == 0) {
            gamepieceController.stopClaw();
        } else if (Options[0] == 1) {
            gamepieceController.moveClaw();
        }
        return gamepieceController.clawAtSetpoint();
    }
    
}
