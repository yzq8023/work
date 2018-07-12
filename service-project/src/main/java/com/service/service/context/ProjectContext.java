package com.service.service.context;

import com.service.service.FileSettings;
import com.service.service.IStoredSettings;
import com.service.service.Params;
import com.service.service.extensions.LifeCycleListener;
import com.service.service.managers.*;
import com.service.service.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hollykunge
 * @Description:
 * @Date: 创建于 2018/6/15
 * @Modified:
 */
@WebListener
public class ProjectContext implements ServletContextListener {

    private static ProjectContext projectContext;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final List<IManager> managers = new ArrayList<IManager>();

    private final IStoredSettings goSettings;

    private final File goBaseFolder;

    public ProjectContext() {
        this(null, null);
    }

    public ProjectContext(IStoredSettings settings, File baseFolder) {
        String folder = "data";
        Params.baseFolder = folder;
        Params params = new Params();
        baseFolder = getBaseFolder(params);
        settings = params.FILESETTINGS;
        this.goSettings = settings;
        this.goBaseFolder = baseFolder;
        projectContext = this;
    }

    private static File getBaseFolder(Params params) {
        String path = System.getProperty("GITBLIT_HOME", Params.baseFolder);
        if (!StringUtils.isEmpty(System.getenv("GITBLIT_HOME"))) {
            path = System.getenv("GITBLIT_HOME");
        }

        return new File(path).getAbsoluteFile();
    }

    protected void startCore(ServletContext servletContext) {
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        IStoredSettings runtimeSettings = context.getBean(IStoredSettings.class);
        final File baseFolder;

        // Gitblit GO
        baseFolder = configureGO(servletContext, goSettings, goBaseFolder, runtimeSettings);

        // Manually configure IRuntimeManager
        logManager(IRuntimeManager.class);
        IRuntimeManager runtime = context.getBean(RuntimeManager.class);
        runtime.setBaseFolder(baseFolder);
        runtime.getStatus().isGO = goSettings != null;
        runtime.getStatus().servletContainer = servletContext.getServerInfo();
        runtime.start();
        managers.add(runtime);

        startManager(context.getBean(UserManager.class));
        startManager(context.getBean(AuthenticationManager.class));
        startManager(context.getBean(RepositoryManager.class));
        startManager(context.getBean(ProjectManager.class));
        startManager(context.getBean(IWorkHub.class));
//        startManager(context.getBean(IServicesManager.class));
        startManager(context.getBean(FilestoreManager.class));
        startManager(context.getBean(PluginManager.class));
        logger.info("");
        logger.info("All managers started.");
        logger.info("");

        IPluginManager pluginManager = context.getBean(PluginManager.class);
        for (LifeCycleListener listener : pluginManager.getExtensions(LifeCycleListener.class)) {
            try {
                listener.onStartup();
            } catch (Throwable t) {
                logger.error(null, t);
            }
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        startCore(servletContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    protected <X extends IManager> X startManager(X x) {
        x.start();
        managers.add(x);
        return x;
    }

    /**
     * Configures Gitblit GO
     *
     * @param context
     * @param goSettings
     * @param goBaseFolder
     * @param runtimeSettings
     * @return the base folder
     */
    protected File configureGO(
            ServletContext context,
            IStoredSettings goSettings,
            File goBaseFolder,
            IStoredSettings runtimeSettings) {

        logger.debug("configuring Gitblit GO");

        // merge the stored settings into the runtime settings
        //
        // if runtimeSettings is also a FileSettings w/o a specified target file,
        // the target file for runtimeSettings is set to "localSettings".
        runtimeSettings.merge(goSettings);
        File base = goBaseFolder;
        return base;
    }

    protected void logManager(Class<? extends IManager> clazz) {
        logger.info("");
        logger.info("----[{}]----", clazz.getName());
    }

    public static <X extends IManager> X getManager(Class<X> managerClass) {
        for (IManager manager : projectContext.managers) {
            if (managerClass.isAssignableFrom(manager.getClass())) {
                return (X) manager;
            }
        }
        return null;
    }
}
