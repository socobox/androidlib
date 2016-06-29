package archivo.digital.android;

/**
 * @author https://archivo.digital
 */
public interface ADCallback<T> {

    public void ok(T obj);
    public void err(String msg);
}
