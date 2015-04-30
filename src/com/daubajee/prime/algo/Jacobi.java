package com.daubajee.prime.algo;

import java.math.BigInteger;
import java.util.Random;

public class Jacobi {
	static BigInteger two = new BigInteger("2");

	static int valuation2(BigInteger nombre) {
		int valuation = 0;
		while ((nombre.mod(two)).compareTo(BigInteger.ZERO) == 0) {
			valuation++;
			nombre = nombre.divide(two);
		}
		return valuation;
	}

	/**
	* Calcule le symbole de Jacobi a/b
	* 
	* @param a
	*            la partie haute
	* @param b
	*            la partie basse
	* @return 0 si a == 0, 2 si b est pair, 1 ou -1 sinon
	*/
	static int jacobi(BigInteger a, BigInteger b) {
		int jacobi = 1;
		if (b.mod(two).compareTo(BigInteger.ZERO) == 0) {
			return 2;
		}
		if (a.compareTo(BigInteger.ZERO) == 0) {
			return 0;
		}
		/*
		* if (a.compareTo(BigInteger.ONE) == 0) { return 1; }
		*/
		int k = valuation2(a);
		BigInteger m = a.divide(two.pow(k));
		int sigma = 1;
		if (k % 2 == 0) {
			sigma = 1;
		} else if (((((b.multiply(b)).subtract(BigInteger.ONE)).divide(new BigInteger("8"))).mod(two)).compareTo(BigInteger.ZERO) == 0) {
			sigma = 1;
		} else {
			sigma = -1;
		}
		if (m.compareTo(BigInteger.ONE) == 0) {
			jacobi = sigma;
		} else {
			if (((((m.subtract(BigInteger.ONE)).multiply(b.subtract(BigInteger.ONE))).divide(new BigInteger("4"))).mod(two))
					.compareTo(BigInteger.ONE) == 0) {
				sigma = -sigma;
			}
			jacobi = sigma * jacobi(b.mod(m), m);
		}
		return jacobi;
	}

	/**
	* Teste la primalité d'un nombre premier
	* 
	* @param n
	*            l'entier dont on veut tester la primalité
	* @param k
	*            le nombre de tests à effectuer
	* @return true si probablement premier, false sinon
	*/
	static boolean nbPremier(BigInteger p, int k) {
		BigInteger a;
		BigInteger j;
		BigInteger m;
		for (int i = 0; i < k; i++) {
			a = randomBigInt2ToXMin1(p);
			j = BigInteger.valueOf(jacobi(a, p)).mod(p);
			m = a.modPow(p.subtract(BigInteger.ONE).divide(two), p);
			if (j.compareTo(BigInteger.ZERO) == 0 || j.compareTo(m) != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	* Teste si un nombre est nombre premier sûr
	* 
	* @param n
	*            l'entier dont on veut tester la primalité
	* @param k
	*            le nombre de tests à effectuer
	* @return true si premier sûr, false sinon
	*/
	static boolean nbPremierSur(BigInteger p, int k) {
		if (nbPremier(p, k)) {
			BigInteger s = p.multiply(two).add(BigInteger.ONE);
			if (nbPremier(s, k)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	* Génère un nombre entier entre 2 et x - 1 avec x fourni
	* 
	* @param x
	*            le nombre de référence
	* @return un nombre entier compris entre 2 et x - 1
	*/
	static BigInteger randomBigInt2ToXMin1(BigInteger x) {
		Random rnd = new Random();
		do {
			BigInteger res = new BigInteger(x.bitLength(), rnd);
			if ((res.compareTo(x) < 0) && (res.compareTo(two) >= 0)) {
				return res;
			}
		} while (true);
	}

	/**
	* Génère un nombre entier du nombre de bits spécifié (en ignorant ceux qui commencent par 0 et ne font pas le nombre de bits en réalité)
	* 
	* @param nbBits
	*            le nombre de bits que doit faire notre nombre
	* @return un nombre entier de la bonne longueur
	*/
	static BigInteger randomBigIntOfBitSize(int nbBits) {
		Random rnd = new Random();
		BigInteger res;
		String minS = "1";
		String maxS = "1";
		for (int i = 1; i < nbBits; i++) {
			minS += "0";
			maxS += "1";
		}
		BigInteger min = new BigInteger(minS, 2);
		BigInteger max = new BigInteger(maxS, 2);
		do {
			res = new BigInteger(nbBits, rnd);
		} while ((res.compareTo(min) == -1) || (res.compareTo(max) == 1));
		return res;
	}
	
	static BigInteger randomBigIntBetween(BigInteger start, BigInteger end){
		
		BigInteger diff = end.subtract(start);
		BigInteger randDiff = randomBigInt2ToXMin1(diff).add(start);
		return randDiff;
	}

	public static void testClassic(){
		long startTime = System.currentTimeMillis();
		BigInteger res;
		do {
			res = randomBigIntOfBitSize(1024);
		} while (!nbPremierSur(res, 32));
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println("Temps de travail : " + elapsedTime + " ms\n" + res.toString());
	}
	
	public static void testRandomInterval(){
		BigInteger big1 = randomBigIntOfBitSize(1024);
		BigInteger big2 = randomBigIntOfBitSize(1024);
		BigInteger big3;
		if(big1.compareTo(big2) > 0 ){
			big3 = big1;
			big1 = big2;
			big2 = big3;
		}
		
		big3 = randomBigIntBetween(big1, big2);

		System.out.println(big1.toString());
		System.out.println(big2.toString());
		System.out.println(big3.toString());
	}
	
	
	
	public static void main(String[] args) {
		testRandomInterval();
	}
}