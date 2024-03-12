package application;

import java.util.Arrays;

public class MavlinkStream {

    private static StringBuilder incompleteRtcmData;

   // private static RTKDataSimulator dataSim = new RTKDataSimulator();
    
    public MavlinkStream() {
        MavlinkStream.setIncompleteRtcmData(new StringBuilder());
    }
   

    static void processIncomingData(byte[] data) {
        try {
        //	byte[] data1 = RTKDataSimulator.main(null);
            int rtcmSizeThreshold = 180;
            System.out.println("input Bytes : " + data.length);

            for (int i = 0; i < data.length; i += rtcmSizeThreshold) {
                int endIndex = Math.min(i + rtcmSizeThreshold, data.length);
                byte[] chunk = Arrays.copyOfRange(data, i, endIndex);
                
                processCompleteRtcmData(chunk);
            }
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
