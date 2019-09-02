package id.web.michsan.hellotransfer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class HttpResponse {
    @Getter
    private int status;
    @Getter
    private String responseBody;
    @Getter
    private Map<String, List<String>> headerFields;
}
