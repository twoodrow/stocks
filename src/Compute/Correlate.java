package Compute;

import java.awt.List;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import base.QuantQuoteStockFree;
import base.StockMetricDay;
import base.StockMetrics;
import base.StockRelationResult;

public class Correlate {

	public static StockRelationResult[] ComputeStockRelations(
			final QuantQuoteStockFree[] stocks, 
			final StockMetrics metric) throws Exception
	{
		final int count = stocks.length;
		final int total = (count*(count - 1))/2;
		StockRelationResult[] result = new StockRelationResult[total];
		QuantQuoteStockFree pivot, current;
		
		// compute
		int idx = 0;
		for(int piv = 0; piv < count; piv++)
		{
			for(int cur = piv+1; cur < count; cur++)
			{
				pivot = stocks[piv];
				current = stocks[cur];
				
				float[][] feature = Correlate.GetCorrespondingFeature(pivot, current, metric);
				float R = ComputeR(feature[0], feature[1]);
				int strength = feature[0].length;
				result[idx] = new StockRelationResult(
						pivot.GetSymbol(),
						current.GetSymbol(),
						metric,
						R,
						strength);
				idx++;
			}
		}
		
		return result;
	}
	
	public static float ComputeR(float[] one, float[] two) throws Exception
	{
		// check lengths match
		if(one.length != two.length)
			throw new Exception("Arrays must be the same length.");
		
		// vars
		final int L = one.length;
		float R = 0f;
		float x = 0f, xx = 0f, y = 0f, yy = 0f, xy = 0f;
		float o, t;
		
		// compute x, xx, y, yy, xy
		for(int i = 0; i < L; i++)
		{
			o = one[i];
			t = two[i];
			
			x += o;
			y += t;
			xx += (o*o);
			yy += (t*t);
			xy += (o*t);
		}
		
		// compute R
		float numerator = L * xy;
		numerator -= (x * y);
		numerator *= numerator;
		
		float denomO = L * xx - (x * x);
		float denomT = L * yy - (y * y);
		float denominator = denomO * denomT;
		
		R = numerator / denominator;
		
		return R;
	}
	
	public static LinkedList<Integer> GetCorrespondingDates(QuantQuoteStockFree one, QuantQuoteStockFree two)
	{
		// vars
		Set<Integer> oneKeys = one.m_db.keySet();
		LinkedList<Integer> bothKeys = new LinkedList<Integer>();
		
		// get keys contained in both one and two's data
		for(Integer i : oneKeys)
		{
			if(two.m_db.containsKey(i))
				bothKeys.add(i);
		}
		
		return bothKeys;
	}
	
	public static float[][] GetCorrespondingFeature(
			QuantQuoteStockFree one,
			QuantQuoteStockFree two,
			StockMetrics metric)
	{
		// vars
		final HashMap<Integer, StockMetricDay> dbOne = one.m_db, dbTwo = two.m_db;
		final LinkedList<Integer> dates = GetCorrespondingDates(one, two);
		final int L = dates.size();
		final float[][] pair = new float[2][];
		pair[0] = new float[L];
		pair[1] = new float[L];
		
		// construct array pair
		int i = 0;
		for(Integer date : dates)
		{
			StockMetricDay o = dbOne.get(date), t = dbTwo.get(date);
			pair[0][i] = o.GetMetric(metric);
			pair[1][i] = t.GetMetric(metric);
			i++;
		}
		
		return pair;
	}
}


















