package frc.robot.Actions;

public class StopAction implements AutoAction {
    public boolean Act() {
        gamepieceController.setLocation(0);
        gamepieceController.stopClaw();
        return false;
    }  
}
