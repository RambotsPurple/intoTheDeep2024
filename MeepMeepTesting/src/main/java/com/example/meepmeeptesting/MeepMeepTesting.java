package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

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
        mySecondBot.runAction(mySecondBot.getDrive().actionBuilder(new Pose2d(0, 60, Math.toRadians(270)))

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

                .setTangent(Math.PI)
                .lineToX(50)
                .setTangent(Math.PI/2)
                .lineToY(40)
                .waitSeconds(2)

                .setTangent(Math.PI)
                .lineToX(56)
                .waitSeconds(2)
                .setTangent(Math.PI)
                .waitSeconds(2)

                .lineToX(62)

                .setTangent(Math.PI/2)
                .lineToY(50)
                .setTangent(Math.PI)
                .lineToX(-50)



//                .waitSeconds(3);
                // We set this bot to be blue

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