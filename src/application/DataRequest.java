package application;

import com.fazecast.jSerialComm.SerialPort;

import java.util.Arrays;

public class DataRequest {

    private SerialPort comPort;

    public void setSerialport(String selectedSerial, int selectedBaudrate) {
        try {
            this.comPort = SerialPort.getCommPort(selectedSerial);
            this.comPort.setBaudRate(selectedBaudrate);

            if (comPort.openPort()) {
                System.out.println("Serial port successfully opened.");
            } else {
                System.err.println("Error opening the serial port.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error creating SerialPort: " + e.getMessage());
        }
    }

    
 
 // sendRTCM 메서드
    public void sendRTCM() {
        try {
            if (isSerialPortOpen()) {
                byte[] RTCMStc = CommandFactory.RTCMStc();
                sendCommandWithChecksum("RTCM", RTCMStc);
            } else {
                handleClosedSerialPortError();
            }
        } catch (Exception e) {
            handleException("RTCM 호출 중 예외 발생", e);
        }
    }

    // sendSurveyin 메서드
    public void sendSurveyin() {
        try {
            if (isSerialPortOpen()) {
                byte[] surveyinStc = CommandFactory.SurveyinStc();
                sendCommandWithChecksum("Surveyin", surveyinStc);
            } else {
                handleClosedSerialPortError();
            }
        } catch (Exception e) {
            handleException("Surveyin 호출 중 오류 발생", e);
        }
    }

    private void sendCommandWithChecksum(String commandType, byte[] command) {
        byte[] calculatedChecksum = ChecksumCalculator.calculateChecksum(command);
        byte[] commandWithChecksum = Arrays.copyOf(command, command.length + 2);
        
        System.arraycopy(calculatedChecksum, 0, commandWithChecksum, command.length, 2);
        
        printChecksumAndData(commandType, calculatedChecksum, commandWithChecksum);

        int bytesWritten = comPort.writeBytes(commandWithChecksum, commandWithChecksum.length);
        System.out.println("Bytes Written (" + commandType + "): " + bytesWritten);

        System.out.println(commandType + " 데이터 전송 완료\n");
    }

    private void printChecksumAndData(String commandType, byte[] calculatedChecksum, byte[] commandWithChecksum) {
        System.out.println("Checksum (" + commandType + "): " +
                String.format("%02X ", calculatedChecksum[0]) +
                String.format("%02X ", calculatedChecksum[1]));

        System.out.println("Sending Data (" + commandType + "): " +
                String.format("%02X ", commandWithChecksum[commandWithChecksum.length - 2]) +
                String.format("%02X ", commandWithChecksum[commandWithChecksum.length - 1]));
    }

    private void handleException(String message, Exception e) {
        e.printStackTrace();
        System.err.println(message + ": " + e.getMessage());
    }

    private boolean isSerialPortOpen() {
        return comPort != null && comPort.isOpen();
    }

    private void handleClosedSerialPortError() {
        System.err.println("시리얼 포트가 열려있지 않습니다.");
    }

    public String readData() {
        StringBuilder receivedData = new StringBuilder();
        try {
            while (comPort.bytesAvailable() > 0) {
                byte[] readBuffer = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(readBuffer, readBuffer.length);
                receivedData.append("data size : ").append(numRead).append("byte\n");
                parseData(readBuffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while dividing tokens: " + e.getMessage());
        }
        return receivedData.toString();
    }
    

 // parseRTCM, parseUBX, parseNMEA 메서드 
 private void parseRTCM(byte[] token) {
     System.out.print("RTCM : ");
     for (byte b : token) {
         System.out.print(String.format("%02X ", b));
     }
     System.out.println();
 }

 private void parseUBX(byte[] token) {
     System.out.print("UBX : ");
     for (byte b : token) {
         System.out.print(String.format("%02X ", b));
     }
     System.out.println();
 }

 private void parseNMEA(byte[] token) {
     System.out.print("NMEA : ");
     for (byte b : token) {
         System.out.print(String.format("%02X ", b));
     }
     System.out.println();
 }


	private void parseData(byte[] data) {
	    int type = 0;
	    int offset = 0;
	    byte[] token = null; // 토큰을 초기화합니다.

	    for (int i = 1; i < data.length; i++) {
	        if (data[i - 1] == (byte) 0xB5 && data[i] == (byte) 0x62) { // UBX
	            if (type != 0) {
	                token = Arrays.copyOfRange(data, offset, i - 1);
	                processToken(type, token);
	            }
	            System.out.println(" ");
	            System.out.println(i + " : UBX  ");
	            type = 2;
	            offset = i - 1;
	        }

	        if (data[i - 1] == (byte) 0x24 && data[i] == (byte) 0x47) { // NMEA
	            System.out.println(" ");
	            System.out.println(i + " : NMEA  ");
	            type = 1;
	            offset = i - 1;
	        }

	        if (data[i - 1] == (byte) 0xD3) { // RTCM
	            System.out.println(i + " : RTCM  ");
	            type = 3;
	            offset = i - 1;
	        }
	    }

	    if (type != 0) {
	        token = Arrays.copyOfRange(data, offset, data.length);
	        processToken(type, token);
	    }
	}

	private void processToken(int type, byte[] token) {
	    switch (type) {
	        case 1:
	            parseNMEA(token);
	            break;
	        case 2:
	            parseUBX(token);
	            break;
	        case 3:
	            parseRTCM(token);
	            break;
	        // Add more cases if needed
	    }
	}


	public void closePort() {
        if (comPort != null && comPort.isOpen()) {
            comPort.closePort();
            System.out.println("시리얼 포트가 닫혔습니다.");
        }
    }
	}



	
