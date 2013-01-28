/*
 * Copyright 2013 Arjun Asthana
 * This file is part of qr-gen-java.
 *
 * qr-gen-java is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * qr-gen-java is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with Foobar. If not, see http://www.gnu.org/licenses/.
 */

package com.iiitd.qre;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

public class Sign extends Common {

	public static Signature initPrivDsa(String filename)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			IOException, InvalidKeyException, NoSuchProviderException {
		Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
		dsa.initSign(Common.readPrivKey(filename));
		return dsa;
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 3)
			System.out.println("Usage: Sign <privkey> <file> <sigout>");
		else try {
			Common.writeFileContents(args[2], sign(args[0], args[1]));
		} catch (Exception e) {
			System.err.println("Caught exception " + e.toString());
			throw e;
		}
	}

	public static byte[] sign(String privKeyFile, String inFile)
			throws SignatureException, IOException, InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchProviderException {
		Signature dsa = initPrivDsa(privKeyFile);
		Common.executeDsa(dsa, inFile);
		return dsa.sign();
	}

	public static byte[] sign(String privKeyFile, byte[] data)
			throws InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchProviderException, IOException,
			SignatureException {
		Signature dsa = initPrivDsa(privKeyFile);
		dsa.update(data);
		return dsa.sign();
	}
}
