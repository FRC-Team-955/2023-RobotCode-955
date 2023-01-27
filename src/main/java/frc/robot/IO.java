package frc.robot;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import frc.robot.AutoAlign.GridAlignState;
import frc.robot.Swerve.SwerveSettings;

public final class IO {
    private static Joystick joy0 = new Joystick(Constants.IO.joy0.joy0Id);
    private static Joystick joy1 = new Joystick(Constants.IO.joy1.joy1Id);
    private static SlewRateLimiter forwardAxisSlewRateLimiter = new SlewRateLimiter(Constants.forwardRateLimiter);
    private static SlewRateLimiter strafeAxisSlewRateLimiter = new SlewRateLimiter(Constants.strafeRateLimiter);
    private static Joystick key0 = new Joystick(2);
    private static Joystick key1 = new Joystick(3);

    private static Joystick joystick  = new Joystick(0);
    
    private static boolean toggleOn = false;
    private static boolean togglePressed = false;

    public void teleopPeriodic(){
        updateToggle();

        //if button pressed then do this
        if (toggleOn){
            System.out.println("Slow");
        }

        //If pressed again do this
        else{
            System.out.println("Fast");
        }
    }

    public void updateToggle(){
        if(joystick.getRawButton(1)){
            if(!togglePressed){
                toggleOn = !toggleOn;
                togglePressed = true;
            }
            else{
                togglePressed = false;
            }
        }
    }
    public static class Drivebase{
        public static boolean isAutoAlignActive() {
            return true;
        }

        public static boolean isAutoBalanceActive(){
            return false;
        }

        public static double getSwerveRotation(){
            //What is axis number reffering too?
            double rotAxis = joy0.getRawAxis(Constants.IO.joy0.rotAxis);
            double deadband = 0.15;

            if (Math.abs(rotAxis) < Math.abs(deadband)) return 0.0;
            
            return deadband * Math.signum(rotAxis) + ((rotAxis - deadband) / (1.0 - deadband));

            // if (Math.abs(rotAxis) < 0.15) {
            //     return 0.0;
            // } else {
            //     return SwerveSettings.SwerveConstants.maxAngularVelocity * (rotAxis - (Math.signum(rotAxis) * 0.15)) / (1 - 0.15);
            // }
        }

        private static double norm(Translation2d tAxes) {
            return Math.hypot(tAxes.getX(), tAxes.getY());
        }

        private static Translation2d scale(double s, Translation2d tAxes) {
            return new Translation2d(tAxes.getX() * s, tAxes.getY() * s);
        }

        private static Translation2d fromPolar(Rotation2d direction, double magnitude){
            return new Translation2d(direction.getCos() * magnitude, direction.getSin() * magnitude);
        }

        public static Translation2d getSwerveTranslation(){
            double forwardRawAxis = joy0.getRawAxis(Constants.IO.joy0.forwardRawAxis);
            double strafeRawAxis = joy0.getRawAxis(Constants.IO.joy0.strafeRawAxis);
            double forwardAxis = forwardAxisSlewRateLimiter.calculate(forwardRawAxis);
            double strafeAxis = strafeAxisSlewRateLimiter.calculate(strafeRawAxis);

        Translation2d tAxes = new Translation2d(thrustEnabled() ? forwardAxis : forwardAxis*0.7, thrustEnabled() ? strafeAxis : strafeAxis*0.7);

            if (Math.abs(norm(tAxes)) < 0.15) {
                return new Translation2d();
            } else {
                Rotation2d deadband_direction = new Rotation2d(tAxes.getX(), tAxes.getY());
                Translation2d deadband_vector = fromPolar(deadband_direction, 0.15);

                double scaled_x = tAxes.getX() - (deadband_vector.getX()) / (1 - deadband_vector.getX());
                double scaled_y = tAxes.getY() - (deadband_vector.getY()) / (1 - deadband_vector.getY());
                return scale(SwerveSettings.SwerveConstants.maxSpeed, new Translation2d(scaled_x, scaled_y));
            }
        }

        public static boolean rotationOverrideEnabled(){
            return joy0.getRawButton(Constants.IO.joy0.rotationOverrideButton);
        }
        public static boolean thrustEnabled(){
            return joy0.getRawAxis(Constants.IO.joy0.thrustAxis) > 0.2;
        }
    }
    public static void rumbleJoy0(){
        joy0.setRumble(RumbleType.kBothRumble, 0.5);
    }

    public static Translation2d keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue0: Constants.FieldPositions.AutoAlignPositions.red0;
    public static boolean isConeNodePosition = true;

    public static void keyInputOdometryMapping() {
        if (key0.getRawButton(0)|| key0.getRawButton(1)|| key0.getRawButton(2)){
            keyInputOdometryPosition = Constants.FieldPositions.AutoAlignPositions.blue0;
            isConeNodePosition = true;
            AutoAlign.gridAlignState = GridAlignState.AlignedToOdometry;
        }

    }
}

