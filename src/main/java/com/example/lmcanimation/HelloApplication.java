package com.example.lmcanimation;

import javafx.animation.ParallelTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import java.io.IOException;

public class HelloApplication extends Application {
    private int pc = 0;
    private int accumulator = 0;
    private final int MEMORY_SIZE = 16;
    private int[] memory = new int[MEMORY_SIZE];
    private boolean halted = false;
    private Label[] mailboxLabels =
            new Label[MEMORY_SIZE];
    private Timeline timeline;
    private Label calcLabel;
    private Label pcLabel;
    private Label carryingLabel;
    private ImageView littleMan;
    private Button stepButton;
    private Button runButton;
    private Button pauseButton;

    private void moveLittleManTo(double targetX, double targetY) {

        TranslateTransition go = new TranslateTransition();

        go.setNode(littleMan);
        go.setDuration(Duration.seconds(1));

        go.setToX(targetX - littleMan.getLayoutX());
        go.setToY(targetY - littleMan.getLayoutY());

        TranslateTransition back = new TranslateTransition();

        back.setNode(littleMan);
        back.setDuration(Duration.seconds(1));

        back.setToX(0);
        back.setToY(0);

        SequentialTransition sequence = new SequentialTransition(go, back);

        sequence.play();
    }
    private void highlightMailbox(int index) {

        for (int i = 0; i < MEMORY_SIZE; i++) {

            mailboxLabels[i].setStyle(
                    "-fx-border-color: black;" +
                            "-fx-alignment: center;"
            );
        }

        mailboxLabels[index].setStyle(
                "-fx-border-color: black;" +
                        "-fx-alignment: center;" +
                        "-fx-background-color: yellow;"
        );
    }

    private void executeStep() {
        if (halted) {

            return;
        }
        boolean jumped = false;

        int currentInstruction = pc;

        int instruction = memory[pc];

        int opcode = instruction / 100;

        int address = instruction % 100;



        if (instruction == 901) {

            inputInstruction();

        }
        else if (instruction == 902) {

            outputInstruction();

        }
        else if (opcode == 1) {

            addInstruction(address);

        }
        else if (opcode == 2) {

            subInstruction(address);

        }
        else if (opcode == 5) {

            loadInstruction(address);

        }
        else if (opcode == 3) {

            storeInstruction(address);

        }

        if (opcode == 6) {

            pc = address;

            jumped = true;
        }
        else if (opcode == 7) {

            if (accumulator == 0) {

                pc = address;

                jumped = true;
            }
        }
        else if (opcode == 8) {

            if (accumulator >= 0) {

                pc = address;

                jumped = true;
            }
        }
        else if (instruction == 0) {

            halted = true;

            stepButton.setDisable(true);
            timeline.stop();
            runButton.setDisable(true);
            pauseButton.setDisable(true);
        }


        highlightMailbox(currentInstruction);

        updateGUI();

        if (!jumped) {

            pc++;

        }
    }

