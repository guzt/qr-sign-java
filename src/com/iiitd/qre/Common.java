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
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.zip.GZIPOutputStream;

public class Common {
	public static void executeDsa(Signature dsa, String filename)
			throws SignatureException, IOException {
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(
				filename));
		byte[] buf = new byte[1024];
		int len;
		while (fis.available() > 0) {
			len = fis.read(buf);
			dsa.update(buf, 0, len);
		}
		fis.close();
	}

	public static byte[] readFileContents(String filename) throws IOException {
		InputStream fis;

		if ("-".equals(filename))
			fis = System.in;
		else fis = new FileInputStream(filename);

		byte[] data = new byte[fis.available()];
		fis.read(data);
		fis.close();
		return data;
	}

	public static byte[] gzip(byte[] data) throws IOException {
		ByteArrayOutputStream zDataStream = new ByteArrayOutputStream();
		GZIPOutputStream gzout = new GZIPOutputStream(zDataStream);
		gzout.write(data);
		gzout.finish();
		gzout.close();

		return zDataStream.toByteArray();
	}

	public static PrivateKey readPrivKey(String filename) throws IOException,
			NoSuchAlgorithmException, InvalidKeySpecException {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
				readFileContents(filename));
		return KeyFactory.getInstance("DSA").generatePrivate(keySpec);
	}

	public static PublicKey readPubKey(String filename)
			throws InvalidKeySpecException, NoSuchAlgorithmException,
			NoSuchProviderException, IOException {
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(
				readFileContents(filename));
		return KeyFactory.getInstance("DSA").generatePublic(pubKeySpec);
	}

	public static void writeFileContents(String filename, byte[] data)
			throws IOException {
		FileOutputStream fos = new FileOutputStream(filename);
		fos.write(data);
		fos.close();
	}

	public static void writeKey(Key key, String filename) throws IOException {
		writeFileContents(filename, key.getEncoded());
	}
}
