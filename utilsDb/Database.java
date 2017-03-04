package utilsDb;
import java.io.File;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Database{
	
	//VARIABLE ISTANCE 
	private File route;
	private GraphDatabaseService db;
		
	
	//COSTRUCTOR
	public Database( File route ){
		
		this.route = route;
		this.db = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder( route ).newGraphDatabase();
	}
	
	public boolean equals( Database database){ // IL METODO EQUAL CONFRONTA SOLO LA ROUTE DELL' ISTANZA IN QUANTO DA QUELLO SI DETERMINA IL COLLEGAMENTO AL DATABASE
		if( this.route == database.route) return true;
		else return false;
	}
	
	//METODI GET
	public File getFile(){
		return this.route;
	}
	
	public GraphDatabaseService getDb(){
		return this.db;
	}
	
}