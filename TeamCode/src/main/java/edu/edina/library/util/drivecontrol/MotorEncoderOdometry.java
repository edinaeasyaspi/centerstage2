package edu.edina.library.util.drivecontrol;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class MotorEncoderOdometry {
    private static final double POS_TO_DEG = 1114.0 / 360.0;
    private static final double DEG_TO_IN = 37.5;
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
        return pos / POS_TO_DEG / DEG_TO_IN;
    }

    public double estRadius(double yawRadians) {
        return motorInches() / yawRadians + offset;
    }
}
