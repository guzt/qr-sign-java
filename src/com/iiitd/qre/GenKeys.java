/*
 * Copyright 2013 Arjun Asthana
 * This file is part of qr-sign-java.
 *
 * qr-sign-java is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * qr-sign-java is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with qr-sign-java. If not, see http://www.gnu.org/licenses/.
 */

package com.iiitd.qre;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class GenKeys extends Common {

	public static KeyPair genKeyPair() throws NoSuchAlgorithmException,
			NoSuchProviderException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

		keyGen.initialize(1024, random);

		return keyGen.generateKeyPair();
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2)
			System.out.println("Usage: GenKeys <privout> <pubout>");
		else try {
			KeyPair pair = genKeyPair();
			Common.writeKey(pair.getPrivate(), args[0]);
			Common.writeKey(pair.getPublic(), args[1]);
		} catch (Exception e) {
			System.err.println("Caught exception " + e.toString());
			throw e;
		}
	}

}
