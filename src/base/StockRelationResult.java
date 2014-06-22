package base;

public class StockRelationResult {

	final String symbol1;
	final String symbol2;
	final StockMetrics metric;
	final int count;
	final float result;
	
	public StockRelationResult(String symbolOne, String symbolTwo, StockMetrics theMetric, float theResult, int count)
	{
		symbol1 = symbolOne;
		symbol2 = symbolTwo;
		metric = theMetric;
		result = theResult;
		this.count = count;
	}
	
}
