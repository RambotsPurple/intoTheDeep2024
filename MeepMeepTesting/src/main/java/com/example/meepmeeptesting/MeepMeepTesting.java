package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);


        RoadRunnerBotEntity main  = new DefaultBotBuilder(meepMeep)
                .setColorScheme(new ColorSchemeRedDark())
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .build();

        Pose2d sample  = new Pose2d(-11.5, -60, Math.toRadians(90));
        Pose2d Speci  = new Pose2d(11.5, -60, Math.toRadians(90));
        main.runAction(main.getDrive().actionBuilder(Speci)

//                  Sample 0+2
//
//                .strafeToLinearHeading(new Vector2d(-55, -54), Math.toRadians(225))
//                .waitSeconds(2)
//                .strafeToLinearHeading(new Vector2d(-48, -38), Math.toRadians(90))
//                .waitSeconds(2)
//                .strafeToLinearHeading(new Vector2d(-55, -54), Math.toRadians(225))
//                .waitSeconds(2)
//
//
//                .strafeToLinearHeading(new Vector2d(-30, -5), Math.PI / 2)
//
//                Speci 3+0
                .strafeToLinearHeading(new Vector2d(11.5, -35), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(30, -35), Math.toRadians(270))
                .splineToConstantHeading(new Vector2d(35, -15), Math.toRadians(90))
                .strafeTo(new Vector2d(49, -13))
//                .splineToSplineHeading(new Pose2d(47, -16, 0), Math.toRadians(270))
                .strafeTo(new Vector2d(49, -55))

                .strafeTo(new Vector2d(49, -13))
                .strafeTo(new Vector2d(55, -13))
                .strafeTo(new Vector2d(55, -55))
                .strafeToLinearHeading(new Vector2d(0, -35), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(44, -55), Math.toRadians(270))
                .strafeToLinearHeading(new Vector2d(0, -35), Math.toRadians(90))
                .strafeToLinearHeading(new Vector2d(44, -55), Math.toRadians(270))




//                        .splineTo(new Vector2d(47, -16), Math.toRadians(270))

                .waitSeconds(2)
//                .strafeToLinearHeading(new Vector2d(-48, -38), Math.toRadians(90))
//                .waitSeconds(2)
//                .strafeToLinearHeading(new Vector2d(-55, -54), Math.toRadians(225))
//                .waitSeconds(2)

                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                // Add both of our declared bot entities
                .addEntity(main)

                .start();
    }
}