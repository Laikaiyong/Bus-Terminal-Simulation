package model;

import java.util.concurrent.Semaphore;

public class Inspector 
{
    Semaphore semInspector = new Semaphore(1);

    // Default waiting area Two
    String location = "Two";
    
    public Ticket inspectingTicket(Customer customer)
    {
        if(!location.equals(customer.ticket.destination) || semInspector.availablePermits() == 0)
        {
            System.out.println("Customer "+ customer.custID + " is waiting for the Inspector");
        }
        try
        {
            semInspector.acquire();
        }
        catch(InterruptedException e){}
        if(!location.equals(customer.ticket.destination))
        {
            System.out.println("Inspector is moving to Waiting Area: " + customer.ticket.destination);
            location = customer.ticket.destination;
            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e){}
            System.out.println("Inspector is in Waiting Area: " + customer.ticket.destination);
        }
        System.out.println("Inspector is inspecting Customer " + customer.custID + "'s Ticket");
        try
        {
            Thread.sleep(500);
        }
        catch(InterruptedException e){}

        customer.ticket.inspectTicket();
        System.out.println("Customer " + customer.custID + "'s Ticket has been inspected and valid");
        semInspector.release();

        return customer.ticket;
    }
}
