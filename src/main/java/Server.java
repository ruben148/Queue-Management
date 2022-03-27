import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Server extends Thread{
    BlockingQueue<Client> clients = new ArrayBlockingQueue<>(100);
    int waitingTime = 0;
    int id;
    SimulationManager sim;

    public Server(SimulationManager sim){
        this.sim = sim;
    }

    @Override
    public void run() {
        while(true) {
            try {
                try{
                    Thread.sleep(clients.element().executionTime * 1000L);
                    Client c = clients.take();
                    System.out.println("Done "+c.id);
                    sim.clientFinished(id, c);
                    waitingTime -= c.executionTime;
                }
                catch(NoSuchElementException e){}
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void addTask(Client client){
        try {
            clients.put(client);
            waitingTime += client.executionTime;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String toString(){
        String r = "";
        for(Client c:clients){
            r+=c.toString()+"; ";
        }
        return r;
    }
}
