package com.sbxcloud.android.sbxcloudsdk;

import com.sbxcloud.android.sbxcloudsdk.query.SbxKey;
import com.sbxcloud.android.sbxcloudsdk.query.SbxModelName;
import com.sbxcloud.android.sbxcloudsdk.query.SbxParamField;

import java.util.Date;

/**
 * Created by lgguzman on 20/02/17.
 */

@SbxModelName("Product")
public class Product {

    @SbxKey
    String key;

    @SbxParamField(name = "description")
    String name;

    @SbxParamField()
    double price;

    @SbxParamField()
    Date expireAt;

    @SbxParamField()
    Category category;

    public Product(String name, double price, Category category) {
        this.category = category;
        this.price = price;
        this.name = name;
        this.expireAt=new Date();
    }

    public Product(){}

    @Override
    public boolean equals(Object obj) {
        Product product= (Product)obj;
        return this.price==product.price && this.name.equals(product.name);
    }
}
