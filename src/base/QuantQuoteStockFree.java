package base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


/**
 * Representation for QuantQuote's 
 * free data set. It consists only
 * of daily stock values for open,
 * close, high, low, and volume.
 * @author Travis
 *
 */
public class QuantQuoteStockFree {

	// vars
	protected String stockName;
	protected int entries;
	
	public static final String sp500freeRoot = 
			"P:\\workspace\\Stocks\\data\\quantquote_daily_sp500_83986\\daily\\";
	
	
	// data
	protected int[] dates;
	protected float[] open;
	protected float[] high;
	protected float[] low;
	protected float[] close;
	protected float[] volume;
	
	public HashMap<Integer, StockMetricDay> m_db;
	
	public QuantQuoteStockFree(String stockName, String[] tableEntries)
	{
		CommonConstruct(stockName, tableEntries);
	}
	
	/**
	 * Uses fileName to parse a data table. Assumes fileName
	 * is of the format "table_(stockSymbol).csv".
	 * @param fileName
	 * @throws Exception 
	 */
	public QuantQuoteStockFree(String fileName) throws Exception
	{
		// vars
		FileInputStream fis = new FileInputStream(fileName);
		Scanner s = new Scanner(fis);

		// parse stock symbol
		String name = GetSymbolFromFileName(fileName);
		
		// load file data
		ArrayList<String> lines = new ArrayList<String>();
		while(s.hasNextLine())
			lines.add(s.nextLine());
		
		String[] linesArray = new String[lines.size()];
		lines.toArray(linesArray);
		
		
		// fill values
		CommonConstruct(name, linesArray);
		
		// close streams
		s.close();
	}
	
	/**
	 * Parses table entries into class
	 * @param symbol
	 * @param tableEntries
	 */
	private void CommonConstruct(String symbol, String[] tableEntries)
	{
		stockName = symbol;
		final int L = tableEntries.length;
		entries = L;
		AllocDataArrays(L);
		m_db = new HashMap<Integer, StockMetricDay>();
		
		// parse
		for(int i = 0; i < L; i++)
		{
			String line = tableEntries[i];
			String[] lineValues = SeparateTableLine(line);
			AddLineToArraysAndHashMap(i, lineValues);
		}
	}
	
	/**
	 * Allocates all arrays used for data representation.
	 * @param size
	 */
	private void AllocDataArrays(int size)
	{
		// ints
		dates = new int[size];
		
		// floats
		open = new float[size];
		close = new float[size];
		high = new float[size];
		low = new float[size];
		volume = new float[size];
	}
	
	private String[] SeparateTableLine(String tableLine)
	{
		return tableLine.split(",");
	}
	
	/**
	 * Adds a line from a stock's table file according to the
	 * file specification:
	 * 1. Date
	 * 2. Time (IGNORE)
	 * 3. Open
	 * 4. High
	 * 5. Low
	 * 6. Close
	 * 7. Volume
	 * @param lineNumber
	 * @param lineValues
	 */
	private void AddLineToArraysAndHashMap(int lineNumber, String[] lineValues)
	{
		// vars
		final int i = lineNumber;
		final String[] lv = lineValues;
		
		// parse and add
		dates[i] = Integer.parseInt(lv[0]);
		open[i] = Float.parseFloat(lv[2]);
		high[i] = Float.parseFloat(lv[3]);
		low[i] = Float.parseFloat(lv[4]);
		close[i] = Float.parseFloat(lv[5]);
		volume[i] = Float.parseFloat(lv[6]);
		
		StockMetricDay smd = new StockMetricDay();
		smd.open = open[i];
		smd.close = close[i];
		smd.low = low[i];
		smd.high = high[i];
		smd.volume = volume[i]; 
		m_db.put(dates[i], smd);
	}
	
	private String GetSymbolFromFileName(String fileName) throws Exception
	{
		// vars
		final String filePrefix = "table_";
		final String fileSuffix = ".csv";
		final int startingIndex = filePrefix.length();
		final int trailingCharactersToIgnore = fileSuffix.length();
		final int start = fileName.lastIndexOf(filePrefix);
		final String file = fileName.substring(start);
		
		// check correctly formatted
		boolean correctlyFormatted = true;
		correctlyFormatted &= file.endsWith(fileSuffix);
		correctlyFormatted &= file.startsWith(filePrefix);
		correctlyFormatted &= file.length() > (startingIndex + trailingCharactersToIgnore);
		if(!correctlyFormatted)
			throw new Exception("fileName is not formatted correctly!");
		
		
		// parse
		final int clip = file.length() - trailingCharactersToIgnore;
		return file.substring(startingIndex, clip);
	}
	
	public String GetSymbol()
	{
		return stockName;
	}
	
	/**
	 * Loads all records located in the sp500freeRoot file location.
	 * @return
	 * @throws Exception
	 */
	public static QuantQuoteStockFree[] LoadDatabase() throws Exception
	{
		// vars
		final String fileLoc = sp500freeRoot;
		final File folder = new File(fileLoc);
		final File[] files = folder.listFiles();
		final int L = files.length;
		
		// build database
		QuantQuoteStockFree[] db = new QuantQuoteStockFree[L];	
		for(int i = 0; i < L; i++)
		{
			String path = files[i].getAbsolutePath();
			db[i] = new QuantQuoteStockFree(path);
		}
		
		return db;
	}
	
	
}
























