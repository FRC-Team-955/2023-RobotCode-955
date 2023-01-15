package frc.robot;

import frc.robot.Swerve.SwerveSettings;

public final class IO {
    // Drivebase
    private static Joystick joy0 = new Joystick(0);
    private static Joystick joy1 = new Joystick(1);

    public static boolean isAutoAlignActive() {
        return true;
    }

    public static boolean isAutoBalanceActive(){
        return false;
    }

    public static double getSwerveRotation(){
        double rotAxis = joy0.getRawAxis(4);

        if (Math.abs(rotAxis) < 0.15) {
            return 0.0;
        } else {
            return SwerveSettings.SwerveConstants.maxAngularVelocity * (rotAxis - (Math.signum(rotAxis) * 0.15)) / (1 - 0.15);
        }
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
        double forwardAxis = joy0.getRawAxis(0);
        double strafeAxis = joy0.getRawAxis(1);

        Translation2d tAxes = new Translation2d(forwardAxis, strafeAxis);

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
        return joy0.getRawButton(0);
    }
}

