package archivo.digital.android;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author https://archivo.digital
 */
public interface ADJSONAware<T> {

    T mapFromJSONObject(JSONObject object) throws JSONException;

    JSONObject toJSONObject() throws JSONException;

}
