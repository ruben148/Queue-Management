import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class SimulationManager extends Thread{
    Scheduler scheduler;
    ArrayList<Client> clients;
    public int timeLimit;
    public int numberOfClients;
    public int numberOfServers;
    public int minProcessingTime;
    public int maxProcessingTime;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int waitTime;
    public int serviceTime;
    public int maxWaitTime=0;
    public int peakHour;
    ArrayList<Integer> heads;

    File file;
    FileWriter writer;
    double currentTime;
    Gui gui = new Gui();

    NumberFormat formatter = new DecimalFormat("0.00");

    public SimulationManager() {
        gui.generate.addActionListener(e -> {
            numberOfClients = Integer.parseInt(gui.numberOfClients.getText());
            numberOfServers = Integer.parseInt(gui.numberOfQueues.getText());
            timeLimit = Integer.parseInt(gui.simulationTime.getText());
            minProcessingTime = Integer.parseInt(gui.serviceTime1.getText());
            maxProcessingTime = Integer.parseInt(gui.serviceTime2.getText());
            minArrivalTime = Integer.parseInt(gui.arrivalTime1.getText());
            maxArrivalTime = Integer.parseInt(gui.arrivalTime2.getText());
            heads = new ArrayList<>(numberOfServers);
            gui.progress.setMaximum(timeLimit*100);
            gui.newSPane();
            gui.gen(numberOfServers);
        });
        gui.start.addActionListener(e -> {
            clients = randomClientGenerator(numberOfClients, minArrivalTime, maxArrivalTime, minProcessingTime, maxProcessingTime);
            int fileName = 0;
            file = new File("log.txt");
            try {
                while (!file.createNewFile()){
                    fileName++;
                    file = new File("log"+fileName+".txt");
                }
            }
            catch(Exception r){}

            try {
                writer = new FileWriter(file.getName());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            gui.progress.setValue(0);
            start();
        });
    }

    public void clientFinished(int q, Client c){
        c.exit = (int)currentTime;
        waitTime += c.exit - c.entry;
        int wt=0;
        for(Server s: scheduler.servers)
            wt+=s.waitingTime;
        changePeakHour(wt);
        gui.removeClient(q);
    }

    public void changePeakHour(int wt){
        if(wt>maxWaitTime) {
            maxWaitTime = wt;
            peakHour = (int)currentTime;
        }
    }

    public ArrayList<Client> randomClientGenerator(int n, int arr1, int arr2, int ser1, int ser2){
        Random rand = new Random();
        ArrayList<Client> _clients = new ArrayList<>(n);
        for(int i=0;i<n;i++){
            Client client = new Client(i,rand.nextInt((arr2 - arr1) + 1) + arr1,rand.nextInt((ser2 - ser1) + 1) + ser1);
            _clients.add(client);
        }
        _clients.sort(Comparator.comparingInt(m -> m.runTime));
        for(Client c:_clients){
            serviceTime+=c.executionTime;
            System.out.println(c);
        }
        return _clients;
    }

    @Override
    public void run() {
        currentTime=0;
        scheduler = new Scheduler(this, numberOfServers);
        Client c;
        while(currentTime<timeLimit+0.1){

            for(int i=0;i<clients.size();i++) {
                c = clients.get(i);
                if(c.runTime <= currentTime){
                    int j = scheduler.dispatchTask(c);
                    gui.addClient(Gui.getCButton(Integer.toString(c.id)),j);
                    c.entry = (int)currentTime;
                    clients.remove(i);
                    i--;
                }
            }
            if(Math.ceil(currentTime) == currentTime) {
                try {
                    writer.write("\nTime " + currentTime + "\n");
                    writer.write("\nWaiting clients: ");
                    for (Client client : clients)
                        writer.write(client.toString() + "; ");
                    writer.write("\n");
                    for (Server s : scheduler.servers) {
                        writer.write("Queue " + s.id + ": " + s.toString() + "\n");
                    }
                } catch (IOException e) {}
            }
            gui.progress.setValue((int)(100*currentTime));
            System.out.println(currentTime);
            currentTime += 0.25;
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {}
            gui.revalidate();
            gui.repaint();
        }
        try {
            writer.write("\nAverage service time: "+formatter.format(1.0*serviceTime/numberOfClients) + "\n");
            writer.write("\nAverage wait time: "+formatter.format(1.0*waitTime/numberOfClients)+"\n");
            writer.write("\nPeak hour: "+peakHour);
        }
        catch(Exception e){}
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
            writer.close();
        } catch (IOException e) {}
        gui.avgServiceTime.setText("Average service time: "+formatter.format(1.0*serviceTime/numberOfClients));
        gui.avgWaitTime.setText("Average wait time: "+formatter.format(1.0*waitTime/numberOfClients));
        gui.peakHour.setText("Peak hour: "+peakHour);
        gui.revalidate();
        gui.repaint();
    }
}
