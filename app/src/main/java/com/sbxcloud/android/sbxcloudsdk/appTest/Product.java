package com.sbxcloud.android.sbxcloudsdk.appTest;

import com.sbxcloud.android.sbxcloudsdk.net.model.SbxModel;
import com.sbxcloud.android.sbxcloudsdk.query.annotation.SbxKey;
import com.sbxcloud.android.sbxcloudsdk.query.annotation.SbxModelName;
import com.sbxcloud.android.sbxcloudsdk.query.annotation.SbxParamField;

import java.util.Date;

/**
 * Created by lgguzman on 20/02/17.
 */

@SbxModelName("Product")
public class Product extends SbxModel {

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
        return this.price==product.price && this.name.equals(product.name) &&
                this.category.key.equals(product.category.key);
    }
}
