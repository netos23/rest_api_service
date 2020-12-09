package ru.fbtw.navigator.rest_api_service.io;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;


public class ImageUtils {
	public static final String DEFAULT_TYPE = "png";
	public static final String DEFAULT_FOLDER = "C:\\Users\\nikmo\\IdeaProjects\\navigator\\parent_navigation_bot\\test_output\\";

	public static BufferedImage imageFromBase64(String base64Image) throws IOException {
		byte[] buffer = Base64.decodeBase64(base64Image);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
		BufferedImage image = ImageIO.read(inputStream);


		return image;
	}

	@Deprecated
	public static byte[] getImageFromNode(Node node, int width, int height) {
		WritableImage image = new WritableImage(width,height);
		node.snapshot(null,image);


		RenderedImage renderedImage = SwingFXUtils.fromFXImage(image,null);
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try {
			ImageIO.write(renderedImage,DEFAULT_TYPE,output);

			File file = new File(nextFilename());
			ImageIO.write(renderedImage,DEFAULT_TYPE,file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return output.toByteArray();
	}

	public static void write(BufferedImage image) throws IOException {
		File file = new File(nextFilename());
		ImageIO.write(image,DEFAULT_TYPE,file);
	}

	@Deprecated
	public static Image flattenImage(Node node, int width, int height) {

		WritableImage image = new WritableImage(width,height);
		node.snapshot(null,image);


		BufferedImage bufferedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		SwingFXUtils.fromFXImage(image,bufferedImage);

		return SwingFXUtils.toFXImage(bufferedImage,null);
	}

	public static String nextFilename() {
		return DEFAULT_FOLDER
				+ UUID.randomUUID().toString().substring(0, 8)
				+ "." + DEFAULT_TYPE;
	}
}
