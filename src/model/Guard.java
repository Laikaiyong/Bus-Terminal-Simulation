package model;


public class Guard implements Runnable
{
    Terminal busTerminal;
    
    public Guard(Terminal busTerminal)
    {
        System.out.println("Guard created.");
        this.busTerminal = busTerminal;
    }

    @Override
    public void run()
    {
        while(true)
        {   
            // foyer is full, guard proceed to block
            if(busTerminal.busTerminalSem.availablePermits()==0)
            {
                System.out.println("Foyer is full.");
                busTerminal.block();
            }
        }
    }
}

