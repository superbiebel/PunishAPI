package me.superbiebel.punishapi.abstractions;

import me.superbiebel.punishapi.exceptions.ServiceAlreadyRegisteredException;
import me.superbiebel.punishapi.exceptions.ServiceNotFoundException;
import me.superbiebel.punishapi.exceptions.ShutDownException;
import me.superbiebel.punishapi.exceptions.StartupException;
import me.superbiebel.punishapi.services.Service;
import org.apache.logging.log4j.LogManager;

import java.util.concurrent.ConcurrentHashMap;

public abstract class ServiceRegistry<T extends Enum<T>> extends System {
    
    private ConcurrentHashMap<T, Service> serviceRegistryMap;
    
    public void addService(T serviceType, Service service) throws StartupException, ServiceAlreadyRegisteredException {
        if(serviceRegistryMap.containsKey(serviceType)) {
            throw new ServiceAlreadyRegisteredException("Service was already registered");
        }
        serviceRegistryMap.put(serviceType,service);
        service.startup(false);
    }
    public void removeService(T serviceType, boolean kill) throws ShutDownException, ServiceNotFoundException {
        Service service = serviceRegistryMap.remove(serviceType);
        if (service == null) {
            throw new ServiceNotFoundException("Servicetype not found");
        }
        if (kill) {
            service.kill();
        } else {
            service.shutdown();
        }
    }
    public void emptyServiceRegistry(boolean kill) throws ShutDownException, ServiceNotFoundException {
        serviceRegistryMap.keys().asIterator().forEachRemaining(dataServiceType -> {
            try {
                this.removeService(dataServiceType, true);
            } catch (Exception e) {
                LogManager.getLogger().error("Could not unregister and shutdown servicetype: {}", dataServiceType, e);
            }
        });
    }
    protected abstract void onServiceAddedBegin(T serviceType, Service service) throws StartupException, ServiceAlreadyRegisteredException;
    protected abstract void onServiceAddedEnd(T serviceType, Service service) throws StartupException, ServiceAlreadyRegisteredException;
    protected abstract void onServiceRemovedBegin(T serviceType, boolean kill) throws ShutDownException, ServiceNotFoundException;
    protected abstract void onServiceRemovedEnd(T serviceType, boolean kill) throws ShutDownException, ServiceNotFoundException;
    protected abstract void onServiceRegistryEmptyingBegin(boolean kill) throws ShutDownException, ServiceNotFoundException;
    protected abstract void onServiceRegistryEmptyingEnd(boolean kill) throws ShutDownException, ServiceNotFoundException;
    
    
}
