package operation;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import model.Bus;
import counter.BusNoGenerator;
import model.WaitingArea;

public class BusManager implements Runnable
{
    String destination;
    WaitingArea waitingArea;
    ExecutorService terminal;
    BusNoGenerator busNoGenerator;
    boolean waitFull;
    LinkedList<Future<Bus>> buses;
    
   public BusManager(
        BusNoGenerator busNoGenerator,
        String destination,
        WaitingArea waitingArea,
        ExecutorService terminal,
        LinkedList<Future<Bus>> buses
   )
   {
       this.busNoGenerator = busNoGenerator;
       this.waitingArea = waitingArea;
       this.destination = destination;
       this.terminal = terminal;
       this.buses = buses;
   }
   
   @Override
   public void run()
   {
       while(true)
       {
           CountDownLatch latch = new CountDownLatch(10);
           int busID = busNoGenerator.getBusNumber();
           int rand = new Random().nextInt(2);
           waitFull = (rand == 0);
           while(true)
           {
               if(waitingArea.busDock.hasQueuedThreads()==true)
               {
                   try
                   {
                        Thread.sleep(5000);
                   }
                   catch(InterruptedException e){}
               }
               else
               {
                   break;
               }
           }
           Future<Bus> futureBus = terminal.submit(
                new Bus(
                    busID,
                    destination,
                    waitingArea,
                    latch,
                    waitFull
                )
           );
           buses.addLast(futureBus);
           
           // 30 seconds interval           
           try
           {
               Thread.sleep(30000);
           }
           catch(InterruptedException e){}
       }
   }
}
