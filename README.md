# androidlib
#Archivo.Digital SDK



El propósito de esta librería es facilitar el desarrollo de aplicaciones android con Archivo.Digital

NOTA: Todos los ejemplos de código mencionados en esta documentación, los puedes ver en ejecución en nuestra aplicación de ejemplo que se encuentra aquí: https://github.com/ArchivoDigital/archivo.digital-androidstart

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

Hay 2 componentes básicos: ADQueryBuilder y ADService

*ADQueryBuilder* es una utilidad para crear queries de manera rápida y evitando errores de sintáxis, supongamos que queremos buscar los registros de tipo PRODUCTO de nuestro dominio de tutorial DEMOAPP.

            int idDominio = 73;
            String nombre = "PRODUCTO";
            int numPagina = 1;
            int tamanoPagina = 100;
            // creamos un query básico con los siguiente parámetros:
            QueryBuilder qb = new QueryBuilder(idDominio, nombre, numPagina, tamanoPagina);
            // adicionalmente vamos a decir que con los resultados del query, nos traiga de una vez el GRUPO al que pertenece un PRODUCTO, lo cual est´á definido en el modelo del PRODUCTO: 
            // PRODUCTO-> GRUPO->	REFERENCE->	GRUPO_PRODUCTO
            qb.fetch(new String[]{"GRUPO"});
            // Finalmente compilamos el query para que nos genere el JSON que lo representa.
            JSONObject jsonQuery = qb.compile();
            
A partir de este momento, podemos usar el json para mandar el query a Archivo.Digital con cualquier librería de conexión HTTP que deseemos y al final recibiremos un JSON de repsuesta que podremos manipular a nuestro antojo.


*ADService* es una utilidad que nos va a permitir agregar un nivel adicional de facilidad para agregar soporte de Archivo.Digital a nuestro proyecto de Android.

Operaciones como cargar datos de *Archivo.Digital* en nuestro modelo de datos serán mucho más simples, veamos un ejemplo:

En el esquema de datos de ejemplo tenemos dos modelos que saremos aquí: PRODUCTO y GRUPO, donde un PRODUCTO tiene una REFERENCIA a su GRUPO a través de la propiedad GRUPO_PRODUCTO.

En nuestro proyecto de Android de ejemplo encontraremos dos clases en dominio escritas usando Java así:

            public class GrupoProducto {
                private String key;
                private String nombre;
                private float vlrDomicilio;

                // ... código get set de los miembros de la clase.

            }

            public class Producto {
                private String key;
                private String nombre;
                private String description;
                private String grupo;
                private GrupoProducto grupoReference;

                // ... código get set de los miembros de la clase.

            }


Lo primero que vamos a hacer es agregar una interfaz a ambas clases cuyo contrato nos va a permitir darles visibilidad en nuestro componente clave: ADService.

Vamos a quedar con:

public class Producto implements ADJSONAware<Producto> {

Y con:

public class GrupoProducto implements ADJSONAware<GrupoProducto> {

Es importante saber que al agregar esta interfaz a nuestras clases, debemos de implementar 2 métodos:

            mapFromJSONObject(JSONObject object) throws JSONException;

            JSONObject toJSONObject() throws JSONException;

mapFromJSONObject es el método que va a recibir el objeto de JSON por parte ADService y a su vez va a definir el mapeo entre las propiedades del JSON y las propiedades del Java Bean, veamos un ejemplo con GrupoProducto:

            @Override
            public GrupoProducto mapFromJSONObject(JSONObject object) throws JSONException {
                setKey(object.getString("_KEY"));
                setNombre(object.getString("NOMBRE"));
                setVlrDomicilio(object.getInt("VALOR_DOMICILIO"));
                return this;
            }


Aquí, tomamos los valores definidos en nuestro modelo que nos llegaron en el JSONObject y los asignamos a las diferentes propiedades de nuestro Java Bean *GrupoProducto*

toJSONObject es el método que nos permite definir usando un JSONObject los valores que serán enviados a nuestro repositorio de datos en Archivo.Digital veamos un ejemplo con GrupoProducto:

            @Override
            public JSONObject toJSONObject() throws JSONException {
                JSONObject obj = new JSONObject();

                // if the object has a key
                if(getKey()!=null){
                    obj.put("_KEY", getKey());
                }

                obj.put("NOMBRE", getNombre());
                obj.put("VALOR_DOMICILIO", getVlrDomicilio());

                return obj;
            }


Listo! Ya nuestro dominio sabe cómo convertirse en JSON y cómo cargarse desde un JSON, con esto resuelto podemos pedir a ADService que nos cargue datos así:






