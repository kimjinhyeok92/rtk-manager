package application;

public class MavlinkStream {

    private static StringBuilder incompleteRtcmData;

    public MavlinkStream() {
        this.incompleteRtcmData = new StringBuilder();
    }

    static void processIncomingData(byte[] data) {
        try {
            int rtcmSizeThreshold = 180;

            for (byte b : data) {
                incompleteRtcmData.append(String.format("%02X ", b));

                if (incompleteRtcmData.length() >= rtcmSizeThreshold) {
                    // 180바이트까지만 저장
                    processCompleteRtcmData(incompleteRtcmData.substring(0, rtcmSizeThreshold));

                    // StringBuilder 초기화
                    incompleteRtcmData.setLength(0);
                }
            }
        } catch (Exception e) {
            handleException("들어오는 데이터 처리 중 오류 발생", e);
        }
    }

    public static void processCompleteRtcmData(String rtcmData) {
        // 여기에서 완전한 RTCM 데이터를 저장하거나 처리하는 논리를 구현합니다.
        // 예를 들어 파일에 저장하거나 다른 모듈로 보낼 수 있습니다.
        System.out.println("완전한 RTCM 데이터: " + rtcmData);
    }

    private static void handleException(String message, Exception e) {
        e.printStackTrace();
        System.err.println(message + ": " + e.getMessage());
    }
}
