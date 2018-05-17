package pk.wei.com.gserver.LibraryService;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.regex.Pattern;

import pk.wei.com.gserver.DataManager;

public class Response {

    private static final int BUFFER_LENGTH = 1024;
    private Request mRequest;
    private OutputStream mOutputStream;

    private DataManager dataManager;

    public Response(OutputStream output) {
        mOutputStream = output;
        dataManager = DataManager.getInstance();
    }

    public void setRequest(Request request) {
        mRequest = request;
    }

    public void handleRequest() throws IOException {
        switch (mRequest.getMethod()) {
            case "GET":
                handleGET();
                break;
            case "POST":
                handlePOST();
                break;
            default:
                String messageBody = createMessage(404, "Bad Method\n", "text/html");
                mOutputStream.write(messageBody.getBytes());
        }
    }

    private void handleGET() throws IOException {
        String path = mRequest.getUri();
        String[] requestWord = Pattern.compile("[/]+").split(path);
        if (requestWord.length > 3) {
            switch (requestWord[1]) {
                case "service":
                    service(requestWord[2], requestWord[3]);
                    break;
                default:
                    mOutputStream.write(statusCode_404().getBytes());
            }
        } else {
            mOutputStream.write(statusCode_404().getBytes());
        }
    }

    private void handlePOST() throws IOException {
        String path = mRequest.getUri();
        String[] pathWord = Pattern.compile("[/]+").split(path);
        if (pathWord.length > 3) {
            switch (pathWord[1]) {
                case "service":
                    service(pathWord[2], pathWord[3]);
                    break;
                default:
                    mOutputStream.write(statusCode_404().getBytes());
            }
        } else {
            mOutputStream.write(statusCode_404().getBytes());
        }
    }

    private void service(String version, String function) throws IOException {
        String messageBody = "";
        switch (function) {
            case "login":
                HashMap<String, String> params = mRequest.getmMethodParams();
                String userName =params.get("name");
                int check = dataManager.login(userName);
                if (check == 1) {
                    String body = CreateJson(202, function, "LOGIN_OK");
                    messageBody = createMessage(202, body, "application/json");
                }
                break;
            default:
                messageBody = statusCode_404();
        }
        mOutputStream.write(messageBody.getBytes());
    }

    private String statusCode_404() {
        String body = CreateJson(404,
                mRequest.getUri() + " 错误接口，请更正后重试\"",
                "");
        return createMessage(404, body, "application/json");
    }

    private String createJsonMessage(int code, String message, String result) {
        String body = CreateJson(code, message, result);
        return createMessage(202, body, "application/json");
    }

    private String CreateJson(int code, String message, String result) {
        return "{\n" +
                "    \"code\":" + code + ",\n" +
                "    \"message\":\"" + message + "\",\n" +
                "    \"result\":\"" + result + "\"\n" +
                "}\n";
    }

    private String createMessage(int code, String body, String type) {
        String codeText = "";
        switch (code) {
            case 202:
                codeText = code + " OK";
                break;
            case 400:
                codeText = code + " Bad Request";
                break;
            case 404:
                codeText = code + " Not Found";
                break;
        }
        return "HTTP/1.1 " + codeText + "\r\n"
                + "Content-Type: " + type + ";charset=utf-8\r\n"
                + "Content-Length: " + stringLength(body) + "\r\n" + "\r\n"
                + body;
    }

    public int stringLength(String s) {
        try {
            return s.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
