package pk.wei.com.gserver.LibraryService;


import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Request {
    private static final String TAG = "Request: ";
    private String mMethod;
    HashMap<String, String> mMethodParams;
    private String mUri;
    //        private String mProtocol;
    HashMap<String, String> mHeaderFlied = new HashMap<>();
    private String mBody;

    private static final int BUFFER_LENGTH = 1024;
    private InputStream mInputStream;

    public Request(InputStream input) {
        mInputStream = input;
    }

    public void parse() {
        StringBuffer request = new StringBuffer();
        byte[] buffer = new byte[BUFFER_LENGTH];
        int length;
        try {
            length = mInputStream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            length = -1;
        }

        for (int i = 0; i < length; i++) {
            request.append((char) buffer[i]);
        }

        int lineStart = 0;
        int lineEnd = request.indexOf("\r\n");
        String line = request.substring(lineStart, lineEnd);
        parseHeader(line);
        lineStart = lineEnd + 2;

        while (lineStart != request.length() - 2) {
            lineEnd = request.indexOf("\r\n", lineStart);
            line = request.substring(lineStart, lineEnd);
            lineStart = lineEnd + 2;
            if (line.isEmpty())
                break;
            parseValues(line);
        }

        lineEnd = request.length();
        mBody =  request.substring(lineStart, lineEnd);

        Log.d(TAG, "parse(): " + request.toString());
    }

    private void parseHeader(String line) {
        String[] headerWord = Pattern.compile("[ ]+").split(line);
        mMethod = headerWord[0];
        String[] url = Pattern.compile("[?]+").split(headerWord[1]);
        mUri = url[0];
        if (url.length > 1) {
            String[] params = Pattern.compile("[&]+").split(url[1]);
            mMethodParams = new HashMap<>();
            for (String value : params) {
                String[] param = Pattern.compile("[=]").split(value);
                mMethodParams.put(param[0], param[1]);
            }
        }
//            mProtocol = headerWord[3];  // Todo 保存协议版本
    }

    private void parseValues(String line) {
        String[] valueWord = Pattern.compile("[:]+").split(line);
        mHeaderFlied.put(valueWord[0], valueWord[1]);
    }

    public String getUri() {
        return mUri;
    }

    public String getMethod() {
        return mMethod;
    }

    public HashMap<String, String> getmMethodParams() {
        return mMethodParams;
    }

    public String getBody() {
        return mBody;
    }
}
