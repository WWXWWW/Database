package database;
import java.sql.Types;
import java.util.HashMap;

/**
 * This class represents a tuple that will contain a single row's worth of information
 * from a table. It also includes information about where it is stored
 * @author Sam Madden modified by Doug Shook
 *
 */

public class Tuple {
	TupleDesc tuple;
	HashMap<String, Field> map;
	int pid;
	int id;
	
	
	/**
	 * Creates a new tuple with the given description
	 * @param t the schema for this tuple
	 */
	public Tuple(TupleDesc t) {
		
		tuple = t;
		map = new HashMap<String, Field>();
	}
	
	public TupleDesc getDesc() {
		
		return tuple;
	}
	
	/**
	 * retrieves the page id where this tuple is stored
	 * @return the page id of this tuple
	 */
	
	public int getPid() {
		
		return pid;
		
	}

	public void setPid(int pid) {
	
		this.pid = pid;
	}

	/**
	 * retrieves the tuple (slot) id of this tuple
	 * @return the slot where this tuple is stored
	 */
	public int getId() {
		
		return id;
	}

	public void setId(int id) {

		this.id = id;
	}
	
	public void setDesc(TupleDesc td) {
		
		this.tuple = td;
	}
	
	/**
	 * Stores the given data at the i-th field
	 * @param i the field number to store the data
	 * @param v the data
	 */
	public void setField(int i, Field v) {
		
		String key = tuple.getFieldName(i);
		map.put(key, v);
		
	}
	
	public Field getField(int i) {
		
		String key = tuple.getFieldName(i);
		return map.get(key);
	}
	
	/**
	 * Creates a string representation of this tuple that displays its contents.
	 * You should convert the binary data into a readable format (i.e. display the ints in base-10 and convert
	 * the String columns to readable text).
	 */
	
	
	public String toString() {
		
		String ans = "";
		
		for(int i = 0; i<map.size();i++) {
			String key = tuple.getFieldName(i);
			if( i == map.size() - 1) {
				ans += map.get(key).toString() + "\n";
			}else {
				ans += map.get(key).toString() + "\t";
			}
			
		}
		return ans;

	}
}
	