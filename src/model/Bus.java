package model;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Bus implements Callable<Bus>
{
    int busID;

    CountDownLatch latch;
    WaitingArea waitArea;

    LinkedList<Customer> customers = new LinkedList<>();
    String destination;
    boolean waitFull;
    
    public Bus(
        int busID,
        String destination,
        WaitingArea waitArea,
        CountDownLatch latch,
        boolean waitFull
    )
    {
        this.busID = busID;
        this.destination = destination;
        this.waitArea = waitArea;
        this.latch = latch;
        this.waitFull = waitFull;
    }
    
    public void enterBus(Customer customer)
    {
        waitArea.leaveWaitArea(customer); 
        customers.addFirst(customer);
        latch.countDown();

        System.out.println(
            "Customer " + customer.custID +
            " has entered Bus: " + busID +
            " (Bus " + busID + ": " +
            (10 - latch.getCount()) + "/10)"
        );

        try
        {
            Thread.sleep(100);
        }
        catch(InterruptedException e){}
    }
    
    public void departWithDetails()
    {
        System.out.println(
            "All (" + (10 - latch.getCount()) +
            ") customers in Bus " + busID + " :"
        );

        for(Customer customer: customers)
        {
            System.out.println(
                "Customer  " + customer.custID +
                " is in Bus " + busID
            );
        }
        waitArea.busDepart();
    }
    
    @Override
    public Bus call() throws Exception
    {
        waitArea.busArrive(this);

        if(waitFull == true)
        {
            System.out.println(
                "Bus " + busID +
                " is waiting to be full before departing"
            );
            latch.await();
            
            System.out.println("Bus: " + busID + " is full");
            
            departWithDetails();
        }
        else
        {
            System.out.println(
                "Bus " + busID + 
                " will depart in 50 seconds (or full)"
            );
            latch.await(50, TimeUnit.SECONDS);

            if(customers.size() == 10 || 10 == latch.getCount())
            {
                System.out.println("Bus " + busID + " is full");
            }
            else
            {
                System.out.println("Bus " + busID + " has waited 50 seconds");
            }
                        
            departWithDetails();
        }
        return this;
    }
}
