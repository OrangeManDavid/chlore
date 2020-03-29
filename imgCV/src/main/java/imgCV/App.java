package imgCV;

import java.io.File;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import entity.ImgFileEntity;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
//    	if(args.length < 2) {
//    		System.out.println("必须传入2个参数");
//    		return;
//    	}
//    	System.out.println("参数1: ");
//    	System.out.println(args[0]);
//    	System.out.println("参数2: ");
//    	System.out.println(args[1]);
		logger.info("hello this is log4j info log");
		logger.info("hello this is log4j info log1");
		logger.info("hello this is log4j info log2");
		logger.info("hello this is log4j info log3");
		logger.debug("Hello debug log");
		logger.warn("Hello warnning log");
		logger.error("Hello Error Log");

		Scanner sc = new Scanner(System.in);
		System.out.println("请输入需要转换的pdf的地址");
		String fileAddress = sc.nextLine();
		System.out.println("请输入需要转换的pdf的名称，不要加.pdf后缀，例如 操作系统概念：");
		String filename = sc.nextLine();
		System.out.println("请输入开始转换的页码，从0开始，例如 5：");
		int indexOfStart = sc.nextInt();
		System.out.println("请输入停止转换的页码，-1为全部，例如 10：");
		int indexOfEnd = sc.nextInt();
		if (indexOfEnd == -1) {
			Pdf2Img.pdf2png(fileAddress, filename);
		} else {
			Pdf2Img.pdf2png(fileAddress, filename, indexOfStart, indexOfEnd);
		}
		System.out.println("OK");

	}

	public static void jointImg() {
		// TODO Auto-generated method stub
		ImgFileEntity imgFileEntity = new ImgFileEntity();
		try {
			ImgProcessor.mergeImage(imgFileEntity.getInputFilePath(), imgFileEntity.getOutputFilePath(),
					imgFileEntity.getRows(), imgFileEntity.getColumns(), imgFileEntity.getOutputFileType());
		} catch (Exception e) {
			logger.error("ImgProcessor.mergeImage: Function call failed");
		}
	}

	/**
	 * File中的 list方法: static File[] listRoots() : 列出可用的文件系统根。 ： 即列出计算机磁盘的盘符 String[]
	 * list() : 返回一个字符串数组，这些字符串指定file路径下的所有文件和目录 File[] listFiles() :
	 * 返回一个抽象路径名数组，这些路径名表示此抽象路径名表示的目录中的文件。
	 * 
	 * @author 郑清
	 */

	public static void aaaa() {
		System.out.println("列出计算机磁盘的盘符:");
		File[] listRoots = File.listRoots();
		for (File file : listRoots) {
			System.out.println(file);
		}

		System.out.println("\n列出D:/1路径下的所有文件和文件夹:");
		File file = new File("D:/1");
		String[] list = file.list();
		for (String string : list) {// 迭代出来的都是文件名
			System.out.println(string);
		}

		System.out.println("\n列出D:/1路径下的所有文件和文件夹的绝对路径:");
		File[] listFiles = file.listFiles();
		for (File file2 : listFiles) {// 迭代出来的都是文件的绝对路径
			System.out.println(file2);
		}
	}
}