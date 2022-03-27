import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class Gui extends JFrame {

    ArrayList<JPanel> QPanels;
    ArrayList<JScrollPane> QScrolls;
    JPanel QsPanel;


    JPanel labels = new JPanel(new GridLayout(5,1));
    JPanel textFields1 = new JPanel(new GridLayout(3,1));
    JPanel textFields2 = new JPanel(new GridLayout(2,3));
    JSplitPane textFields = new JSplitPane(JSplitPane.VERTICAL_SPLIT,textFields1,textFields2);
    JPanel input = new JPanel();
    JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

    JLabel numberOfClientsLabel = new JLabel("Number of clients");
    JLabel numberOfQueuesLabel = new JLabel("Number of queues");
    JLabel simulationTimeLabel = new JLabel("Simulation time");
    JLabel arrivalTimeLabel = new JLabel("Arrival time interval");
    JLabel serviceTimeLabel = new JLabel("Service time interval");
    JLabel line1 = new JLabel("-");
    JLabel line2 = new JLabel("-");

    JButton generate = new JButton("Generate");
    JButton start = new JButton("Start");

    JPanel buttons = new JPanel();

    JTextField numberOfClients = new JTextField("1000");
    JTextField numberOfQueues = new JTextField("20");
    JTextField simulationTime = new JTextField("30");
    JTextField arrivalTime1 = new JTextField("1");
    JTextField arrivalTime2 = new JTextField("25");
    JTextField serviceTime1 = new JTextField("1");
    JTextField serviceTime2 = new JTextField("6");

    JLabel avgWaitTime = new JLabel("Average wait time: ");
    JLabel avgServiceTime = new JLabel("Average service time: ");
    JLabel peakHour = new JLabel("Peak hour: ");

    JProgressBar progress = new JProgressBar();

    public Gui(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);

        buttons.add(generate);
        buttons.add(start);
        buttons.add(progress);
        buttons.add(avgWaitTime);
        buttons.add(avgServiceTime);
        buttons.add(peakHour);


        input.setLayout(new GridLayout(1,3));
        input.add(labels);
        input.add(textFields);
        input.add(buttons);

        line1.setHorizontalAlignment(SwingConstants.CENTER);
        line2.setHorizontalAlignment(SwingConstants.CENTER);

        labels.add(numberOfClientsLabel);
        labels.add(numberOfQueuesLabel);
        labels.add(simulationTimeLabel);
        labels.add(arrivalTimeLabel);
        labels.add(serviceTimeLabel);

        textFields1.add(numberOfClients);
        textFields1.add(numberOfQueues);
        textFields1.add(simulationTime);

        textFields2.add(arrivalTime1);
        textFields2.add(line1);
        textFields2.add(arrivalTime2);
        textFields2.add(serviceTime1);
        textFields2.add(line2);
        textFields2.add(serviceTime2);

        textFields.setDividerSize(1);
        textFields.setContinuousLayout(true);
        textFields.setDividerLocation(.66);
        pane.setDividerSize(1);
        pane.setContinuousLayout(true);
        pane.setDividerLocation(.21);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                textFields.setDividerLocation(.66);
                pane.setDividerLocation(.21);
            }
        });

        newSPane();

        add(pane);
        setVisible(true);
    }

    public JScrollPane newSPane(){
        QsPanel = new JPanel();
        QsPanel.setLayout(new BoxLayout(QsPanel, BoxLayout.X_AXIS));
        QPanels = new ArrayList<>();
        QScrolls = new ArrayList<>();
        JScrollPane s = new JScrollPane(QsPanel,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        pane.setBottomComponent(s);
        pane.setTopComponent(input);
        return s;
    }


    public void gen(int Q){
        JScrollPane s;
        JPanel p;
        for(int i=0;i<Q;i++){
            p = new JPanel();
            QPanels.add(i, p);
            p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
            p.add(getSButton(Integer.toString(i), i));
            s = new JScrollPane(p);
            QScrolls.add(i, s);
            QsPanel.add(s);
        }
        pane.setDividerLocation(.21);
        revalidate();
        repaint();
    }

    public static SButton getSButton(String text, int i) {
        SButton b = new SButton(text, i);
        return b;
    }
    public static JButton getCButton(String text) {
        Font f1 = new Font("Times new roman", Font.PLAIN, 10);
        JButton b = new JButton(text);
        b.setBackground(Color.YELLOW);
        b.setFont(f1);
        return b;
    }
    public void addClient(Component c, int i){
        QPanels.get(i).add(c);
        QPanels.get(i).revalidate();
        QPanels.get(i).repaint();
    }
    public void removeClient(int i){
        QPanels.get(i).remove(1);
    }
}
