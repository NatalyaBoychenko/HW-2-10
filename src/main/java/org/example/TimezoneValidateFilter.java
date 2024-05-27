package org.example;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        Set<String> availableUTC = getAvailableUTC();
        String zone = TimeZoneUtil.getZone(req);

        if (availableUTC.contains(zone)){
            chain.doFilter(req, res);
        } else {
            res.setStatus(400);
            res.setContentType("text/html; charset=utf-8");
            res.getWriter().write("<h1>Invalid timezone</h1>");
            res.getWriter().close();
        }
    }

    private static Set<String> getAvailableUTC() {
        Set<String> timeZones = new HashSet<>();

        for (String id : TimeZone.getAvailableIDs()) {
            TimeZone tz = TimeZone.getTimeZone(id);
            int offsetHr = tz.getRawOffset() / 1000 / 60 / 60;
            String timeZone = "%+d".formatted(offsetHr);
            timeZones.add(timeZone);
        }

        return timeZones;
    }

}
