package model;

import java.util.concurrent.Semaphore;

public class WaitingArea 
{
    public Semaphore busDock=new Semaphore(1, true);
    Semaphore enterBus=new Semaphore(1, true);
    Semaphore seatingWaitArea=new Semaphore(10, true);

    String waitAreaName;
    Bus bus = null;

    Terminal busTerminal;
    
    public WaitingArea(String waitAreaName, Terminal busTerminal)
    {
        this.waitAreaName = waitAreaName;
        this.busTerminal = busTerminal;
    }
    
    public void busArrive(Bus bus) throws InterruptedException
    {
        Thread.sleep(5000);

        // If there is already a bus
        if(busDock.availablePermits()==0)
        {
            System.out.println("There is already a bus docked in front of Waiting Area: " + waitAreaName);
            System.out.println("Bus " + bus.busID + " is waiting to be docked");
        }
        try
        {
            busDock.acquire(); 
            System.out.println("Bus " + bus.busID + " is docked at Waiting Area: " + waitAreaName);
        }
        catch(InterruptedException e){}
        this.bus = bus;        
    }
    
    public void busDepart()
    {
        System.out.println("Bus " + bus.busID + " is leaving Waiting Area: " + waitAreaName);
        bus = null;
        busDock.release();
    }
    
    public void enterWaitingArea(Customer customer)
    {
        if(seatingWaitArea.availablePermits()==0)
        {
            System.out.println(
                "Waiting Area " + waitAreaName +
                " is full... Customer " + customer.custID +
                " is waiting to enter the Waiting Area..."
            );   
        }
        try
        {
            seatingWaitArea.acquire();
            System.out.println(
                "Customer " + customer.custID +
                " has entered Waiting Area: " + waitAreaName +
                " (Wait " + waitAreaName + ": " + (10 - seatingWaitArea.availablePermits()) + ")"
            );
            
            busTerminal.leaveTerminal(customer);
        }
        catch(InterruptedException e){}
    }
    
    public void leaveWaitArea(Customer c)
    {
        System.out.println("Customer " + c.custID + " is leaving the Waiting Area: "+waitAreaName);
        seatingWaitArea.release();
    }
    
    public void enterBus(Customer c)
    {          
        try
        {
            enterBus.acquire();
        }
        catch(InterruptedException e){}
        while(true)
        {
            if(bus != null)
            {
                bus.enterBus(c);
                enterBus.release();
                break;
            }
            else
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch(InterruptedException e){}
            }
        }
    }
}

