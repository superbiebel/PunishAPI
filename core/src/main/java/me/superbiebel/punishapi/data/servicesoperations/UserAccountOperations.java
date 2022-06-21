package me.superbiebel.punishapi.data.servicesoperations;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.superbiebel.punishapi.data.servicesoperations.dataapi.UserRetrieveOperations;
import me.superbiebel.punishapi.dataobjects.UserAccount;
import me.superbiebel.punishapi.exceptions.FailedDataOperationException;

public interface UserAccountOperations extends UserRetrieveOperations {
    UserAccount createUser(Map<String, String> attributes) throws FailedDataOperationException;
    
    
    boolean setUserAttribute(UUID userUUID, String key, String value) throws FailedDataOperationException;
    
    boolean removeUserAttribute(UUID userUUID, String key) throws FailedDataOperationException;
    
    List<UserAccount> getUsersByAttribute(String key, String value) throws FailedDataOperationException;
    
    List<UserAccount> getUsersByAttributeKey(String key) throws FailedDataOperationException;
    
    List<UserAccount> getUsersByAttributeValue(String value) throws FailedDataOperationException;
}