    private void inputInstruction() {

        int value = 123;

        double inputX = 300;
        double inputY = 230;

        double calculatorX = 600;
        double calculatorY = 180;

        TranslateTransition goInput =
                new TranslateTransition();

        goInput.setNode(littleMan);

        goInput.setDuration(
                Duration.seconds(1)
        );

        goInput.setToX(
                inputX - littleMan.getLayoutX()
        );

        goInput.setToY(
                inputY - littleMan.getLayoutY()
        );

        TranslateTransition goCalculator =
                new TranslateTransition();

        goCalculator.setNode(littleMan);

        goCalculator.setDuration(
                Duration.seconds(1)
        );

        goCalculator.setToX(
                calculatorX - littleMan.getLayoutX()
        );

        goCalculator.setToY(
                calculatorY - littleMan.getLayoutY()
        );

        goInput.setOnFinished(e -> {

            carryingLabel.setText(
                    String.format("%03d", value)
            );

        });

        goCalculator.setOnFinished(e -> {

            accumulator = value;

            carryingLabel.setText("");

            updateGUI();
        });

        TranslateTransition backHome =
                new TranslateTransition();

        backHome.setNode(littleMan);

        backHome.setDuration(
                Duration.seconds(1)
        );

        backHome.setToX(0);

        backHome.setToY(0);
        TranslateTransition labelGoInput =
                new TranslateTransition();

        labelGoInput.setNode(carryingLabel);

        labelGoInput.setDuration(
                Duration.seconds(1)
        );

        labelGoInput.setToX(
                inputX - littleMan.getLayoutX()
        );

        labelGoInput.setToY(
                inputY - littleMan.getLayoutY()
        );

        TranslateTransition labelGoCalculator =
                new TranslateTransition();

        labelGoCalculator.setNode(carryingLabel);

        labelGoCalculator.setDuration(
                Duration.seconds(1)
        );

        labelGoCalculator.setToX(
                calculatorX - littleMan.getLayoutX()
        );

        labelGoCalculator.setToY(
                calculatorY - littleMan.getLayoutY()
        );

        TranslateTransition labelBackHome =
                new TranslateTransition();

        labelBackHome.setNode(carryingLabel);

        labelBackHome.setDuration(
                Duration.seconds(1)
        );

        labelBackHome.setToX(0);

        labelBackHome.setToY(0);

        SequentialTransition sequence =
                new SequentialTransition(

                        new ParallelTransition(
                                goInput,
                                labelGoInput
                        ),

                        new ParallelTransition(
                                goCalculator,
                                labelGoCalculator
                        ),

                        new ParallelTransition(
                                backHome,
                                labelBackHome
                        )
                );

        sequence.play();
    }

    private void outputInstruction() {

        int value = accumulator;

        double calculatorX = 600;
        double calculatorY = 180;

        double outputX = 300;
        double outputY = 380;

        TranslateTransition goCalculator =
                new TranslateTransition();

        goCalculator.setNode(littleMan);

        goCalculator.setDuration(
                Duration.seconds(1)
        );

        goCalculator.setToX(
                calculatorX - littleMan.getLayoutX()
        );

        goCalculator.setToY(
                calculatorY - littleMan.getLayoutY()
        );

        TranslateTransition goOutput =
                new TranslateTransition();

        goOutput.setNode(littleMan);

        goOutput.setDuration(
                Duration.seconds(1)
        );

        goOutput.setToX(
                outputX - littleMan.getLayoutX()
        );

        goOutput.setToY(
                outputY - littleMan.getLayoutY()
        );

        goCalculator.setOnFinished(e -> {

            carryingLabel.setText(
                    String.format("%03d", value)
            );


        });

        goOutput.setOnFinished(e -> {

            carryingLabel.setText("");
        });

        TranslateTransition backHome =
                new TranslateTransition();

        backHome.setNode(littleMan);

        backHome.setDuration(
                Duration.seconds(1)
        );

        backHome.setToX(0);

        backHome.setToY(0);

        TranslateTransition labelGoCalculator =
                new TranslateTransition();

        labelGoCalculator.setNode(carryingLabel);

        labelGoCalculator.setDuration(
                Duration.seconds(1)
        );

        labelGoCalculator.setToX(
                calculatorX - littleMan.getLayoutX()
        );

        labelGoCalculator.setToY(
                calculatorY - littleMan.getLayoutY()
        );

        TranslateTransition labelGoOutput =
                new TranslateTransition();

        labelGoOutput.setNode(carryingLabel);

        labelGoOutput.setDuration(
                Duration.seconds(1)
        );

        labelGoOutput.setToX(
                outputX - littleMan.getLayoutX()
        );

        labelGoOutput.setToY(
                outputY - littleMan.getLayoutY()
        );

        TranslateTransition labelBackHome =
                new TranslateTransition();

        labelBackHome.setNode(carryingLabel);

        labelBackHome.setDuration(
                Duration.seconds(1)
        );

        labelBackHome.setToX(0);

        labelBackHome.setToY(0);

        SequentialTransition sequence =
                new SequentialTransition(

                        new ParallelTransition(
                                goCalculator,
                                labelGoCalculator
                        ),

                        new ParallelTransition(
                                goOutput,
                                labelGoOutput
                        ),

                        new ParallelTransition(
                                backHome,
                                labelBackHome
                        )
                );
        sequence.play();
    }

