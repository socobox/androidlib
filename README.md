# androidlib
#Archivo.Digital SDK



El propósito de esta librería es facilitar el desarrollo de aplicaciones android con Archivo.Digital

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

ADQueryBuilder es una utilidad para crear queries de manera rápida y evitando errores de sintáxis, supongamos que queremos buscar los registros de tipo PRODUCTO de nuestro dominio de tutorial DEMOAPP.

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
            
A partir de este momento, podemos usar el json para mandar el query a Archivo.Digital con cualquier librería de conexión HTTP que deseemos.

ADService es una utilidad que nos va a permitir agregar un nivel adicional de facilidad para agregar soporte de Archivo.Digital a nuestro proyecto de Android.



