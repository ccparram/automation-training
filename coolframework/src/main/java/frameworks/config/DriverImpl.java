package frameworks.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;

/**
 * @author Juan Krzemien
 */

@JsonSerialize
class DriverImpl implements Driver {

    @JsonProperty
    private Map<String, Object> capabilities;

    @JsonProperty
    private List<String> arguments;

    @Override
    public Map<String, Object> getCapabilities() {
        return ofNullable(capabilities).orElse(emptyMap());
    }

    @Override
    public List<String> getArguments() {
        return ofNullable(arguments).orElse(emptyList());
    }

}
