package base;

public class StockMetricDay {
	
	// vars
	public float open;
	public float close;
	public float high;
	public float low;
	public float volume;

	public float GetMetric(StockMetrics metric)
	{
		switch(metric)
		{
		case open:
			return this.open;
		case close:
			return this.close;
		case high:
			return this.high;
		case low:
			return this.low;
		case volume:
			return this.volume;
		default:
			return -1;			
		}
	}
}
