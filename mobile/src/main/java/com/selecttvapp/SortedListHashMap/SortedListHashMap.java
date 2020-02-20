package com.selecttvapp.SortedListHashMap;



import java.util.ArrayList;
        import java.util.Collection;
        import java.util.Collections;
        import java.util.Comparator;
        import java.util.HashMap;
        import java.util.Iterator;

/**
 * This class is used to hold key-value pairs data in some sorted order.
 *
 * @param <X>
 * @param <Y>
 */
public class SortedListHashMap<X, Y> implements Iterable<X> {

    private ArrayList<X> list;
    private ArrayList<X> filteredList;
    private HashMap<X, Y> map;
    private Comparator<Y> mComparator;

    public SortedListHashMap(Comparator<Y> comparator) {
        list = new ArrayList<X>();
        map = new HashMap<X, Y>();
        mComparator = comparator;
    }

    public void add(X key, Y value) {
        int pos = -1;
        if (map.containsKey(key)){
            pos = list.indexOf(key);
            list.remove(key);
        }
        if (pos != -1)
            list.add(pos, key);
        else
            list.add(key);

        map.put(key, value);


        if (mComparator != null) {

            for (int i = list.size() - 1; i > 0 && mComparator.compare(value, map.get(list.get(i - 1))) < 0; i--) {

                try {

                    Collections.swap(list, i, i - 1);

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        }


    }

    public boolean add(int index, X key, Y value) {
        if (mComparator != null) {
            return false;
        }
        if (map.containsKey(key))
            list.remove(key);
        list.add(index, key);
        map.put(key, value);
        return true;
    }

    public void addAll(SortedListHashMap<X, Y> sMap) {
        if (sMap == null)
            return;
        list.addAll(sMap.list);
        map.putAll(sMap.map);
        mComparator = sMap.mComparator;
        if (sMap.filteredList != null) {
            filteredList = new ArrayList<X>();
            filteredList.addAll(sMap.filteredList);
        }
    }

    public void setComparator(Comparator<Y> comparator) {
        mComparator = comparator;
    }

    public int size() {
        return list.size();
    }

    public Y get(int index) {
        return map.get(list.get(index));
    }

    public Y getByKey(X key) {
        return map.get(key);
    }

    public Y remove(int index) {
        X key = list.get(index);
        return removeKey(key);
    }

    public Y removeKey(X key) {
        list.remove(key);
        if (filteredList != null && filteredList.contains(key))
            filteredList.remove(key);
        return map.remove(key);
    }

    public boolean containsKey(X key) {
        return list.contains(key);
    }

    public void clear() {
        list.clear();
        map.clear();
        if (filteredList != null)
            filteredList.clear();
    }

    @Override
    public Iterator<X> iterator() {
        return Collections.unmodifiableList(list).iterator();
    }


    public Collection<Y> getValues() {

        return map.values();
    }

    public Collection<X> getKeys() {

        return map.keySet();
    }
}