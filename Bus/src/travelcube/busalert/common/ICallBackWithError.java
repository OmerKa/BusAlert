/**
 * 
 */
package travelcube.busalert.common;

/**
 * @author omer
 *
 */
public interface ICallBackWithError {
	
	public void callback(Object... arg0);
	
	public void onError(Exception ex);

}
