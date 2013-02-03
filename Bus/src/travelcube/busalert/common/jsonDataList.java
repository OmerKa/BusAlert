/**
 * 
 */
package travelcube.busalert.common;

import java.util.List;


/**
 * @author omer
 *
 */
public class jsonDataList<T> {

	private List<T> data;

	/**
	 * @return the data
	 */
	public List<T> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<T> data) {
		this.data = data;
	}
}
