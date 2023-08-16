package model;

import java.util.Random;

public class Customer implements Runnable
{
    int custID;
    Ticket ticket = null;
    String destination;
    
    Terminal busTerminal;
    
    TicketCounter ticketCounterA;
    TicketCounter ticketCounterB;
    TicketMachine ticketMachine;

    WaitingArea waitAreaOne;
    WaitingArea waitAreaTwo;
    WaitingArea waitAreaThree;
    WaitingArea waitAreaCurrent;

    Inspector ticketInspector;
    
    public Customer(
        int custID,
        Terminal busTerminal,
        TicketCounter ticketCounterA,
        TicketCounter ticketCounterB,
        TicketMachine ticketMachine,
        WaitingArea waitAreaOne,
        WaitingArea waitAreaTwo,
        WaitingArea waitAreaThree,
        Inspector ticketInspector
    )
    {
        this.custID = custID;
        this.busTerminal = busTerminal;
        
        this.ticketCounterA = ticketCounterA;
        this.ticketCounterB = ticketCounterB;
        this.ticketMachine = ticketMachine;
        
        this.waitAreaOne = waitAreaOne;
        this.waitAreaTwo = waitAreaTwo;
        this.waitAreaThree = waitAreaThree;
        
        this.ticketInspector = ticketInspector;
    }
    
    @Override
    public void run()
    {
        System.out.println("Customer " + custID + " created.");
        
        busTerminal.enterTerminal(this);

        int randNum = new Random().nextInt(3);
        switch (randNum) {
            case 0:
                destination="One";
                waitAreaCurrent=waitAreaOne;
                break;
            case 1:
                destination="Two";
                waitAreaCurrent=waitAreaTwo;
                break;
            default:
                destination="Three";
                waitAreaCurrent=waitAreaThree;
                break;
        }
        
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException e){}

        while(ticket == null)
        {
            randNum = new Random().nextInt(3);
            switch (randNum) {
                case 0:
                    if(ticketMachine.available == true)
                    {
                        ticket = ticketMachine.getTicket(this, destination);
                    }   break;
                case 1:
                    if(ticketCounterA.available == true)
                    {
                        ticket = ticketCounterA.getTicket(this, destination);
                    }   break;
                case 2:
                    if(ticketCounterB.available == true)
                    {
                        ticket = ticketCounterB.getTicket(this, destination);
                    }   break;
                default:
                    try
                    {
                        Thread.sleep(10);
                    }
                    catch(Exception e){}
                    break;
            }
        }
        
        try
        {
            Thread.sleep(2500);
        }
        catch(Exception e){}
        
        System.out.println("Customer " + custID + " is going to Waiting Area " + ticket.destination);
        
        waitAreaCurrent.enterWaitingArea(this);

        ticketInspector.inspectingTicket(this);
 
        waitAreaCurrent.enterBus(this);
    }
}
