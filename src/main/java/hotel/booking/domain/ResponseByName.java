package hotel.booking.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ResponseByName<K, V> extends HashMap<K, V> implements Serializable {
    private static final long serialVersionUID = -2101106112821558776L;

    public static Builder Builder() {
        return new Builder();
    }

    public static Builder Builder(String name, Object value) {
        return new Builder(name, value);
    }

    public static class Builder {
        private String name = "data";
        private Object value;

        public Builder() {}

        public Builder(String name, Object value) {
            this.name = name;
            this.value = value;
        }


        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder value(Object value) {
            this.value = value;
            return this;
        }

        public ResponseByName<String, Object> build() {
            ResponseByName<String, Object> response  = new ResponseByName<>();
            response.put(name, value);
            return  response;
        }
    }
}
