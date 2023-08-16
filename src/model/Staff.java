package model;

public class Staff implements Runnable
{
    TicketCounter ticketCounter;
    
    public Staff(TicketCounter ticketCounter)
    {
        this.ticketCounter = ticketCounter;
        System.out.println("Staff created for Counter " + ticketCounter.ticketCounterName);
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                Thread.sleep(20000);
            }
            catch(InterruptedException e){}
            while(true)
            {
                if(ticketCounter.tcSem.tryAcquire()==true)
                {
                    break;
                }
                
            }
            ticketCounter.goToilet();
            ticketCounter.tcSem.release();
            try
            {
                Thread.sleep(10000);
            }
            catch(InterruptedException e){}
            ticketCounter.returnFromToilet();
        }
    }
}

