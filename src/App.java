import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import javax.swing.*;

public class App {
    public static void main(String[] args) extends JFrame throws Exception {
        
        JOptionPane.showMessageDialog(null, "Message");

        String ip = getIP(NetworkInterface.getNetworkInterfaces());
        if (ip == null) System.exit(1);

        String full_addr = "full address:s:" + ip;

        String default_properties = """
        screen mode id:i:1
        use multimon:i:0
        desktopwidth:i:1280
        desktopheight:i:720""";
        
        try {
            PrintWriter pw = new PrintWriter("./Connect.rdp");

            pw.println(full_addr);
            pw.println(default_properties);

            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static String getIP(Enumeration<NetworkInterface> ifs) {
        String result = null;

        if (null != ifs){
            while (ifs.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) ifs.nextElement();
                Enumeration<InetAddress> enuAddrs = ni.getInetAddresses();
                while (enuAddrs.hasMoreElements()) {
                    InetAddress in4 = (InetAddress) enuAddrs.nextElement();
                    if (in4.getHostAddress().startsWith("192.168.")){
                        result = in4.getHostAddress();
                    }
                }
            }
        }

        return result;
    }
}
