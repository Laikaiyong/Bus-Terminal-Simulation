package model;

import java.util.concurrent.Semaphore;

public class TicketCounter 
{
    Semaphore tcSem=new Semaphore(1,true);
    int nQueue = 0;
    Ticket ticket;
    String ticketCounterName;
    boolean available;
    boolean servingCustomer;
    
    public TicketCounter(String ticketCounterName)
    {
        this.ticketCounterName = ticketCounterName;
        available = true;
        System.out.println("Counter " + ticketCounterName + " created.");
    }
    
    public void goToilet()
    {
        available = false;
        System.out.println("Staff from Counter " + ticketCounterName + " is going to toiletbreak");
        System.out.println("Counter " + ticketCounterName + " is closed");
    }
    
    public void returnFromToilet()
    {
        System.out.println("Staff from Counter " + ticketCounterName + " has return");
        System.out.println("Counter " + ticketCounterName + " is open");
        available = true;
    }
    
    public Ticket getTicket(Customer customer, String destination)
    {
        if(tcSem.availablePermits() == 0)
        {
            nQueue++;
            System.out.println("Customer " + customer.custID + " is queuing Counter " + ticketCounterName + " (Counter " + ticketCounterName + ": " + nQueue +"/5)");
        }
        try
        {
            tcSem.acquire();
        }
        catch(InterruptedException e){}
        
        if(available == false)
        {
            System.out.println("Customer " + customer.custID + " exit the queue at Counter " + ticketCounterName);
            tcSem.release();
            return null;
        }
        else
        {
            if (nQueue > 0)
            {
                nQueue--;
            }
            servingCustomer = true;
            System.out.println("Customer " + customer.custID + " is at Counter " + ticketCounterName);
            try
            {
                Thread.sleep(3000);
            }
            catch(InterruptedException e){}
            ticket = new Ticket(destination);
            System.out.println("Customer " + customer.custID + " got a ticket from Counter " + ticketCounterName);
            servingCustomer=false;
            tcSem.release();
            return ticket;
        }
        
    }
}
