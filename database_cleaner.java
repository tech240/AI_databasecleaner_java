import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class database_cleaner {
    public static void main(String[] args) {
        //add main function to system java
        new systemUI();
        
    }
}

// Database add
class DBConnection{

    static String url;
    static String user;
    static String pass;

    public static void setConfig(String db,String username,String password){
        url = "jdbc:mysql://localhost:3306/" + db;
        user = username;
        pass = password;
    }

    public static Connection connect(){
        try {
            return DriverManager.getConnection(url,user,pass);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


// servise java
class cleanservice{
    public static void removeDuplicate(){

        try {
            Connection con = DBConnection.connect();
            String sql = "DELETE t1 FROM users t1" +
            "INNER JOIN users t2"+
            "WHERE t1.id > t2.id AND t1.email = t2.email";

            PreparedStatement ps = con.prepareStatement(sql);
            int rows = ps.executeUpdate();

            System.out.println("Duplicate removed" + rows);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteEmpty(){
        
        try {
            Connection con = DBConnection.connect();
            String sql = "DELETE FROM users WHERE name IS NULL OR name = ''" ;

            PreparedStatement ps = con.prepareStatement(sql);
            int rows = ps.executeUpdate();

            System.out.println("Empty Data removed" + rows);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void deleteOld(){
        
        try {
            Connection con = DBConnection.connect();
            String sql = "DELETE FROM users WHERE created_at < NOW() - INTERVAL 30 DAY";

            PreparedStatement ps = con.prepareStatement(sql);
            int rows = ps.executeUpdate();

            System.out.println("Old Data removed" + rows);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// system UI
class systemUI {
    public systemUI(){
        JFrame frame = new JFrame("database_cleaner");
        frame.setSize(400,400);
        frame.setLayout(new GridLayout(4,1));

        // inputs
        JTextField database = new JTextField("database");
        JTextField username = new JTextField("username");
        JTextField password = new JTextField("password");


        // add buttons
        JButton btn1 = new JButton("Remove Duplicates"); 
        JButton btn2 = new JButton("Delete Empty Data"); 
        JButton btn3 = new JButton("Delete old Data");

        JButton connectBtn = new JButton("Connect DB");

        //Status
        JLabel status = new JLabel("Status : Ready", JLabel.CENTER);

        //connect database
        connectBtn.addActionListener(e -> {

            String db = database.getText();
            String user = username.getText();
            String pass = password.getText();

            DBConnection.setConfig(db, user, pass);

            Connection con = DBConnection.connect();

            if (con != null) {
                status.setText("Connected Successfully ✅");
            } else {
                status.setText("Connection Failed ❌");
            }
        });
    
        btn1.addActionListener(e->{
            cleanservice.removeDuplicate();
            status.setText("Duplicates Cleaned Successfull");
        });

        btn2.addActionListener(e->{
            cleanservice.deleteEmpty();
            status.setText("Empty Data Cleaned Successfull");
        });


        btn3.addActionListener(e->{
            cleanservice.deleteOld();
            status.setText("Old Data Cleaned Successfull");
        });

        //add inputs to frame
        frame.add(new JLabel("Database"));
        frame.add(database);

        frame.add(new JLabel("Username"));
        frame.add(username);

        frame.add(new JLabel("Password"));
        frame.add(password);

        //add button to frame
        frame.add(connectBtn);
        frame.add(btn1);
        frame.add(btn2);
        frame.add(btn3);
        frame.add(status);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }    


}
