package digital.archivo.archivodigital_androidlib;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * @author https://archivo.digital
 */
public class ArchivoDigitalUtil {


    private static ArchivoDigitalUtil singleton;
    private String key;
    private static final String URL = "https://archivo.digital/api/data/v1";

    private Context ctx;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();


    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private ArchivoDigitalUtil(Context ctx) {

        this.ctx = ctx;

        try {
            ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            key = bundle.getString("archivo.digital-key");

            if (key == null || key.length() < 1) {
                throw new RuntimeException("Please define a meta-data item at AndroidManifest.xml likes this:  <meta-data android:name=\"archivo.digital-key\" android:value=\"xxx-yyy-zzzz\" />");
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

    }

    /* Static 'instance' method */
    public static ArchivoDigitalUtil getInstance(Context ctx) {

        if (singleton == null) {
            singleton = new ArchivoDigitalUtil(ctx);
        }

        return singleton;
    }

    public <T extends JSONAware> void findAllByQuery(String token, final JSONObject query, final CallbackListener<List<T>> callback, final Class<T> type) {

        final String modelName;
        try {
            modelName = query.getString("row_model");
            RequestBody body = RequestBody.create(JSON, query.toString());

            Request request = new Request.Builder()
                    .url(URL + "/row/find")
                    .addHeader("App-Key", key)
                    .header("User-Agent", "AndroidLib")
                    .addHeader("Authorization", "Bearer " + token)
                    .post(body)
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.err(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) callback.err("Error");


                    Log.d("ArchivoDigitalUtil", response.body().string());

                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        JSONArray items = obj.getJSONArray("results");
                        JSONArray model = obj.getJSONArray("model");

                        HashMap<String, String> mapping = new HashMap<>();

                        JSONObject fetchedObjects = new JSONObject();

                        if (query.has("fetch") && obj.has("fetched_results")) {
                            JSONArray fetch = query.getJSONArray("fetch");

                            for (int j = 0; j < fetch.length(); j++) {
                                String fetchName = fetch.getString(j);

                                // no support for 2nd level
                                if (fetchName.indexOf('.') >= 0) {
                                    continue;
                                }

                                for (int z = 0; z < model.length(); z++) {

                                    JSONObject modelItem = model.getJSONObject(z);

                                    if (modelItem.getString("type").equals("REFERENCE") && modelItem.getString("name").equals(fetchName)) {
                                        mapping.put(fetchName, modelItem.getString("reference_type_name"));
                                    }

                                }

                            }

                            fetchedObjects = obj.getJSONObject("fetched_results");
                        }


                        List<T> itemsList = new ArrayList<>(items.length());

                        for (int i = 0; i < items.length(); i++) {
                            JSONObject object = items.getJSONObject(i);
                            JSONAware tmp = type.newInstance();


                            if (query.has("fetch") && obj.has("fetched_results")) {
                                JSONArray fetch = query.getJSONArray("fetch");


                                // iterate over the fetched properties to bind the fetched objects into the results objects
                                for (int j = 0; j < fetch.length(); j++) {
                                    String fetchName = fetch.getString(j);
                                    String typeName = mapping.get(fetchName);

                                    // retrieve all the key based referenced objects
                                    JSONObject refs = fetchedObjects.getJSONObject(typeName);

                                    if (object.has(fetchName) && refs.has(object.getString(fetchName))) {
                                        object.put(fetchName, refs.getJSONObject(object.getString(fetchName)));
                                    }

                                }

                            }

                            //let the end user mappings do the magic
                            tmp.mapFromJSONObject(object);
                            itemsList.add((T) tmp);
                        }


                        callback.ok(itemsList);

                    } catch (JSONException | IllegalAccessException | InstantiationException e) {
                        e.printStackTrace();
                        callback.err(e.getMessage());
                    }


                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            callback.err(e.getMessage());
        }

    }


}
