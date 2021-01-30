package com.lagou.config;

import com.lagou.utils.ParameterMapping;

import java.util.List;

public class BoundSql {

    private String parseSql;

    private List<ParameterMapping> parameterMappings;

    public BoundSql(String parseSql, List<ParameterMapping> parameterMappings) {
        this.parseSql = parseSql;
        this.parameterMappings = parameterMappings;
    }

    public String getParseSql() {
        return parseSql;
    }

    public void setParseSql(String parseSql) {
        this.parseSql = parseSql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }
}
