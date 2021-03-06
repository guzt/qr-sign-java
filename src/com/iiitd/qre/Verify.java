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
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class Verify extends Common {

	public static Signature initPubDsa(byte[] pubKeyData)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			IOException, InvalidKeyException, NoSuchProviderException {
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyData);
		PublicKey pubKey = KeyFactory.getInstance("DSA").generatePublic(
				pubKeySpec);
		Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
		dsa.initVerify(pubKey);
		return dsa;
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 3)
			System.out.println("Usage: Verify <pubkey> <file> <signature>");
		else try {
			Signature dsa = initPubDsa(Common.readFileContents(args[0]));
			Common.executeDsa(dsa, args[1]);
			if (dsa.verify(Common.readFileContents(args[2])))
				System.out.println("verfied");
			else System.out.println("failed");
		} catch (Exception e) {
			System.err.println("Caught exception " + e.toString());
			throw e;
		}
	}
}
