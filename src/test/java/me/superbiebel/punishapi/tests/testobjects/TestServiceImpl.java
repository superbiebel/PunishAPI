package me.superbiebel.punishapi.tests.testobjects;

import lombok.Getter;
import me.superbiebel.punishapi.SystemStatus;
import me.superbiebel.punishapi.services.data.TestService;
import me.superbiebel.punishapi.exceptions.FailedServiceOperationException;
import me.superbiebel.punishapi.exceptions.ShutDownException;
import me.superbiebel.punishapi.exceptions.StartupException;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestServiceImpl implements TestService {
    
    private final Lock startupShutdownLock = new ReentrantLock(true);
    
    @Getter
    private AtomicReference<SystemStatus> status = new AtomicReference<>(SystemStatus.DOWN);
    
    @Override
    public String exampleOperation(String returnString) throws FailedServiceOperationException {
        return returnString;
    }
    
    @Override
    public void startup(boolean force) throws StartupException {
        try {
            startupShutdownLock.lock();
            if (status.get() == SystemStatus.STARTING_UP) {
                throw new IllegalStateException("Cannot start up while already starting up!");
            }
            if ((status.get() == SystemStatus.READY || status.get() == SystemStatus.FORCED_READY) && !force) {
                throw new IllegalStateException("System already started up!");
            }
            status.set(SystemStatus.STARTING_UP);
    
            //insert startup sequence here
    
    
            if (force) {
                status.set(SystemStatus.FORCED_READY);
            } else {
                status.set(SystemStatus.READY);
            }
    
        } catch (Exception e) {
            throw new StartupException(e);
        } finally {
            startupShutdownLock.unlock();
        }
    }
    
    @Override
    public void shutdown() throws ShutDownException {
        try {
            startupShutdownLock.lock();
            if (status.get() ==SystemStatus.DOWN || status.get() ==SystemStatus.KILLED) {
                throw new IllegalStateException("System already shut down!");
            }
            //insert shutdown sequence here
            
            status.set(SystemStatus.DOWN);
    
        } catch (Exception e) {
            throw new ShutDownException(e);
        } finally {
            startupShutdownLock.unlock();
        }
    }
    
    @Override
    public void kill() {
        try {
            startupShutdownLock.lock();
            if (status.get() ==SystemStatus.KILLED) {
                throw new IllegalStateException("System already killed!");
            } else if (status.get() ==SystemStatus.DOWN) {
                throw new IllegalStateException("System already shut down!");
            } else {
                //insert kill sequence here
    
    
    
                status.set(SystemStatus.KILLED);
            }
        
        } finally {
            startupShutdownLock.unlock();
        }
    }
    
    @Override
    public SystemStatus status() {
        return status.get();
    }
    
}
