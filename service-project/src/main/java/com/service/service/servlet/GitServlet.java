package com.service.service.servlet;

import org.eclipse.jgit.http.server.GitFilter;
import com.service.service.git.GitblitReceivePackFactory;
import com.service.service.git.GitblitUploadPackFactory;
import com.service.service.git.RepositoryResolver;
import com.service.service.managers.IWorkHub;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Enumeration;

import static com.service.service.Constants.GIT_PATH;
import static com.service.service.Constants.R_PATH;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * @auther: dk
 * @date: 2018/7/7
 * @des:
 */
@WebServlet(urlPatterns = {R_PATH, GIT_PATH})
public class GitServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final GitFilter gitFilter;

    @Autowired
    public GitServlet(IWorkHub gitblit) {
        gitFilter = new GitFilter();
        gitFilter.setRepositoryResolver(new RepositoryResolver<HttpServletRequest>(gitblit));
        gitFilter.setUploadPackFactory(new GitblitUploadPackFactory<HttpServletRequest>(gitblit));
        gitFilter.setReceivePackFactory(new GitblitReceivePackFactory<HttpServletRequest>(gitblit));
    }

    @Override
    public void init(final ServletConfig config) throws ServletException {

        gitFilter.init(new FilterConfig() {
            @Override
            public String getFilterName() {
                return gitFilter.getClass().getName();
            }

            @Override
            public String getInitParameter(String name) {
                return config.getInitParameter(name);
            }

            @Override
            public Enumeration<String> getInitParameterNames() {
                return config.getInitParameterNames();
            }

            @Override
            public ServletContext getServletContext() {
                return config.getServletContext();
            }
        });

        init();
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        gitFilter.doFilter(req, res, new FilterChain() {
            @Override
            public void doFilter(ServletRequest request,
                                 ServletResponse response) throws IOException,
                    ServletException {
                ((HttpServletResponse) response).sendError(SC_NOT_FOUND);
            }
        });
    }

    @Override
    public void destroy() {
        gitFilter.destroy();
    }
}
