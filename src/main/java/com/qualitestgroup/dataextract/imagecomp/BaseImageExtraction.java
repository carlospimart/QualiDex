package main.java.com.qualitestgroup.dataextract.imagecomp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseImageExtraction {

	private static final Log logger = LogFactory.getLog(BaseImageExtraction.class);

	public static double imageCompare(String refImagePath, String extractedImagePath) {
		BufferedImage imgA = null;
		BufferedImage imgB = null;
		double percentage = 0;

		try {
			File fileA = new File(refImagePath);
			File fileB = new File(extractedImagePath);

			imgA = ImageIO.read(fileA);
			imgB = ImageIO.read(fileB);
			double width1 = imgA.getWidth();
			double width2 = imgB.getWidth();
			double height1 = imgA.getHeight();
			double height2 = imgB.getHeight();

			if ((width1 != width2) || (height1 != height2))
				logger.info("Error: Images dimensions" + " mismatch");

			else {
				long difference = 0;
				for (int y = 0; y < height1; y++) {
					for (int x = 0; x < width1; x++) {
						int rgbA = imgA.getRGB(x, y);
						int rgbB = imgB.getRGB(x, y);
						int redA = (rgbA >> 16) & 0xff;
						int greenA = (rgbA >> 8) & 0xff;
						int blueA = (rgbA) & 0xff;
						int redB = (rgbB >> 16) & 0xff;
						int greenB = (rgbB >> 8) & 0xff;
						int blueB = (rgbB) & 0xff;
						difference += Math.abs(redA - redB);
						difference += Math.abs(greenA - greenB);
						difference += Math.abs(blueA - blueB);
					}
				}

				double totalPixels = width1 * height1 * 3;
				double avgDifferentPixels = difference / totalPixels;
				// There are 255 values of pixels in total
				percentage = (avgDifferentPixels / 255) * 100;
			}

		} catch (IOException e) {
			logger.info("Error " + e);
		}

		return percentage;
	}

	public static BufferedImage joinBufferedImage(BufferedImage img1, BufferedImage img2) {
		int offset = 2;
		int width = img1.getWidth() + img2.getWidth() + offset;
		int height = Math.max(img1.getHeight(), img2.getHeight()) + offset;
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = newImage.createGraphics();
		Color oldColor = g2.getColor();
		g2.setPaint(Color.BLACK);
		g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		g2.drawImage(img1, null, 0, 0);
		g2.drawImage(img2, null, img1.getWidth() + offset, 0);
		g2.dispose();
		return newImage;
	}
	
	

	public static BufferedImage compareWithBaseImage(BufferedImage bImage, BufferedImage cImage) {

		int height = bImage.getHeight();
		int width = bImage.getWidth();
		BufferedImage rImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				try {
					int pixelC = cImage.getRGB(x, y);
					int pixelB = bImage.getRGB(x, y);
					if (pixelB == pixelC) {
						rImage.setRGB(x, y, bImage.getRGB(x, y));
					} else {
						int a = 0xff | bImage.getRGB(x, y) >> 24;
						int r = 0xFF & bImage.getRGB(x, y) >> 16;
						int g = 0xCC & bImage.getRGB(x, y) >> 8;
						int b = 0x00 & bImage.getRGB(x, y);

						int modifiedRGB = a << 24 | r << 16 | g << 8 | b;
						rImage.setRGB(x, y, modifiedRGB);
					}
				} catch (Exception e) {

					rImage.setRGB(x, y, 0x80ff0000);
				}
			}
		}
		return rImage;
	}

}