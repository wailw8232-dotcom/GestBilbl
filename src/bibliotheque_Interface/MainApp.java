 package bibliotheque_Interface;
import javax.swing.*;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;


public class MainApp {
	public static void main(String[] args) {
		
		FlatMacDarkLaf.setup();
    	
    	
        new LoginForm();
    }
}