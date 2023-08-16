package counter;

public class BusNoGenerator
{
    private static Counter counter = new Counter();
    
    public synchronized int getBusNumber()
    {
        counter.increment();
        return counter.value();
    }
}
