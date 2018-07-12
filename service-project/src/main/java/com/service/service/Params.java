package com.service.service;

import org.kohsuke.args4j.Option;

import java.io.File;

/**
 * @Author: hollykunge
 * @Description:
 * @Date: 创建于 2018/6/15
 * @Modified:
 */
public class Params {
    public static String baseFolder;

    public final FileSettings FILESETTINGS = new FileSettings(new File(baseFolder, Constants.PROPERTIES_FILE).getAbsolutePath());

    /**
     * Server parameters
     */
    @Option(name = "--help", aliases = { "-h"}, usage = "Show this help")
    public Boolean help = false;

    @Option(name = "--stop", usage = "Stop Server")
    public Boolean stop = false;

    @Option(name = "--tempFolder", usage = "Folder for server to extract built-in webapp", metaVar="PATH")
    public String temp = FILESETTINGS.getString(Keys.server.tempFolder, "temp");

    @Option(name = "--dailyLogFile", usage = "Log to a rolling daily log file INSTEAD of stdout.")
    public Boolean dailyLogFile = false;

    /**
     * GIT Servlet Parameters
     */
    @Option(name = "--repositoriesFolder", usage = "Git Repositories Folder", metaVar="PATH")
    public String repositoriesFolder = FILESETTINGS.getString(Keys.git.repositoriesFolder,
            "git");

    /**
     * Authentication Parameters
     */
    @Option(name = "--userService", usage = "Authentication and Authorization Service (filename or fully qualified classname)")
    public String userService = FILESETTINGS.getString(Keys.realm.userService,
            "users.conf");

    /**
     * JETTY Parameters
     */
    @Option(name = "--httpPort", usage = "HTTP port for to serve. (port <= 0 will disable this connector)", metaVar="PORT")
    public Integer port = FILESETTINGS.getInteger(Keys.server.httpPort, 0);

    @Option(name = "--httpsPort", usage = "HTTPS port to serve.  (port <= 0 will disable this connector)", metaVar="PORT")
    public Integer securePort = FILESETTINGS.getInteger(Keys.server.httpsPort, 8443);

    @Option(name = "--gitPort", usage = "Git Daemon port to serve.  (port <= 0 will disable this connector)", metaVar="PORT")
    public Integer gitPort = FILESETTINGS.getInteger(Keys.git.daemonPort, 9418);

    @Option(name = "--sshPort", usage = "Git SSH port to serve.  (port <= 0 will disable this connector)", metaVar = "PORT")
    public Integer sshPort = FILESETTINGS.getInteger(Keys.git.sshPort, 29418);

    @Option(name = "--alias", usage = "Alias of SSL certificate in keystore for serving https.", metaVar="ALIAS")
    public String alias = FILESETTINGS.getString(Keys.server.certificateAlias, "");

    @Option(name = "--storePassword", usage = "Password for SSL (https) keystore.", metaVar="PASSWORD")
    public String storePassword = FILESETTINGS.getString(Keys.server.storePassword, "");

    @Option(name = "--shutdownPort", usage = "Port for Shutdown Monitor to listen on. (port <= 0 will disable this monitor)", metaVar="PORT")
    public Integer shutdownPort = FILESETTINGS.getInteger(Keys.server.shutdownPort, 8081);

    @Option(name = "--requireClientCertificates", usage = "Require client X509 certificates for https connections.")
    public Boolean requireClientCertificates = FILESETTINGS.getBoolean(Keys.server.requireClientCertificates, false);

    /**
     * Setting overrides
     */
    @Option(name = "--settings", usage = "Path to alternative settings", metaVar="FILE")
    public String settingsfile;

    @Option(name = "--ldapLdifFile", usage = "Path to LDIF file.  This will cause an in-memory LDAP server to be started according to gitblit settings", metaVar="FILE")
    public String ldapLdifFile;

}
