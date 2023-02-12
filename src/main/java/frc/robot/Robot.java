package frc.robot;
import frc.robot.Subsystems.Arm;
import frc.robot.Subsystems.Claw;
import frc.robot.Subsystems.Elevator;
import frc.robot.Subsystems.Intake;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.IO.GridArmPosition;
import frc.robot.IO.GridRowPosition;
import frc.robot.Sensors.ColorSensor;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends TimedRobot {
  // Robot States
  public enum RobotState {
    DRIVING,
    AUTO_ALIGN,
    AUTO_BALANCE
  }
  // Objects
  static Arm arm = new Arm();
  static ColorSensor colorSensor = new ColorSensor();
  static RobotState robotState = RobotState.DRIVING;
  static Elevator elevator = new Elevator(); 
  static Intake intake = new Intake(); 
  static Claw claw = new Claw(); 
  Joystick joy0 = new Joystick(0);
  AutoAlign autoAlign = new AutoAlign();
  int elevatorstate = 0;  
  int armstate = 0; 
  double joystick_position; 
  @Override
  public void robotInit() {}

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
  Intake.setup(); 
  Arm.setup();
  Elevator.setup();
  Claw.setup();
  }

  public void teleopAllState(){
    Drivebase.logData();
    IO.keyInputOdometryMapping();
    IO.keyInputRowPosition();
  }

  @Override
  public void teleopPeriodic() {

//Arm Test
 if(joy0.getRawButton(4) == true) //Y button on Xbox controller
 {
  armstate ++; 
  switch(armstate)
{
 case 0: 
 Arm.setArm(GridArmPosition.Retract); 
 case 1: 
 Arm.setArm(GridArmPosition.ConePrep); 
 case 2: 
 Arm.setArm(GridArmPosition.ConeReady);
 case 3: 
 Arm.setArm(GridArmPosition.CubePrep); 
 case 4: 
 Arm.setArm(GridArmPosition.CubeReady);
 case 5:
 armstate = 0; 
} 
}

//Elevator Test
if(joy0.getRawButton(3) == true) //X button on Xbox controller
{
  elevatorstate ++; 
  switch(elevatorstate) {
 case 0: 
 Elevator.setElevator(GridRowPosition.Retract);  
 case 1:
 Elevator.setElevator(GridRowPosition.Low); 
 case 2: 
 Elevator.setElevator(GridRowPosition.Mid); 
 case 3: 
 Elevator.setElevator(GridRowPosition.High); 
 case 4: 
 elevatorstate = 0; 

  }
} 

//Intake Test
if(joy0.getRawAxis(3) == 1) //Right trigger on Xbox controller
{ 
Intake.runEthanWheels();
}

if(joy0.getRawAxis(2) == 1) //Left trigger on Xbox controller
{
Intake.reverseEthanWheels(); 
}

if(joy0.getRawButton(8) == true) //Start arrow button on Xbox controller
{
Intake.foldOutIntake();
}

if(joy0.getRawButton(7) == true) //Back arrow button on Xbox controller
{
Intake.foldInIntake();
}

//Claw Test 
if(joy0.getRawButton(6) == true) //Right bumper on Xbox controller
{
Claw.intakeGamePiece();
}

if(joy0.getRawButton(5) == true) //Left bumper on Xbox controller
{
Claw.outputGamePiece();
}

  }
    // selectTeleopState();
    // teleopAllState();
    // switch(robotState){
    //   case AUTO_ALIGN:
    //   //move the auto align into game piece along with the arm code
    //     if (autoAlign.moveToGridPosition()){
    //       //run drop function
    //     }
    //   case AUTO_BALANCE:
    //     Drivebase.autoBalance();
    //   default: // DRIVE
    //     AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
    //     Drivebase.driveFieldRelative();
    // }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}

  public void selectTeleopState(){

    if (IO.Drivebase.isAutoAlignActive()) {
      robotState = RobotState.AUTO_ALIGN;
    } 
    //once auto balance is added into false
    else if (IO.Drivebase.isAutoBalanceActive()){
      robotState = RobotState.AUTO_BALANCE;
    } else {
      robotState = RobotState.DRIVING;
    }
  }
}
