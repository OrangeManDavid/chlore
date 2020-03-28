package imgCV;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App 
{
	private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
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
		String filename =sc.nextLine();
		System.out.println("请输入开始转换的页码，从0开始，例如 5：");
		int indexOfStart=sc.nextInt();
		System.out.println("请输入停止转换的页码，-1为全部，例如 10：");
		int indexOfEnd=sc.nextInt();
		if (indexOfEnd==-1) {
			PDF2img.pdf2png(fileAddress, filename);
		}
		else {
			PDF2img.pdf2png(fileAddress, filename, indexOfStart, indexOfEnd);
		}
        System.out.println( "OK" );
    }
}