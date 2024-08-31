package org.mwangi.maya.utility;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {
    public  static String getSiteURL(HttpServletRequest request){
        //get the url of the string
        String siteURL=request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(),"");
    }
}
