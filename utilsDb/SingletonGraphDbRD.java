package utilsDb;
/*
 * QUANDO SI RICHIEDE UN DATABASE PRIMA DI UTILIZZARLO VA FATTO UN CONTROLLO,
 * SE TALE PUNTATORE RESTITUITO DA getDb E' A NULL SU QUEL PUNTAOTRE SI E' RICHIESTA L'ELIMINAZIONE, 
 * DI CONSEGUENZA NON VI SI PUO PIU ACCEDERE PER PROBLEMATICHE LEGATE ALLA PRECENZA TRA LETTURA ED ELIMINAZIONE
 * 
 * RICORDARSI DI DECREMENTARE IL PUNTATORE UNA VOLTA FINITO
 */


import java.io.File;
import java.util.ArrayList;

import org.neo4j.graphdb.GraphDatabaseService;

public class SingletonGraphDbRD{
	
	//VARIABILI DI ISATANZA 
    private static SingletonGraphDbRD instance = null; // ISTANZA DELLA STESSA CLASSE CHE CONTIENE I DATI, INIZIALMENTE A NULL 
    
    private ArrayList<DatabaseReadDelete> databaseList = new ArrayList<DatabaseReadDelete>();
    
    //COSTRUTTORE
    private SingletonGraphDbRD(){
  
    }
    
    public static GraphDatabaseService getDb( String nameDb){
    	
    	if( instance == null ) instance = new SingletonGraphDbRD(); // SE L'ISTANZA E' A NULL(QUINDI E' LA PRIMA VOLTA CHE LA USO) LA INIZIALIZZO 
    	
    	File route = new File(DatabaseRoute.stringRoute + nameDb); // prendo il percordo da DatabaseRoute e vi aggiungo il nome
    	
    	int count = getIndexDatabase( getDatabaseList(), route ); // CERCO IL DATABASE, TORNA UN VALORE >= DI 0 SE ESISTE, -1 SE NON ESISTE
    	
    	if( count == -1 ){ // SE L'ARRAY NON CONTIENE IL PERCORSO.... potrei anche usare return instance.data....
    		
    		getDatabaseList().add( new DatabaseReadDelete(route) ); // AGGIUNGO IL PERCORSO
    		
    		int x = getDatabaseList().size() - 1; // CALCOLO L'INDICE DEL DATABASE
    		
    		getDatabaseList().get( x ).increaseReaderCount(); // incremento il contatore degli utilizzatori
    		    		
    		return getDatabaseList().get( x ).getDb(); // RESTITUISCO IL PUNTATORE AL DATABASE 
    	}
    	else{ // SE HO GIA CREATO IL PUNTATORE AL DATABASE PERCORSO
    		
    		if( getDatabaseList().get(count).getRequestDelete().get() == true ){ //SE reuesteDelete SI TROVA A TRUE NON POSSO FARLO LEGGERE 
    
    			return null; // torno null in modo che non si effettuino operazioni su tale database, RICORDARE DI FARE IL CECK A NULL
    		}
    		
    		getDatabaseList().get( count ).increaseReaderCount(); // incremento il contatore degli utilizzatori
    		 		
    		return getDatabaseList().get(count).getDb(); // EVIDENTEMENTE HO TROVATO IL PERCORSO, GLI INDICI DEI DUE ARRAY (PERCORSI E DB ) COINCIDONO, QUINDI PASSO IL PUNTATORE AL DB CHE CORRISPONDE AL DATABASE DEL PERCORSO POASSATOMI
    	}
  
    }
    
    protected static boolean decreaseCount( String nameDb){ 
    	
    	if( instance == null ){
    		System.out.println("the database doesn't exist");
    		return false;
    	}
    	
    	File route = new File(DatabaseRoute.stringRoute + nameDb); // prendo il percordo da DatabaseRoute e vi aggiungo il nome
    	    	
    	int count = getIndexDatabase(getDatabaseList(), route);
    	  	
    	if( count == -1 ){
    		System.out.println("the database doesn't exist");
    		return false;
    	}
		
    	getDatabaseList().get(count).decreaseReaderCount(); // DECREMENTO IL CONTATORE DEL DatabaseReaderCount SELEZIONATO
    	
    	if ( getDatabaseList().get(count).getRequestDelete().get() == true && instance.databaseList.get(count).getReaderCount().get() == 0 ){ //SI LO SO E' UN MACELLO: semplicemente mi prendo dall'array dei database contenuti in instance il valore della variabile booleana requestDelete e vedo se si trova a true, a destra dell' and stessa cosa per il reader count

			getDatabaseList().get(count).getDb().shutdown();

			getDatabaseList().remove(count);

			new Delete2(route);	
		}
		return true;
    }
    
    protected static boolean requestDelete( String nameDb) { // TORNA TRUE SE HO ELIMINATO SUBITO IL DATABASE
    	
    	if( instance == null ){
    		System.out.println("the database doesn't exist");
    		return false;
    	} 
    	
    	File route = new File(DatabaseRoute.stringRoute + nameDb); // prendo il percordo da DatabaseRoute e vi aggiungo il nome
    	
    	int count = getIndexDatabase(getDatabaseList(), route);
    	
    	if( count == -1 ){
    		System.out.println("the database doesn't exist");
    		return false;
    	}    	
		
		instance.databaseList.get(count).setRequestDeleteTrue();
		
		System.out.println("requestDelete: " + getDatabaseList().get(count).getRequestDelete().get());
		System.out.println("readerCount: " + getDatabaseList().get(count).getReaderCount().get());
		
		if ( getDatabaseList().get(count).getRequestDelete().get() == true && getDatabaseList().get(count).getReaderCount().get() <= 0 ){ //SI LO SO E' UN MACELLO: semplicemente mi prendo dall'array dei database contenuti in instance il valore della variabile booleana requestDelete e vedo se si trova a true, a destra dell' and stessa cosa per il reader count

			getDatabaseList().get(count).getDb().shutdown(); //CHIUDO IL COLLEGAMENTO AL DATABASE
			System.out.println("puntatore chiuso");

			getDatabaseList().remove(count); // RIMUOVO IL DATABASE DALL'ARRAYLIST
			System.out.println("puntatore rimosso dall' ArrayList");
			
			new Delete2(route);
			return true;
		}
		else{ 
			return false;
		}
    }
    
    
    private static int getIndexDatabase( ArrayList<DatabaseReadDelete> databaseList, File route ){
    	
    	int count = 0; // INIZIALIZZO UN CONTATORE
		
		while( count < getDatabaseList().size() ){ // CICLO PER SCORRERE L'ARRAY DEI PERCORSI	
		    if( ( getDatabaseList().get(count).getFile() ).equals(route) ){
		    	return count; // CONFRONTO 
		    }
		    count++;
		}
		return -1; // SE NON LO TROVO TORNA -1
    }
    
    private static ArrayList<DatabaseReadDelete> getDatabaseList(){
    	return instance.databaseList;
    }
 
}