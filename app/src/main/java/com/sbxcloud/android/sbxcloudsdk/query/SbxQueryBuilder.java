package com.sbxcloud.android.sbxcloudsdk.query;

import com.sbxcloud.android.sbxcloudsdk.net.model.SbxQuery;
import com.sbxcloud.android.sbxcloudsdk.util.SbxDataValidator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lgguzman on 18/02/17.
 */

public class SbxQueryBuilder {
    /**
     * @author https://archivo.digital
     *
     * Example
     *
     *   SELECT * from mi_modelo WHERE (profesion = 'cantante' OR
     *   profesion = 'guitarrista') AND (ganancias > 100000 AND ventas < 10)
     *
     *        SbxQueryBuilder queryBuilder = new SbxQueryBuilder(ApiConstants.DOMAIN,"mi_modelo");
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
     *        SbxQueryBuilder queryBuilder = new SbxQueryBuilder(ApiConstants.DOMAIN,"mi_modelo", SbxQueryBuilder.TYPE.INSERT);
     *            queryBuilder.insertNewEmptyRow()
     *            .insertFieldLastRow("profesion","cantante")
     *            .insertFieldLastRow("ganancias",3000)
     *            .insertFieldLastRow("ventas",9);
     *            JSONObject jsonObject=queryBuilder.compile();
     *
     *
     */


        private JSONArray fetch;
        private JSONObject obj;
        private JSONObject referenceJoin;
        private JSONArray required;
        private JSONArray where;
        private JSONArray keysD;
        private JSONObject currentGroup;
        private JSONArray row;
        public enum TYPE {
            INSERT, SELECT, DELETE
        }

        public enum ANDOR {
            AND, OR
        }

        private ANDOR lastANDOR=null;
        public enum OP {
            EQ("="),
            LIKE("LIKE"),
            IN("IN"),
            NOTIN("NOT IN"),
            LT("<"),
            GT(">"),
            LET("<="),
            GET(">="),
            NOT("<>"),
            IS("IS"),
            ISNOT("IS NOT");

            private String val;

            OP(String s) {
                this.val = s;
            }

            public String getValue() {
                return val;
            }
        }

    public JSONArray getKeysD() {
        return keysD;
    }

    public SbxQueryBuilder(int domain, String rowModel) throws JSONException {
            init(domain, rowModel);
        }

        private void init(int domain, String rowModel)throws JSONException {
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



        public SbxQueryBuilder(int domain, String rowModel, TYPE type ) throws JSONException {
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
                case DELETE:
                    obj = new JSONObject();
                    obj.put("domain", domain);
                    obj.put("row_model", rowModel);
                    keysD = new JSONArray();
                    break;
            }

        }

