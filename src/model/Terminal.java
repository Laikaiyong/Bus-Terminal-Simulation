package model;

import operation.RepairMachine;
import counter.BusNoGenerator;
import counter.CustomerNoGenerator;
import operation.BusManager;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

public class Terminal extends Thread
{
    Semaphore busTerminalSem = new Semaphore(15,true);
    Semaphore gateSem = new Semaphore(1,true);

    WaitingArea waitAreaOne;
    WaitingArea waitAreaTwo;
    WaitingArea waitAreaThree;
    
    TicketCounter ticketCounterOne;
    TicketCounter ticketCounterTwo;
    TicketMachine ticketMachine;
    
    Inspector ticketInspector;
    
    BusNoGenerator busNoGenerator;
    CustomerNoGenerator cusNoGenerator;
    
    LinkedList<Future<Bus>> busesOne;
    LinkedList<Future<Bus>> busesTwo;
    LinkedList<Future<Bus>> busesThree;
    
    boolean open;
    
    public Terminal()
    {
        System.out.println("Terminal created.");
        open=true;
        
        ticketMachine = new TicketMachine();
        
        busNoGenerator = new BusNoGenerator();
        cusNoGenerator = new CustomerNoGenerator();
        
        waitAreaOne = new WaitingArea("One", this);
        waitAreaTwo = new WaitingArea("Two", this);
        waitAreaThree = new WaitingArea("Three", this);
        
        ticketCounterOne = new TicketCounter("1");
        ticketCounterTwo = new TicketCounter( "2");
        ticketInspector = new Inspector();
        
        busesOne = new LinkedList<>();
        busesTwo = new LinkedList<>();
        busesThree = new LinkedList<>();
    }
    
    @Override
    public void run()
    {
        ExecutorService terminal=Executors.newCachedThreadPool();

        terminal.submit(new Guard(this));
        
        terminal.submit(new RepairMachine(ticketMachine));
        terminal.submit(new Staff(ticketCounterOne));
        terminal.submit(new Staff(ticketCounterTwo));
        
        terminal.submit(new BusManager(busNoGenerator, "One", waitAreaOne, terminal, busesOne)); 
        terminal.submit(new BusManager(busNoGenerator, "Two", waitAreaTwo, terminal, busesTwo));
        terminal.submit(new BusManager(busNoGenerator, "Three", waitAreaThree, terminal, busesThree));
        
        int custID = 0;
        while(custID <= 79)
        {
            custID = cusNoGenerator.getCustomerNumber();
            terminal.submit(new Customer(
                    custID, this,
                    ticketCounterOne, ticketCounterTwo,
                    ticketMachine, waitAreaOne, waitAreaTwo,
                    waitAreaThree, ticketInspector)
            ); 
            
            try
            {
                Thread.sleep(1000 * (1 + new Random().nextInt(3)));
            }
            catch(InterruptedException e){}
        }
    }
    
    // Foyer Block    
    public void block()
    {
        open=false;
        System.out.println("Bus Terminal entrance is block");
        try 
        {
            busTerminalSem.acquire(15);
            System.out.println("Bus Terminal entrance is open");
            busTerminalSem.release(15);
        }
        catch(InterruptedException e){}

        open=true;
    }
    
    public void enterTerminal(Customer customer)
    {
        if(open == false)
        {
            System.out.println(
                "Guard is blocking the entrance... Customer " +
                customer.custID + " is waiting..."
            );
        }
        try
        {
            gateSem.acquire();
            busTerminalSem.acquire();
            Thread.sleep(10);
            System.out.println(
                "Customer " + customer.custID +
                " has entered the Bus Terminal (Foyer: " +
                (15 - busTerminalSem.availablePermits()) + "/15)"
            );
            Thread.sleep(100);
            gateSem.release();
        }
        catch(InterruptedException e){}  
    }
    
    public void leaveTerminal(Customer customer)
    {
        System.out.println(
            "Customer " + customer.custID +
            " is leaving the Bus Terminal"
        );
        busTerminalSem.release();
    }
}
