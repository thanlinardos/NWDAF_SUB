package io.nwdaf.eventsubscription.repository;

import java.util.Map;

public interface TableEntity {
    public Map<String, Object> getData();
    public void setData(Map<String, Object> data);
    public Long getId();
    public void setId(Long id);
}
