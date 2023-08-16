package model;

public class Ticket 
{
    String destination;
    boolean scanned;
    boolean inspected;
    
    public Ticket(String destination)
    {
        this.destination=destination;
    }
    
    public void scanTicket()
    {
        this.scanned = true;
    }
    
    public void inspectTicket()
    {
        this.inspected = true;
    }
}

