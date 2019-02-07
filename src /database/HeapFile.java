//Peiyun Xie

package database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A heap file stores a collection of tuples. It is also responsible for managing pages.
 * It needs to be able to manage page creation as well as correctly manipulating pages
 * when tuples are added or deleted.
 * @author Sam Madden modified by Doug Shook
 *
 */
public class HeapFile {
	
	public static final int PAGE_SIZE = 4096;
	private File f;
	private TupleDesc type;
	/**
	 * Creates a new heap file in the given location that can accept tuples of the given type
	 * @param f location of the heap file
	 * @param types type of tuples contained in the file
	 */
	public HeapFile(File f, TupleDesc type) {
		
		this.f = f;
		this.type = type;
		
	}
	
	public File getFile() {
		
		return f;
	}
	
	public TupleDesc getTupleDesc() {
		
		return type;
	}
	
	/**
	 * Creates a HeapPage object representing the page at the given page number.
	 * Because it will be necessary to arbitrarily move around the file, a RandomAccessFile object
	 * should be used here.
	 * @param id the page number to be retrieved
	 * @return a HeapPage at the given page number
	 */
	
	public HeapPage readPage(int id) {
		
		try {
			
			byte[] page = new byte[HeapFile.PAGE_SIZE];
			int offset = HeapFile.PAGE_SIZE * id;
			RandomAccessFile randomFile = new RandomAccessFile(f, "r");
			
			randomFile.seek(offset);
			randomFile.readFully(page);
			randomFile.close();
			
			HeapPage newHeapPage = new HeapPage(id, page, this.getId());
			return newHeapPage;
			
		}catch(FileNotFoundException e){
			throw new IllegalArgumentException();
			
		}catch(IOException e) {
			throw new IllegalArgumentException();
		}
		
		
	}
	
	/**
	 * Returns a unique id number for this heap file. Consider using
	 * the hash of the File itself.
	 * @return
	 */
	public int getId() {

		return f.getAbsolutePath().hashCode();
	}
	
	/**
	 * Writes the given HeapPage to disk. Because of the need to seek through the file,
	 * a RandomAccessFile object should be used in this method.
	 * @param p the page to write to disk
	 */
	public void writePage(HeapPage p) {
		
		try {
			
			int offset = HeapFile.PAGE_SIZE * (p.getId());
			RandomAccessFile randomFile = new RandomAccessFile(f, "rw");
			randomFile.seek(offset);
			randomFile.write(p.getPageData(), 0, HeapFile.PAGE_SIZE);
			randomFile.close();
		}
		catch(FileNotFoundException e){
			
			throw new IllegalArgumentException();
		}catch(IOException e) {
			
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Adds a tuple. This method must first find a page with an open slot, creating a new page
	 * if all others are full. It then passes the tuple to this page to be stored. It then writes
	 * the page to disk (see writePage)
	 * @param t The tuple to be stored
	 * @return The HeapPage that contains the tuple
	 * @throws Exception 
	 */
	public HeapPage addTuple(Tuple t) throws Exception {
				
		ArrayList<HeapPage> openSlotPages= new ArrayList<>();
		HeapPage page;
		
		for (int i = 0;i < this.getNumPages();i++) {
			
			HeapPage hp = this.readPage(i);
			
			for (int j = 0; j< hp.getNumSlots(); j++) {
				if (!hp.slotOccupied(i)) {
					openSlotPages.add(hp);
					break;
				}
			}
		}

		if(!openSlotPages.isEmpty()) {
			
			page = openSlotPages.get(0);
			page.addTuple(t);
		}
		else {
		
			int pageNums = this.getNumPages()+1;
			int newId = this.getId();
			byte[] newByte = new byte[HeapFile.PAGE_SIZE];
			page = new HeapPage(pageNums,newByte,newId);
			page.addTuple(t);
		}
		
		this.writePage(page);
		return page;
	}
	
	/**
	 * This method will examine the tuple to find out where it is stored, then delete it
	 * from the proper HeapPage. It then writes the modified page to disk.
	 * @param t the Tuple to be deleted
	 * @throws Exception 
	 */
	public void deleteTuple(Tuple t) throws Exception{
	
		int pid = t.getPid();
		HeapPage page = this.readPage(pid);
		page.deleteTuple(t);
		writePage(page);
	}
	
	/**
	 * Returns an ArrayList containing all of the tuples in this HeapFile. It must
	 * access each HeapPage to do this (see iterator() in HeapPage)
	 * @return
	 */
	public ArrayList<Tuple> getAllTuples() {
		
		ArrayList<Tuple> myList = new ArrayList<Tuple>();
		
		for (int i=0;i<this.getNumPages();i++) {
			
			HeapPage page = this.readPage(i);
			Iterator<Tuple> iter = page.iterator();
			
			while(iter.hasNext()) {
				myList.add(iter.next());
			}
		}
		return myList;	
	
	}
	
	
	/**
	 * Computes and returns the total number of pages contained in this HeapFile
	 * @return the number of pages
	 */
	public int getNumPages() {
		//your code here
		return (int)Math.ceil((double)(f.length()/HeapFile.PAGE_SIZE));
	}
}
