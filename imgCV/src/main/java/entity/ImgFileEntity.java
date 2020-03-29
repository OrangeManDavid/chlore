package entity;

public class ImgFileEntity {
	private String inputFilePath;
	private String outputFilePath;
	private String inputFileName;
	private String outputFileName;
	private String inputFileType;
	private String outputFileType;
	private int rows;
	private int columns;
	
	public String getInputFilePath() {
		return inputFilePath;
	}
	public void setInputFilePath(String inputFilePath) {
		this.inputFilePath = inputFilePath;
	}
	public String getOutputFilePath() {
		return outputFilePath;
	}
	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}
	public String getInputFileName() {
		return inputFileName;
	}
	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}
	public String getOutputFileName() {
		return outputFileName;
	}
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	public String getInputFileType() {
		return inputFileType;
	}
	public void setInputFileType(String inputFileType) {
		this.inputFileType = inputFileType;
	}
	public String getOutputFileType() {
		return outputFileType;
	}
	public void setOutputFileType(String outputFileType) {
		this.outputFileType = outputFileType;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getColumns() {
		return columns;
	}
	public void setColumns(int columns) {
		this.columns = columns;
	}
	
}
