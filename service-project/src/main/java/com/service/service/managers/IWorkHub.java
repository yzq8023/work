package com.service.service.managers;

import com.service.service.entity.*;
import com.service.service.exception.GitBlitException;
import com.service.service.tickets.ITicketService;

import java.util.Collection;

public interface IWorkHub extends IManager,
        IRuntimeManager,
        INotificationManager,
        IUserManager,
        IAuthenticationManager,
        IRepositoryManager,
        IProjectManager,
        IFilestoreManager,
        IPluginManager {

    /**
     * @param user
     * @throws GitBlitException
     */
    void addUser(UserModel user) throws GitBlitException;

    /**
     * @param username
     * @param user
     * @throws GitBlitException
     */
    void reviseUser(String username, UserModel user) throws GitBlitException;

    /**
     * @param team
     * @throws GitBlitException
     */
    void addTeam(TeamModel team) throws GitBlitException;

    /**
     * @param teamname
     * @param team
     * @throws GitBlitException
     */
    void reviseTeam(String teamname, TeamModel team) throws GitBlitException;

    /**
     * @param repository
     * @param user
     * @return
     * @throws GitBlitException
     */
    TaskEntity fork(TaskEntity repository, UserModel user) throws GitBlitException;

    /**
     * @return
     */
    Collection<GitClientApplication> getClientApplications();

    /**
     * Returns the ticket service.
     *
     * @return a ticket service
     * @since 1.4.0
     */
    ITicketService getTicketService();
}
