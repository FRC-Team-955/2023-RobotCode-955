package frc.robot.Actions;

public class StopAction implements AutoAction {
    public boolean Act() {
        gamepieceManager.setLocation(0);
        gamepieceManager.moveHandoff(0);
        gamepieceManager.foldIntake(0);
        gamepieceManager.runEthanWheels(0);
        return false;
    }  
}
