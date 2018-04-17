// RSATestFast: Test Fast RSA Implementation
import java.math.*; // for BigInteger
import java.util.*; // for Random
import java.security.SecureRandom;
import java.io.FileInputStream;
import java.security.MessageDigest;
public class RSATestFast {
 
   public static void elapsedTime(long startTime) {
      long stopTime = System.currentTimeMillis();
      double elapsedTime = ((double)(stopTime - startTime));
      System.out.println("Thoi gian thuc hien: " + elapsedTime + " milliseconds");
   }

   public static void main(String[] args) {
	
      Random rnd = new Random();
      BigInteger m, m1, m2, m3, c, s, s1;
      RSAPrivateKeyFast alice = new RSAPrivateKeyFast(1024, rnd, "Alice");
      RSAPrivateKeyFast bob   = new RSAPrivateKeyFast(1024, rnd, "Bob  ");
	   
	   String sm1 = "Using RSA to sign message";
	   byte[] bytes = sm1.getBytes();
	   
	   m = new BigInteger(bytes);
	   /*System.out.println("Message can gui dang text: "+sm1+"\n");
	   String sm = m.toString(2);
	   System.out.println("Message can gui dang binary: "+sm);
	   System.out.println("Message can gui dang BigInteger: "+m+"\n");*/
	   
	  long startTime = System.currentTimeMillis();
	   
      c = bob.RSAEncrypt(m); // Using Bob's public key
      //System.out.println("Ma hoa message su dung public key:\n" + c + "\n");
	
      m1 = bob.RSADecrypt(c); // Using Bob's private key
      //System.out.println("Giai ma message su dung private key:\n" + m1 + "\n");
	   
	   

	   /*MessageDigest md = MessageDigest.getInstance("SHA-256");
	   FileInputStream fis = new FileInputStream("/Users/vannguyen/Dropbox/Msc. HCMUS/HK2 - Ma Hoa Mat Ma Chuyen Sau - PGS TS Nguyen Dinh Thuc/Labwork/RSA/RSA_CRT.txt");
	   
	   byte[] dataBytes = new byte[1024];
	   
	   int nread = 0;
	   while ((nread = fis.read(dataBytes)) != -1) {
		   md.update(dataBytes, 0, nread);
	   };
	   byte[] mdbytes = md.digest();
	   */
	   
	   //convert the byte to hex format method 1
	   StringBuffer sb = new StringBuffer();
	   for (int i = 0; i < bytes.length; i++) {
		   sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	   }
	   
	   System.out.println("Hex format : " + sb.toString());
	   
	   
	   
	  System.out.println("ALICE SIGNS m FOR BOB; BOB VERIFIES SIGNATURE:");
      s = alice.RSASign(m); // Using Alice's private key
      System.out.println("Message signed with Alice's private key:\n" +
         s + "\n");
	   
      m2 = alice.RSAVerify(s); // Using Alice's public key
      System.out.println("Original message back, verified:\n" + m2 + "\n");

      System.out.println("BOB SIGNS AND ENCRYPTS m FOR ALICE;" +
         "\n   ALICE VERIFIES SIGNATURE AND DECRYPTS:");
      c  = bob.RSASignAndEncrypt(m2, alice);
      System.out.println("Message signed and encrypted," +
         "\n  using Bob's secret key and Alice's public key:\n" + c + "\n");
      m3 = alice.RSADecryptAndVerify(c, bob);
      System.out.println("Original message back, verified and decrypted," +
         "\n  using Alice's secret key and Bob's public key:\n" + m3);
 
	   byte[] decmess = m1.toByteArray();
	   String str1 = new String(decmess);
	   System.out.println("Message da duoc chuyen lai dang text: "+str1);
	   elapsedTime(startTime);

   }
}