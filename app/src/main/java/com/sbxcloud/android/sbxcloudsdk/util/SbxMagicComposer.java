package com.sbxcloud.android.sbxcloudsdk.util;

import com.sbxcloud.android.sbxcloudsdk.exception.SbxAuthException;
import com.sbxcloud.android.sbxcloudsdk.query.SbxParamField;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * Created by lgguzman on 20/02/17.
 */


public class SbxMagicComposer {

    public static Object getSbxModel(JSONObject jsonObject,  Class<?> clazz, int level)throws  Exception{
        level++;
        //accept only two level
        if (level==2)
            return null;
        Constructor<?> ctor = clazz.getConstructor();
        Object o = ctor.newInstance();
        final Field[] variables = clazz.getDeclaredFields();

        for (final Field variable : variables) {

            final Annotation annotation = variable.getAnnotation(SbxParamField.class);

            if (annotation != null && annotation instanceof SbxParamField) {
                try {
                    boolean isAccessible=variable.isAccessible();
                    variable.setAccessible(true);
                    String name=((SbxParamField)annotation).name();
                    String variabletype=variable.getGenericType().toString();
                    switch (variabletype){
                        case "class java.lang.String":{
                            variable.set(o,jsonObject.optString(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "int":{
                            variable.set(o,jsonObject.optInt(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "long":{
                            variable.set(o,jsonObject.optLong(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "double":{
                            variable.set(o,jsonObject.optDouble(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "float":{
                            variable.set(o,jsonObject.optDouble(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        case "class java.util.Date":{
                            variable.set(o,
                                    SbxDataValidator.getDate(
                                    jsonObject.optString(SbxDataValidator.getAnnotationName(variable,annotation))));
                            break;
                        }
                        case "boolean":{
                            variable.set(o,jsonObject.optBoolean(SbxDataValidator.getAnnotationName(variable,annotation)));
                            break;
                        }
                        default:{

//                            Object obj= jsonObject.get(SbxDataValidator.getAnnotationName(variable,annotation));
//                            if(obj instanceof String){
//                                variable.set(o,null);
//                            }else {
//                                variable.set(o,
//                                        getSbxModel(
//                                                jsonObject.optJSONObject(SbxDataValidator.getAnnotationName(variable, annotation))
//                                                , clazz, level)
//                                );
//                            }
                            break;
                        }


                    }

                    variable.setAccessible(isAccessible);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new SbxAuthException(e);
                }

            }


        }


        return o;
    }
}

/*
*{
  "success": true,
  "model": [
    {
      "id": 2493,
      "type": "STRING",
      "name": "name",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 2497,
      "type": "FLOAT",
      "name": "lon",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 2498,
      "type": "FLOAT",
      "name": "lat",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 2499,
      "type": "STRING",
      "name": "phone",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 2500,
      "type": "STRING",
      "name": "address",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 2502,
      "type": "STRING",
      "name": "iduser",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 2503,
      "type": "STRING",
      "name": "rol",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 2504,
      "type": "STRING",
      "name": "payment",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 2669,
      "type": "STRING",
      "name": "urlImage",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 3856,
      "type": "BOOLEAN",
      "name": "isStore",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 3896,
      "type": "STRING",
      "name": "idDevice",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 3897,
      "type": "STRING",
      "name": "email",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 4746,
      "type": "BOOLEAN",
      "name": "active",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 4748,
      "type": "INT",
      "name": "sessionCount",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 4752,
      "type": "STRING",
      "name": "phone2",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 4753,
      "type": "STRING",
      "name": "phone3",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 4754,
      "type": "STRING",
      "name": "phone4",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    },
    {
      "id": 6828,
      "type": "BOOLEAN",
      "name": "installed",
      "reference_type": null,
      "reference_type_name": null,
      "is_label": false,
      "is_array": false,
      "is_required": false,
      "is_sequence": false
    }
  ],
  "row_count": 1,
  "total_pages": 1,
  "results": [
    {
      "name": "prueba3",
      "lon": 34.3434,
      "lat": 456.342,
      "phone": "3432345",
      "address": "calle 45 56",
      "iduser": "417",
      "rol": "customer",
      "payment": "yes",
      "urlImage": null,
      "isStore": false,
      "idDevice": null,
      "email": null,
      "active": false,
      "sessionCount": null,
      "phone2": null,
      "phone3": null,
      "phone4": null,
      "installed": false,
      "_KEY": "95979b68-cc4a-416d-885d-46c550380031",
      "_META": {
        "created_time": "2016-06-29T12:18:18.000Z",
        "updated_time": "2016-06-29T12:18:18.000Z"
      }
    }
  ]
}
 */
