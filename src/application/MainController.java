package application;

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
    
    // RTCMRequest 객체 생성
    private RTCMRequest rtcMRequest = new RTCMRequest(); 
    
    // 백그라운드 스레드에서 데이터를 계속 읽고 UI 업데이트
    private Thread backgroundThread;

    private boolean isRunning = false;

    @FXML
    private void initialize() {
        // ChoiceBox에 노트북에 연결된 시리얼 포트 목록 추가
        ObservableList<String> portList = FXCollections.observableArrayList();
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        for (SerialPort port : serialPorts) {
            portList.add(port.getSystemPortName());
        }
        serial1.setItems(portList);
        serial2.setItems(portList);

        // Baudrate ChoiceBox에 항목 추가 (예: 9600, 115200, ...)
        baudrate1.getItems().addAll(9600, 115200);
        baudrate2.getItems().addAll(9600, 115200);

        // Button 클릭 이벤트 핸들러 등록
        startButton.setOnAction(event -> onControlButtonClick());
    }

    private void onControlButtonClick() {
        if (isRunning) {
            // 백그라운드 스레드 중지
            stopBackgroundThread();

            
            // UI 업데이트
            startButton.setText("Start");
        } else {
            // 선택된 값을 가져와서 시리얼 통신을 시작하도록 구현
            String selectedSerial1 = serial1.getValue();
            Integer selectedBaudrate1 = baudrate1.getValue();
            
            // 값이 선택되었는지 확인
            if (selectedSerial1 != null && selectedBaudrate1 != null) {
                selectedBaudrate1 = selectedBaudrate1.intValue();
                System.out.println("Selected Serial: " + selectedSerial1);
                System.out.println("Selected Baudrate: " + selectedBaudrate1);

                // RTCMRequest 객체 생성 및 초기화
                rtcMRequest = new RTCMRequest();
                rtcMRequest.setSerialport(selectedSerial1, selectedBaudrate1);
                

                rtcMRequest.sendRTCM();
                rtcMRequest.sendSurveyin();

                // CheckBox 상태에 따라 추가적인 동작 수행
                boolean isDataCheckBoxSelected = dataCheckBox.isSelected();

                // 나머지 로직 수행
                startBackgroundThread(isDataCheckBoxSelected);

                // UI 업데이트
                startButton.setText("Stop");
            } else {
                System.out.println("Please select Serial and Baudrate.");
            }
        }
        // 상태 업데이트
        isRunning = !isRunning;
    }

    private void startBackgroundThread(boolean includeData) {
        
    	backgroundThread = new Thread(() -> {
    		
    		try {
  
                while (isRunning) {
                	String receivedData;
                	// 수신된 데이터 처리
                    receivedData = rtcMRequest.readData();
                    
                 //   System.out.println("ReceivedData: " + receivedData);
                    Platform.runLater(() -> dataLabel.setText(receivedData));
                    // 잠시 쉬었다가 다음 데이터 수신을 위해 계속 진행
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // (옵션) 다시 interrupt 상태로 만들거나 필요에 따라 예외를 무시
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
            // Serial 포트 닫기
            if (rtcMRequest != null) {
                rtcMRequest.closePort();
            }
        }
    }
}
