public class Client {
    int id;
    int runTime;
    int executionTime;
    int entry;
    int exit;
    public Client(int id, int runTime, int executionTime){
        this.id = id;
        this.runTime = runTime;
        this.executionTime = executionTime;
    }
    public String toString() {
        String r = "(" + id + "," + runTime + "," + executionTime + ")";
        return r;
    }
}
