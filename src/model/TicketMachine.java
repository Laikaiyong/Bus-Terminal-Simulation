package model;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class TicketMachine 
{
    Semaphore tmSem=new Semaphore(1,true);
    Ticket ticket;
    int nQueue;
    public boolean available;
    
    public TicketMachine()
    {
        available=true;
        System.out.println("Ticketing Machine created.");
    }
    
    public Ticket getTicket(Customer customer, String destination)
    {
        if(tmSem.availablePermits() == 0)
        {
            nQueue++;
            System.out.println("Customer " + customer.custID + " is queuing the ticketing machine (Machine: " + nQueue +"/5)");
        }
        try
        {
            tmSem.acquire();
        }
        catch(InterruptedException e){}
        if(available == false)
        {
            System.out.println("Customer " + customer.custID + " exit the queue from ticketing machine");
            tmSem.release();
            return null;
        }
        else
        {
            if(nQueue > 0)
            {
                nQueue--;
            }
            System.out.println("Customer " + customer.custID + " is using the ticketing machine");
            try
            {
                Thread.sleep(500);
            }
            catch(InterruptedException e){}
            // 10% probability ticket machine break            
            int rand = new Random().nextInt(10);
            if(rand == 0)
            {
                breakDown();
                System.out.println("Customer " + customer.custID + " haven't receive any Ticket");
                System.out.println("Customer " + customer.custID + " is leaving the ticketing machine");
                tmSem.release();
                return null;
            }
            try
            {
                Thread.sleep(500);
            }
            catch(InterruptedException e){}
            ticket = new Ticket(destination);
            System.out.println("Customer " + customer.custID + " got a ticket from the ticket machine.");
            tmSem.release();
            return ticket;
        }
    }
    
    public void breakDown()
    {
        available = false;
        System.out.println("Ticket machine has breakdown... Ticket machine cannot be use...");
    }
    
    public void repair()
    {
        System.out.println("Ticket machine is fixed... Ticket machine can be use...");
        available = true;
    }
}

