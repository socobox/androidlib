package com.sbxcloud.android.sbxcloudsdk.appTest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.sbxcloud.android.sbxcloudsdk.R;
import com.sbxcloud.android.sbxcloudsdk.auth.SbxAuth;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxArrayResponse;
import com.sbxcloud.android.sbxcloudsdk.net.callback.SbxSimpleResponse;
import com.sbxcloud.android.sbxcloudsdk.net.model.SbxQuery;

import java.util.List;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        try {
            //Iniciar la librería con el dominio y el App-key, puede ser en tu custom Application.class
            SbxAuth.initialize(110, "d4cd3cac-043a-48ab-9d06-18aa4fd23cbd");
            //Log http request and response
            SbxAuth.getDefaultSbxAuth().setHttpLog(true);

            User user= new User("luis gabriel","lgguzmansbxcloud2","lgguzman@sbxcloud2.co","123456");
            user.logInBackground(new SbxSimpleResponse<User>() {
                @Override
                public void onError(Exception e) {


                }

                @Override
                public  void onSuccess(User user) {
                    try {
                        final Category category = new Category("lacteos");
                        category.saveInBackground(new SbxSimpleResponse<Category>() {
                            @Override
                            public void onError(Exception e) {
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                            }

                            @Override
                            public void onSuccess(Category category1) {
                                //Do something qith category1
                                try {
                                    System.out.println(category1.key);
                                    Product produc = new Product("leche", 13.00, category);
                                    produc.saveInBackground(new SbxSimpleResponse<Product>() {
                                         @Override
                                         public void onError(Exception e) {
                                             Log.e("ERROR", e.getMessage());
                                             e.printStackTrace();
                                         }
                                        @Override
                                        public void onSuccess(Product product1) {
                                            System.out.println(product1.key);
                                        }
                                    });
                                    Product product = new Product("leche", 13.00, category);
                                    product.saveInBackground(new SbxSimpleResponse<Product>() {
                                        @Override
                                        public void onError(Exception e) {
                                            Log.e("ERROR",e.getMessage());
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onSuccess(Product product1) {
                                            //Do something qith product1
                                            product1.name="queso";
                                            try {
                                                product1.saveInBackground(new SbxSimpleResponse<Product>() {
                                                    @Override
                                                    public void onError(Exception e) {
                                                        Log.e("ERROR",e.getMessage());
                                                        e.printStackTrace();
                                                    }

                                                    @Override
                                                    public void onSuccess(Product tClass) {
                                                        try{
                                                            SbxQuery queryProduct= new SbxQuery(Product.class);
                                                            queryProduct.whereLessThan("price",40);
                                                            queryProduct.findInBackground(new SbxArrayResponse<Product>() {

                                                                @Override
                                                                public void onError(Exception e) {
                                                                    Log.e("ERROR",e.getMessage());
                                                                    e.printStackTrace();
                                                                }

                                                                @Override
                                                                public void onSuccess(List<Product> products) {
                                                                        //do something with products
                                                                    for (Product p:products){
                                                                        if(p.name.equals("queso")){
                                                                            try {
                                                                                p.deleteInBackground(new SbxSimpleResponse<Product>() {
                                                                                    @Override
                                                                                    public void onError(Exception e) {
                                                                                        Log.e("ERROR",e.getMessage());
                                                                                        e.printStackTrace();
                                                                                    }

                                                                                    @Override
                                                                                    public void onSuccess(Product tClass) {
                                                                                        Log.e("ELIMINADO",tClass.name);
                                                                                    }
                                                                                });
                                                                            }catch (Exception ex){
                                                                                Log.e("ERROR",ex.getMessage());
                                                                                ex.printStackTrace();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                        }catch (Exception ex){
                                                            Log.e("ERROR",ex.getMessage());
                                                            ex.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }catch (Exception ex){
                                                Log.e("ERROR",ex.getMessage());
                                                ex.printStackTrace();
                                            }

                                        }
                                    });
                                }catch (Exception ex){
                                    Log.e("ERROR",ex.getMessage());
                                    ex.printStackTrace();
                                }
                            }
                        });
                    }catch (Exception ex){
                        Log.e("ERROR",ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });
        }catch (Exception ex){
            Log.e("ERROR",ex.getMessage());
            ex.printStackTrace();
        }
    }
}
