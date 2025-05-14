package tcp.server;

import java.util.HashMap;

//gi obrabotuva podatocite
public class ReuestProcessor {

    private String command;// dali e get ili post
    private String uri;//localhost:900/time   time e uri
    private String version;
    private HashMap<String, String> headers;


    //ovde e niza od string za posle da mojt da gi obrabotvit linija po linija
    private ReuestProcessor(String[] request) {
        String cmd = request[0];
        String[] parts = cmd.split("\\s");
        this.command = parts[0];
        this.uri = parts[1];
        this.version = parts[2];
        headers = new HashMap<>();

        for (int i = 1; i < request.length; i++) {
            parts = request[i].split(":\\s");
            headers.put(parts[0], parts[1]);
        }
    }
    // staticen bilder metod za da mojme da kreirame nekoja nova istanca
    public static ReuestProcessor of(String[] request) {
        return new ReuestProcessor(request);
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public String getCommand() {
        return command;
    }

    public String getUri() {
        return uri;
    }

    public String getVersion() {
        return version;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }
}
