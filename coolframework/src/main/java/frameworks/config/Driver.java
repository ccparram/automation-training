package frameworks.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Juan Krzemien
 */

@JsonSerialize
public class Driver {

    @JsonProperty
    private String driverServerPath = "";

    @JsonProperty
    private Map<String, Object> capabilities = new HashMap<>();

    @JsonProperty
    private List<String> arguments = new ArrayList<>();

    public String getDriverServerPath() {
        return driverServerPath;
    }

    public Map<String, Object> getCapabilities() {
        return capabilities;
    }

    public List<String> getArguments() {
        return arguments;
    }

}
