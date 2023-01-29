// package frc.robot.Actions;
// import frc.robot.*;

/*
==================== OBTAIN ACTION ====================

Obtain action will pick up a piece from the ground using the appropriate intake (if applicable)
Steps:
    1 - Identify the closest piece to the front of the robot (Or closest to the appropriate intake if multiple) matching the specified type
    2 - Auto align to the found piece
    3 - Use the correct intake to gain control of the game piece

*/

// public class ObtainAction implements AutoAction {    

//     enum State {
//         Locate,
//         Align,
//         Obtain
//     }

//     State ActionState;

//     double TargetX;
//     double TargetY;

//     public boolean Act(int[] Options) {
//         //Options[0]: Game Piece Type

//         //Depending on the specified game piece
//         switch (ActionState) {

//             //Cone Case
//             case Locate:
//                 //GamePieceInfo[] Visible = Vision.VisibleObjects(Options[0]);
//                 if (/*VisibleObjects.length > 0*/ true) {
//                     int ClosestIndex = 0;
//                     for (int i = 1; i < /*VisibleObjects.length*/ 5; i++) {
//                         if (/*VisibleObjects[i].Distance*/0 < 1/*VisibleObjects[ClosestIndex].Distance*/)
//                             ClosestIndex = i;
//                     }
//                     TargetX = 0/*VisibleObjects[ClosestIndex].FieldX*/;
//                     TargetY = 0/*VisibleObjects[ClosestIndex].FieldY*/;

//                     ActionState = State.Align;
//                 }
//                 break;

//             //Cube Case
//             case Align:
//             switch (Options[0]) {
//                 //Cones
//                 case 0:
//                     if (/*ConeIntake.Align(TargetX, TargetY)*/ true)
//                         ActionState = State.Obtain;
//                     break;

//                 //Cubes
//                 case 1:
//                     if (/*CubeIntake.Align(TargetX, TargetY)*/ true)
//                         ActionState = State.Obtain;
//                     break;

//                 //Any
//                 case 2:
//                     if (/*HybridIntake.Align(TargetX, TargetY)*/ true)
//                         ActionState = State.Obtain;
//                     break;
//             }
//                 break;
            
//             //Any Case
//             case Obtain:
//                 switch (Options[0]) {
//                     //Cones
//                     case 0:
//                         //ConeIntake.In();
//                         if (/*ConeIntakeSensor.HasPiece()*/ true)
//                             return true;
//                         break;

//                     //Cubes
//                     case 1:
//                         //CubeIntake.In();
//                         if (/*CubeIntakeSensor.HasPiece()*/ true)
//                             return true;
//                         break;

//                     //Any
//                     case 2:
//                         //HybridIntake.In();
//                         if (/*HybridIntakeSensor.HasPiece()*/ true)
//                             return true;
//                         break;
//                 }
//                 break;
//         }

//         return false;
//     }
// }