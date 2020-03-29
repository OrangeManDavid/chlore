package imgCV;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author yanding.li
 * @version 创建时间：Sun, 29 Mar 2020 14:26:09 GMT 类说明
 */

public class ImgProcessor {

	/**
	 * 切割图片
	 *
	 * @throws Exception
	 */
	private static void splitImage() throws Exception {
		String originalImg = "C:\\Users\\liwj\\Desktop\\tidb\\image\\ori.jpg";
		File file = new File(originalImg);
		FileInputStream fis = new FileInputStream(file);
		BufferedImage image = ImageIO.read(fis);

		int rows = 2;
		int cols = 2;
		int chunks = rows * cols;

		int chunkWidth = image.getWidth() / cols;
		int chunkHeight = image.getHeight() / rows;

		int count = 0;
		BufferedImage[] imgs = new BufferedImage[chunks];
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < cols; y++) {
				imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

				Graphics2D gr = imgs[count++].createGraphics();
				gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x,
						chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
				gr.dispose();
			}
		}
		for (int i = 0; i < imgs.length; i++) {
			ImageIO.write(imgs[i], "jpg", new File("C:\\Users\\liwj\\Desktop\\tidb\\image\\" + i + ".jpg"));
		}
	}

	/**
	 * 合并图片
	 *
	 * @throws Exception
	 */
	private static void mergeImage() throws Exception {
		int rows = 2;
		int cols = 2;
		int chunks = rows * cols;

		int chunkWidth, chunkHeight;
		int type;

		File[] imgFiles = new File[chunks];
		for (int i = 0; i < chunks; i++) {
			imgFiles[i] = new File("C:\\Users\\liwj\\Desktop\\tidb\\image\\" + i + ".jpg");
		}

		BufferedImage[] buffImages = new BufferedImage[chunks];
		for (int i = 0; i < chunks; i++) {
			buffImages[i] = ImageIO.read(imgFiles[i]);
		}
		type = buffImages[0].getType();
		chunkWidth = buffImages[0].getWidth();
		chunkHeight = buffImages[0].getHeight();

		BufferedImage finalImg = new BufferedImage(chunkWidth * cols, chunkHeight * rows, type);

		int num = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				finalImg.createGraphics().drawImage(buffImages[num], chunkWidth * j, chunkHeight * i, null);
				num++;
			}
		}

		ImageIO.write(finalImg, "jpeg", new File("C:\\Users\\liwj\\Desktop\\tidb\\image\\finalImg.jpg"));
	}

//    public static void main(String[] args) {
//        try {
//            splitImage();
//            mergeImage();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

	/**
	 * 缩放
	 */
	public static void zoomByScale(BufferedImage bufImage, double scale) throws Exception {
		// 获取缩放后的长和宽
		int _width = (int) (scale * bufImage.getWidth());
		int _height = (int) (scale * bufImage.getHeight());
		// 获取缩放后的Image对象
		Image _img = bufImage.getScaledInstance(_width, _height, Image.SCALE_DEFAULT);
		// 新建一个和Image对象相同大小的画布
		BufferedImage image = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_RGB);
		// 获取画笔
		Graphics2D graphics = image.createGraphics();
		// 将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
		graphics.drawImage(_img, 0, 0, null);
		// 释放资源
		graphics.dispose();
		// 使用ImageIO的方法进行输出,记得关闭资源
		OutputStream out = new FileOutputStream("/Users/sunxianyan/Downloads/缩放.jpg");
		ImageIO.write(image, "jpeg", out);
		out.close();
	}

	/**
	 * 旋转
	 */
	public static void rotate(BufferedImage bufImage, int degree) throws Exception {
		int w = bufImage.getWidth();// 得到图片宽度。
		int h = bufImage.getHeight();// 得到图片高度。

		int swidth = 0; // 旋转后的宽度
		int sheight = 0;// 旋转后的高度
		int x;// 原点横坐标
		int y;// 原点纵坐标

		degree = degree % 360;
		if (degree < 0) {
			degree = 360 + degree;
		}
		double theta = Math.toRadians(degree);
		if (degree == 180 || degree == 0 || degree == 360) {
			sheight = bufImage.getWidth();
			swidth = bufImage.getHeight();
		} else if (degree == 90 || degree == 270) {
			sheight = bufImage.getWidth();
			swidth = bufImage.getHeight();
		} else {
			swidth = (int) (Math.sqrt(w * w + h * h));
			sheight = (int) (Math.sqrt(w * w + h * h));
		}
		x = (swidth / 2) - (w / 2);// 确定原点坐标
		y = (sheight / 2) - (h / 2);
		BufferedImage spinImage = new BufferedImage(swidth, sheight, bufImage.getType());
		// 设置图片背景颜色
		Graphics2D gs = (Graphics2D) spinImage.getGraphics();
		gs.setColor(Color.white);
		gs.fillRect(0, 0, swidth, sheight);// 以给定颜色绘制旋转后图片的背景

		AffineTransform at = new AffineTransform();
		at.rotate(theta, swidth / 2, sheight / 2);// 旋转图象
		at.translate(x, y);
		AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
		spinImage = op.filter(bufImage, spinImage);
		ImageIO.write(spinImage, "jpeg", new File("/Users/sunxianyan/Downloads/旋转.jpg"));
	}

	/**
	 * 裁剪
	 */
	public static void clip(BufferedImage bufImage) throws Exception {
		ImageIO.write(bufImage.getSubimage(0, 0, 100, 100), "JPEG", new File("/Users/sunxianyan/Downloads/裁剪.jpg"));
	}

	/**
	 * 镜像
	 */
	public static void mirror(BufferedImage bufImage) throws Exception {

		// 获取图片的宽高
		final int width = bufImage.getWidth();
		final int height = bufImage.getHeight();

		// 读取出图片的所有像素
		int[] rgbs = bufImage.getRGB(0, 0, width, height, null, 0, width);

		// 对图片的像素矩阵进行水平镜像
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width / 2; col++) {
				int temp = rgbs[row * width + col];
				rgbs[row * width + col] = rgbs[row * width + (width - 1 - col)];
				rgbs[row * width + (width - 1 - col)] = temp;
			}
		}

		// 把水平镜像后的像素矩阵设置回 bufImage
		bufImage.setRGB(0, 0, width, height, rgbs, 0, width);
		ImageIO.write(bufImage, "JPEG", new File("/Users/sunxianyan/Downloads/镜像.jpg"));
	}

	/*    *//**
			 * 拼接
			 *//*
				 * public static void add() throws Exception { String fileUrl =
				 * "https://static.cdn.longmaosoft.com/00039447dd4fa068e1835148c3d5431e.png";
				 * String filepath = "/Users/sunxianyan/Downloads/223233fmbmgbxrmfwmi3mw.jpg";
				 * 
				 * BufferedImage bufImage = ImageIO.read(new URL(fileUrl)); BufferedImage
				 * addImage = ImageIO.read(new File(filepath));
				 * 
				 * Graphics2D g2d = (Graphics2D) bufImage.getGraphics().create();
				 * 
				 * // 绘制图片（如果宽高传的不是图片原本的宽高, 则图片将会适当缩放绘制） g2d.drawImage(addImage, 50, 50,
				 * addImage.getWidth(), addImage.getHeight(),null);
				 * 
				 * g2d.dispose();
				 * 
				 * ImageIO.write(bufImage,"jpeg", new
				 * File("/Users/sunxianyan/Downloads/拼接.jpg")); }
				 */
}
