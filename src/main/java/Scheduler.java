import java.util.ArrayList;

public class Scheduler {
    public ArrayList<Server> servers;
    SimulationManager sim;
    public Scheduler(SimulationManager sim, int Q) {
        servers = new ArrayList<>(Q);
        this.sim = sim;
        for (int i = 0; i < Q; i++) {
            Server s = new Server(sim);
            s.id=i;
            servers.add(s);
        }
    }
    public int dispatchTask(Client client){
        int min=10000;
        int imin=0;
        Server smin = null;
        for(Server s : servers){
            if(s.waitingTime < min) {
                min = s.waitingTime;
                smin = s;
                imin = s.id;
            }
        }

        try {
            smin.start();
        }
        catch(Exception e){}
        smin.addTask(client);
        int wt=0;
        for(Server s: servers)
            wt+=s.waitingTime;
        sim.changePeakHour(wt);
        return imin;
    }
}
