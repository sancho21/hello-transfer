package id.web.michsan.hellotransfer;

import org.apache.commons.io.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

public class HttpUtils {

    public static HttpResponse readResponse(HttpURLConnection con) throws IOException {
        int status = con.getResponseCode();

        if (status > 299) {
            return new HttpResponse(status, IOUtils.toString(con.getErrorStream(), Charset.forName("UTF-8")),
                    con.getHeaderFields());
        } else {
            HttpResponse response = new HttpResponse(status, IOUtils.toString(con.getInputStream(), Charset.forName(
                    "UTF-8")), con.getHeaderFields());
            return response;
        }
    }

    public static void writeRequestBody(HttpURLConnection con, String requestBody) throws IOException {
        con.setDoOutput(true);
        try (DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
            out.writeBytes(requestBody);
            out.flush();
        }
    }

}
