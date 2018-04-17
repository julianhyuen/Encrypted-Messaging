/* Written by:
 Kenny C.K. Fong, Jonathan M. Hunt, Soyini D. Liburd, Eduardo I. McLean */

import java.math.*;
import java.util.*;
import java.lang.reflect.*;

public class PrimeGenerator{
	
	BigInteger BigIntegerTWO = new BigInteger ("2");
	modExponent modExp = new modExponent();
	alphaToDecimal toDecimal = new alphaToDecimal();
	
	//Constructor
	PrimeGenerator(){}
	
	public static void main (String[] args){
		PrimeGenerator pg = new PrimeGenerator();
		//pg.names_generate("Soyini Kenny Eduardo Jonathan", 64);
		pg.names_generate("Soyini Kenny Eduardo Jonathan", 1024);
	}
	
	// g is a generator if g^(p-1/k) is not equal to 1 (mod p)
	// where k is a prime and a divisor of p - 1.
	// WE ASSUME THAT P IS A SOPHIE-GERMAIN PRIME,
	// then k=2, q, where p = 2q + 1
	//
	//POST CONDITION:
	// Returns 1 if g is a generator of Z*p
	// Returns 0 if g is not a generator of Z*p
	// Returns -1 if the result is inconclusive
	
	// takes p, g, where p is a prime.
	public int is_generator(BigInteger g, BigInteger p){
		
		BigInteger q = p.subtract(BigInteger.ONE);
		q = p.divide(BigIntegerTWO);
		
		// inconclusive
		if(!modExp.millerRabin(q, 20)){
			return -1;
		}
		
		// else sophie-germain
		BigInteger g_2 = modExp.exp(g, BigIntegerTWO, p);
		BigInteger g_q = modExp.exp(g, q, p);
		
		if(!g_2.equals(BigInteger.ONE) && !g_q.equals(BigInteger.ONE)){
			return 1;
		}
		else {
			return 0;
		}
	}
	
	// takes a list of names, a number b, and a random number
	// produces a b-bit prime number p such that each name is a generator
	//	public BigInteger names_generate(String names, int b, int ran){
	public Vector names_generate(String vnames, int b){
		Vector result = new Vector();
		
		//extract individual names from string
		String [] names = vnames.split(" ");
		int num_names = Array.getLength(names);
		
		int found_p = 0;
		BigInteger p = BigInteger.ZERO;
		Vector p_data;
		
		int total_attempts = 0;
		//int top_attempts = 0;
		
		//BigInteger [] names_dec = new BigInteger [num_names];
		BigInteger [] names_dec = new BigInteger [num_names];
		
		//fill array names_dec with decimal representation of names
		for(int i=0; i< num_names; i++){
			names_dec[i] = toDecimal.convertFormat(names[i]);
		}
		
		
		// try primes until we find one such that
		// all the names are generators of Z*p
		while(found_p == 0){
			// haven't found a suitable prime yet, so try another p
			p_data = modExp.bBitPrime(b);
			p = (BigInteger) p_data.get(0);
			// increase number of attempts
			total_attempts = total_attempts + ((Integer) p_data.get(1)).intValue();
			//top_attempts++;
			
			// tentatively set found_p to 1
			// and see if loop maintains it
			found_p = 1;
			
			for(int i=0; i< num_names; i++){
				if(is_generator(names_dec[i], p) != 1){
					found_p = 0;
				}
			}
			
			if(found_p == 0){
				System.out.println("Trying another p"+p+", number of random numbers tried so far: "+total_attempts+"...\n");
			}
		}
		
		System.out.println("Each name in ("+vnames+") is a generator of: "+p+". \n\n"+total_attempts+
		       " random numbers were tried before we obtained this result. \n");
		
		result.add(p);
		result.add(new Integer(total_attempts));
		
		return result;
	}
	
}