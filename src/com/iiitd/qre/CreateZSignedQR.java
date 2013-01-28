package com.iiitd.qre;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.json.XML;

import sun.misc.BASE64Encoder;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

public class CreateZSignedQR extends Common {
	private static final int	BLACK	= 0xFF000000;
	private static final int	WHITE	= 0xFFFFFFFF;

	public static void main(String args[]) throws Exception {
		if (args.length != 4)
			System.out.println("Usage: CreateZSignedQR <signer>"
					+ " <privkey> <xml-file> <imgout>");
		else try {
			byte[] data = XML
					.toJSONObject(new String(Common.readFileContents(args[2])))
					.toString().getBytes();
			byte[] signature = Sign.sign(args[1], data);
			byte[] zdata = Common.gzip(data);

			BASE64Encoder benc64 = new BASE64Encoder();

			String content = args[0] + "|" + benc64.encode(zdata) + "|"
					+ benc64.encode(signature);

			System.out.println(content);
			QRCode qrc = new QRCode();
			Encoder.encode(content, ErrorCorrectionLevel.L, qrc);

			writeToStream(qrc.getMatrix(), "PNG", new FileOutputStream(args[3]));
		} catch (Exception e) {
			System.err.println("Caught exception " + e.toString());
			throw e;
		}
	}

	public static BufferedImage toBufferedImage(ByteMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int scale = 6;
		int margin = 3;
		BufferedImage image = new BufferedImage(scale * (width + 2 * margin),
				scale * (height + 2 * margin), BufferedImage.TYPE_INT_ARGB);

		int imgX, imgY = margin * scale;
		for (int y = 0; y < height; y++) {
			for (int i = 0; i < scale; ++i) {
				imgX = margin * scale;
				for (int x = 0; x < width; x++) {
					for (int j = 0; j < scale; ++j) {
						if (matrix.get(x, y) != 0)
							image.setRGB(imgX, imgY, BLACK);
						++imgX;
					}
				}
				++imgY;
			}
		}
		return image;
	}

	public static void writeToStream(ByteMatrix matrix, String format,
			OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		/*System.out.println("m: " + matrix + ", f: " + format + ", s: " + stream
				+ ", i: " + image);*/
		ImageIO.write(image, format, stream);
	}
}
