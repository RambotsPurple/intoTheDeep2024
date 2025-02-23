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

        // Declare our first bot
        RoadRunnerBotEntity myFirstBot = new DefaultBotBuilder(meepMeep)
                // We set this bot to be blue
                .setColorScheme(new ColorSchemeRedDark())
                .setConstraints(100, 100, Math.toRadians(180), Math.toRadians(180), 13)
                .build();

        myFirstBot.runAction(myFirstBot.getDrive().actionBuilder(new Pose2d(0, -60, Math.toRadians(90)))
                    .lineToY(-34)
                    .setTangent(Math.PI)
                    .lineToX(35)
                    .setTangent(Math.PI/2)
                    .lineToY(-5)
                    .setTangent(Math.PI)
                    .lineToX(48)
                    .setTangent(Math.PI/2)
                    .lineToY(-55)
                    .lineToY(-5)
                    .setTangent(Math.PI)
                    .lineToX(54)
                    .setTangent(Math.PI/2)
                    .lineToY(-55)
                    .lineToY(-5)
                    .setTangent(Math.PI)
                    .lineToX(62)
                    .setTangent(Math.PI/2)
                    .lineToY(-55)
                    .setTangent(Math.PI)
                    .lineToX(40)
                    .turn(Math.toRadians(90))

    //                clips
                    .setTangent(Math.PI/-6)
                    .lineToY(-34)
                    .turn(Math.toRadians(180))
                    .turn(Math.toRadians(180))
                    .setTangent(Math.PI/-6)
                    .lineToY(-55)

                    .setTangent(Math.PI/-6)
                    .lineToY(-34)
                    .turn(Math.toRadians(180))
                    .turn(Math.toRadians(180))
                    .setTangent(Math.PI/-6)
                    .lineToY(-55)

                    .setTangent(Math.PI/-6)
                    .lineToY(-34)
                    .turn(Math.toRadians(180))
                    .turn(Math.toRadians(180))
                    .setTangent(Math.PI/-6)
                    .lineToY(-55)


                    .setTangent(Math.PI/-6)
                    .lineToY(-34)
                    .turn(Math.toRadians(180))
                    .turn(Math.toRadians(180))
                    .setTangent(Math.PI/-6)
                    .lineToY(-55)

                    .setTangent(Math.PI/-6)
                    .lineToY(-34)
                    .turn(Math.toRadians(180))
                    .turn(Math.toRadians(180))
                    .setTangent(Math.PI/-6)
                    .lineToY(-55)

                .build());

        RoadRunnerBotEntity mySecondBot = new DefaultBotBuilder(meepMeep)
                .setColorScheme(new ColorSchemeBlueDark())
                .setConstraints(100, 100, Math.toRadians(180), Math.toRadians(180), 12)
                .build();
        mySecondBot.runAction(mySecondBot.getDrive().actionBuilder(new Pose2d(-11, 60, Math.toRadians(-90)))

//                .lineToY(34)  // Inverse of -34
//                .turn(Math.toRadians(-90))  // Inverse of 90°
//                .lineToX(-35)  // Inverse of 35
//                .setTangent(Math.PI/-2)  // Inverse of Math.PI/2
//                .lineToY(5)  // Inverse of -5
//                .setTangent(Math.PI)  // Inverse of Math.PI
//                .lineToX(-48)  // Inverse of 48
//                .setTangent(Math.PI/-2)  // Inverse of Math.PI/2
//                .lineToY(55)  // Inverse of -55
//                .lineToY(5)  // Inverse of -5
//                .setTangent(Math.PI)  // Inverse of Math.PI
//                .lineToX(-54)  // Inverse of 54
//                .setTangent(Math.PI/-2)  // Inverse of Math.PI/2
//                .lineToY(55)  // Inverse of -55
//                .lineToY(5)  // Inverse of -5
//                .setTangent(Math.PI)  // Inverse of Math.PI
//                .lineToX(-62)  // Inverse of 62
//                .setTangent(Math.PI/-2)  // Inverse of Math.PI/2
//                .lineToY(55)  // Inverse of -55
//                .setTangent(Math.PI)  // Inverse of Math.PI
//                .lineToX(-40)  // Inverse of 40
//                .turn(Math.toRadians(-90))  // Inverse of 90°
//
//
//// Clips
//                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
//                .lineToY(34)  // Inverse of -34
//                .turn(Math.toRadians(-180))  // Inverse of 180°
//                .turn(Math.toRadians(-180))  // Inverse of 180°
//                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
//                .lineToY(55)  // Inverse of -55
//
//                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
//                .lineToY(34)  // Inverse of -34
//                .turn(Math.toRadians(-180))  // Inverse of 180°
//                .turn(Math.toRadians(-180))  // Inverse of 180°
//                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
//                .lineToY(55)  // Inverse of -55
//
//                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
//                .lineToY(34)  // Inverse of -34
//                .turn(Math.toRadians(-180))  // Inverse of 180°
//                .turn(Math.toRadians(-180))  // Inverse of 180°
//                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
//                .lineToY(55)  // Inverse of -55
//
//                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
//                .lineToY(34)  // Inverse of -34
//                .turn(Math.toRadians(-180))  // Inverse of 180°
//                .turn(Math.toRadians(-180))  // Inverse of 180°
//                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
//                .lineToY(55)  // Inverse of -55
//
//                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
//                .lineToY(34)  // Inverse of -34
//                .turn(Math.toRadians(-180))  // Inverse of 180°
//                .turn(Math.toRadians(-180))  // Inverse of 180°
//                .setTangent(Math.PI/-6)  // Inverse of Math.PI/-6
//                .lineToY(55)  // Inverse of -55
//                .setTangent(0)
                    //start
//                .splineTo(new Vector2d(50, 40), Math.PI / -8)
//                .setTangent(Math.PI)
//                .lineToX(50)
//                .setTangent(Math.PI/2)
//                .lineToY(40)
//                .waitSeconds(2)
                //loot get
//                        .setTangent(Math.PI)
//                        .lineToX(56)
//                        .turn(Math.toRadians(-45))

                //speci one

//                .setTangent(0)
//                .lineToXSplineHeading(40, Math.toRadians(225))
//                .strafeTo(new Vector2d(58, 58))
//                .waitSeconds(4)
//
//                .setTangent(Math.PI/2)
//               .lineToYSplineHeading(40, Math.toRadians(270))
//                .setTangent(0)
//                .lineToX(54)
//               .splineToConstantHeading(new Vector2d(30, 5), Math.PI / 2)


//                .setTangent(0)
//                .lineToXSplineHeading(45, 5*Math.PI / 4)
//                .strafeTo(new Vector2d(58, 58))
//
//                .waitSeconds(4)
//
//                .setTangent(0)
//                .splineToConstantHeading(new Vector2d(48, 38), Math.PI / 2)
//                        .turn(Math.toRadians(45))
//
//
//                .setTangent(0)
//
////                back to basket
//                .splineToConstantHeading(new Vector2d(56, 56), Math.PI / 2)
//                .turn(Math.toRadians(-45))
//
//                .splineToConstantHeading(new Vector2d(58, 38), Math.PI / 2)
//                .turn(Math.toRadians(45))
//
//
////                back to basket
//                .splineToConstantHeading(new Vector2d(56, 56), Math.PI / 2)
//                .turn(Math.toRadians(-45))
//
////                final Sample
//
//                .splineToConstantHeading(new Vector2d(58, 38), Math.PI / 2)
//                .turn(Math.toRadians(90))
//
//
////               Final back to basket
//                        .splineToConstantHeading(new Vector2d(56, 56), Math.PI / 2)
//                        .turn(Math.toRadians(-90))
//
//                .setTangent(0)
//                        .lineToX(35)
//                .splineToConstantHeading(new Vector2d(30, 5), Math.PI / -2)




//                .waitSeconds(3);
                // We set this bot to be blue
//
//                        .setTangent(0)
//                        .lineToX(-60)
                .setTangent(Math.PI/2)
                .lineToY(54)
                .setTangent(0)
                .lineToXSplineHeading(40, 5*Math.PI / 4)
                .strafeTo(new Vector2d(60, 60))
                .waitSeconds(2)

                .setTangent(0)
                .splineToConstantHeading(new Vector2d(38, 38), Math.PI / 2)
                .turn(Math.toRadians(45))

                .build());


        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                // Add both of our declared bot entities
                .addEntity(myFirstBot)
                .addEntity(mySecondBot)

                .start();
    }
}