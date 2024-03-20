package application;


import com.fazecast.jSerialComm.SerialPort;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.common.msg_gps_rtcm_data;

public class MavlinkStream {
	
	private static StringBuilder incompleteRtcmData;
    private byte[] mavlinkpacket;
    private SerialPort comPort2;
   // private static RTKDataSimulator dataSim = new RTKDataSimulator();
    
    public MavlinkStream() {
        MavlinkStream.setIncompleteRtcmData(new StringBuilder());
    }
   
    

    public void setSerialport(String selectedSerial2, int selectedBaudrate2) {
        try {
            this.comPort2 = SerialPort.getCommPort(selectedSerial2);
            this.comPort2.setBaudRate(selectedBaudrate2);

            if (comPort2.openPort()) {
                System.out.println("Serial2 port successfully opened.");
            } else {
                System.err.println("Error opening the serial2 port.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error creating SerialPort: " + e.getMessage());
        }
    }

    public void processIncomingData(byte[] rtcmData) {
        try {        	
        	// msg_gps_rtcm_data 클래스의 인스턴스 생성
            msg_gps_rtcm_data rtcmMessage = new msg_gps_rtcm_data();
            
            byte _sequenceId = 0;
            
            //short receivedFlags = 1;
         //   short[] receivedData = new short[]{0xAA, 0xBB, 0xCC, 0xDD, 0xEE, 0xFF, 0x11, 0x22};
            short receivedLen = (short) rtcmData.length; // RTCM 데이터의 길이를 설정
                       
            rtcmMessage.flags = (byte) ((_sequenceId & 0x1F) << 3);

            rtcmMessage.len = receivedLen;
            rtcmMessage.data = new short[rtcmData.length]; // 바이트 배열을 short 배열로 변환
            
            // 바이트 배열을 short 배열로 변환
            for (int i = 0; i < rtcmData.length; i++) {
            	
            	rtcmMessage.data[i] = (short) (rtcmData[i] & 0xFF); // 부호 확장을 피하기 위해 0xFF와의 비트 AND 연산을 수행
            	System.out.print(String.format("%02X ", rtcmMessage.data[i]));
           }
            // D3 00 04 4C E0 00 80 ED ED D6
            
            rtcmMessage.sysid = 0;
            rtcmMessage.compid = 0;
            rtcmMessage.isMavlink2 = false;

         // 위 변수 페이로드 패킹한다
            MAVLinkPacket payloadPacket = rtcmMessage.pack();         
                 
                      
         // MAVLinkPacket 출력 시 패킷 내용을 확인할 수 있도록 수정
            System.out.println("mavlink 언팩킹 완료 : " + payloadPacket.unpack());           
//            System.out.println("mavlink 팩킹 완료 : " + payloadPacket.encodePacket());
            // MAVLINK_MSG_ID_GPS_RTCM_DATA - sysid:0 compid:0 flags:0 len:10 data:D300044CE00080EDEDD6
            
            mavlinkpacket = payloadPacket.encodePacket();
            // FD 0C 00 00 00 00 00 E9 00 00 00 0A D3 00 04 4C E0 00 80 ED ED D6 7D 1D
            //MavpacketDatatoLora(payloadPacket.encodePacket());
            for (byte b : payloadPacket.encodePacket()) {
                System.out.print(String.format("%02X ", b));
            }
            System.out.println(" ");
         
           
//			  byte[] data1 = RTKDataSimulator.main(null);
//            int rtcmSizeThreshold = 180;
//            System.out.println("input Bytes : " + data.length);
//
//            for (int i = 0; i < data.length; i += rtcmSizeThreshold) {
//                int endIndex = Math.min(i + rtcmSizeThreshold, data.length);
//                byte[] chunk = Arrays.copyOfRange(data, i, endIndex);
//                
//                processCompleteRtcmData(chunk);
//            }
        } catch (Exception e) {
            handleException("Error processing incoming data", e);
        }   
    } 
    
   
    
    public void MavpacketDatatoLora() {
    	try {
    		if(isSerialPortOpen()) {
    			int bytesWritten = comPort2.writeBytes(mavlinkpacket, mavlinkpacket.length);
    			System.out.printf("Lora로 데이터 전송 완료"+ " "+bytesWritten);
    			 for (byte b : mavlinkpacket) {
    	                System.out.print(String.format("%02X ", b));
    	            }
    		
    			
    		}
    	}catch (Exception e) {
                handleException("Lora전송 중 오류 발생", e);
    	}
 
    }
    
    public int sendToLora() {
        final int LORA_PACKET_MAX = 240;
        final int LORA_PAYLOAD = 237;
        final int LORA_CHANNEL = 72;
        final byte[] LORA_HEADER = { (byte) 0xFF, (byte) 0xFF, (byte) LORA_CHANNEL };

        byte[] lora_buffer = new byte[LORA_PAYLOAD];
        byte[] packet = new byte[LORA_PACKET_MAX];


        int len = mavlinkpacket.length;
        System.arraycopy(LORA_HEADER, 0, packet, 0, LORA_HEADER.length);
        // [(byte) 0xFF, (byte) 0xFF, (byte) LORA_CHANNEL,]
        System.arraycopy(lora_buffer, 0, packet, LORA_HEADER.length, len);
        // [(byte) 0xFF, (byte) 0xFF, (byte) LORA_CHANNEL, FD 0C 00 00 00 00 00 E9 00 00 00 0A D3 00 04 4C E0 00 80 ED ED D6 7D 1D ]
        
        int ret = comPort2.writeBytes(packet, 0, len + LORA_HEADER.length);
        
        //long now = System.currentTimeMillis();

        if (ret < 0) {
            System.err.println("Failed to write to serial port");
        }
        return 0;
    }
    
//    public static void processCompleteRtcmData(byte[] rtcmData) {
//        // Here, you can implement logic to store or process each chunk of RTCM data.
//        // For demonstration purposes, we print the chunk as a hexadecimal string.
//        System.out.print("Complete RTCM Data: ");
//        for (byte b : rtcmData) {
//            System.out.print(String.format("%02X ", b));
//        }
//        System.out.println();
//        System.out.println("Bytes Written : " + rtcmData.length);
//    }

    
    private boolean isSerialPortOpen() {
        return comPort2 != null && comPort2.isOpen();
    }
    
    private static void handleException(String message, Exception e) {
        e.printStackTrace();
        System.err.println(message + ": " + e.getMessage());
    }
   
	public static StringBuilder getIncompleteRtcmData() {
		return incompleteRtcmData;
	}

	public static void setIncompleteRtcmData(StringBuilder incompleteRtcmData) {
		MavlinkStream.incompleteRtcmData = incompleteRtcmData;
	}
	
	public void closePort() {
        if (comPort2 != null && comPort2.isOpen()) {
            comPort2.closePort();
            System.out.println("시리얼 포트가 닫혔습니다.");
        }
    }
}