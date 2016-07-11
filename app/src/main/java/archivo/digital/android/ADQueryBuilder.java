package archivo.digital.android;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author https://archivo.digital
 */
/**
 * @author https://archivo.digital
 *
 * Example
 *
 *   SELECT * from mi_modelo WHERE (profesion = 'cantante' OR
 *   profesion = 'guitarrista') AND (ganancias > 100000 AND ventas < 10)
 *
 *        ADQueryBuilder queryBuilder = new ADQueryBuilder(ApiConstants.DOMAIN,"mi_modelo");
 *            queryBuilder.whereIsEqual("profesion","cantante")
 *            .addOR()
 *            .whereIsEqual("profesion","guitarrista")
 *            .newGroup(ADQueryBuilder.ANDOR.AND)
 *            .whereGreaterThan("ganancias",100000)
 *            .addAND()   // by default is the last operator used or AND it is the first time
 *            .whereLessThan("ventas",10);
 *            JSONObject jsonObject=queryBuilder.compile();
 *
 *    INSERT INTO mi_modelo (profesion,ganancias,ventas) VALUES ('cantante',30000,9)
 *
 *        ADQueryBuilder queryBuilder = new ADQueryBuilder(ApiConstants.DOMAIN,"mi_modelo", ADQueryBuilder.TYPE.INSERT);
 *            queryBuilder.insertNewEmptyRow()
 *            .insertFieldLastRow("profesion","cantante")
 *            .insertFieldLastRow("ganancias",3000)
 *            .insertFieldLastRow("ventas",9);
 *            JSONObject jsonObject=queryBuilder.compile();
 *
 *
 *
 */


public class ADQueryBuilder {

    private JSONArray fetch;
    private JSONObject obj;
    private JSONArray required;
    private JSONArray where;
    private JSONObject currentGroup;
    private JSONArray row;
    public enum TYPE {
        INSERT, SELECT
    }

    public enum ANDOR {
        AND, OR
    }

    private ANDOR lastANDOR=ANDOR.AND;
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
        init(domain, rowModel);
    }

    private void init(int domain, String rowModel)throws JSONException{
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


    public ADQueryBuilder(int domain, String rowModel, TYPE type ) throws JSONException {
        switch (type){
            case SELECT:
                init(domain,rowModel);
                break;
            case INSERT:
                obj = new JSONObject();
                obj.put("domain", domain);
                obj.put("row_model", rowModel);
                row = new JSONArray();
                break;
        }

    }

    public ADQueryBuilder insert(JSONObject jsonObject)throws  Exception{
        if(row!=null){
            row.put(jsonObject);
        }else{
            new Exception("it is not insert type");
        }
        return this;
    }

    public ADQueryBuilder insertNewEmptyRow()throws  Exception{
        if(row!=null){
            row.put(new JSONObject());
        }else{
            new Exception("it is not insert type");
        }
        return this;
    }

    public ADQueryBuilder insertFieldAtRow(String field, Object data, int position)throws  Exception{
        if(row!=null){
            if(position < row.length())
                row.getJSONObject(position).put(field,data);
            else
                new Exception("index Of Bound");
        }else{
            new Exception("it is not insert type");
        }
        return this;
    }

    public ADQueryBuilder insertFieldFirstRow(String field, Object data)throws  Exception{
        int position=0;
        if(row!=null){
            if(position < row.length())
                row.getJSONObject(position).put(field,data);
            else
                new Exception("index Of bound");
        }else{
            new Exception("it is not insert type");
        }
        return this;
    }

    public ADQueryBuilder insertFieldLastRow(String field, Object data)throws  Exception{

        if(row!=null){
            if( row.length() > 0)
                row.getJSONObject(row.length()-1).put(field,data);
            else
                new Exception("is Empty");
        }else{
            new Exception("it is not insert type");
        }
        return this;
    }

    public ADQueryBuilder(int domain, String rowModel, int page, int size) throws JSONException {
        init(domain,rowModel);
        obj.put("page", page);
        obj.put("size", size);
    }

    public ADQueryBuilder newGroup(ANDOR andor) throws JSONException {
        if(row==null) {
            where.put(currentGroup);
            currentGroup = new JSONObject();
            currentGroup.put("ANDOR", andor);
            currentGroup.put("GROUP", new JSONArray());
        }
        return this;
    }

    public ADQueryBuilder addField(OP op, ANDOR andor, String field, Object value) throws JSONException {
        if(row==null) {
            JSONObject tmp = new JSONObject();
            if(currentGroup.getJSONArray("GROUP").length() > 0)
                tmp.put("ANDOR", andor.toString());
            else
                tmp.put("ANDOR", ANDOR.AND.toString());
            tmp.put("FIELD", field);
            tmp.put("OP", op.getValue());
            tmp.put("VAL", value);
            currentGroup.getJSONArray("GROUP").put(tmp);
        }
        return this;
    }

    public ADQueryBuilder addAND(){
        lastANDOR = ANDOR.AND;
        return  this;
    }

    public ADQueryBuilder addOR(){
        lastANDOR = ANDOR.OR;
        return  this;
    }

    public ADQueryBuilder whereIsEqual(String field, Object value) throws JSONException{
        return   addField(OP.EQ,lastANDOR,field,value);
    }

    public ADQueryBuilder whereGreaterThan(String field, Object value) throws JSONException{
        return   addField(OP.GT,lastANDOR,field,value);
    }

    public ADQueryBuilder whereLessThan(String field, Object value) throws JSONException{
        return   addField(OP.LT,lastANDOR,field,value);
    }

    public ADQueryBuilder whereIsNotEqual(String field, Object value) throws JSONException{
        return   addField(OP.NOT,lastANDOR,field,value);
    }

    public ADQueryBuilder whereLike(String field, Object value) throws JSONException{
        return   addField(OP.LIKE,lastANDOR,field,value);
    }

    public ADQueryBuilder addRequire(String model) {
        if(row==null) {
            required.put(model);
        }
        return this;
    }

    public ADQueryBuilder fetch(String[] models) {
        if(row==null) {
            for (String model : models) {
                fetch.put(model);

            }
        }
        return this;
    }


    public JSONObject compileWithKeys(String[] keys) throws JSONException {
        if(row==null) {
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

            if (fetch.length() > 0) {
                obj.put("fetch", fetch);
            }

            obj.put("where", tmp);
        }
        return obj;
    }

    public JSONObject compile() throws JSONException {

        if(row==null) {
            if (currentGroup.getJSONArray("GROUP").length() > 0) {
                where.put(currentGroup);
            }

            if (where.length() > 0) {
                obj.put("where", where);
            }

            if (required.length() > 0) {
                obj.put("require", required);
            }

            if (fetch.length() > 0) {
                obj.put("fetch", fetch);
            }
        }else{
            if (row.length() > 0) {
                obj.put("rows", row);
            }
        }
        return obj;
    }


}