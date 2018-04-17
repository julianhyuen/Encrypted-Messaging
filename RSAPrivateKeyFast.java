// RSAPrivateKeyFast: RSA private key, using fast CRT algorithm
import java.math.*; // for BigInteger
import java.util.*; // for Random
public class RSAPrivateKeyFast extends RSAPublicKey{ 
   private final BigInteger TWO = new BigInteger("2");
   private final BigInteger THREE = new BigInteger("3");

   private BigInteger p; // first prime
   private BigInteger q; // second prime
   private BigInteger d; // decryption exponent

   private BigInteger p1, pM1, q1, qM1, phiN; // for key generation
   private BigInteger dp, dq, c2; // for fast decryption

   public RSAPrivateKeyFast(int size, Random rnd, String name) {
      super(name); generateKeyPair(size, rnd);
   }

   public void generateKeyPair(int size, Random rnd) { // size = n in bits
      // want sizes of primes close, but not too close. Here 10-20 bits apart.
      int size1 = size/2;
      int size2 = size1;
      int offset1 = (int)(5.0*(rnd.nextDouble()) + 5.0);
      int offset2 = -offset1;
      if (rnd.nextDouble() < 0.5) {
         offset1 = -offset1; offset2 = -offset2;
      }
      size1 += offset1; size2 += offset2;
      // generate two random primes, so that p*q = n has size bits
      p1   = new BigInteger(size1, rnd); // random int
      p    = nextPrime(p1);
      pM1  = p.subtract(BigInteger.ONE);
      q1   = new BigInteger(size2, rnd);
      q    = nextPrime(q1);
      qM1  = q.subtract(BigInteger.ONE);
      n    = p.multiply(q);
      phiN = pM1.multiply(qM1); // (p-1)*(q-1)
      d    = e.modInverse(phiN);
      // remaining stuff needed for fast CRT decryption
      dp = d.remainder(pM1);
      dq = d.remainder(qM1);
      c2 = p.modInverse(q);
   }

   // nextPrime: next prime p after x, with p-1 and 3 relatively prime
   public BigInteger nextPrime(BigInteger x) {
      if ((x.remainder(TWO)).equals(BigInteger.ZERO))
         x = x.add(BigInteger.ONE);
      while(true) {
         BigInteger xM1 = x.subtract(BigInteger.ONE);
         if (!(xM1.remainder(THREE)).equals(BigInteger.ZERO))
            if (x.isProbablePrime(10)) break;
         x = x.add(TWO);
      }
      return x;
   }

   // RSADecrypt: decryption function, fast CRT version
   public BigInteger RSADecrypt(BigInteger c) {
      // See 14.71 and 14.75 in Handbook of Applied Cryptography, by 
      //    Menezes, van Oorschot and Vanstone
      // return c.modPow(d, n);
      BigInteger cDp = c.modPow(dp, p);
      BigInteger cDq = c.modPow(dq, q);
      BigInteger u = ((cDq.subtract(cDp)).multiply(c2)).remainder(q);
      if (u.compareTo(BigInteger.ZERO) < 0) u = u.add(q);
      return cDp.add(u.multiply(p));
   }

   // RSASign: same as decryption for RSA (since it is a symmetric PKC)
   public BigInteger RSASign(BigInteger m) {
      // return m.modPow(d, n);
      return RSADecrypt(m); // use fast CRT version
   }

   public BigInteger RSASignAndEncrypt(BigInteger m, RSAPublicKey other) {
      // two ways to go, depending on sizes of n and other.getN()
      if (n.compareTo(other.getN()) > 0)
          return RSASign(other.RSAEncrypt(m));
      else
          return other.RSAEncrypt(RSASign(m));
   }

   public BigInteger RSADecryptAndVerify(BigInteger c,
                                         RSAPrivateKeyFast other) {
      // two ways to go, depending on sizes of n and other.getN()
      if (n.compareTo(other.getN()) > 0)
          return other.RSAVerify(RSADecrypt(c));
      else
          return RSADecrypt(other.RSAVerify(c));
   }
}