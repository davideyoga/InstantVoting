package utilsDb;
//CREARE UN OGGETTO CHE CONTIENE IL FILE E IL PUNTATORE CORRISPONDENTE 

import java.io.File;
import java.util.ArrayList;
import org.neo4j.graphdb.GraphDatabaseService;

public class SingletonGraphDb{
	
	//VARIABILI DI ISATANZA 
    private static SingletonGraphDb instance = null; // ISTANZA DELLA STESSA CLASSE CHE CONTIENE I DATI, INIZIALMENTE A NULL 
    
    private ArrayList<Database> databaseList = new ArrayList<Database>();

    
    //COSTRUTTORE
    private SingletonGraphDb(){
  
    }
    
    //METODI
    public static GraphDatabaseService getDb( File route){
    	
    	if( instance == null ) instance = new SingletonGraphDb(); // SE L'ISTANZA E' A NULL(QUINDI E' LA PRIMA VOLTA CHE LA USO) LA INIZIALIZZO 
  
    	if( !controllaEsistenzaFile( instance.databaseList, route ) ){ // SE L'ARRAY NON CONTIENE IL PERCORSO.... potrei anche usare return instance.data....
    		
    		instance.databaseList.add( new Database(route) ); // AGGIUNGO IL PERCORSO
    		
    		int x = instance.databaseList.size() - 1; // CALCOLO L'INDICE DEL DATABASE
    		    		
    		return instance.databaseList.get( x ).getDb(); // RESTITUISCO IL PUNTATORE AL DATABASE 
    	}
    	else{ // SE HO GIA CREATO IL PUNTATORE AL DATABASE PERCORSO
    		
    		int count = 0; // INIZIALIZZO UN CONTATORE
    		
    		while(count < instance.databaseList.size() ){ // CICLO PER SCORRERE L'ARRAY DEI PERCORSI
    		    
    		    if( ( instance.databaseList.get(count).getFile() ).equals(route) ) break; // CONFRONTO 
    		    
    		    count++; // EVIDENTEMENTE NON HO TROVATO IL FILE DESIDERATO, QUINDI INCREMENTO COUNT
    		}
    		 		
    		return instance.databaseList.get(count).getDb(); // EVIDENTEMENTE HO TROVATO IL PERCORSO, GLI INDICI DEI DUE ARRAY (PERCORSI E DB ) COINCIDONO, QUINDI PASSO IL PUNTATORE AL DB CHE CORRISPONDE AL DATABASE DEL PERCORSO POASSATOMI
    	}
  
    }
    
    private static boolean controllaEsistenzaFile( ArrayList<Database> databaseList, File route ){
    	
    	for( int i = 0; i < databaseList.size(); i++ ){
    		
    		if( databaseList.get(i).getFile().equals(route) ){
    			return true;
    		}
    	}
    	return false;
    }
}





