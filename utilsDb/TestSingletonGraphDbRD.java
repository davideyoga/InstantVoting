package utilsDb;
import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

public class TestSingletonGraphDbRD{
	
	
	public static void main(String args[]){

		GraphDatabaseService graphDb1 = SingletonGraphDbRD.getDb("graphDb");
		GraphDatabaseService graphDb3 = SingletonGraphDbRD.getDb("graphDb");
		SingletonGraphDbRD.decreaseCount("graphDb");
		
		SingletonGraphDbRD.requestDelete("graphDb");
		
		GraphDatabaseService graphDb2 = SingletonGraphDbRD.getDb("graphDb");
		SingletonGraphDbRD.decreaseCount("graphDb");
		
		SingletonGraphDbRD.requestDelete("graphDb");
		
	}

}