    private void loadInstruction(int address) {

        int value = memory[address];

        double mailboxX = 760;
        double mailboxY = 110 + address * 30;

        double calculatorX = 600;
        double calculatorY = 180;

        TranslateTransition goMailbox = new TranslateTransition();
        TranslateTransition labelGoMailbox =
                new TranslateTransition();

        labelGoMailbox.setNode(carryingLabel);

        labelGoMailbox.setDuration(
                Duration.seconds(1)
        );

        labelGoMailbox.setToX(
                mailboxX - littleMan.getLayoutX()
        );

        labelGoMailbox.setToY(
                mailboxY - littleMan.getLayoutY()
        );
        goMailbox.setNode(littleMan);
        goMailbox.setDuration(Duration.seconds(1));
        goMailbox.setToX(mailboxX - littleMan.getLayoutX());
        goMailbox.setToY(mailboxY - littleMan.getLayoutY());

        TranslateTransition goCalculator = new TranslateTransition();
        TranslateTransition labelGoCalculator =
                new TranslateTransition();

        labelGoCalculator.setNode(carryingLabel);

        labelGoCalculator.setDuration(
                Duration.seconds(1)
        );

        labelGoCalculator.setToX(
                calculatorX - littleMan.getLayoutX()
        );

        labelGoCalculator.setToY(
                calculatorY - littleMan.getLayoutY()
        );
        goCalculator.setNode(littleMan);
        goCalculator.setDuration(Duration.seconds(1));
        goCalculator.setToX(calculatorX - littleMan.getLayoutX());
        goCalculator.setToY(calculatorY - littleMan.getLayoutY());

        goMailbox.setOnFinished(e -> {
            carryingLabel.setText(String.format("%03d", value));
        });

        goCalculator.setOnFinished(e -> {
            accumulator = value;
            carryingLabel.setText("");
            updateGUI();
        });

        TranslateTransition backHome = new TranslateTransition();
        TranslateTransition labelBackHome =
                new TranslateTransition();

        labelBackHome.setNode(carryingLabel);

        labelBackHome.setDuration(
                Duration.seconds(1)
        );

        labelBackHome.setToX(0);

        labelBackHome.setToY(0);
        backHome.setNode(littleMan);
        backHome.setDuration(Duration.seconds(1));
        backHome.setToX(0);
        backHome.setToY(0);

        SequentialTransition sequence =
                new SequentialTransition(

                        new ParallelTransition(
                                goMailbox,
                                labelGoMailbox
                        ),

                        new ParallelTransition(
                                goCalculator,
                                labelGoCalculator
                        ),

                        new ParallelTransition(
                                backHome,
                                labelBackHome
                        )
                );

        sequence.play();
    }

