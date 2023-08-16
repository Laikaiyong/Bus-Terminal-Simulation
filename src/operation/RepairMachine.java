/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package operation;

import java.util.Random;
import model.TicketMachine;

public class RepairMachine implements Runnable
{
    TicketMachine ticketMachine;
    
    public RepairMachine(TicketMachine ticketMachine)
    {
        this.ticketMachine = ticketMachine;
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            if(ticketMachine.available==true)
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch(InterruptedException e){}
            }
            else
            {
                try
                {
                    Thread.sleep(5000);
                }
                catch(InterruptedException e){}
                ticketMachine.repair();
            }
        }
    }
}
