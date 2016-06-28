package digital.archivo.archivodigital_androidlib;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author https://archivo.digital
 */
public interface JSONAware<T> {

    T mapFromJSONObject(JSONObject object) throws JSONException;

    JSONObject toJSONObject() throws JSONException;

}