    private void storeInstruction(int address) {

        int value = accumulator;

        double calculatorX = 600;
        double calculatorY = 180;

        double mailboxX = 760;
        double mailboxY = 110 + address * 30;

        TranslateTransition goCalculator = new TranslateTransition();
        TranslateTransition labelGoCalculator =
                new TranslateTransition();

        labelGoCalculator.setNode(carryingLabel);

        labelGoCalculator.setDuration(
                Duration.seconds(1)
        );

        labelGoCalculator.setToX(
                calculatorX - littleMan.getLayoutX()
        );

        labelGoCalculator.setToY(
                calculatorY - littleMan.getLayoutY()
        );

        goCalculator.setNode(littleMan);

        goCalculator.setDuration(
                Duration.seconds(1)
        );

        goCalculator.setToX(
                calculatorX - littleMan.getLayoutX()
        );

        goCalculator.setToY(
                calculatorY - littleMan.getLayoutY()
        );

        TranslateTransition goMailbox = new TranslateTransition();
        TranslateTransition labelGoMailbox =
                new TranslateTransition();

        labelGoMailbox.setNode(carryingLabel);

        labelGoMailbox.setDuration(
                Duration.seconds(1)
        );

        labelGoMailbox.setToX(
                mailboxX - littleMan.getLayoutX()
        );

        labelGoMailbox.setToY(
                mailboxY - littleMan.getLayoutY()
        );

        goMailbox.setNode(littleMan);

        goMailbox.setDuration(
                Duration.seconds(1)
        );

        goMailbox.setToX(
                mailboxX - littleMan.getLayoutX()
        );

        goMailbox.setToY(
                mailboxY - littleMan.getLayoutY()
        );

        goCalculator.setOnFinished(e -> {

            carryingLabel.setText(
                    String.format("%03d", value)
            );

        });

        goMailbox.setOnFinished(e -> {

            memory[address] = value;

            carryingLabel.setText("");

            updateGUI();
        });

        TranslateTransition backHome = new TranslateTransition();
        TranslateTransition labelBackHome =
                new TranslateTransition();

        labelBackHome.setNode(carryingLabel);

        labelBackHome.setDuration(
                Duration.seconds(1)
        );

        labelBackHome.setToX(0);

        labelBackHome.setToY(0);

        backHome.setNode(littleMan);

        backHome.setDuration(
                Duration.seconds(1)
        );

        backHome.setToX(0);

        backHome.setToY(0);

        SequentialTransition sequence =
                new SequentialTransition(

                        new ParallelTransition(
                                goCalculator,
                                labelGoCalculator
                        ),

                        new ParallelTransition(
                                goMailbox,
                                labelGoMailbox
                        ),

                        new ParallelTransition(
                                backHome,
                                labelBackHome
                        )
                );

        sequence.play();
    }
    private void addInstruction(int address) {

        int value = memory[address];

        double mailboxX = 760;
        double mailboxY = 110 + address * 30;

        double calculatorX = 600;
        double calculatorY = 180;

        TranslateTransition goMailbox =
                new TranslateTransition();

        goMailbox.setNode(littleMan);

        goMailbox.setDuration(
                Duration.seconds(1)
        );

        goMailbox.setToX(
                mailboxX - littleMan.getLayoutX()
        );

        goMailbox.setToY(
                mailboxY - littleMan.getLayoutY()
        );

        TranslateTransition goCalculator =
                new TranslateTransition();

        goCalculator.setNode(littleMan);

        goCalculator.setDuration(
                Duration.seconds(1)
        );

        goCalculator.setToX(
                calculatorX - littleMan.getLayoutX()
        );

        goCalculator.setToY(
                calculatorY - littleMan.getLayoutY()
        );

        goMailbox.setOnFinished(e -> {

            carryingLabel.setText(
                    String.format("%03d", value)
            );

        });

        goCalculator.setOnFinished(e -> {

            accumulator += value;

            carryingLabel.setText("");

            updateGUI();
        });

        TranslateTransition backHome =
                new TranslateTransition();

        backHome.setNode(littleMan);

        backHome.setDuration(
                Duration.seconds(1)
        );

        backHome.setToX(0);

        backHome.setToY(0);

        TranslateTransition labelGoMailbox =
                new TranslateTransition();

        labelGoMailbox.setNode(carryingLabel);

        labelGoMailbox.setDuration(
                Duration.seconds(1)
        );

        labelGoMailbox.setToX(
                mailboxX - littleMan.getLayoutX()
        );

        labelGoMailbox.setToY(
                mailboxY - littleMan.getLayoutY()
        );

        TranslateTransition labelGoCalculator =
                new TranslateTransition();

        labelGoCalculator.setNode(carryingLabel);

        labelGoCalculator.setDuration(
                Duration.seconds(1)
        );

        labelGoCalculator.setToX(
                calculatorX - littleMan.getLayoutX()
        );

        labelGoCalculator.setToY(
                calculatorY - littleMan.getLayoutY()
        );

        TranslateTransition labelBackHome =
                new TranslateTransition();

        labelBackHome.setNode(carryingLabel);

        labelBackHome.setDuration(
                Duration.seconds(1)
        );

        labelBackHome.setToX(0);

        labelBackHome.setToY(0);

        SequentialTransition sequence =
                new SequentialTransition(

                        new ParallelTransition(
                                goMailbox,
                                labelGoMailbox
                        ),

                        new ParallelTransition(
                                goCalculator,
                                labelGoCalculator
                        ),

                        new ParallelTransition(
                                backHome,
                                labelBackHome
                        )
                );

        sequence.play();
    }
    private void subInstruction(int address) {

        int value = memory[address];

        double mailboxX = 760;
        double mailboxY = 110 + address * 30;

        double calculatorX = 600;
        double calculatorY = 180;

        TranslateTransition goMailbox =
                new TranslateTransition();

        goMailbox.setNode(littleMan);

        goMailbox.setDuration(
                Duration.seconds(1)
        );

        goMailbox.setToX(
                mailboxX - littleMan.getLayoutX()
        );

        goMailbox.setToY(
                mailboxY - littleMan.getLayoutY()
        );

        TranslateTransition goCalculator =
                new TranslateTransition();

        goCalculator.setNode(littleMan);

        goCalculator.setDuration(
                Duration.seconds(1)
        );

        goCalculator.setToX(
                calculatorX - littleMan.getLayoutX()
        );

        goCalculator.setToY(
                calculatorY - littleMan.getLayoutY()
        );

        goMailbox.setOnFinished(e -> {

            carryingLabel.setText(
                    String.format("%03d", value)
            );

        });

        goCalculator.setOnFinished(e -> {

            accumulator -= value;

            carryingLabel.setText("");

            updateGUI();
        });

        TranslateTransition backHome =
                new TranslateTransition();

        backHome.setNode(littleMan);

        backHome.setDuration(
                Duration.seconds(1)
        );

        backHome.setToX(0);

        backHome.setToY(0);

        TranslateTransition labelGoMailbox =
                new TranslateTransition();

        labelGoMailbox.setNode(carryingLabel);

        labelGoMailbox.setDuration(
                Duration.seconds(1)
        );

        labelGoMailbox.setToX(
                mailboxX - littleMan.getLayoutX()
        );

        labelGoMailbox.setToY(
                mailboxY - littleMan.getLayoutY()
        );

        TranslateTransition labelGoCalculator =
                new TranslateTransition();

        labelGoCalculator.setNode(carryingLabel);

        labelGoCalculator.setDuration(
                Duration.seconds(1)
        );

        labelGoCalculator.setToX(
                calculatorX - littleMan.getLayoutX()
        );

        labelGoCalculator.setToY(
                calculatorY - littleMan.getLayoutY()
        );

        TranslateTransition labelBackHome =
                new TranslateTransition();

        labelBackHome.setNode(carryingLabel);

        labelBackHome.setDuration(
                Duration.seconds(1)
        );

        labelBackHome.setToX(0);

        labelBackHome.setToY(0);

        SequentialTransition sequence =
                new SequentialTransition(

                        new ParallelTransition(
                                goMailbox,
                                labelGoMailbox
                        ),

                        new ParallelTransition(
                                goCalculator,
                                labelGoCalculator
                        ),

                        new ParallelTransition(
                                backHome,
                                labelBackHome
                        )
                );

        sequence.play();
    }
    private void jumpInstruction(int address) {

        pc = address;
    }
    private void branchZeroInstruction(int address) {

        if (accumulator == 0) {

            pc = address;

        }
    }
    private void branchPositiveInstruction(int address) {

        if (accumulator >= 0) {

            pc = address;

        }
    }

