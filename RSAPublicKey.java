// RSAPublicKey: RSA public key
import java.math.*; // for BigInteger
public class RSAPublicKey { 
   public BigInteger n; // public modulus
   public BigInteger e = new BigInteger("3"); // encryption exponent
   public String userName; // attach name to each public/private key pair

   public RSAPublicKey(String name) {
      userName = name;
   }

   // setN: to give n a value in case only have public key
   public void setN(BigInteger newN) {
      n = newN;
   }

   // getN: provide n
   public BigInteger getN() {
      return n;
   }

   // RSAEncrypt: just raise m to power e (3) mod n
   public BigInteger RSAEncrypt(BigInteger m) {
      return m.modPow(e, n);
   }

   // RSAVerify: same as encryption, since RSA is symmetric
   public BigInteger RSAVerify(BigInteger s) {
      return s.modPow(e, n);
   }
}