        public SbxQueryBuilder addGeoSort(double lat, double lon, String latName, String lonName){

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("lat",lat);
                jsonObject.put("lon",lon);
                jsonObject.put("latProperty",latName);
                jsonObject.put("lonProperty",lonName);
                obj.put("geosort",jsonObject);
            }catch (Exception ex){}
            return this;
        }

        public SbxQueryBuilder insert(JSONObject jsonObject)throws  Exception{
            if(row!=null){
                row.put(jsonObject);
            }else{
                new Exception("it is not insert type");
            }
            return this;
        }

        public SbxQueryBuilder insertNewEmptyRow()throws  Exception{
            if(row!=null){
                row.put(new JSONObject());
            }else{
                new Exception("it is not insert type");
            }
            return this;
        }

        public SbxQueryBuilder addDeleteKey(String key){
            if(keysD!=null){
                keysD.put(key);
            }
            return this;
        }


    public SbxQueryBuilder insertFieldAtRow(String field, Object data, int position)throws  Exception{
            if(row!=null){
                if(position < row.length())
                    row.getJSONObject(position).put(field, SbxDataValidator.validate( data));
                else
                    new Exception("index Of Bound");
            }else{
                new Exception("it is not insert type");
            }
            return this;
        }

        public SbxQueryBuilder insertFieldFirstRow(String field, Object data)throws  Exception{
            int position=0;
            if(row!=null){
                if(position < row.length())
                    row.getJSONObject(position).put(field, SbxDataValidator.validate( data));
                else
                    new Exception("index Of bound");
            }else{
                new Exception("it is not insert type");
            }
            return this;
        }

        public SbxQueryBuilder insertFieldLastRow(String field, Object data)throws  Exception{

            if(row!=null){
                if( row.length() > 0)
                    row.getJSONObject(row.length()-1).put(field, SbxDataValidator.validate( data));
                else
                    new Exception("is Empty");
            }else{
                new Exception("it is not insert type");
            }
            return this;
        }

        public void setPage(int page)  throws JSONException{
            obj.put("page", page);
        }

        public int getPage(){
           return  obj.optInt("page",1);
        }

        public SbxQueryBuilder(int domain, String rowModel, int page, int size) throws JSONException {
            init(domain,rowModel);
            obj.put("page", page);
            obj.put("size", size);
        }

        private SbxQueryBuilder newGroup(ANDOR andor) throws JSONException {

            if(row==null) {
                lastANDOR=null;
                where.put(currentGroup);
                currentGroup = new JSONObject();
                currentGroup.put("ANDOR", andor);
                currentGroup.put("GROUP", new JSONArray());
            }
            return this;
        }

        public SbxQueryBuilder newGroupWithAnd() throws JSONException {
            return newGroup(ANDOR.AND);
        }

        public SbxQueryBuilder newGroupWithOr() throws JSONException {
            return newGroup(ANDOR.OR);
        }

        private SbxQueryBuilder addField(OP op, ANDOR andor, String field, Object value) throws JSONException {
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

    private SbxQueryBuilder setReferenceJoin(OP op, String model , String filterField,String referenceField, Object value) throws JSONException {
        referenceJoin = new JSONObject();
        referenceJoin.put("row_model",model);
        JSONObject filter = new JSONObject();
        filter.put("OP", op.getValue());
        filter.put("VAL",value);
        filter.put("FIELD",filterField);
        referenceJoin.put("reference_field",referenceField);
        referenceJoin.put("filer", filter);
        return this;
    }

        // PRIVATE QUERY METHODS

        private SbxQueryBuilder addAND(){
            lastANDOR = ANDOR.AND;
            return  this;
        }

        private SbxQueryBuilder addOR(){
            if (lastANDOR != null)
                lastANDOR = ANDOR.OR;
            else
                lastANDOR = ANDOR.AND;
            return  this;
        }

        private SbxQueryBuilder whereIsEqual(String field, Object value) throws JSONException{
            return   addField(OP.EQ,lastANDOR,field,value);
        }

        private SbxQueryBuilder whereIsNotNull(String field) throws JSONException{
            return   addField(OP.ISNOT,lastANDOR,field,null);
        }

        private SbxQueryBuilder whereIsNull(String field) throws JSONException{
            return   addField(OP.IS,lastANDOR,field,null);
        }

        private SbxQueryBuilder whereIn(String field, Object value) throws JSONException{
            return   addField(OP.IN,lastANDOR,field,value);
        }

        private SbxQueryBuilder whereNotIn(String field, Object value) throws JSONException{
            return   addField(OP.NOTIN,lastANDOR,field,value);
        }

        private SbxQueryBuilder whereGreaterThan(String field, Object value) throws JSONException{
            return   addField(OP.GT,lastANDOR,field,value);
        }

        private SbxQueryBuilder whereLessThan(String field, Object value) throws JSONException{
            return   addField(OP.LT,lastANDOR,field,value);
        }

        private SbxQueryBuilder whereGreaterOrEqualThan(String field, Object value) throws JSONException{
            return   addField(OP.GET,lastANDOR,field,value);
        }

        private SbxQueryBuilder whereLessOrEqualThan(String field, Object value) throws JSONException{
            return   addField(OP.LET,lastANDOR,field,value);
        }

        private SbxQueryBuilder whereIsNotEqual(String field, Object value) throws JSONException{
            return   addField(OP.NOT,lastANDOR,field,value);
        }

        private SbxQueryBuilder whereLike(String field, Object value) throws JSONException{
            return   addField(OP.LIKE,lastANDOR,field,value);
        }


    public class ReferenceJoin {

        private SbxQueryBuilder find;
        private String field;
        private String referenceField;

        ReferenceJoin(SbxQueryBuilder find, String field, String referenceField, String type) throws Exception {
            this.find = find;
            this.field = field;
            this.referenceField = referenceField;
            if (type.equals("AND")) {
                this.find.andWhereIn(this.field, "@reference_join@");
            } else {
                this.find.orWhereIn(this.field, "@reference_join@");
            }
        }


        public FilterJoin in(String referenceModel) {
            return new FilterJoin(this.find, this.field, this.referenceField, referenceModel);
        }
    }


    public class FilterJoin {

        private SbxQueryBuilder find;
        private String field;
        private String referenceField;
        private String referenceModel;


        FilterJoin(SbxQueryBuilder find,String field,String referenceField,String referenceModel) {
            this.find = find;
            this.field = field;
            this.referenceField = referenceField;
            this.referenceModel = referenceModel;
        }


        public SbxQueryBuilder filterWhereIsEqual(String field, Object value) throws Exception{
            this.find.setReferenceJoin(OP.EQ, this.field, this.referenceField, this.referenceModel, value);
            return this.find;
        }

        public SbxQueryBuilder FilterWhereIsNotNull(String field) throws  Exception{
            this.find.setReferenceJoin(OP.ISNOT, this.field, this.referenceField, this.referenceModel, null);
            return this.find;
        }

        public SbxQueryBuilder FilterWhereIsNull(String field) throws  Exception{
            this.find.setReferenceJoin(OP.IS, this.field, this.referenceField, this.referenceModel, null);
            return this.find;
        }

        public SbxQueryBuilder FilterWhereGreaterThan(String field, Object value) throws  Exception{
            this.find.setReferenceJoin(OP.GT, this.field, this.referenceField, this.referenceModel, value);
            return this.find;
        }

        public SbxQueryBuilder FilterWhereLessThan(String field, Object value) throws  Exception{
            this.find.setReferenceJoin(OP.LT, this.field, this.referenceField, this.referenceModel, value);
            return this.find;
        }

        public SbxQueryBuilder FilterWhereGreaterOrEqualThan(String field, Object value) throws  Exception{
            this.find.setReferenceJoin(OP.GET, this.field, this.referenceField, this.referenceModel, value);
            return this.find;
        }

        public SbxQueryBuilder FilterWhereLessOrEqualThan(String field, Object value) throws  Exception{
            this.find.setReferenceJoin(OP.LET, this.field, this.referenceField, this.referenceModel, value);
            return this.find;
        }

        public SbxQueryBuilder FilterWhereIsNotEqual(String field, Object value) throws  Exception{
            this.find.setReferenceJoin(OP.ISNOT, this.field, this.referenceField, this.referenceModel, value);
            return this.find;
        }

        public SbxQueryBuilder FilterWhereLike(String field, Object value) throws  Exception{
            this.find.setReferenceJoin(OP.LIKE, this.field, this.referenceField, this.referenceModel, value);
            return this.find;
        }

        public SbxQueryBuilder FilterWhereIn(String field, Object value) throws  Exception{
            this.find.setReferenceJoin(OP.IN, this.field, this.referenceField, this.referenceModel, value);
            return this.find;
        }

        public SbxQueryBuilder FilterWhereNotIn(String field, Object value) throws  Exception{
            this.find.setReferenceJoin(OP.NOTIN, this.field, this.referenceField, this.referenceModel, value);
            return this.find;
        }

    }


    // PUBLIC QUERY METHODS

        public SbxQueryBuilder andWhereIsEqual(String field, Object value) throws JSONException{
            addAND();
            return   whereIsEqual(field,value);
        }

        public SbxQueryBuilder andWhereIsNotNull(String field) throws JSONException{
            addAND();
            return   whereIsNotNull(field );
        }

        public SbxQueryBuilder andWhereGreaterThan(String field, Object value) throws JSONException{
            addAND();
            return   whereGreaterThan(field,value);
        }

        public SbxQueryBuilder andWhereLessThan(String field, Object value) throws JSONException{
            addAND();
            return   whereLessThan(field,value);
        }

        public SbxQueryBuilder andWhereGreaterOrEqualThan(String field, Object value) throws JSONException{
            addAND();
            return   whereGreaterOrEqualThan(field,value);
        }

        public SbxQueryBuilder andWhereLessOrEqualThan(String field, Object value) throws JSONException{
            addAND();
            return   whereLessOrEqualThan(field,value);
        }

        public SbxQueryBuilder andWhereIsNotEqual(String field, Object value) throws JSONException{
            addAND();
            return   whereIsNotEqual(field,value);
        }

        public SbxQueryBuilder andWhereLike(String field, Object value) throws JSONException{
            addAND();
            return   whereLike(field,value);
        }

        public SbxQueryBuilder andWhereIn(String field, Object value) throws JSONException{
            addAND();
            return   whereIn(field,value);
        }


        public SbxQueryBuilder orWhereIsEqual(String field, Object value) throws JSONException{
            addOR();
            return   whereIsEqual(field,value);
        }

        public SbxQueryBuilder orWhereIsNotNull(String field) throws JSONException{
            addOR();
            return   whereIsNotNull(field );
        }

        public SbxQueryBuilder orWhereGreaterThan(String field, Object value) throws JSONException{
            addOR();
            return   whereGreaterThan(field,value);
        }

        public SbxQueryBuilder orWhereLessThan(String field, Object value) throws JSONException{
            addOR();
            return   whereLessThan(field,value);
        }

        public SbxQueryBuilder orWhereGreaterOrEqualThan(String field, Object value) throws JSONException{
            addOR();
            return   whereGreaterOrEqualThan(field,value);
        }

        public SbxQueryBuilder orWhereLessOrEqualThan(String field, Object value) throws JSONException{
            addOR();
            return   whereLessOrEqualThan(field,value);
        }

        public SbxQueryBuilder orWhereIsNotEqual(String field, Object value) throws JSONException{
            addOR();
            return   whereIsNotEqual(field,value);
        }

        public SbxQueryBuilder orWhereLike(String field, Object value) throws JSONException{
            addOR();
            return   whereLike(field,value);
        }

        public SbxQueryBuilder orWhereIn(String field, Object value) throws JSONException{
            addOR();
            return   whereIn(field,value);
        }

        public ReferenceJoin orWhereReferenceJoinBetween(String field, String referenceField) throws Exception {
            return new ReferenceJoin(this, field, referenceField, "OR");
        }

        public ReferenceJoin andWhereReferenceJoinBetween(String field, String referenceField) throws Exception {
            return new ReferenceJoin(this, field, referenceField, "AND");
        }

        public SbxQueryBuilder addRequire(String model) {
            if(row==null) {
                required.put(model);
            }
            return this;
        }

        public SbxQueryBuilder fetch(String[] models) {
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

        public JSONObject compile() throws Exception {

            if(row==null) {
                if (currentGroup!=null && currentGroup.getJSONArray("GROUP").length() > 0) {
                    where.put(currentGroup);
                }

                if (where!=null && where.length() > 0) {
                    obj.put("where", where);
                }
                if (referenceJoin!=null ) {
                    obj.put("reference_join", referenceJoin);
                }

                if (required!=null && required.length() > 0) {
                    obj.put("require", required);
                }

                if (fetch!=null && fetch.length() > 0) {
                    obj.put("fetch", fetch);
                }

                if (keysD!=null){
                    if(keysD.length()>0){
                        JSONObject jsonObject= new JSONObject();
                        jsonObject.put("keys",keysD);
                        obj.put("where",jsonObject);
                    }else{
                        throw  new Exception("A Key is required");
                    }
                }
            }else{
                if (row.length() > 0) {
                    obj.put("rows", row);
                }
            }
            return obj;
        }


    }
