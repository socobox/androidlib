package com.sbxcloud.android.sbxcloudsdk;

import com.sbxcloud.android.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxArrayResponse;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxSimpleResponse;
import com.sbxcloud.android.sbxcloudsdk.net.model.SbxQuery;
import com.sbxcloud.android.sbxcloudsdk.query.SbxModelHelper;
import com.sbxcloud.android.sbxcloudsdk.query.SbxQueryBuilder;
import com.sbxcloud.android.sbxcloudsdk.util.SbxMagicComposer;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);


    }

    @Test
    public void inizialiceSbxAuth() throws Exception {

        //Iniciar la librería con el dominio y el App-key, puede ser en tu custom Application.class
        SbxAuth.initialize(110,"d4cd3cac-043a-48ab-9d06-18aa4fd23cbd");
        //Log http request and response
        SbxAuth.getDefaultSbxAuth().setHttpLog(true);

//
//        //Datos
//        //Registrar un usuario
//        User user= new User("luis gabriel","lgguzman","lgguzman@sbxcloud.co","123456");
//        System.out.println(SbxAuth.getDefaultSbxAuth().getUrlSigIn(user));
//
//        //login usuario
//        System.out.println(SbxAuth.getDefaultSbxAuth().getUrllogin(user));
//
//        user.token="token-asdf-234-asd";
//        SbxAuth.getDefaultSbxAuth().refreshToken(user);
//
//        //insertar un modelo
//        Category category = new Category("lacteos");
//        System.out.println(SbxModelHelper.getUrlInsertOrUpdateRow(category));
//
//        //actualizar modelo
//        category.key="laksdf-asdf-234-asdf";
//        category.name="lacteo";
//        System.out.println(SbxModelHelper.getUrlInsertOrUpdateRow(category));
//
//        //insertar un modelo con referencia
//        Product product= new Product("leche",13.00,category);
//        System.out.println(SbxModelHelper.getUrlInsertOrUpdateRow(product));
//
//
//        //Buscar los primeros 100 productos cuyo precio sea menor de 40
//        SbxQueryBuilder sbxQueryBuilder= SbxModelHelper.prepareQuery(Product.class,1,100);
//        sbxQueryBuilder.whereLessThan("price",40);
//        System.out.println(SbxModelHelper.getUrlQuery(sbxQueryBuilder));
//
//
//        //Create a new CustomObjevt form Json
//        JSONObject jsonObject = new JSONObject("{\"price\":13,\"description\":\"leche\",\"expireAt\":\"2017-02-20T20:45:36.756Z\",\"category\":\"laksdf-asdf-234-asdf\"}");
//        Product p= (Product) SbxMagicComposer.getSbxModel(jsonObject,Product.class,0);
//
//        //Create nested Object
//        jsonObject = new JSONObject("{\"_KEY\": \"95979b68-cc4a-416d-46c550380031\",\"price\":13,\"description\":\"leche\",\"expireAt\":\"2017-02-20T20:45:36.756Z\",\"category\":{\"_KEY\": \"laksdf-asdf-234-asdf\",\"description\":\"lacteos\"}}");
//        p= (Product) SbxMagicComposer.getSbxModel(jsonObject,Product.class,0);


        //Also

        User user= new User("luis gabriel","lgguzman","lgguzman@sbxcloud.co","123456");
        user.signUpInBackground(new SbxSimpleResponse<User>() {
            @Override
            public void onError(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public  void onSuccess(User user) {
                System.out.println(user.token);
            }
        });
//        user.logInBackground(new SbxSimpleResponse<User>() {
//            @Override
//            public void onError(Exception e) {
//                System.out.println(e.getMessage());
//            }
//
//            @Override
//            public  void onSuccess(User user) {
//                System.out.println();
//            }
//        });

//        final Category category = new Category("lacteos");
//        category.saveInBackground(new SbxSimpleResponse<Category>() {
//            @Override
//            public void onError(Exception e) {
//                System.out.println(e.getMessage());
//            }
//
//            @Override
//            public void onSuccess(Category category1) {
//                //Do something qith category1
//                System.out.println(category1.key);
//            }
//        });
//        Product product= new Product("leche",13.00,category);
//        product.saveInBackground(new SbxSimpleResponse<Product>() {
//            @Override
//            public void onError(Exception e) {
//
//            }
//
//            @Override
//            public void onSuccess(Product product1) {
//                //Do something qith product1
//                System.out.printf(product1.key);
//
//            }
//        });
//        SbxQuery queryProduct= new SbxQuery(Product.class);
//        queryProduct.whereLessThan("price",40);
//        queryProduct.findInBackground(new SbxArrayResponse<Product>() {
//
//            @Override
//            public void onError(Exception e) {
//                System.out.println(e.getMessage());
//            }
//
//            @Override
//            public void onSuccess(List<Product> products) {
//                    //do something with products
//                for (Product p:products){
//                    System.out.printf(p.key);
//                }
//            }
//        });
//
//        user.logOut();




    }
}