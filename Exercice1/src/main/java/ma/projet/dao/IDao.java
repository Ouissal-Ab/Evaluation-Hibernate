package ma.projet.dao;

import java.util.List;

public interface IDao<T> {
    boolean create(T o);
    boolean update(T o);
    boolean delete(T o);
    T getById(int id);
    List<T> getAll();
}