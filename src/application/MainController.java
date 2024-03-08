package application;

import java.util.Arrays;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private ChoiceBox<String> serial1;
    @FXML
    private ChoiceBox<String> serial2;
    @FXML
    private ChoiceBox<Integer> baudrate1;
    @FXML
    private ChoiceBox<Integer> baudrate2;
    @FXML
    private CheckBox dataCheckBox;
    @FXML
    private Button startButton;
    @FXML
    private Label dataLabel;

    private DataRequest dataRequest = new DataRequest();
    private Thread backgroundThread;
    private boolean isRunning = false;

    @FXML
    private void initialize() {
        initializeSerialPorts();
        initializeBaudrates();
        startButton.setOnAction(event -> onControlButtonClick());
    }

    private void initializeSerialPorts() {
        ObservableList<String> portList = FXCollections.observableArrayList();
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        Arrays.stream(serialPorts).map(SerialPort::getSystemPortName).forEach(portList::add);
        serial1.setItems(portList);
        serial2.setItems(portList);
    }

    private void initializeBaudrates() {
        baudrate1.getItems().addAll(9600, 115200);
        baudrate2.getItems().addAll(9600, 115200);
    }

    private void onControlButtonClick() {
        if (isRunning) {
            stopBackgroundThread();
            startButton.setText("Start");
        } else {
            String selectedSerial1 = serial1.getValue();
            Integer selectedBaudrate1 = baudrate1.getValue();

            if (selectedSerial1 != null && selectedBaudrate1 != null) {
                selectedBaudrate1 = selectedBaudrate1.intValue();
                initializeDataRequest(selectedSerial1, selectedBaudrate1);

                dataRequest.sendRTCM();
                dataRequest.sendSurveyin();

                boolean isDataCheckBoxSelected = dataCheckBox.isSelected();
                startBackgroundThread(isDataCheckBoxSelected);

                startButton.setText("Stop");
            } else {
                System.out.println("Please select Serial and Baudrate.");
            }
        }
        isRunning = !isRunning;
    }

    private void initializeDataRequest(String selectedSerial, int selectedBaudrate) {
        dataRequest = new DataRequest();
        dataRequest.setSerialport(selectedSerial, selectedBaudrate);
    }

    private void startBackgroundThread(boolean includeData) {
        backgroundThread = new Thread(() -> {
            try {
                while (isRunning) {
                    String receivedData = dataRequest.readData();
                    Platform.runLater(() -> dataLabel.setText(receivedData));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void stopBackgroundThread() {
        if (backgroundThread != null && backgroundThread.isAlive()) {
            backgroundThread.interrupt();
            dataRequest.closePort();
        }
    }
}
