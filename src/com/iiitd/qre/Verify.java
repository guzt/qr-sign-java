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
