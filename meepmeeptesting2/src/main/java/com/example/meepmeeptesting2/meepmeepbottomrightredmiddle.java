package com.example.meepmeeptesting2;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class meepmeepbottomrightredmiddle {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(100, 100, Math.toRadians(180), Math.toRadians(180), 16)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-36, -70, 0))
                                .turn(Math.toRadians(90))
                                .forward(38)
                                .back((10))
                                .splineTo(new Vector2d(-57, -54), Math.toRadians(90))
                                .splineTo(new Vector2d(-40, -10), Math.toRadians(0))
                                .splineTo(new Vector2d(33, -13), Math.toRadians(0))
                                .splineTo(new Vector2d(49, -34), Math.toRadians(0))
                                //  .splineToLinearHeading(new Pose2d(-15 -12, Math.toRadians(90)), Math.toRadians(0))

                                //This is bottomRightRedMiddle





                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }}
