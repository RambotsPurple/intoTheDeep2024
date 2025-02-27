package org.firstinspires.ftc.teamcode.Ops.AUTO;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RobotConfig;


@Autonomous(name = "Simple park pls hardstop")
public class simplepark extends LinearOpMode {

    @Override
    public void runOpMode() {

        // initializing hardware
        RobotConfig.initialize(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double pow = -0.5;
            RobotConfig.frontLeft.setPower(pow);
            RobotConfig.rearLeft.setPower(pow);
            RobotConfig.frontRight.setPower(pow);
            RobotConfig.rearRight.setPower(pow);

        }
    }

}