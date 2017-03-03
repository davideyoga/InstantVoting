package manager;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import utilsDb.DatabaseRoute;
import utilsDb.SingletonGraphDb;

public class CreateElection{
	
	private int numUser;
	private int numCandidates;
	private String[] nameCandidates;
	private GraphDatabaseService graphDb;
	
	
	public CreateElection( String nameElection, int numUserMax, int numCandidates, String[] nameCandidate ){
		
		this.numUser = numUserMax;
		this.numCandidates = numCandidates;
		this.nameCandidates = nameCandidate;
		
		
		String stringRoute = DatabaseRoute.stringRoute + nameElection;

		
		File fileRoute = new File(stringRoute);
		
		this.graphDb = SingletonGraphDb.getDb(fileRoute); // richiedo il puntatore al database( in questo caso verra' creato)
		
		try ( Transaction tx = graphDb.beginTx() )
		{
			
			Label label = Label.label( "DATA" );
				
		    Node dataNode = graphDb.createNode(label);
		    dataNode.setProperty("nameElection", nameElection);
		    dataNode.setProperty("numUserMax", numUserMax);
		    dataNode.setProperty("numCandidates", numCandidates);
		    
		    CreateCandidate( numCandidates, nameCandidate );
		    
		    
		    tx.success();
		}
		SingletonGraphDb.decreaseCount(fileRoute);
		
	}
	
	private void CreateCandidate( int numCandidates, String[] nameCandidate ){
	    
		for( int i = 0; i<numCandidates; i++ ){
	    	
			Label label = Label.label( "Candidate" );
			
			Node candidate = this.graphDb.createNode(label);
			
			candidate.setProperty("name", nameCandidate[i]);
	    }
	}


	public int getNumUser() {
		return numUser;
	}


	public void setNumUser(int numUser) {
		this.numUser = numUser;
	}


	public int getNumCandidates() {
		return numCandidates;
	}


	public void setNumCandidates(int numCandidates) {
		this.numCandidates = numCandidates;
	}


	public String[] getNameCandidates() {
		return nameCandidates;
	}


	public void setNameCandidates(String[] nameCandidates) {
		this.nameCandidates = nameCandidates;
	}
	
	
}