/*
To Do:
Fix Reverse Driving
Make only one side fire (right)

 */


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

//red turns left
//blue turns right




/*
To Do;
Double gears on shooter
Rotate Block and Top Part of Beacon Pusher 90 degrees.  The servo end position is currently level
    with the end of the robot instead of sideways
 */
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.SystemClock.sleep;

/**
 * Registers OpCode and Initializes Variables
 */
@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Autonomous α", group = "FTC772")
public class Autonomous extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft, frontRight, intake, dispenserLeft, dispenserRight, liftLeft, liftRight, midtake;
    private Servo dispenser, beaconAngleLeft, beaconAngleRight, forkliftLeft, forkliftRight;
    private boolean drivingForward = true;
    //private boolean init = false;
    //private final double DISPENSER_POWER = 1;
    private double BEACON_LEFT_IN;
    private double BEACON_RIGHT_IN;
    private final int INITIAL_FORWARD = 1000;
    private final int RAMP_UP = 1000;
    private final int TURN_ONE = 300;
    private final int FORWARD_TWO = 500;
    private final int TURN_TWO = 300;
    private final int FORWARD_THREE = 300;
    private final int COLOR_CORRECTION = 50;
    private final int FORWARD_FOUR = 400;
    private final int TURN_THREE = 500;
    private final int FORWARD_FIVE = 500;
    private final boolean isRed = true;
    private boolean didColorCorrection = false;

    private boolean wasChangingAngle = false;
    private ColorSensor colorSensor;
    private TouchSensor leftTouchSensor, rightTouchSensor;
//    @Override
//    public void init() {
//        /*
//        Initialize DcMotors
//         */
//        frontLeft = hardwareMap.dcMotor.get("frontLeft");
//        frontRight = hardwareMap.dcMotor.get("frontRight");
//
//        //intake = hardwareMap.dcMotor.get("intake");
//        dispenserLeft = hardwareMap.dcMotor.get("dispenserLeft");
//        dispenserRight = hardwareMap.dcMotor.get("dispenserRight");
//
//        /*
//        Initialize Servos
//         */
//        dispenserAngle = hardwareMap.servo.get("dispenserAngle");
//        beaconAngle = hardwareMap.servo.get("beaconAngle");
//
//
//        /*
//        Initialize Sensors
//        */
//        colorSensor = hardwareMap.colorSensor.get("colorSensor");
//        leftTouchSensor = hardwareMap.touchSensor.get("leftTouchSensor");
//        rightTouchSensor = hardwareMap.touchSensor.get("rightTouchSensor");
//
//        //Display completion message
//        telemetry.addData("Status", "Initialized");
//    }

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
    /*
    public void start() {
        /*
        Initialize all motors/servos to position
         */
        //runtime.reset();
        //dispenserAngle.setPosition(DEFAULT_ANGLE);
   // }

    /*
     * This method will be called repeatedly in a loop
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
     */




    @Override
    public void runOpMode() throws InterruptedException {

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
        /*
        Steps to Autonomous:
        Fire starting balls
        Drive to beacon 1
        Press beacon 1
        Drive to beacon 2
        Press beacon 2
        Drive to center and park while knocking ball off
         */

        frontLeft.setPower(1);
        frontRight.setPower(-1);
        sleep(INITIAL_FORWARD);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        dispenserLeft.setPower(1);
        dispenserRight.setPower(1);
        sleep(RAMP_UP);
        intake.setPower(1);
        midtake.setPower(1);
        dispenser.setPosition(0);
        sleep(500);
        dispenser.setPosition(.45);
        sleep(150);
        dispenser.setPosition(0);
        sleep(500);
        dispenser.setPosition(.45);
        intake.setPower(0);
        midtake.setPower(0);
        dispenserRight.setPower(0);
        dispenserLeft.setPower(0);
        if (isRed) {
            frontLeft.setPower(1);
            frontRight.setPower(1);
            sleep(TURN_ONE);
            frontRight.setPower(-1);
        }
        else {
            frontLeft.setPower(-1);
            frontRight.setPower(-1);
            sleep(TURN_ONE);
            frontLeft.setPower(1);
        }
        sleep(FORWARD_TWO);
        if (!isRed) {
            frontLeft.setPower(-1);
            sleep(TURN_TWO);
            frontLeft.setPower(1);
        }
        else {
            frontRight.setPower(1);
            sleep(TURN_TWO);
            frontRight.setPower(-1);
        }
        sleep(FORWARD_THREE);
        frontLeft.setPower(0);
        frontRight.setPower(0);
        if (!isRed) {
            if (colorSensor.red()<colorSensor.blue()) {
                beaconAngleRight.setPosition(Math.abs(.5-BEACON_RIGHT_IN));
            }
            else {
                frontLeft.setPower(1);
                frontRight.setPower(-1);
                sleep(COLOR_CORRECTION);
                didColorCorrection = true;
                frontLeft.setPower(0);
                frontRight.setPower(0);
                beaconAngleRight.setPosition(Math.abs(.5-BEACON_RIGHT_IN));
            }
        }
        else {
            if (colorSensor.red()>colorSensor.blue()) {
                beaconAngleLeft.setPosition(Math.abs(.5-BEACON_LEFT_IN));
            }
            else {
                frontLeft.setPower(1);
                frontRight.setPower(-1);
                sleep(COLOR_CORRECTION);
                didColorCorrection = true;
                frontLeft.setPower(0);
                frontRight.setPower(0);
                beaconAngleLeft.setPosition(Math.abs(.5-BEACON_LEFT_IN));
            }
        }
        frontLeft.setPower(1);
        frontRight.setPower(-1);
        if (didColorCorrection) {
            sleep(FORWARD_FOUR-COLOR_CORRECTION);
        }
        else {
            sleep(FORWARD_FOUR);
        }
        frontLeft.setPower(0);
        frontRight.setPower(0);
        if (!isRed) {
            if (colorSensor.red()<colorSensor.blue()) {
                beaconAngleRight.setPosition(Math.abs(.5-BEACON_RIGHT_IN));
            }
            else {
                frontLeft.setPower(1);
                frontRight.setPower(-1);
                sleep(COLOR_CORRECTION);
                frontLeft.setPower(0);
                frontRight.setPower(0);
                beaconAngleRight.setPosition(Math.abs(.5-BEACON_RIGHT_IN));
            }
        }
        else {
            if (colorSensor.red()>colorSensor.blue()) {
                beaconAngleLeft.setPosition(Math.abs(.5-BEACON_LEFT_IN));
            }
            else {
                frontLeft.setPower(1);
                frontRight.setPower(-1);
                sleep(COLOR_CORRECTION);
                frontLeft.setPower(0);
                frontRight.setPower(0);
                beaconAngleLeft.setPosition(Math.abs(.5-BEACON_LEFT_IN));
            }
        }
        frontLeft.setPower(1);
        frontRight.setPower(1);
        sleep(TURN_THREE);
        frontRight.setPower(-1);
        sleep(FORWARD_FIVE);
        telemetry.addData("Status", "Run Time: " + runtime.toString());








        /*
        This section is the short version of the autonomous for in case the other part doesn't work.
        It drives straight forward and knocks the cap ball off in the center.
         */
        sleep(10000);
        frontLeft.setPower(1);
        frontRight.setPower(-1);
        sleep(4000);
        frontRight.setPower(0);
        frontLeft.setPower(0);
        sleep(10000);
    }
}
