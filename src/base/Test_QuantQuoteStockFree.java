package base;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Test;

import Compute.Correlate;
import base.QuantQuoteStockFree;

public class Test_QuantQuoteStockFree {

	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	@Test
	public void TestLoadFile() throws Exception
	{
		// test loading of aapl
		String fileLoc = QuantQuoteStockFree.sp500freeRoot + "table_aapl.csv";
		
		QuantQuoteStockFree stock = new QuantQuoteStockFree(fileLoc);
		
		assertTrue(stock != null);
	}
	
	@Test
	public void TestLoadAllFiles() throws Exception
	{
		String fileLoc = QuantQuoteStockFree.sp500freeRoot;
		File folder = new File(fileLoc);
		File[] files = folder.listFiles();
		
		for(File f : files)
		{
			String path = f.getAbsolutePath();
			QuantQuoteStockFree stock = new QuantQuoteStockFree(path);
			assertTrue(stock != null);
		}
	}
	
	@Test
	public void TestGetCorrespondingDates() throws Exception
	{
		QuantQuoteStockFree[] stocks = QuantQuoteStockFree.LoadDatabase();
		LinkedList<Integer> dates = Correlate.GetCorrespondingDates(stocks[1], stocks[0]);
		
		for(Integer date : dates)
			System.out.println(date);
		
		final int total = dates.size();
		System.out.println(String.format("Total: %s", total));
	}
	
	@Test
	public void TestGetCorrespondingFeature() throws Exception
	{
		QuantQuoteStockFree[] stocks = QuantQuoteStockFree.LoadDatabase();
		QuantQuoteStockFree one = stocks[0], two = stocks[1];
		float[][] feature = Correlate.GetCorrespondingFeature(one, two, StockMetrics.open);
		
		final int L = feature[0].length;
		for(int i = 0; i < L; i++)
		{
			float o = feature[0][i], t = feature[1][i];
			System.out.println(String.format("%f, %f", o, t));
		}
	}
	
	@Test
	public void TestComputeR() throws Exception
	{
		QuantQuoteStockFree[] stocks = QuantQuoteStockFree.LoadDatabase();
		QuantQuoteStockFree one = stocks[0], two = stocks[1];
		float[][] feature = Correlate.GetCorrespondingFeature(one, two, StockMetrics.open);
		float R = Correlate.ComputeR(feature[0], feature[1]);
		
		System.out.println(String.format("R = %f", R));
	}
	
	@Test
	public void TestCorrelateSomeFiles() throws Exception
	{
		final int count = 10;
		QuantQuoteStockFree[] stocks = QuantQuoteStockFree.LoadDatabase();
		QuantQuoteStockFree[] some = new QuantQuoteStockFree[count];
		for(int i = 0; i < count; i++)
			some[i] = stocks[i];
		StockMetrics metric = StockMetrics.high;
		StockRelationResult[] results = Correlate.ComputeStockRelations(some, metric);
		
		for(StockRelationResult r : results)
		{
			System.out.println(String.format(
					"%s and %s: %f with %d samples",
					r.symbol1,
					r.symbol2,
					r.result,
					r.count));
		}
	}
	
	@Test
	public void TestCorrelateAllFiles() throws Exception
	{
		QuantQuoteStockFree[] stocks = QuantQuoteStockFree.LoadDatabase();
		StockMetrics metric = StockMetrics.high;
		StockRelationResult[] results = Correlate.ComputeStockRelations(stocks, metric);
		
		Arrays.sort(results, new StockRelationComparator());
		
		for(StockRelationResult r : results)
		{
			System.out.println(String.format(
					"%s and %s: %f with %d samples",
					r.symbol1,
					r.symbol2,
					r.result,
					r.count));
		}
	}
	
	@Test
	public void IAmAnIdiot()
	{
		final int count = 100;
		int total = 0;
		for(int piv = 0; piv < count; piv++)
			for(int cur = piv+1; cur < count; cur++)
				total++;
		
		int closed = (count * (count-1)) / 2;
		String out = String.format("loop = %d\nclosed = %d", total, closed);
		System.out.println(out);
	}

}











