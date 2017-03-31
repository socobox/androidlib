# androidlib
## **SbxCloud** Android SDK



El propósito de esta librería es facilitar el desarrollo de aplicaciones android con **SbxCloud**



Si tienes alguna duda te invitamos a participar de nuestro canal de slack, simplemente ingresa a: https://archivodigitalslack.herokuapp.com y pide tu invitación, allí encontrarás personas que como tú están desarrollando sus soluciones con nuestra plataforma.


Para comenzar a usar el SDK de Android, simplemente debemos de agregar la siguientes líneas a nuestra configuración de gradle(build.gradle):

Agregamos jitpack.io como repositorio

            repositories {
                maven {
                    url "https://jitpack.io"
                }
            }

Agregamos la librería como dependencia  

            dependencies {
                //...otras dependencias de tu proyeco aquí.....
                compile 'com.github.sbxcloud:androidlib:v2.2.0-beta5'
            }
            
Esta librería se basa en annotaciones. Para crear tu propia Clase usuario puedes hacerla así:
```java
public class User {
    @SbxNameField
    String name;

    @SbxUsernameField
    String username;

    @SbxEmailField
    String email;

    @SbxPasswordField
    String password;

    @SbxAuthToken
    String token;

    @SbxKey
    String key;
    
    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
```
Para crear un modelo lo puedes realizar de la siguiente forma:
Donde SbxModelName marca el nombre del modelo  y el campo name de SbxParamField hace referencia a el nombre de los atributos en Sbxcloud.com

```java
@SbxModelName("Category")
public class Category {

    @SbxKey
    String key;

    @SbxParamField(name = "description")
    String name;

    public Category(){}

    public Category(String name) {
        this.name = name;
    }
}
```
Si un atributo hace referencia a otro modelo, se puede realizar de la siguiente forma:

```java
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

    public Product(){}

    public Product(String name, double price, Category category) {
        this.category = category;
        this.price = price;
        this.name = name;
        this.expireAt=new Date();
    }
}
```


Luego, puedes conectarte con tus datos utilizando el cliente hhtp que consideres conveniente. La clase SbxUrlComposer te genera los datos de conexión necesarios y accedes a estos con los métodos getUrl(), getType(), getHeader(), getBody()

```java
        //Iniciar la librería con el dominio y el App-key, puede ser en tu custom Application.class
         SbxAuth.initializeIfIsNecessary(Context,110,"d4cd3cac-043a-48ab-9d06-18aa4fd23cbd");

        //Datos
        //Registrar un usuario
        User user= new User("luis gabriel","lgguzman","lgguzman@sbxcloud.co","123456");
        System.out.println(SbxAuth.getDefaultSbxAuth().getUrlSigIn(user));

        //login usuario
        System.out.println(SbxAuth.getDefaultSbxAuth().getUrllogin(user));
        //add manually the response token to the user
        user.token="token-asdf-234-asd";
        SbxAuth.getDefaultSbxAuth().refreshToken(user);

        //insertar un modelo
        Category category = new Category("lacteos");
        System.out.println(SbxModelHelper.getUrlInsertOrUpdateRow(category));
        //add manually the response key to the category
        category.key="laksdf-asdf-234-asdf";

        //insertar un modelo con referencia
        Product product= new Product("leche",13.00,category);
        System.out.println(SbxModelHelper.getUrlInsertOrUpdateRow(product));

        //Buscar los primeros 100 productos cuyo precio sea menor de 40
        SbxQueryBuilder sbxQueryBuilder= SbxModelHelper.prepareQuery(Product.class,1,100);
        sbxQueryBuilder.whereLessThan("price",40);
        System.out.println(SbxModelHelper.getUrlQuery(sbxQueryBuilder));


         //crea un objeto a partir de un Json y una clase personalizada con anotaciones
        JSONObject jsonObject = new JSONObject("{\"price\":13,\"description\":\"leche\",\"expireAt\":\"2017-02-20T20:45:36.756Z\",\"category\":\"laksdf-asdf-234-asdf\"}");
        Product p= (Product) SbxMagicComposer.getSbxModel(jsonObject,Product.class,0);

        //crea objeto incluso si hace referencia a otro
        jsonObject = new JSONObject("{\"_KEY\": \"95979b68-cc4a-416d-46c550380031\",\"price\":13,\"description\":\"leche\",\"expireAt\":\"2017-02-20T20:45:36.756Z\",\"category\":{\"_KEY\": \"laksdf-asdf-234-asdf\",\"description\":\"lacteos\"}}");
        p= (Product) SbxMagicComposer.getSbxModel(jsonObject,Product.class,0);

        SbxQueryBuilder sbxQueryBuilder = SbxModelHelper.prepareQueryToDelete(Product.class);
        sbxQueryBuilder.addDeleteKey(SbxModelHelper.getKeyFromAnnotation(p));
        System.out.println(SbxModelHelper.getUrlDelete(sbxQueryBuilder));


```


