package edu.edina.library.util.drivecontrol;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class MotorEncoderOdometry {
    private static final double POS_TO_INCH = (1114.0 / 360.0) / 37.5;
    private final DcMotorEx motor;
    private final double mult;
    private final double offset;
    private final int startPos;

    public MotorEncoderOdometry(DcMotorEx motor, double mult, double offset) {
        this.motor = motor;
        this.mult = mult;
        this.offset = offset;
        startPos = motor.getCurrentPosition();
    }

    public double motorInches() {
        double pos = startPos - motor.getCurrentPosition() * mult;
        double distance = pos / POS_TO_INCH;
        return distance;
    }

    public double estRadius(double yawRadians) {
        return motorInches() / yawRadians + offset;
    }
}