    private void updateGUI() {

        calcLabel.setText(
                String.format("%03d", accumulator)
        );

        pcLabel.setText(
                String.format("%02d", pc)
        );

        for (int i = 0; i < MEMORY_SIZE; i++) {

            mailboxLabels[i].setText(
                    String.format(
                            "%02d   %03d",
                            i,
                            memory[i]
                    )
            );
        }
    }
    private void moveLittleManFromMailboxToCalculator(int address) {

        double mailboxX = 760;
        double mailboxY = 150 + address * 30;

        double calculatorX = 600;
        double calculatorY = 180;

        TranslateTransition goMailbox = new TranslateTransition();
        goMailbox.setNode(littleMan);
        goMailbox.setDuration(Duration.seconds(1));
        goMailbox.setToX(mailboxX - littleMan.getLayoutX());
        goMailbox.setToY(mailboxY - littleMan.getLayoutY());

        TranslateTransition goCalculator = new TranslateTransition();
        goCalculator.setNode(littleMan);
        goCalculator.setDuration(Duration.seconds(1));
        goCalculator.setToX(calculatorX - littleMan.getLayoutX());
        goCalculator.setToY(calculatorY - littleMan.getLayoutY());

        TranslateTransition backHome = new TranslateTransition();
        backHome.setNode(littleMan);
        backHome.setDuration(Duration.seconds(1));
        backHome.setToX(0);
        backHome.setToY(0);

        SequentialTransition sequence = new SequentialTransition(
                goMailbox,
                goCalculator,
                backHome
        );

        sequence.play();
    }
    private void moveLittleManFromCalculatorToMailbox(int address) {

        double calculatorX = 600;
        double calculatorY = 180;

        double mailboxX = 760;
        double mailboxY = 150 + address * 30;

        TranslateTransition goCalculator = new TranslateTransition();
        goCalculator.setNode(littleMan);
        goCalculator.setDuration(Duration.seconds(1));
        goCalculator.setToX(calculatorX - littleMan.getLayoutX());
        goCalculator.setToY(calculatorY - littleMan.getLayoutY());

        TranslateTransition goMailbox = new TranslateTransition();
        goMailbox.setNode(littleMan);
        goMailbox.setDuration(Duration.seconds(1));
        goMailbox.setToX(mailboxX - littleMan.getLayoutX());
        goMailbox.setToY(mailboxY - littleMan.getLayoutY());

        TranslateTransition backHome = new TranslateTransition();
        backHome.setNode(littleMan);
        backHome.setDuration(Duration.seconds(1));
        backHome.setToX(0);
        backHome.setToY(0);

        SequentialTransition sequence = new SequentialTransition(
                goCalculator,
                goMailbox,
                backHome
        );

        sequence.play();
    }
    @Override
    public void start(Stage stage) {
        Pane root = new Pane();

        // ===== Main System Box =====
        Rectangle systemBox = new Rectangle(
                250,
                100,
                700,
                500
        );

        systemBox.setFill(Color.WHITE);

        systemBox.setStroke(Color.BLACK);

        systemBox.setStrokeWidth(3);

        // ===== Boxes & Labels =====

        VBox mailboxBox = new VBox();

        mailboxBox.setLayoutX(820);

        mailboxBox.setLayoutY(110);

        mailboxBox.setStyle(
                "-fx-border-color: black;" +
                        "-fx-border-width: 2;"
        );

        for (int i = 0; i < MEMORY_SIZE; i++) {

            mailboxLabels[i] = new Label(
                    String.format("%02d   %03d", i, 0)
            );

            mailboxLabels[i].setPrefSize(100, 30);

            mailboxLabels[i].setStyle(
                    "-fx-border-color: black;" +
                            "-fx-alignment: center;"
            );

            mailboxBox.getChildren().add(mailboxLabels[i]);
        }
        Rectangle calculator = new Rectangle(
                120,
                100
        );

        calculator.setLayoutX(600);

        calculator.setLayoutY(120);

        calculator.setFill(Color.WHITE);

        calculator.setStroke(Color.BLACK);

        calculator.setStrokeWidth(3);

        calcLabel = new Label("000");
        calcLabel.setLayoutX(635);
        calcLabel.setLayoutY(140);
        calcLabel.setStyle(
                "-fx-font-size: 28px;"
        );

        Rectangle pcBox = new Rectangle(
                120,
                60
        );

        pcBox.setLayoutX(520);

        pcBox.setLayoutY(520);

        pcBox.setFill(Color.WHITE);

        pcBox.setStroke(Color.BLACK);

        pcBox.setStrokeWidth(3);

        pcLabel = new Label("00");

        pcLabel.setLayoutX(560);

        pcLabel.setLayoutY(530);

        pcLabel.setStyle(
                "-fx-font-size: 30px;"
        );

        Line inputLeft = new Line(
                250,
                250,
                280,
                270
        );
        Line inputBottom = new Line(
                280,
                270,
                340,
                270
        );
        Line inputRight = new Line(
                340,
                270,
                370,
                250
        );
        inputLeft.setStrokeWidth(3);

        inputBottom.setStrokeWidth(3);

        inputRight.setStrokeWidth(3);

        Line outputLeft = new Line(
                250,
                400,
                280,
                420
        );
        Line outputBottom = new Line(
                280,
                420,
                340,
                420
        );
        Line outputRight = new Line(
                340,
                420,
                370,
                400
        );
        outputLeft.setStrokeWidth(3);

        outputBottom.setStrokeWidth(3);

        outputRight.setStrokeWidth(3);

        // ===== Little Man Image =====

        Image lmImage = new Image(
                getClass().getResourceAsStream("/images/littleman.png")
        );

        littleMan = new ImageView(lmImage);

        littleMan.setFitWidth(120);

        littleMan.setFitHeight(160);

        littleMan.setLayoutX(550);

        littleMan.setLayoutY(300);

        carryingLabel = new Label("");

        carryingLabel.setLayoutX(590);
        carryingLabel.setLayoutY(270);

        carryingLabel.setStyle(
                "-fx-font-size: 22px;" +
                        "-fx-text-fill: red;" +
                        "-fx-font-weight: bold;"
        );

        // ===== Titles =====
        stepButton = new Button("STEP");

        stepButton.setLayoutX(1000);

        stepButton.setLayoutY(650);

        stepButton.setPrefSize(120, 50);

        stepButton.setStyle(
                "-fx-font-size: 20px;"
        );
        stepButton.setOnAction(e -> {

            executeStep();

        });

        runButton = new Button("RUN");

        runButton.setLayoutX(1000);
        runButton.setLayoutY(580);

        runButton.setPrefSize(120, 50);

        runButton.setStyle(
                "-fx-font-size: 20px;"
        );

        pauseButton = new Button("PAUSE");

        pauseButton.setLayoutX(1000);
        pauseButton.setLayoutY(720);

        pauseButton.setPrefSize(120, 50);

        pauseButton.setStyle(
                "-fx-font-size: 18px;"
        );

        timeline = new Timeline(

                new KeyFrame(
                        Duration.seconds(3),
                        e -> executeStep()
                )
        );

        timeline.setCycleCount(
                Timeline.INDEFINITE
        );

        Label mailboxTitle = new Label("Mailboxes");

        mailboxTitle.setLayoutX(825);

        mailboxTitle.setLayoutY(70);

        mailboxTitle.setStyle(
                "-fx-font-size: 24px;"
        );

        Label calculatorTitle = new Label("Calculator");

        calculatorTitle.setLayoutX(610);

        calculatorTitle.setLayoutY(70);

        calculatorTitle.setStyle(
                "-fx-font-size: 24px;"
        );

        Label inputTitle = new Label("Input Basket");

        inputTitle.setLayoutX(120);

        inputTitle.setLayoutY(250);

        inputTitle.setStyle(
                "-fx-font-size: 22px;"
        );

        Label outputTitle = new Label("Output Basket");

        outputTitle.setLayoutX(105);

        outputTitle.setLayoutY(400);

        outputTitle.setStyle(
                "-fx-font-size: 22px;"
        );

        Label pcTitle = new Label("Program Counter");

        pcTitle.setLayoutX(500);

        pcTitle.setLayoutY(600);

        pcTitle.setStyle(
                "-fx-font-size: 22px;"
        );

        runButton.setOnAction(e -> {

            timeline.play();

        });
        pauseButton.setOnAction(e -> {

            timeline.pause();

        });



        // ===== Add everything to screen =====
        root.getChildren().addAll(
                systemBox,

                mailboxBox,
                mailboxTitle,

                calculator,
                calcLabel,
                calculatorTitle,

                pcBox,
                pcLabel,
                carryingLabel,
                littleMan,
                pcTitle,

                inputTitle,
                outputTitle,
                runButton,
                pauseButton,
                stepButton

        );
        root.getChildren().addAll(
                inputLeft,
                inputBottom,
                inputRight,
                outputLeft,
                outputBottom,
                outputRight
        );

        Scene scene = new Scene(
                root,
                1200,
                800
        );

        stage.setTitle("LMC Animation");

        stage.setScene(scene);

        memory[0] = 514;
        memory[1] = 215;
        memory[2] = 314;
        memory[3] = 708;
        memory[4] = 514;
        memory[5] = 902;
        memory[6] = 600;
        memory[7] = 000;
        memory[8] = 000;

        memory[14] = 30;
        memory[15] = 10;
        stage.show();
        updateGUI();
    }
}
