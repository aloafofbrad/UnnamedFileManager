/**
 * @author Bradley Nickle
 */

public interface ManagerObserver {
    public void update(Subject s);
    public void search(Subject s);
    public void sort(Subject s);
}
