package org.example;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.rest.security.auth.ProcessEngineAuthenticationFilter;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CustomFilter extends ProcessEngineAuthenticationFilter {

    Cache<String, List<String>> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();


    protected void setAuthenticatedUser(ProcessEngine engine, String userId, List<String> groupIds, List<String> tenantIds){
        try {
            List<String> groupId = cache.get(userId, () -> getGroupsOfUser(engine,userId));
            // One Tenant only
            engine.getIdentityService().setAuthentication(userId, groupId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
