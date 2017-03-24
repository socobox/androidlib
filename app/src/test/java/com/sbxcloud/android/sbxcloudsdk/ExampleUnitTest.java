package com.sbxcloud.android.sbxcloudsdk;

import android.app.Instrumentation;
import android.content.Context;
import android.test.InstrumentationTestCase;
import android.test.mock.MockContext;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

import com.sbxcloud.android.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxArrayResponse;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxSimpleResponse;
import com.sbxcloud.android.sbxcloudsdk.net.model.SbxQuery;
import com.sbxcloud.android.sbxcloudsdk.query.SbxModelHelper;
import com.sbxcloud.android.sbxcloudsdk.query.SbxQueryBuilder;
import com.sbxcloud.android.sbxcloudsdk.util.SbxMagicComposer;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.functions.Consumer;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class ExampleUnitTest  {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);


    }

    Context mMockContext;



    @Test
    public void inizialiceSbxAuth() throws Exception {
        User user= new User("luis gabriel","lgguzman","lgguzman@sbxcloud.co","123456");
//        user.logIn().subscribe(new Consumer<T>() {
//        });


//        JSONObject jsonObject = new JSONObject("{\"description\":\"leche5\",\"price\":1123,\"category\":\"28a1ca86-96f9-462c-b2df-c158a8e87c58\",\"expireAt\":\"2017-03-18T00:42:53.000Z\",\"_KEY\":\"1d3cd5e6-21a8-4de8-97c9-f4177aa6bad4\",\"_META\":{\"created_time\":\"2017-03-18T00:42:53.000Z\",\"updated_time\":\"2017-03-18T00:42:53.000Z\"}}");
//        JSONObject fecth = new JSONObject("{\"Category\":{\"030347f4-e148-4c9d-956d-6cf9e2048b22\":{\"description\":\"lacteos3\",\"_KEY\":\"030347f4-e148-4c9d-956d-6cf9e2048b22\",\"_META\":{\"created_time\":\"2017-03-18T00:40:01.000Z\",\"updated_time\":\"2017-03-18T00:40:01.000Z\"}},\"1c2b41e7-8101-4d05-b7b5-0515235bf9f6\":{\"_META\":{\"created_time\":\"2017-03-18T00:41:30.000Z\",\"updated_time\":\"2017-03-18T00:41:30.000Z\"},\"_KEY\":\"1c2b41e7-8101-4d05-b7b5-0515235bf9f6\",\"description\":\"lacteos3\"},\"28a1ca86-96f9-462c-b2df-c158a8e87c58\":{\"_META\":{\"created_time\":\"2017-03-18T00:42:53.000Z\",\"updated_time\":\"2017-03-18T00:42:53.000Z\"},\"_KEY\":\"28a1ca86-96f9-462c-b2df-c158a8e87c58\",\"description\":\"lacteos8\"},\"6c2b7678-89eb-4fd3-9dfe-9ec9eb7829ba\":{\"_META\":{\"created_time\":\"2017-03-18T00:37:37.000Z\",\"updated_time\":\"2017-03-18T00:37:37.000Z\"},\"_KEY\":\"6c2b7678-89eb-4fd3-9dfe-9ec9eb7829ba\",\"description\":\"lacteos3\"}}}");
//        Product product= (Product) SbxMagicComposer.getSbxModel(jsonObject,Product.class,0,fecth);
//        Log.e("data",product.key);
//        Log.e("data",product.name);
//        Log.e("data",product.category.key);
//        Assert.assertEquals("lacteos8",product.category.name);
//        Log.e("data",product.category.name);


//        //Mockito.mock(Context.class);
//
//        //Iniciar la librería con el dominio y el App-key, puede ser en tu custom Application.class
//        SbxAuth.initializeIfIsNecessary( mMockContext,110,"d4cd3cac-043a-48ab-9d06-18aa4fd23cbd");
//  //      Log http request and response
//        SbxAuth.getDefaultSbxAuth().setHttpLog(true);
//
////
////        //Datos
////        //Registrar un usuario
////        User user= new User("luis gabriel","lgguzman","lgguzman@sbxcloud.co","123456");
////        System.out.println(SbxAuth.getDefaultSbxAuth().getUrlSigIn(user));
////
////        //login usuario
////        System.out.println(SbxAuth.getDefaultSbxAuth().getUrllogin(user));
////
////        user.token="token-asdf-234-asd";
////        SbxAuth.getDefaultSbxAuth().refreshToken(user);
////
////        //insertar un modelo
////        Category category = new Category("lacteos");
////        System.out.println(SbxModelHelper.getUrlInsertOrUpdateRow(category));
////
////        //actualizar modelo
////        category.key="laksdf-asdf-234-asdf";
////        category.name="lacteo";
////        System.out.println(SbxModelHelper.getUrlInsertOrUpdateRow(category));
////
////        //insertar un modelo con referencia
////        Product product= new Product("leche",13.00,category);
////        System.out.println(SbxModelHelper.getUrlInsertOrUpdateRow(product));
////
////
////        //Buscar los primeros 100 productos cuyo precio sea menor de 40
////        SbxQueryBuilder sbxQueryBuilder= SbxModelHelper.prepareQuery(Product.class,1,100);
////        sbxQueryBuilder.whereLessThan("price",40);
////        System.out.println(SbxModelHelper.getUrlQuery(sbxQueryBuilder));
////
////
////        //Create a new CustomObjevt form Json
////        JSONObject jsonObject = new JSONObject("{\"price\":13,\"description\":\"leche\",\"expireAt\":\"2017-02-20T20:45:36.756Z\",\"category\":\"laksdf-asdf-234-asdf\"}");
////        Product p= (Product) SbxMagicComposer.getSbxModel(jsonObject,Product.class,0);
////
////        //Create nested Object
////        jsonObject = new JSONObject("{\"_KEY\": \"95979b68-cc4a-416d-46c550380031\",\"price\":13,\"description\":\"leche\",\"expireAt\":\"2017-02-20T20:45:36.756Z\",\"category\":{\"_KEY\": \"laksdf-asdf-234-asdf\",\"description\":\"lacteos\"}}");
////        p= (Product) SbxMagicComposer.getSbxModel(jsonObject,Product.class,0);
//
//
//        //Also
//
//        User user= new User("luis gabriel","lgguzman","lgguzman@sbxcloud.co","123456");
////        user.signUpInBackground(new SbxSimpleResponse<User>() {
////            @Override
////            public void onError(Exception e) {
////                System.out.println(e.getMessage());
////            }
////
////            @Override
////            public  void onSuccess(User user) {
////                System.out.println(user.token);
////            }
////        });
//        user.logInBackground(new SbxSimpleResponse<User>() {
//            @Override
//            public void onError(Exception e) {
//                System.out.println(e.getMessage());
//            }
//
//            @Override
//            public  void onSuccess(User user) {
//                System.out.println(user.token);
//                try {
//                    SbxQuery queryProduct = new SbxQuery(Product.class);
//                    queryProduct.whereLessThan("price", 40);
//                    queryProduct.findInBackground(new SbxArrayResponse<Product>() {
//
//                        @Override
//                        public void onError(Exception e) {
//                            System.out.println(e.getMessage());
//                        }
//
//                        @Override
//                        public void onSuccess(List<Product> products) {
//                            //do something with products
//                            for (Product p : products) {
//                                System.out.printf(p.key);
//                            }
//                        }
//                    });
//                }catch (Exception ex){ex.printStackTrace();}
//            }
//        });
//
////        final Category category = new Category("lacteos");
////        category.saveInBackground(new SbxSimpleResponse<Category>() {
////            @Override
////            public void onError(Exception e) {
////                System.out.println(e.getMessage());
////            }
////
////            @Override
////            public void onSuccess(Category category1) {
////                //Do something qith category1
////                System.out.println(category1.key);
////            }
////        });
////        Product product= new Product("leche",13.00,category);
////        product.saveInBackground(new SbxSimpleResponse<Product>() {
////            @Override
////            public void onError(Exception e) {
////
////            }
////
////            @Override
////            public void onSuccess(Product product1) {
////                //Do something qith product1
////                System.out.printf(product1.key);
////
////            }
////        });


//        user.logOut();




    }
}