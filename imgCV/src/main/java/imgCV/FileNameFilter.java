package imgCV;

/*
 * 文件过滤器：多指定特点的文件进行过滤
 * 步骤：
 * 1.把目标文件夹封装为File对象
 * 2.调用list方法获取名字并存在String数组当中
 * 3.将过滤器作为参数传给List方法
 * 4.可以采用匿名内部类的方式直接定义过滤器，定义原则为覆盖其中的accept方法，accep中的参数
 * 为（File 指定文件，String name）
 * 5.运行即可完成过滤，过滤的结果存在String数组当中，可以通过高级For循环将他打印出来
 */
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class FileNameFilter {

	public static void getFileNameFilter(String path, final String findString) {
		File file = new File(path);
		String[] arr = file.list(new FilenameFilter() {
			public boolean accept(File file, String name) {
				return name.contains(findString);
			}
		});
		for (String x : arr) {
			System.out.println(x);
		}
	}

	public ArrayList<File> getFilesIteration(String path) {
		// 目标集合fileList
		ArrayList<File> fileList = new ArrayList<File>();
		File file = new File(path);
		if (file.isDirectory()) {

			File[] files = file.listFiles();
			for (File fileIndex : files) {
				// 如果这个文件是目录，则进行递归搜索
				if (fileIndex.isDirectory()) {
					getFilesIteration(fileIndex.getPath());
				} else {
					fileList.add(fileIndex);
				}
			}
		}
		return fileList;
	}

	public ArrayList<File> getFilesNotIteration(String path) {
		// 目标集合fileList
		ArrayList<File> fileList = new ArrayList<File>();
		File file = new File(path);
		if (file.isDirectory()) {

			File[] files = file.listFiles();
			for (File fileIndex : files) {
				// 如果这个文件是目录，则进行递归搜索
				if (fileIndex.isDirectory()) {
					getFilesNotIteration(fileIndex.getPath());
				} else {
					fileList.add(fileIndex);
				}
			}
		}
		return fileList;
	}
}