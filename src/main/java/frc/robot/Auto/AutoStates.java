// package frc.robot.Auto;

// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import frc.robot.Actions.AutoAction;
// import frc.robot.Actions.StopAction;
// import frc.robot.GamepieceManager;

// public class AutoStates {
//     AutoAction[] autoActionList = new AutoAction[]{new StopAction()};

//     public AutoStates() {
//         AutoProfileNew.autoVersion("Auto1");
//         autoActionList = AutoProfileNew.AutoActions.get(0);
//     }

//     public void autoPeriodic() {
//         boolean stepFinished = true;
//         for (int i = 0; i < autoActionList.length; i++) {
//             if (!autoActionList[i].Act()) {
//                 stepFinished = false;
//             }
//         }
//         if (GamepieceManager.runExtention() && stepFinished) {
//             AutoProfileNew.AutoActions.remove(0);
//             autoActionList = AutoProfileNew.AutoActions.get(0);
//         }
//     }

//     public void displayData() {
//         SmartDashboard.putNumber("Step", AutoProfileNew.getOriginalList().size() - AutoProfileNew.AutoActions.size() + 1);
//     }
// }