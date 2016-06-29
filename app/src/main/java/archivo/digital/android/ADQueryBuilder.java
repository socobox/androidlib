package archivo.digital.android;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author https://archivo.digital
 */
public class ADQueryBuilder {

    private JSONArray fetch;
    private JSONObject obj;
    private JSONArray required;
    private JSONArray where;
    private JSONObject currentGroup;

    public enum ANDOR {
        AND, OR
    }

    public enum OP {
        EQ("="),
        LIKE("LIKE"),
        LT("<"),
        GT(">"),
        NOT("<>");

        private String val;

        OP(String s) {
            this.val = s;
        }

        public String getValue() {
            return val;
        }
    }

    public ADQueryBuilder(int domain, String rowModel) throws JSONException {
        obj = new JSONObject();
        obj.put("domain", domain);
        obj.put("row_model", rowModel);
        required = new JSONArray();
        fetch = new JSONArray();
        where = new JSONArray();
        currentGroup = new JSONObject();
        currentGroup.put("ANDOR", ANDOR.AND);
        currentGroup.put("GROUP", new JSONArray());
    }

    public ADQueryBuilder(int domain, String rowModel, int page, int size) throws JSONException {
        obj = new JSONObject();
        obj.put("domain", domain);
        obj.put("row_model", rowModel);
        obj.put("page", page);
        obj.put("size", size);
        required = new JSONArray();
        fetch = new JSONArray();
        where = new JSONArray();
        currentGroup = new JSONObject();
        currentGroup.put("ANDOR", ANDOR.AND);
        currentGroup.put("GROUP", new JSONArray());
    }

    public ADQueryBuilder newGroup(ANDOR andor) throws JSONException {
        where.put(currentGroup);
        currentGroup = new JSONObject();
        currentGroup.put("ANDOR", andor);
        currentGroup.put("GROUP", new JSONArray());
        return this;
    }

    public ADQueryBuilder addField(OP op, ANDOR andor, String field, Object value) throws JSONException {
        JSONObject tmp = new JSONObject();
        tmp.put("ANDOR", andor.toString());
        tmp.put("FIELD", field);
        tmp.put("OP", op.getValue());
        tmp.put("VAL", value);
        currentGroup.getJSONArray("GROUP").put(tmp);
        return this;
    }

    public ADQueryBuilder addRequire(String model) {
        required.put(model);
        return this;
    }

    public ADQueryBuilder fetch(String[] models) {
        for (String model : models) {
            fetch.put(model);

        }
        return this;
    }


    public JSONObject compileWithKeys(String[] keys) throws JSONException {
        if (where.length() > 0) {
            throw new IllegalStateException("");
        }

        for (String key : keys) {
            where.put(key);
        }

        JSONObject tmp = new JSONObject();
        tmp.put("keys", where);

        if (required.length() > 0) {
            obj.put("require", required);
        }

        if (fetch.length()>0){
            obj.put("fetch", fetch);
        }

        obj.put("where", tmp);
        return obj;
    }

    public JSONObject compile() throws JSONException {
        if (currentGroup.getJSONArray("GROUP").length() > 0) {
            where.put(currentGroup);
        }

        if (where.length() > 0) {
            obj.put("where", where);
        }

        if (required.length() > 0) {
            obj.put("require", required);
        }

        if (fetch.length()>0){
            obj.put("fetch", fetch);
        }
        return obj;
    }


}
