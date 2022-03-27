import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SButton extends JButton {
    int serverNumber;
    public SButton(String s, int i){
        super(s);
        serverNumber = i;
    }
}
