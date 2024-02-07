package edu.edina.opmodes.teleop.test;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import edu.edina.library.util.drivecontrol.PiDrive;

public class ServoThrottle {
    private final Servo servo;
    private final ElapsedTime timer;
    private final double rate;
    private double startTime, startPos, stopTime, stopPos;

    public ServoThrottle(Servo servo, double rate, double initPos) {
        this.servo = servo;
        this.rate = rate;
        this.startPos = this.stopPos = initPos;
        timer = new ElapsedTime();
    }

    public void setTargetPos(double pos) {

    }

    public void run() {
        servo.setPosition(getPosEstimate());
    }

    public double getPosEstimate() {
        double t = timer.seconds();
        if (t < startTime)
            return startPos;

        if (t > stopTime)
            return stopPos;

        double fraction = (t - startTime) / (stopTime - startTime);
        double moved = fraction * (stopPos - startPos);
        return startPos + moved;
    }
}
