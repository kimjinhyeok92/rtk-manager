package application;

public class ChecksumCalculator {

	
    public static byte[] calculateChecksum(byte[] data) {
        byte CK_A = 0;
        byte CK_B = 0;

        for (int i = 2; i < data.length; i++) {            
            CK_A = (byte) ((CK_A + data[i]) & 0xff);
            CK_B = (byte) ((CK_B + CK_A) & 0xff);
        }
               		
        // 계산된 체크섬 값을 byte 배열로 반환합니다.
        return new byte[]{CK_A , CK_B};
    }


}