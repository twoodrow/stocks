package base;

import java.util.Comparator;

public class StockRelationComparator implements Comparator<StockRelationResult>{

	@Override
	public int compare(StockRelationResult o1, StockRelationResult o2) {		
		return Float.compare(o1.result, o2.result);
	}

	
}
