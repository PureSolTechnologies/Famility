package com.puresoltechnologies.famility.server.rest.impl.services;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.famility.server.impl.finance.CurrencyDefinition;
import com.puresoltechnologies.famility.server.impl.finance.TradingManager;
import com.puresoltechnologies.famility.server.rest.impl.auth.RolesAllowed;

@Path("/finance")
public class FinanceService {

    private static final Logger logger = LoggerFactory.getLogger(FinanceService.class);

    private static final TradingManager tradingManager = new TradingManager();

    @GET
    @Path("/currencies")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(roles = { "Administrator" })
    public List<CurrencyDefinition> getCurrencies() throws SQLException {
	return tradingManager.getCurrencies();
    }
}