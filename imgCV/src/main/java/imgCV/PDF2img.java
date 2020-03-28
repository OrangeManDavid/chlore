package imgCV;

/** 
* @author yanding.li
* @version 创建时间：2020年3月2日 下午8:00:00 
* 类说明 
*/
 
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDF2img {
	
	//可自由确定起始页和终止页
	public static void pdf2png(String fileAddress,String filename,int indexOfStart,int indexOfEnd) {
		// 将pdf装图片 并且自定义图片得格式大小
				File file = new File(fileAddress+"\\"+filename+".pdf");
				try {
					PDDocument doc = PDDocument.load(file);
					PDFRenderer renderer = new PDFRenderer(doc);
					int pageCount = doc.getNumberOfPages();
					for (int i = indexOfStart; i < indexOfEnd; i++) {
						BufferedImage image = renderer.renderImageWithDPI(i, 144); // Windows native DPI
						// BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
						ImageIO.write(image, "PNG", new File(fileAddress+"\\"+filename+"_"+(i+1)+".png"));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
	}
	//转换全部的pdf
	public static void pdf2png(String fileAddress,String filename) {
		// 将pdf装图片 并且自定义图片得格式大小
				File file = new File(fileAddress+"\\"+filename+".pdf");
				try {
					PDDocument doc = PDDocument.load(file);
					PDFRenderer renderer = new PDFRenderer(doc);
					int pageCount = doc.getNumberOfPages();
					for (int i = 0; i < pageCount; i++) {
						BufferedImage image = renderer.renderImageWithDPI(i, 144); // Windows native DPI
						// BufferedImage srcImage = resize(image, 240, 240);//产生缩略图
						ImageIO.write(image, "PNG", new File(fileAddress+"\\"+filename+"_"+(i+1)+".png"));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
	}
}
