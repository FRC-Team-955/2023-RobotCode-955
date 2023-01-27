package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;

public class Elevator {
    CANSparkMax motor;
    PIDController pid;
    ElevatorFeedforward feedforward;
    Encoder encoder;

    public Elevator(CANSparkMax motor, Encoder encoder) {
        pid = new PIDController(Constants.Elevator.kPElevator,
                                Constants.Elevator.kIElevator,
                                Constants.Elevator.kDElevator);
        feedforward = new ElevatorFeedforward(Constants.Elevator.kSElevator,
                                            Constants.Elevator.kGElevator,
                                            Constants.Elevator.kVElevator);
        pid.setTolerance(Constants.Elevator.kElevatorTolerance);
        this.motor = motor;
        this.encoder = encoder;
    }

    public void moveElevator(double joyPos) {
        if((encoder.get() <= Constants.Elevator.kElevatorUpperLimit || joyPos < 0)
            && (encoder.get() >= Constants.Elevator.kElevatorLowerLimit || joyPos > 0)) { // if elevator hit the top or bottom
            motor.set(joyPos);
        } else {
            motor.set(0);
        }
    }

    public boolean setElevator(int level) { // level = desired elevator level
            double elevatorSetpoint = Constants.Elevator.kRetracted;
            switch(level) {
                case 0:
                    elevatorSetpoint = Constants.Elevator.kRetracted;
                    break;
                case 1:
                    elevatorSetpoint = Constants.Elevator.kBottomLevel;
                    break;
                case 2:
                    elevatorSetpoint = Constants.Elevator.kMediumLevel;
                    break;
                case 3:
                    elevatorSetpoint = Constants.Elevator.kTopLevel;
                    break;
            }

            double amount = MathUtil.clamp(pid.calculate(encoder.get(), elevatorSetpoint) +
                                            feedforward.calculate(encoder.getRate()), -1, 1);
            
            motor.set(amount);

            return pid.atSetpoint();
        }
}