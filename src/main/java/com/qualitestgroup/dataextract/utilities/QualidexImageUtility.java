package com.qualitestgroup.dataextract.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class QualidexImageUtility {

	private static final Log logger = LogFactory.getLog(QualidexImageUtility.class);	

	static boolean compareAndHighlight(final BufferedImage img1, final BufferedImage img2, String fileName,
			boolean highlight, int colorCode) {
		final int w = img1.getWidth();
		final int h = img1.getHeight();
		final int[] p1 = img1.getRGB(0, 0, w, h, null, 0, w);
		final int[] p2 = img2.getRGB(0, 0, w, h, null, 0, w);

		if (!(java.util.Arrays.equals(p1, p2))) {
			logger.warn("Image compared - does not match");
			if (highlight) {
				for (int i = 0; i < p1.length; i++) {
					if (p1[i] != p2[i]) {
						p1[i] = colorCode;
					}
				}
				final BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
				out.setRGB(0, 0, w, h, p1, 0, w);
				saveImage(out, fileName);
			}
			return false;
		}
		return true;
	}

	static void saveImage(BufferedImage image, String file) {
		try {
			File outputfile = new File(file);
			ImageIO.write(image, "png", outputfile);
		} catch (Exception e) {
			logger.info("Error " + e);
		}
	}

}
