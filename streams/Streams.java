package streams;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 *
 * @author erosh
 */
public class Streams {
    
    private static ArrayList<String> arregloStrings; // Variable que contendrá los strings del archivo txt en git

    //Función principal
    public static void main(String[] args) {
        try {            
            arregloStrings = new ArrayList<>(); //Inicializo la lista que contendrá los Strings
            capturarStrings("https://raw.githubusercontent.com/dwyl/english-words/master/words.txt"); //Cargo los strings del archivo en el arreglo
            int cantidadVocales =  arregloStrings.parallelStream().map(palabra->evaluarVocalesStream(palabra)).reduce(Integer::sum).get();
            int cantidadContenidos =  arregloStrings.parallelStream().map(palabra->evaluarContenidoStream(palabra)).reduce(Integer::sum).get();
            int cantidadPartes =  arregloStrings.parallelStream().map(palabra->evaluarParteStream(palabra)).reduce(Integer::sum).get();
            System.out.println("Palabras con iguales vocales: "+Integer.toString(cantidadVocales));
            System.out.println("Palabras que contienen otras: "+Integer.toString(cantidadContenidos));
            System.out.println("Palabras que son partes de otras: "+Integer.toString(cantidadPartes));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    // Función para capturar los strings del archivo "words.txt" del repositorio de Git
    // Se llena el arreglo de Strings previamente creado, con todos los strings que contiene el archivo
    public static void capturarStrings(String stringURL) throws Exception {
      StringBuilder resultado = new StringBuilder();
      URL url = new URL(stringURL);
      HttpURLConnection connectionToURL = (HttpURLConnection) url.openConnection();
      connectionToURL.setRequestMethod("GET");
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connectionToURL.getInputStream()));
      String lineaLectura;
      while ((lineaLectura = bufferedReader.readLine()) != null) {
         arregloStrings.add(lineaLectura.toLowerCase());
      }
      bufferedReader.close();
    }  
    
    // Implementacion y Conversión de Stream mediante Map Reduce para la función de evaluar Strings como parte de Otros
    // El arreglo se combierte a un stream en paralelo, se le aplica un map con la funcion de evaluarParte a cada elemento    
    public static int evaluarParteStream(String palabra){
        int resultadoEvaluarParte = arregloStrings.parallelStream().map(parte->evaluarParte(palabra, parte)).reduce(Integer::sum).get();      
        return resultadoEvaluarParte - 1; //Se debe retornar con -1 pues el stream empieza en 1
    }
    
    // Implementacion y Conversión de Stream mediante Map Reduce para la función de evaluar Strings contenidos en Otros
    // El arreglo se combierte a un stream en paralelo, se le aplica un map con la funcion de evaluarContenido a cada elemento
    public static int evaluarContenidoStream(String palabra){
        int resultadoEvaluarContenido = arregloStrings.parallelStream().map(contenido->evaluarContenido(palabra, contenido)).reduce(Integer::sum).get();        
        return resultadoEvaluarContenido - 1; //Se debe retornar con -1 pues el stream empieza en 1
    }
    
    // Implementacion y Conversión de Stream mediante Map Reduce para la función de evaluar vocales dados 2 Strings
    // El arreglo se combierte a un stream en paralelo, se le aplica un map con la funcion de evaluarVocales a cada elemento
    public static int evaluarVocalesStream(String palabra){
        int resultadoEvaluarVocales = arregloStrings.parallelStream().map(vocales->evaluarVocales(palabra, vocales)).reduce(Integer::sum).get();        
        return resultadoEvaluarVocales - 1; //Se debe retornar con -1 pues el stream empieza en 1
    }
    
    // Función que determina si una palabra es parte de otra palabra
    public static int evaluarParte(String palabra1, String palabra2){
        for(int caracter = 0; caracter < palabra1.length(); caracter++){ //Por cada caracter en palabra1
            String nuevoString = ""; //creo un string vacio 
            nuevoString += palabra1.charAt(caracter); //voy concatenando cada caracter al string
            if(palabra2.contains(nuevoString)){ //a medida que concateno el string, pregunto si está contenido en palabra2
                return 1; //Si sí es parte de, retorno 1
            }
        }
        return 0; //Si no es parte de, retorno 0
    }    
    
    // Función que determina si una palabra está contenida en otra
    public static int evaluarContenido(String palabra1, String palabra2){
        if(palabra1.contains(palabra2)){ //En caso de que palabra2 esté contenida en palabra1
            return 1; //Retorna 1 si sí
        } else{
            return 0; //Retorna 0 si no
        }
    }
    
    // Función que determina si un string tiene las mismas vocales que otro string
    public static int evaluarVocales(String palabra1, String palabra2){
        for(int caracter = 0; caracter < palabra1.length(); caracter++){ //Por cada caracter en palabra1
            String nuevoString = ""; //creo un string vacio
            nuevoString += palabra1.charAt(caracter); //voy concatenando cada caracter al string
            //Si el nuevo string obtenido de string1 es igual a alguna vocal pero no está contenido en string2
            if( (nuevoString.equals("a")|| nuevoString.equals("e") || nuevoString.equals("i") || nuevoString.equals("o") || nuevoString.equals("u")) && !palabra2.contains(nuevoString)){
                return 0; // retorno 0, significa que no aplica 
            }
        }
        return 1; // retorno 1, ambos string cumplen con las mismas vocales
    }                     
}