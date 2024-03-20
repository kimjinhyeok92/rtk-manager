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
    
    private MavlinkStream mavlinkStream = new MavlinkStream();
    private DataRequest dataRequest = new DataRequest();
    private Thread backgroundThread;
    private boolean isRunning = false;
    private boolean isComportBaudrateDisabled = false;

    @FXML
    private void initialize() {
        initializeSerialPorts();
        initializeBaudrates();
        startButton.setOnAction(event -> onStartButtonClick());
        
        // dataCheckBox에 대한 리스너 추가
        dataCheckBox.setOnAction(event -> {
            if (dataCheckBox.isSelected()) {
                // serial2와 baudrate2가 선택되었는지 확인
                String selectedSerial2 = serial2.getValue();
                Integer selectedBaudrate2 = baudrate2.getValue();
                if (selectedSerial2 != null && selectedBaudrate2 != null) {
                    // serial2와 baudrate2가 모두 선택된 경우 mavlinkStream을 초기화
                    selectedBaudrate2 = selectedBaudrate2.intValue();
                    initializeMavlinkStream(selectedSerial2, selectedBaudrate2);
                } else {
                    // serial2 또는 baudrate2가 선택되지 않은 경우 사용자에게 선택하도록 안내
                    System.out.println("Serial2와 Baudrate2를 선택하세요.");
                    dataCheckBox.setSelected(false); // 체크박스 해제
                }
            }
        });
    }

    private void initializeSerialPorts() {
        ObservableList<String> portList = FXCollections.observableArrayList();
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        Arrays.stream(serialPorts).map(SerialPort::getSystemPortName).forEach(portList::add);
        serial1.setItems(portList);
        serial2.setItems(portList);
    }

    private void initializeBaudrates() {
        baudrate1.getItems().addAll(null,9600, 115200);
        baudrate2.getItems().addAll(null,9600, 115200);
    }

    private void onStartButtonClick()  {
        if (isRunning) {
            stopBackgroundThread();

        } else {
            String selectedSerial1 = serial1.getValue();
            Integer selectedBaudrate1 = baudrate1.getValue();

            if (selectedSerial1 != null && selectedBaudrate1 != null) {
                selectedBaudrate1 = selectedBaudrate1.intValue();
                initializeDataRequest(selectedSerial1, selectedBaudrate1);

                dataRequest.sendRTCM();
                dataRequest.sendSurveyin();
         

                
                startBackgroundThread(isRunning);
                
            } else {
                System.out.println("Please select Serial and Baudrate.");
            }
        }
        isRunning = !isRunning;
        
        if (!isComportBaudrateDisabled) {
            // comport와 baudrate 칸들을 비활성화한다.
            serial1.setDisable(true);
            baudrate1.setDisable(true);
            isComportBaudrateDisabled = true;
            startButton.setText("Stop");
        } else {
            // comport와 baudrate 칸들을 다시 활성화한다.
            serial1.setDisable(false);
            baudrate1.setDisable(false);
            isComportBaudrateDisabled = false;
            startButton.setText("Start");
        }
    
    
        
    }
    


    private void initializeDataRequest(String selectedSerial, int selectedBaudrate) {
        dataRequest = new DataRequest();
        dataRequest.setSerialport(selectedSerial, selectedBaudrate);
    }

    private void initializeMavlinkStream(String selectedSerial2, int selectedBaudrate) {
    	mavlinkStream = dataRequest.mavlinkStream; //   new MavlinkStream();
    	mavlinkStream.setSerialport(selectedSerial2, selectedBaudrate);
    }

    
    private void startBackgroundThread(boolean includeData) {
        backgroundThread = new Thread(() -> {
            try {
                while (isRunning) {
                    String receivedData = dataRequest.readData();
                    Platform.runLater(() -> dataLabel.setText(receivedData));
               
                    
           
                    
                    // dataCheckBox가 선택되었는지 그리고 mavlinkStream이 초기화되었는지 확인
                    if (dataCheckBox.isSelected() && mavlinkStream != null) {
                        // MavpacketDatatoLora 메서드 호출
                    	dataRequest.sinkFlag = 1;
                        //dataRequest.mavlinkStream.MavpacketDatatoLora();
                    }
                    
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