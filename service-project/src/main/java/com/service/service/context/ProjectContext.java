package com.service.service.context;

import com.service.service.FileSettings;
import com.service.service.IStoredSettings;
import com.service.service.Keys;
import com.service.service.Params;
import com.service.service.extensions.LifeCycleListener;
import com.service.service.managers.*;
import com.service.service.utils.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

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
        String folder = "data";
        Params.baseFolder = folder;
        Params params = new Params();
        this.goSettings = params.FILESETTINGS;
        this.goBaseFolder = getBaseFolder(params);
        projectContext = this;
    }
    private final Params start(Params params) {
        final File baseFolder = getBaseFolder(params);
        FileSettings settings = params.FILESETTINGS;
        if (!StringUtils.isEmpty(params.settingsfile)) {
            if (new File(params.settingsfile).exists()) {
                settings = new FileSettings(params.settingsfile);
            }
        }
        if (params.dailyLogFile) {
            // Configure log4j for daily log file generation
            InputStream is = null;
            try {
                is = getClass().getResourceAsStream("/log4j.properties");
                Properties loggingProperties = new Properties();
                loggingProperties.load(is);

                loggingProperties.put("log4j.appender.R.File", new File(baseFolder, "logs/gitblit.log").getAbsolutePath());
                loggingProperties.put("log4j.rootCategory", "INFO, R");

                if (settings.getBoolean(Keys.web.debugMode, false)) {
                    loggingProperties.put("log4j.logger.com.gitblit", "DEBUG");
                }

                PropertyConfigurator.configure(loggingProperties);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //确保有一个已定义的用户服务
        String realmUsers = params.userService;
        if (StringUtils.isEmpty(realmUsers)) {
            logger.error(MessageFormat.format("PLEASE SPECIFY {0}!!", Keys.realm.userService));
            return null;
        }
        // 从命令行中覆盖设置
        settings.overrideSetting(Keys.realm.userService, params.userService);
        settings.overrideSetting(Keys.git.repositoriesFolder, params.repositoriesFolder);
//        settings.overrideSetting(Keys.git.daemonPort, params.gitPort);
//        settings.overrideSetting(Keys.git.sshPort, params.sshPort);

        return params;
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
//        IStoredSettings runtimeSettings = context.getBean(IStoredSettings.class);
        final File baseFolder;



        // Manually configure IRuntimeManager
        logManager(IRuntimeManager.class);
        IRuntimeManager runtime = context.getBean(RuntimeManager.class);

        // Gitblit GO
        baseFolder = configureGO(servletContext, goSettings, goBaseFolder, runtime.getSettings());

        runtime.setBaseFolder(baseFolder);
        runtime.getStatus().isGO = goSettings != null;
        runtime.getStatus().servletContainer = servletContext.getServerInfo();
//        runtime.start();
        managers.add(runtime);

//        startManager(context.getBean(UserManager.class));
//        startManager(context.getBean(AuthenticationManager.class));
//        startManager(context.getBean(RepositoryManager.class));
//        startManager(context.getBean(ProjectManager.class));
//        startManager(context.getBean(IWorkHub.class));
//        startManager(context.getBean(FilestoreManager.class));
//        startManager(context.getBean(PluginManager.class));
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
