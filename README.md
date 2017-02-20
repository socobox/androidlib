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
                compile 'com.github.ArchivoDigital:androidlib:1.0.5'
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


    public Category(String name) {
        this.name = name;
    }
}
```
Si un atributo hace referencia a otro modelo, se puede realizar de la siguiente forma:

```
@SbxModelName("Product")
public class Product {

    @SbxKey
    String key;

    @SbxParamField(name = "description")
    String name;

    @SbxParamField(name = "price")
    double price;

    @SbxParamField(name = "expireAt")
    Date expireAt;

    @SbxParamField(name = "category")
    Category category;

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
        SbxAuth.initialize(80,"KASDFasd-asdfadsf-asdfadsf-asdf");

        //Datos
        //Registrar un usuario
        User user= new User("luis gabriel","lgguzman","lgguzman@sbxcloud.co","123456");
        System.out.println(SbxAuth.getDefaultSbxAuth().getUrlSigIn(user));

        //login usuario
        System.out.println(SbxAuth.getDefaultSbxAuth().getUrllogin(user));
        user.token="token-asdf-234-asd";
        SbxAuth.getDefaultSbxAuth().refreshToken(user);

        //insertar un modelo
        Category category = new Category("lacteos");
        System.out.println(SbxModelHelper.getUrlinsertRow(category));
        category.key="laksdf-asdf-234-asdf";

        //insertar un modelo con referencia
        Product product= new Product("leche",13.00,category);
        System.out.println(SbxModelHelper.getUrlinsertRow(product));

        //Buscar los primeros 100 productos cuyo precio sea menor de 40
        SbxQueryBuilder sbxQueryBuilder= SbxModelHelper.prepareQuery(Product.class,1,100);
        sbxQueryBuilder.whereLessThan("price",40);
        System.out.println(SbxModelHelper.getUrlQuery(sbxQueryBuilder));
```