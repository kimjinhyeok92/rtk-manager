/*
 * 데이터 length가 180이 넘었을때 잘라서 저장하는지 확인하기위한 클래스
 * 
 * 
 */
//package application;
//
//import java.util.Random;
//
//public class RTKDataSimulator {
//    public static byte[] main(String[] args) {
//        while (true) {
//            // Generate 200 bytes of random binary data
//            byte[] binaryData = generateRandomBinaryData(200);
//
//            // Process the incoming data using your parsing code
//            processIncomingData(binaryData);
//
//            try {
//                // Simulate some delay between data packets (adjust as needed)
//                Thread.sleep(1000); // 1 second
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return binaryData;
//        }
//       
//    }
//
//    public static void processIncomingData(byte[] data) {
//        // Add your parsing code here to handle the incoming binary data
//        // For demonstration, just printing the hexadecimal representation
//        System.out.print("Received Data: ");
//        for (byte b : data) {
//            System.out.print(String.format("%02X ", b));
//        }
//        System.out.println();
//    }
//
//    public static byte[] generateRandomBinaryData(int length) {
//        Random random = new Random();
//        byte[] data = new byte[length];
//        random.nextBytes(data);
//        return data;
//    }
//}


