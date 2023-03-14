package frc.robot.Auto.Actions.Deprecated;
// package frc.robot.Auto.Actions;

// import edu.wpi.first.wpilibj.Timer;
// import frc.robot.GamepieceManager;
// import frc.robot.IO.GridArmPosition;
// import frc.robot.IO.GridRowPosition;

// //Use only when intake is mounted, otherwise use other actions
// public class PickupAction extends AutoAction {
//     double timeoutTime = 10;
//     enum States {
//         SETUP,
//         INTAKE,
//         HANDOFF,
//         RESET
//     }
//     States state;
//     Timer timer;

//     public PickupAction() {
//         state = States.SETUP;
//         timer = new Timer();
//     }

//     public boolean Act() {
//         switch(state) {
//             case SETUP:
//                 timer.start();
//                 boolean intakeFoldedDown = GamepieceManager.foldIntakeAuto(0);
//                 GamepieceManager.setExtention(GridRowPosition.Retract, GridArmPosition.Retract);
//                 if ((GamepieceManager.runExtention() && intakeFoldedDown) || timer.hasElapsed(timeoutTime)) {
//                     state = States.INTAKE;
//                     timer.reset();
//                 }
//                 break;
//             case INTAKE:
//                 timer.start();
//                 if (GamepieceManager.runFlapsAuto(1) || timer.hasElapsed(timeoutTime)) {
//                     state = States.HANDOFF;
//                     timer.reset();
//                 }
//                 break;
//             case HANDOFF:
//                 timer.start();
//                 GamepieceManager.moveClawAuto(1);
//                 if(GamepieceManager.foldIntakeAuto(1) || timer.hasElapsed(timeoutTime)) {
//                     state = States.RESET;
//                     GamepieceManager.moveClawAuto(0);
//                     timer.reset();
//                 }
//                 break;
//             case RESET:
//                 return GamepieceManager.foldIntakeAuto(0);

//         }
//         return false;
//     }
    
//     public void Finish() {
        
//     }    
// }
