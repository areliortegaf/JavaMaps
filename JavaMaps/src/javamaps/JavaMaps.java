package javamaps;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
//imports de la libreria de maps
import javax.swing.JPanel;
import maps.java.Elevation;
import maps.java.Geocoding;
import maps.java.MapsJava;
import maps.java.Places;
import maps.java.Route;
import maps.java.StaticMaps;
import maps.java.StreetView;

public class JavaMaps extends Application {

    
    String direccionGeo = "Walter Scott #906, Alamedas, Chihuahua Mexico";
    String puntoA ="Barcelona";
    String puntoB ="Madrid";
    String direccionStreetView = "Walter Scott #906, Alamedas, Chihuahua Mexico";
    String direccionMapaEstatico = "Madrid, Puerta del Sol";
    String claveAPI ="AIza......................";
    double placesLat = 40.4171111;
    double placesLng = -3.7031133;
    String url = "http://google.com";

    //INICIALIZA EL NAVEGADOR
    @Override
    public void start(final Stage stage) {
        stage.setWidth(800);
        stage.setHeight(500);
        Scene scene = new Scene(new Group());

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(browser);

        webEngine.getLoadWorker().stateProperty()
                .addListener(new ChangeListener<State>() {
                    @Override
                    public void changed(ObservableValue ov, State oldState, State newState) {

                        if (newState == Worker.State.SUCCEEDED) {
                            stage.setTitle(webEngine.getLocation());
                        }

                    }
                });
        webEngine.load(url);

        scene.setRoot(scrollPane);

        stage.setScene(scene);
        stage.show();

        JPanel panel = new JPanel();
        
        
        geocoding();
        status();
        Teide();
        ruta();
        streetview();
        MapaEstatico();
        places();
    }
    
    
    //
    //AQUI SE INICIALIZAN LOS METODOS
    //
    
    //METODO DE ERROR

    public static void error(String funcionError) {
        System.err.println("Algo ocurrió, no se pudo ejecutar la función: " + funcionError);
    }

    public void geocoding() {
        Geocoding ObjGeocod = new Geocoding();
        try {

            Point2D.Double resultadoCD = ObjGeocod.getCoordinates(direccionGeo);
            System.out.println("Las coordenadas de " + direccionGeo + ", son: "
                    + resultadoCD.x + "," + resultadoCD.y);

            System.out.println("Los resultados obtenidos para la búsqueda de dirección de "
                    + resultadoCD.x + "," + resultadoCD.y + " son:");
            ArrayList<String> resultadoCI = ObjGeocod.getAddress(resultadoCD.x, resultadoCD.y);
            for (String item : resultadoCI) {
                System.out.println(item);
            }
        } catch (Exception e) {
            error("Codificación geográfica");
        }
    }
//MUESTRA EL STATUS DE LA REGION, MAPA ETC

    public void status() {
        System.out.println("Sensor activo: " + MapsJava.getSensor());
        System.out.println("Idioma: " + MapsJava.getLanguage());
        MapsJava.setRegion("mx");
        System.out.println("Región: " + MapsJava.getRegion());
        System.out.println("Clave: " + MapsJava.getKey());

        System.out.println("Ahora vamos a comprobar el registro de peticiones");
        String[][] registroPeticiones = MapsJava.getStockRequest();
        for (int i = 0; i < registroPeticiones.length; i++) {
            System.out.println("Petición " + i);
            for (int j = 0; j < registroPeticiones[0].length; j++) {
                System.out.print(registroPeticiones[i][j] + "\t");
            }
        }
    }

    public void Teide() {

        Elevation ObjElevat = new Elevation();
        try {
            double resultadoAlt = ObjElevat.getElevation(28.2725, -16.6425);
            System.out.println("La altitud del punto 28.2725,-16.6425 es: " + String.valueOf(resultadoAlt) + " metros");
            System.out.println("La resolución de dicha altitud es de :" + String.valueOf(ObjElevat.getResolution()) + " metros");
        } catch (Exception e) {
            error("Elevación");
        }
    }

    public void ruta() {
        Route ObjRout = new Route();
        try {
            String[][] resultado = ObjRout.getRoute(puntoA, puntoB, null, Boolean.TRUE, Route.mode.driving, Route.avoids.nothing);
            for (int i = 0; i < resultado.length; i++) {
                System.out.println("Tramo " + i + ":");
                for (int j = 0; j < resultado[0].length; j++) {
                    System.out.print(resultado[i][j] + "\t");
                }
                System.out.println("");
            }
        } catch (Exception e) {
            error("Ruta");
        }
    }
     
    public void streetview(){
        StreetView ObjStreet=new StreetView();
        try {
            Image imgResultado=ObjStreet.getStreetView(direccionStreetView, new Dimension(300,300),
                    90, 100, -100);
            System.out.println("La URL asociada a la imagen es: " + MapsJava.getLastRequestURL());
        } catch (Exception e) {
            error("Street View");
        }
    }
    

    public void MapaEstatico(){
        StaticMaps ObjStatMap=new StaticMaps();
        try {
            Image resultadoMapa=ObjStatMap.getStaticMap(direccionMapaEstatico, 14,new Dimension(300,300),
                    1, StaticMaps.Format.png, StaticMaps.Maptype.terrain);
            System.out.println("La URL asociada al mapa es: " + MapsJava.getLastRequestURL());
        } catch (Exception e) {
            error("Mapas estáticos");
        }
    }
    
    public void places(){
        MapsJava.setKey(claveAPI);
        System.out.println("Bien, ya hemos puesto la clave. Ahora estamos comprobando si es correcta..");
        if("OK".equals(MapsJava.APIkeyCheck(claveAPI))){
             System.out.println("La clave es correcta. Ya estamos buscando locales "
                + "cercanos a \"40.4171111,-3.7031133\"...\n");
            Places ObjPlace=new Places();
            try {
                String[][] resultado=ObjPlace.getPlaces(placesLat, placesLng, 
                        3000, "", "", Places.Rankby.prominence, null);

                for(int i=0;i<resultado.length;i++){
                    System.out.println("Place " + i + ":");
                    for(int j=0;j<resultado[0].length;j++){
                        System.out.print(resultado[i][j] + "\t");
                    }
                    System.out.println("");
                }
            } catch (Exception e) {
                error("Place");
            }
            
        }else{
            System.out.println("Lo sentimos, la clave no es correcta :(");
        }
    }
    
//METODO MAIN
    public static void main(String[] args) {
        launch(args);
     
        
    }
}
