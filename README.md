# AppConsultora-Android

AppConsultora es una aplicacion que tiene como proposito ser el principal asesor de negocio de la consultora esto mediante la personalizacion y mejora de la experiencia de usuario mobile.

Para poder realizar esta aplicación se ha utilizado Architecture Clean caracterizada por ser una arquitectura robusta, segura y escalable. 

Architecture Clean permite cumplir los siguientes conjuntos de practicas:

* Independiente de los frameworks. 
* Comprobable
* Independiente de la interfaz de usuario.
* Independiente de la base de datos.
* Independiente de cualquier agente externo.

![Architecture Clean](https://fernandocejas.com/assets/migrated/clean_architecture1.png)

A continuación se describe como se encuentra estructurado la arquitectura que utiliza el app:

![Capas](https://fernandocejas.com/assets/migrated/clean_architecture_android.png)

Y como estas se comunican entre sí:

![Evolucion](https://fernandocejas.com/assets/migrated/clean_architecture_evolution.png)

Trabajar con Dagger 

![Dagger](https://fernandocejas.com/assets/migrated/composed_dagger_graph1.png)

* Modulo: Clase que proporciona las dependencias a traves de sus metodos con el fin de satisfacerlas cuando se construye la instancia de la clase. 
* Component: Vienen siendo injectores entre `@Inject` y el `@Module` su funcion principal es poner ambos juntos.
* Inject: Esta anotación es para solicitar las dependencias.
* Provide: Esta anotación indica como se construira las dependencias mencionadas.
* Scope: Anotación que indica el ciclo de vida de una dependencia `@PerActivity`, `@PerFragment`, `@PerUser`.
* Qualifier: Anotación que indica cuando el tipo de clase es insuficiente para identificar a una dependencia `@ForApplication`, `@ForActivity`

## Generar Hash para PRD Facebook
$JAVA_HOME/bin/keytool -exportcert -alias belcorpstore -keystore config/belcorp.keystore | openssl sha1 -binary | openssl base64
* Hash generado: IPgt5n0zfRiXbCY74Z5b+1NXBy4=

# Pruebas Unitarias 
./gradlew assembleEsikaPpr
./gradlew assembleEsikaPprAndroidTest

appcenter test run espresso --app "Belcorp/Consultoras" --devices "Belcorp/tops" --app-path consultoras-esika-prod-debug-1.0.5.apk  --test-series "master" --locale "es_MX" --test-apk-path presentation-esika-prod-debug-androidTest.apk

java -jar spoon-runner.jar \
    --apk consultoras-esika-ppr-1.1.0.apk \
    --test-apk consultoras-esika-ppr-androidTest.apk \
    --sdk ~/Android/Sdk

## Descargar información de la tienda

```
bundle exec fastlane supply init -m fastlane/metadata/esika -p biz.belcorp.consultoras.esika
bundle exec fastlane supply init -m fastlane/metadata/lbel -p biz.belcorp.consultoras.lbel
``` 