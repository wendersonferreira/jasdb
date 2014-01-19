package nl.renarj.jasdb.core;

import nl.renarj.jasdb.api.metadata.MetadataStore;
import nl.renarj.jasdb.api.model.DBInstanceFactory;
import nl.renarj.jasdb.core.caching.GlobalCachingMemoryManager;
import nl.renarj.jasdb.core.exceptions.ConfigurationException;
import nl.renarj.jasdb.core.exceptions.JasDBException;
import nl.renarj.jasdb.core.exceptions.JasDBStorageException;
import nl.renarj.jasdb.core.exceptions.LocatorException;
import nl.renarj.jasdb.core.locator.NodeInformation;
import nl.renarj.jasdb.core.platform.PlatformManager;
import nl.renarj.jasdb.core.platform.PlatformManagerFactory;
import nl.renarj.jasdb.service.StorageServiceFactory;
import nl.renarj.jasdb.storage.exceptions.RecordStoreInUseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleKernel {
    private static final Logger log = LoggerFactory.getLogger(SimpleKernel.class);

    private static final String FALLBACK_JASDB_XML = "default-jasdb.xml";
    private static final String JASDB_CONFIG = "jasdb.xml";


    private static final String GRID_CONFIG_PATH = "/jasdb/Grid";
    private static final String GRID_ID_CONFIG = "id";

    private static final Lock reconfigureLock = new ReentrantLock();
    private static SimpleKernel INSTANCE;

    private String instanceId = PlatformManagerFactory.getPlatformManager().getProcessId();
    private NodeInformation nodeInformation;
    private String kernelVersion = "unknown";
    private String gridId;

//    private ApplicationContext applicationContext;
    private PlatformManager platformManager;

//    private Configuration configuration;
//	private Injector injector;
    private RemoteService remoteService;

    private List<Extension> loadedExtensions;

    private MetadataStore metadataStore;
    private DBInstanceFactory instanceFactory;
    private StorageServiceFactory storageServiceFactory;

    private CountDownLatch latch = new CountDownLatch(1);

    private SimpleKernel() throws ConfigurationException {
		bootstrapKernel();
        log.debug("Loaded kernel with classloader instance: {}", getClass().getClassLoader().hashCode());
	}
	
	public static DBInstanceFactory getInstanceFactory() throws ConfigurationException {
        return getInstance().platformManager.getComponent(DBInstanceFactory.class);
	}
	
	public static StorageServiceFactory getStorageServiceFactory() throws ConfigurationException {
        return getInstance().platformManager.getComponent(StorageServiceFactory.class);
	}

    public static <T> T getKernelModule(Class<T> requiredModuleType) throws JasDBStorageException {
//        return getInstance().injector.getInstance(requiredModuleType);
        throw new JasDBStorageException("Temporarily disabled");
    }

    public static MetadataStore getMetadataStore() throws JasDBStorageException {
        throw new JasDBStorageException("Temporarily disabled");
    }

    public static NodeInformation getNodeInformation() throws JasDBStorageException {
        try {
            return getInstance().nodeInformation;
        } catch(ConfigurationException e) {
            throw new JasDBStorageException("Unable to get node information: " + e.getMessage());
        }
    }
	
	private static SimpleKernel getInstance() throws ConfigurationException {
		if(INSTANCE == null) {
            loadKernel();
		}
		return INSTANCE;
	}
	
	public static void shutdown() throws JasDBException {
        reconfigureLock.lock();
        try {
            if(INSTANCE != null) {
                INSTANCE.kernelShutdown();
            }
        } finally {
            reconfigureLock.unlock();
        }
	}

    private void kernelShutdown() throws JasDBException {
        log.info("Shutting down kernel");


//        KernelContext context = new KernelContext(injector, configuration, nodeInformation, metadataStore);
//        for(Extension extension : loadedExtensions) {
//            extension.shutdown(context);
//        }

//        log.debug("Stopping remote service endpoint: {}", remoteService.getClass().getName());
//        remoteService.stopService();

        log.debug("Doing kernel shutdown, stopping instance and storage services");
//        getStorageServiceFactory().shutdownServiceFactory();
//        getInstanceFactory().shutdown();

        GlobalCachingMemoryManager.shutdown();

//        metadataStore.closeStore();

        log.info("KernelShutdown shutdown complete");
        INSTANCE.latch.countDown();
        INSTANCE = null;

        PlatformManagerFactory.getPlatformManager().shutdownPlatform();
    }

    /**
     * Public kernel bootstrap initializer, this guarantees the kernel gets started with all services
     * @throws ConfigurationException
     */
	public static void initializeKernel() throws ConfigurationException {
		getInstance();
	}

    public static void waitForShutdown() throws ConfigurationException {
        try {
            getInstance().latch.await();
        } catch(InterruptedException e) {
            log.info("Interrupted whilst waiting for kernel shutdown");
        }
    }

    /**
     * This performs the actual kernel loading in case it was not initialized
     * @throws ConfigurationException
     */
	private static void loadKernel() throws ConfigurationException {
        reconfigureLock.lock();
        try {
            if(INSTANCE == null) {
                INSTANCE = new SimpleKernel();
            }
        } finally {
            reconfigureLock.unlock();
        }
	}
    
	private void bootstrapKernel() throws ConfigurationException {
		log.info("Bootstrapping database kernel");


        platformManager = PlatformManagerFactory.getPlatformManager();
        log.info("Initializing platform: {}", platformManager);

        platformManager.initializePlatform();

        log.info("Finished platform initialization");

//		String kernelBindingModule = configuration.getAttribute("kernel");
//		if(StringUtils.stringNotEmpty(kernelBindingModule)) {
			try {
//				log.info("Loading kernel binding: {}", kernelBindingModule);
//				this.injector = Guice.createInjector(ReflectionLoader.loadClass(AbstractModule.class,
//						kernelBindingModule, new Object[] { this.configuration }));
//
//                this.instanceFactory = injector.getInstance(DBInstanceFactory.class);
//                this.remoteService = this.injector.getInstance(RemoteService.class);
//                this.storageServiceFactory = injector.getInstance(StorageServiceFactory.class);


//                this.nodeInformation = new NodeInformation(instanceId, gridId);
//                this.nodeInformation.addServiceInformation(this.remoteService.getServiceInformation());

//                log.info("Opening central metadata store");
//                metadataStore = new JasDBMetadataStore();
//                metadataStore.openStore();

//                log.info("Initializing storage service factory to: {}", storageServiceFactory.getClass().getName());
//                KernelContext kernelContext = new KernelContext(injector, configuration, nodeInformation, metadataStore);
//                instanceFactory.initializeServices(kernelContext);
//                storageServiceFactory.initializeServices(kernelContext);


//                CredentialsProvider credentialsProvider = this.injector.getInstance(CredentialsProvider.class);
//                log.info("Initializing credentials provider: {}", credentialsProvider.getClass().getName());
//                credentialsProvider.initialize(kernelContext);
//
//                log.info("Starting remote service: {}", remoteService.getClass().getName());
//                this.remoteService.startService();

                kernelVersion = PlatformManagerFactory.getPlatformManager().getVersionData();
                log.info("Booting JasDB version: {}", kernelVersion);
                log.info("JasDB process id: {}", instanceId);
                registerShutdownHooks();

//                ServiceLoader<Extension> extensions = ServiceLoader.load(Extension.class);
                this.loadedExtensions = new ArrayList<>();
//                for(Extension extension : extensions) {
//                    log.info("Loading extension: {}", extension);
//                    extension.load(kernelContext);
//                    this.loadedExtensions.add(extension);
//                }
//			} catch(ReflectionException e) {
//                handleBootupError("Unable to load kernel bindings", e);
//			} catch (ServiceException e) {
//                handleBootupError("Unable to load kernel, remote service could not be started", e);
            }
            catch (LocatorException e) {
                handleBootupError("Unable to load kernel, unable to start locator service", e);
            } catch(RecordStoreInUseException e) {
                handleBootupError("Kernel cannot be loaded, DB is currently in use, are you running another JasDB instance on the same instance location?", e);
            } catch(JasDBException e) {
                handleBootupError("Unable to load kernel, storage services cannot be initialized", e);
            }
//        } else {
//			throw new ConfigurationException("No kernel specified");
//		}
        log.info("Finished booting kernel, JASDB ready for usage...");
	}

    private void handleBootupError(String message, Throwable e) throws ConfigurationException {
        latch.countDown();
        String errorMessage = message + ": " + e.getMessage();
        log.error(message, e);
        throw new ConfigurationException(errorMessage);
    }

    private void registerShutdownHooks() throws JasDBStorageException {
        Thread shutdownThread = new Thread(new KernelShutdown());
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }

	
//	private void loadConfiguration() throws ConfigurationException {
//        try {
//            String overrideConfigProperty = System.getProperty("jasdb-config");
//            if(StringUtils.stringEmpty(overrideConfigProperty)) {
//		        this.configuration = Configuration.loadConfiguration(JASDB_CONFIG);
//            } else {
//                log.info("Override configuration path specified: {}", overrideConfigProperty);
//                this.configuration = Configuration.loadConfiguration(overrideConfigProperty);
//            }
//        } catch (CoreConfigException e) {
//            try {
//                this.configuration = Configuration.loadConfiguration(FALLBACK_JASDB_XML);
//            } catch(CoreConfigException ex) {
//                throw new ConfigurationException("Unable to load Default JasDB configuration file", ex);
//            }
//        }
//	}
	
//	private void configure(Configuration configuration) throws ConfigurationException {
//		Configuration statConfig = configuration.getChildConfiguration("/jasdb/Statistics");
//		if(statConfig != null && statConfig.getAttribute("enabled", false)) {
//			StatisticsMonitor.enableStatistics();
//		}
//
//        Configuration gridConfiguration = configuration.getChildConfiguration(GRID_CONFIG_PATH);
//        if(gridConfiguration != null) {
//            gridId = gridConfiguration.getAttribute(GRID_ID_CONFIG, null);
//        }
//
//    }

    public static String getVersion() throws ConfigurationException {
        return getInstance().kernelVersion;
    }
}
