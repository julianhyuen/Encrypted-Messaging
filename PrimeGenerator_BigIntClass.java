/* PrimeGenerator.java
* Copyright (c) 2013 by Dr. Herong Yang, herongyang.com
*/
import java.math.BigInteger;
import java.util.Random;
class PrimeGenerator_BigIntClass {
public static void main(String[] a) {
if (a.length<2) {
System.out.println("Nhap vao so bits cho so nguyen to va do tin cay.");
return;
}
int length = Integer.parseInt(a[0]);
int certainty = Integer.parseInt(a[1]);
Random rnd = new Random();

long t1 = System.currentTimeMillis();
BigInteger p = new BigInteger(length,certainty,rnd);
long t2 = System.currentTimeMillis();
boolean ok = p.isProbablePrime(certainty);
long t3 = System.currentTimeMillis();

String s1 = p.toString(2);
System.out.println("So dang binary: "+s1);
	
BigInteger two = new BigInteger("2");
System.out.println("So BigInteger co kha nang la SNT: \n"+p);
System.out.println("So nay co phai la SNT khong?(T/F): "+ok);
System.out.println("So bit: "+length);
System.out.println("Do tin cay: "+certainty);
System.out.println("Xac suat (%): "
+(100.0-100.0/(two.pow(certainty)).doubleValue()));
System.out.println("Thoi gian tao SNT (milliseconds): "+(t2-t1));
System.out.println("Thoi gian xac dinh SNT (milliseconds): "+(t3-t2));
}
}