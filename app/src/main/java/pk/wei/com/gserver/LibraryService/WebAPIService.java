package pk.wei.com.gserver.LibraryService;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class WebAPIService extends Thread {
    private static final String TAG = "WebAPIService";
    private Context mContext;
    private class Config {
        public static final int PORT = 8080;
        public static final int BACKLOG = 128;
    }

    public WebAPIService(Context context) {
        mContext = context;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                await();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                Log.d(TAG, "run(): UnknownHostException");
                Thread.currentThread().interrupt();
            }
        }
        Log.d(TAG, " run(): stop." );
    }

    public void stopService() {
        interrupt();
    }

    public void await() throws UnknownHostException {
        ServerSocket serverSocket = null;

        try {
            String Ip = getWIFILocalIpAddress(mContext);
            serverSocket = new ServerSocket(Config.PORT, Config.BACKLOG, InetAddress.getByName(Ip));
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "await: new Socket failed.");
        }

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                Request request = new Request(inputStream);

                request.parse();

                OutputStream outputStream = socket.getOutputStream();
                Response response = new Response(outputStream);
                response.setRequest(request);

                response.handleRequest();

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getWIFILocalIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = formatIpAddress(ipAddress);
        return ip;
    }

    private static String formatIpAddress(int ipAddress) {
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }

}
