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
        mySecondBot.runAction(mySecondBot.getDrive().actionBuilder(new Pose2d(11.5, 60, Math.toRadians(-90)))

//
                .setTangent(Math.PI/2)
                .lineToY(54)
                .setTangent(0)
                .lineToX(58)
                .turnTo(Math.toRadians(225))

//                sample 1
                .turnTo(Math.toRadians(270))
                .setTangent(0)
                .lineToX(48)
                .setTangent(Math.PI/2)
                .lineToY(38)
                .waitSeconds(2)
//                .splineToConstantHeading(new Vector2d(56, 56), Math.PI / 2)
                //backtobucket1
                .lineToY(54)
                .setTangent(0)
                .lineToX(58)
                .turnTo(Math.toRadians(225))
//              sample2
                .turnTo(Math.toRadians(270))
                .lineToY(38)
                .lineToY(54)
                .turnTo(Math.toRadians(225))
                //head back to sub
                .turnTo(Math.toRadians(270))
//                .lineToY(0)
//                .setTangent(0)
//                .lineToX(30)
                .splineToConstantHeading(new Vector2d(30, 5), Math.PI / 2)



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