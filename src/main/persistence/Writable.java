package persistence;

import org.json.JSONObject;

// Interface to make a class writable.
//
// Source: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/blob/master/src/main/persistence/Writable.java
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
