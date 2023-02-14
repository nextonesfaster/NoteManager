package persistence;

import org.json.JSONObject;

// Interface to make a class writable.
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
