import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import java.awt.TrayIcon.MessageType;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

public class App {
    SystemTray tray = SystemTray.getSystemTray();
    Image image = Toolkit.getDefaultToolkit().createImage( ClassLoader.getSystemResource("icon.png") );
    TrayIcon icon = new TrayIcon(image);
    
    public App() throws Exception{
        // Nortification init
        icon.setImageAutoSize(true);
        try {
            tray.add(icon); // システムトレイに追加
        } catch (AWTException e) {
            e.printStackTrace();
        }

        // main 
        String ip = getIP(NetworkInterface.getNetworkInterfaces());
        if (ip == null){
            Nortification(1);
            System.exit(1);
        }
        
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
            Nortification(2);
        }

        Nortification(ip);
        

        // exit
        Thread.sleep(500);
        tray.remove(icon);
    }

    public static void main(String[] args) throws Exception {
        new App();
    }
    
    private void Nortification(int stat){
        icon.displayMessage("エラー", "rdpファイルを作成できませんでした("+stat+")", MessageType.WARNING);
    }
    private void Nortification(String ip){
        icon.displayMessage("プライベートIPアドレス の取得", ip+"\nrdpファイルを作成しました", MessageType.INFO);
    }
        
    private String getIP(Enumeration<NetworkInterface> ifs) {
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
