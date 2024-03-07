package application;

import java.util.Arrays;


import com.fazecast.jSerialComm.*;




public class RTCMRequest {

    private SerialPort comPort;
    


    public void setSerialport(String selectedSerial1, int selectedBaudrateValue1) {
        try {
            this.comPort = SerialPort.getCommPort(selectedSerial1);
            this.comPort.setBaudRate(selectedBaudrateValue1);

            // 포트를 사용하기 전에 열기
            if (comPort.openPort()) {
                System.out.println("시리얼 포트가 성공적으로 열렸습니다.");
            } else {
                System.err.println("시리얼 포트를 열던 중 오류가 발생했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("SerialPort 생성 중 오류 발생: " + e.getMessage());
        }
    }

    
 
 // sendRTCM 메서드
    public void sendRTCM() {
        try {
            if (comPort != null && comPort.isOpen()) {
                byte[] RTCMStc = StreamCommandFactory.RTCMStc();

                // Calculate checksum
                byte[] calculatedChecksum = ChecksumCalculator.calculateChecksum(RTCMStc);
                
//                for (byte b : calculatedChecksum) {
//                    System.out.printf("%02X ", b);
//                }
//                System.out.println();
                
                // Add checksum to the data
                byte[] RTCMStcWithChecksum = new byte[RTCMStc.length + 2];
                System.arraycopy(RTCMStc, 0, RTCMStcWithChecksum, 0, RTCMStc.length);
                RTCMStcWithChecksum[RTCMStcWithChecksum.length - 2] = calculatedChecksum[0];
                RTCMStcWithChecksum[RTCMStcWithChecksum.length - 1] = calculatedChecksum[1];
                
                for (byte b : RTCMStcWithChecksum) {
                    System.out.printf("%02X ", b);
                }
                System.out.println();
                
                System.out.println("Checksum (RTCM): " +
                        String.format("%02X ", calculatedChecksum[0]) +
                        String.format("%02X ", calculatedChecksum[1]));
                System.out.println("sendingData (RTCM): " +
                        String.format("%02X ", RTCMStcWithChecksum[6]) +
                        String.format("%02X ", RTCMStcWithChecksum[7]));

              //  int bytesWritten = comPort.writeBytes(RTCMStc, RTCMStc.length);
                int bytesWritten = comPort.writeBytes(RTCMStcWithChecksum, RTCMStcWithChecksum.length);
                System.out.println("Bytes Written (RTCM): " + bytesWritten);

                System.out.println("RTCM 데이터 전송 완료");
                System.out.println();
            } else {
                System.err.println("시리얼 포트가 열려있지 않습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("RTCM 호출 중 예외가 발생했습니다: " + e.getMessage());
        }
    }

 // sendSurveyin 메서드
    public void sendSurveyin() {
        try {
            if (comPort != null && comPort.isOpen()) {
                byte[] surveyinStc = StreamCommandFactory.SurveyinStc();

                // Calculate checksum
                byte[] calculatedChecksum = ChecksumCalculator.calculateChecksum(surveyinStc);

                
//                for (byte b : calculatedChecksum) {
//                    System.out.printf("%02X ", b);
//                }
//                System.out.println();
                
                // Add checksum to the data
                byte[] surveyinStcWithChecksum = new byte[surveyinStc.length + 2];
                System.arraycopy(surveyinStc, 0, surveyinStcWithChecksum, 0, surveyinStc.length);
                surveyinStcWithChecksum[surveyinStcWithChecksum.length - 2] = calculatedChecksum[0];
                surveyinStcWithChecksum[surveyinStcWithChecksum.length - 1] = calculatedChecksum[1];
                
                for (byte b : surveyinStcWithChecksum) {
                    System.out.printf("%02X ", b);
                }
                System.out.println();

                
                
                
                System.out.println("Checksum (Surveyin): " +
                        String.format("%02X ", calculatedChecksum[0]) +
                        String.format("%02X ", calculatedChecksum[1]));
                System.out.println("sendingData (Surveyin): " +
                        String.format("%02X ", surveyinStcWithChecksum[6]) +
                        String.format("%02X ", surveyinStcWithChecksum[7]));

                //int bytesWritten = comPort.writeBytes(surveyinStc, surveyinStc.length);
                int bytesWritten = comPort.writeBytes(surveyinStcWithChecksum, surveyinStcWithChecksum.length);
                System.out.println("Bytes Written (Surveyin): " + bytesWritten);

                System.out.println("Surveyin 데이터 전송 완료");
                System.out.println();
            } else {
                System.err.println("시리얼 포트가 열려있지 않습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Surveyin 호출 중 오류가 발생했습니다." + e.getMessage());
        }
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

 public String readData() {
	    StringBuilder receivedData = new StringBuilder(); // 수신된 데이터를 저장할 StringBuilder

	    try {
	        while (comPort.bytesAvailable() > 0) {
	            byte[] readBuffer = new byte[comPort.bytesAvailable()];
	            int numRead = comPort.readBytes(readBuffer, readBuffer.length);
	        
	    //        System.out.println("Received data: " + Arrays.toString(readBuffer));

	           
                receivedData.append("data size : ").append(numRead).append("byte\n");
                
               
                
	            parseData(readBuffer); // 수정된 부분
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.err.println("token을 나누던 중 오류가 발생했습니다." + e.getMessage());
	    }

	    return receivedData.toString();
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
	
