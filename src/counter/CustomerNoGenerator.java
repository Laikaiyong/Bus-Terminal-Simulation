/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package counter;

/**
 *
 * @author vandycklai
 */
public class CustomerNoGenerator {
    private static Counter counter = new Counter();
    
    public synchronized int getCustomerNumber()
    {
        counter.increment();
        return counter.value();
    }
}
