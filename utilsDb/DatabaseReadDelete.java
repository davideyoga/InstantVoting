package utilsDb;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseReadDelete extends Database{
	
	private AtomicBoolean requestDelete;
	private AtomicInteger readerCount;
	
	
	public DatabaseReadDelete(File route) {
		
		super(route);
		this.requestDelete = new AtomicBoolean(false);
		this.readerCount = new AtomicInteger(0);
	}
	
	protected void increaseReaderCount(){
		this.readerCount.incrementAndGet();
	}
	
	protected void decreaseReaderCount(){
		this.readerCount.decrementAndGet();
	}
	 
	protected void setRequestDeleteTrue(){
		this.requestDelete.set(true);
	}
	
	//GETTER
	public AtomicBoolean getRequestDelete() {
		return this.requestDelete;
	}

	public AtomicInteger getReaderCount() {
		return readerCount;
	}	
}