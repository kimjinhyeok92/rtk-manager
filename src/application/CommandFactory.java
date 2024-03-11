package application;

public class CommandFactory {

    public static byte[] RTCM1005() {
        // RTCM 데이터 스트림을 활성화하는 UBX 명령 생성
        return new byte[]{
                (byte) 0xB5, (byte) 0x62, // UBX Sync Characters
                (byte) 0x06, (byte) 0x01, // UBX Class: CFG (Configuration),UBX Message ID: MSG (Message)
                (byte) 0x08, (byte) 0x00, // payload length
                (byte) 0xF5, (byte) 0x05, // Message ID: RTCM 1005
                (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x01,
                (byte) 0x00, (byte) 0x00,
           
               
                // Checksum (2 bytes) will be added later
               
        };
    }

    public static byte[] RTCM1077() {
        // RTCM 데이터 스트림을 활성화하는 UBX 명령 생성
        return new byte[]{
                (byte) 0xB5, (byte) 0x62, // UBX Sync Characters
                (byte) 0x06, (byte) 0x01, // UBX Class: CFG (Configuration),UBX Message ID: MSG (Message)
                (byte) 0x08, (byte) 0x00, // payload length
                (byte) 0xF5, (byte) 0x4D, // Message ID: RTCM 1077
                (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x01,
                (byte) 0x00, (byte) 0x00,
           
               
                // Checksum (2 bytes) will be added later
               
        };
    }
    
    public static byte[] RTCM1087() {
        // RTCM 데이터 스트림을 활성화하는 UBX 명령 생성
        return new byte[]{
                (byte) 0xB5, (byte) 0x62, // UBX Sync Characters
                (byte) 0x06, (byte) 0x01, // UBX Class: CFG (Configuration),UBX Message ID: MSG (Message)
                (byte) 0x08, (byte) 0x00, // payload length
                (byte) 0xF5, (byte) 0x57, // Message ID: RTCM 1087
                (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x01,
                (byte) 0x00, (byte) 0x00,
            
               
                // Checksum (2 bytes) will be added later
               
        };
    }
    
    public static byte[] RTCM1230() {
        // RTCM 데이터 스트림을 활성화하는 UBX 명령 생성
        return new byte[]{
                (byte) 0xB5, (byte) 0x62, // UBX Sync Characters
                (byte) 0x06, (byte) 0x01, // UBX Class: CFG (Configuration),UBX Message ID: MSG (Message)
                (byte) 0x08, (byte) 0x00, // payload length
                (byte) 0xF5, (byte) 0xE6, // Message ID: RTCM 1230
                (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x01,
                (byte) 0x00, (byte) 0x00,
           
                // Checksum (2 bytes) will be added later
               
        };
    }
    
    public static byte[] RTCM1097() {
        // RTCM 데이터 스트림을 활성화하는 UBX 명령 생성
        return new byte[]{
                (byte) 0xB5, (byte) 0x62, // UBX Sync Characters
                (byte) 0x06, (byte) 0x01, // UBX Class: CFG (Configuration),UBX Message ID: MSG (Message)
                (byte) 0x08, (byte) 0x00, // payload length
                (byte) 0xF5, (byte) 0x61, // Message ID: RTCM 1097
                (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x01,
                (byte) 0x00, (byte) 0x00,
             
               
                // Checksum (2 bytes) will be added later
               
        };
    }
    
    public static byte[] RTCM1127() {
        // RTCM 데이터 스트림을 활성화하는 UBX 명령 생성
        return new byte[]{
                (byte) 0xB5, (byte) 0x62, // UBX Sync Characters
                (byte) 0x06, (byte) 0x01, // UBX Class: CFG (Configuration),UBX Message ID: MSG (Message)
                (byte) 0x08, (byte) 0x00, // payload length
                (byte) 0xF5, (byte) 0x7F, // Message ID: RTCM 1127
                (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x01,
                (byte) 0x00, (byte) 0x00,
            
               
                // Checksum (2 bytes) will be added later
               
        };
    }
    
    
    public static byte[] SurveyinStc() {
        return new byte[]{
                (byte) 0xB5, (byte) 0x62, // UBX Sync Characters
                (byte) 0x06, (byte) 0x01, // UBX Class: CFG (Configuration), UBX Message ID: MSG (Message)
                (byte) 0x08, (byte) 0x00, // payload length
                (byte) 0x01, (byte) 0x3B, // Message ID: survey-in
                (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x01,
                (byte) 0x00, (byte) 0x00,
             //   (byte) 0x00, (byte) 0x00,
                
              //  (byte) 0x4C, (byte) 0x4B,
                
                // 체크섬 (2 바이트)는 나중에 추가될 것입니다
        };
    }  
}
