package org.primefaces.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class TestTable extends LazyDataModel<TestObject> implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<TestObject> list;

    @PostConstruct
    public void init() {
        list = new ArrayList<>(Arrays.asList(
                new TestObject("Thriller", "Michael Jackson", 1982),
                new TestObject("Back in Black", "AC/DC", 1980),
                new TestObject("The Bodyguard", "Whitney Houston", 1992),
                new TestObject("The Dark Side of the Moon", "Pink Floyd", 1973)
        ));
    }

    private int countHashPrev = Integer.MIN_VALUE;
    private int countPrev = Integer.MIN_VALUE;
    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        int currentHash = filterBy.hashCode();
        if (currentHash != countHashPrev) {
            countPrev = Long.valueOf(list.stream().filter(o -> o.getName().contains(getFilterValue(filterBy, "name", ""))).count()).intValue();
            countHashPrev = currentHash;
        }
        return countPrev;
    }

    private int loadHashPrev = Integer.MIN_VALUE;
    private List<TestObject> loadPrev = Collections.emptyList(); 
    @Override
    public List<TestObject> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        int currentHash = Objects.hash(first, pageSize, sortBy, filterBy);
        if (currentHash != loadHashPrev) {
            loadPrev = list.stream().filter(o -> o.getName().contains(getFilterValue(filterBy, "name", ""))).collect(Collectors.toList());
            loadHashPrev = currentHash;
        }
        return loadPrev;
    }

    private String getFilterValue(Map<String, FilterMeta> filterBy, String field, String defaultValue) {
        return filterBy.containsKey(field) ? (String) filterBy.get(field).getFilterValue() : "";
    }
}
