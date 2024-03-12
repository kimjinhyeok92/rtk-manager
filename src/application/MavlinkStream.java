package application;

import java.util.Arrays;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.common.msg_gps_rtcm_data;

public class MavlinkStream {

    private static StringBuilder incompleteRtcmData;

   // private static RTKDataSimulator dataSim = new RTKDataSimulator();
    
    public MavlinkStream() {
        MavlinkStream.setIncompleteRtcmData(new StringBuilder());
    }
   

    static void processIncomingData(byte[] rtcmData) {
        try {
        	
        	// msg_gps_rtcm_data 클래스의 인스턴스 생성
            msg_gps_rtcm_data rtcmMessage = new msg_gps_rtcm_data();
                        
            short receivedFlags = 1;
            short[] receivedData = new short[]{0xAA, 0xBB, 0xCC, 0xDD, 0xEE, 0xFF, 0x11, 0x22};
            short receivedLen = (short) receivedData.length;
            
            
            rtcmMessage.flags = receivedFlags;
            rtcmMessage.len = receivedLen;
            rtcmMessage.data = receivedData;

            rtcmMessage.sysid = 42;
            rtcmMessage.compid = 0;
            rtcmMessage.isMavlink2 = true;

         // 위 변수 페이로드 패킹한다
            MAVLinkPacket payloadPacket = rtcmMessage.pack();
            
        //    byte[] mavlinkpacket = payloadPacket.encodePacket();
           
         // MAVLinkPacket 출력 시 패킷 내용을 확인할 수 있도록 수정
            System.out.println("패키지 완료 : " + payloadPacket.unpack());
            
          //  System.out.println("패키지 완료 : " + mavlinkpacket);
            for (byte b : payloadPacket.encodePacket()) {
                System.out.print(String.format("%02X ", b));
            }
       

            
        //	byte[] data1 = RTKDataSimulator.main(null);
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

    public static void processCompleteRtcmData(byte[] rtcmData) {
        // Here, you can implement logic to store or process each chunk of RTCM data.
        // For demonstration purposes, we print the chunk as a hexadecimal string.
        System.out.print("Complete RTCM Data: ");
        for (byte b : rtcmData) {
            System.out.print(String.format("%02X ", b));
        }
        System.out.println();

        System.out.println("Bytes Written : " + rtcmData.length);
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
}
