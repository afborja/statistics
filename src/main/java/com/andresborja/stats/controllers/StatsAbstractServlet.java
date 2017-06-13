package com.andresborja.stats.controllers;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.andresborja.stats.statscollector.State;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Setter;

/**
 * General definitions for all servlets in the application.
 * @author afborja
 */
public abstract class StatsAbstractServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ObjectMapper mapper = new ObjectMapper();

    @Inject
    @Setter private State state;

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());
        AutowireCapableBeanFactory ctx = context.getAutowireCapableBeanFactory();
        ctx.autowireBean(this);
    }

    protected State getState() {
        return state;
    }

    protected ObjectMapper getMapper() {
        return mapper;
    }

}
