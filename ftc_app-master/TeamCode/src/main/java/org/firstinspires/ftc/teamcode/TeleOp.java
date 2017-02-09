


/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.*;


import static android.os.SystemClock.sleep;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Registers OpCode and Initializes Variables
 */
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp β", group = "FTC772")
public class TeleOp extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft, frontRight, intake, dispenserLeft, dispenserRight, liftLeft, liftRight, midtake;
    private Servo dispenser, beaconAngleLeft, beaconAngleRight, forkliftLeft, forkliftRight;
    private boolean drivingForward = true;
    private boolean endgame = false;
    private float left = 0, right = 0;
    private final double ANGLE_CONSTANT = 0.02;
    private final double DEFAULT_ANGLE = 0.55;
    private final double BEACON_DEFAULT = 0.4;
    //private final double DISPENSER_POWER = 1;
    private final double BEACON_LEFTPUSH = .2;
    private final double BEACON_RIGHTPUSH = .6;
    private final double LIFT_HEIGHT = 6500;
    private double BEACON_LEFT_IN = 0;
    private double BEACON_RIGHT_IN = 0;
    //private boolean wasChangingAngle = false;
    //private ColorSensor colorSensor;
    //private TouchSensor leftTouchSensor, rightTouchSensor;
    @Override
    public void init() {
        /*
        Initialize DcMotors
         */
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");

        intake = hardwareMap.dcMotor.get("intake");
        midtake = hardwareMap.dcMotor.get("midtake");
        dispenserLeft = hardwareMap.dcMotor.get("dispenserLeft");
        dispenserRight = hardwareMap.dcMotor.get("dispenserRight");
        dispenserLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        dispenserRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        liftLeft = hardwareMap.dcMotor.get("liftLeft");
        liftRight = hardwareMap.dcMotor.get("liftRight");

        liftLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        /*
        Initialize Servos
         */
        dispenser = hardwareMap.servo.get("dispenser");
        beaconAngleLeft = hardwareMap.servo.get("beaconAngleLeft");
        beaconAngleRight = hardwareMap.servo.get("beaconAngleRight");
        forkliftLeft = hardwareMap.servo.get("forkliftLeft");
        forkliftRight = hardwareMap.servo.get("forkliftRight");

        /*
        Initialize Sensors
        */
       //colorSensor = hardwareMap.colorSensor.get("colorSensor");
       //leftTouchSensor = hardwareMap.touchSensor.get("leftTouchSensor");
       //rightTouchSensor = hardwareMap.touchSensor.get("rightTouchSensor");

        //Display completion message
        telemetry.addData("Status", "Initialized");
    }

    /*
       * Code to run when the op mode is first enabled goes here
       * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()

    @Override
    public void init_loop() {
    }*/

    /*
     * This method will be called ONCE when start is pressed
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void start() {
        /*
        Initialize all motors/servos to position
         */
        runtime.reset();
        dispenser.setPosition(DEFAULT_ANGLE);
        forkliftLeft.setPosition(0.4);
        forkliftRight.setPosition(0.4);
        /*BEACON_RIGHT_IN = beaconAngleRight.getPosition();
        BEACON_LEFT_IN = beaconAngleLeft.getPosition();
        beaconAngleRight.setPosition(BEACON_RIGHT_IN);
        beaconAngleLeft.setPosition(BEACON_LEFT_IN);*/
        beaconAngleRight.setPosition(1);
        beaconAngleLeft.setPosition(1);
    }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */
    @Override
    public void loop() {
        /*
        Drive Control Block
         */

        if (gamepad1.y) {
            drivingForward = !drivingForward;
        }

        if (!drivingForward) {
            right = gamepad1.left_stick_y;
            left = -gamepad1.right_stick_y;
        } else {
            left = gamepad1.left_stick_y;
            right = -gamepad1.right_stick_y;
        }
        if (gamepad1.left_bumper) {
            right /= 4;
            left /= 4;
        }
        frontLeft.setPower(left);
        frontRight.setPower(right);

        /*
        Particle Distribution System
         */
        //Intake Motor
        if (gamepad2.a) {
            intake.setPower(-1);
        }
        else if (gamepad2.b) {
            intake.setPower(1);
        }
        else {
            intake.setPower(0);
        }

        //Midtake Motor
        if (gamepad2.x) {
            midtake.setPower(1);
        }
        else if (gamepad2.y) {
            midtake.setPower(-1);
        }
        else {
            midtake.setPower(0);
        }

        //Dispenser Motor
        if (gamepad2.right_trigger > 0.2 && gamepad2.left_trigger < 0.2) {
            dispenserLeft.setPower(gamepad2.right_trigger);
            dispenserRight.setPower(-gamepad2.right_trigger);
        }
        else if (gamepad2.left_trigger > 0.2 && gamepad2.right_trigger < 0.2) {
            dispenserRight.setPower(gamepad2.left_trigger);
            dispenserLeft.setPower(-gamepad2.left_trigger);
        }
        else {
            dispenserRight.setPower(0);
            dispenserLeft.setPower(0);
        }

        //Ball Loading Mechanism
        if (gamepad2.dpad_up) {
            dispenser.setPosition(0);
        }
        else if (gamepad2.dpad_left) {
            dispenser.setPosition(0.35);
        }
        else if(gamepad2.dpad_down) {
            dispenser.setPosition(0.35);
            sleep(500);
            dispenser.setPosition(0.55);
            sleep(100);
            dispenser.setPosition(0);
            sleep(500);
        }
        else {
            dispenser.setPosition(0.55);
        }

        /*
        Cap Ball Placement System
         */

        if (gamepad1.left_bumper && gamepad1.right_bumper && !gamepad1.x) {
            if (liftLeft.getCurrentPosition() >= LIFT_HEIGHT || liftRight.getCurrentPosition() >= LIFT_HEIGHT) {
                liftLeft.setPower(0);
                liftRight.setPower(0);
            } else {
                liftLeft.setPower(-1);
                liftRight.setPower(1);
            }
        }
        else if (gamepad1.left_bumper && gamepad1.right_bumper && gamepad1.x){
            liftLeft.setPower(-.01);
            liftRight.setPower(-.01);
        }
        else {
            liftLeft.setPower(0);
            liftRight.setPower(0);
        }

        if (gamepad1.a) {
            forkliftLeft.setPosition(0);
            forkliftRight.setPosition(1);
        }

        /*
        Beacon Claim System
         */

        if (gamepad2.left_bumper) {
            beaconAngleLeft.setPosition(Math.abs((.5-BEACON_LEFT_IN)));
            beaconAngleRight.setPosition(Math.abs((.5-BEACON_RIGHT_IN)));
        }
        else if (endgame == false) {
            beaconAngleLeft.setPosition(0);
            beaconAngleRight.setPosition(1);
        }

        if (gamepad1.back) {
            beaconAngleLeft.setPosition(Math.abs(1-beaconAngleLeft.getPosition()));
            beaconAngleRight.setPosition(Math.abs(1-beaconAngleRight.getPosition()));
            endgame = true;
        }
        if (gamepad1.start) {
            beaconAngleLeft.setPosition(0);
            beaconAngleRight.setPosition(1);
            endgame = false;
        }

        telemetry.addData("Status", "Servo Position: " + dispenser.getPosition());
    }
}