Si deseas, puedes utilizar nuestras clases para mayor facilidad, puedes crear tu custom user heredando de SbxUser, y tus custom modelos heredando de SbxModel (Todas las clases que hereden deben tener un constructor vacío) de esta forma:

```java
public class User extends SbxUser {

    @SbxNameField
    String name;

    @SbxUsernameField
    String username;

    @SbxEmailField
    String email;

    @SbxPasswordField
    String password;

    @SbxAuthToken
    String token;

    public User(){}

    public User(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
```

```java
@SbxModelName("Category")
public class Category extends SbxModel {

    @SbxKey
    String key;

    @SbxParamField(name = "description")
    String name;


    public Category(String name) {
        this.name = name;
    }

    public Category(){}
}
```

```java

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
```

De esta forma puedes crear objetos en la nube de esta forma:

```java
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
        user.logInBackground(new SbxSimpleResponse<User>() {
            @Override
            public void onError(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public  void onSuccess(User user) {
                System.out.println();
            }
        });

        final Category category = new Category("lacteos");
        category.saveInBackground(new SbxSimpleResponse<Category>() {
            @Override
            public void onError(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onSuccess(Category category1) {
                //Do something qith category1
                System.out.println(category1.key);
            }
        });
        Product product= new Product("leche",13.00,category);
        product.saveInBackground(new SbxSimpleResponse<Product>() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(Product product1) {
                //Do something qith product1
                System.out.printf(product1.key);

            }
        });
        SbxQuery queryProduct= new SbxQuery(Product.class);
        queryProduct.whereLessThan("price",40);
        queryProduct.findInBackground(new SbxArrayResponse<Product>() {

            @Override
            public void onError(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onSuccess(List<Product> products) {
                    //do something with products
                for (Product p:products){
                    System.out.printf(p.key);
                }
            }
        });

        product.deleteInBackground(new SbxSimpleResponse<Product>() {
            @Override
            public void onError(Exception e) {
                
            }

            @Override
            public void onSuccess(Product tClass) {
                
            }
        });

        user.logOut();
```

Incluso puedes hacer fecth a los objetos

```java

    Product p = new Product();
    p.key="1d3cd5e6-21a8-4de8-97c9-f4177aa6bad4";
    p.fetchInBackground(new SbxSimpleResponse<Product>() {
        @Override
        public void onError(Exception e) {
            e.printStackTrace();
        }

        @Override
        public void onSuccess(Product product) {
            Log.e("producto",product.name);
        }
    });
    String []properties={"category"};
    p.fetchInBackground(new SbxSimpleResponse<Product>() {
        @Override
        public void onError(Exception e) {
            e.printStackTrace();
        }

        @Override
        public void onSuccess(Product product) {
            Log.e("producto",product.name);
            Log.e("categoria",product.category.name);
        }
    },properties);

```

Además puedes utilizar RXJava para cualquira de las anteriores fuciones

```java

        SbxQuery sbxquery= new SbxQuery(Product.class);
        sbxquery.fetch(properties).find(Product.class).
                subscribe(new Consumer<List<Product>>() {
            @Override
            public void accept(List<Product> products) throws Exception {
                for (Product p : products) {
                            Log.e("Productos", p.name);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
```






