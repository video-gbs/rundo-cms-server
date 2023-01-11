package com.runjian.auth.server.expression;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName RundoExpressionRoot
 * @Description
 * @date 2023-01-11 周三 10:10
 */
@Component("rundoRbacService")
public class RundoExpressionRoot {
    public boolean hasPermission(HttpServletRequest request, Authentication authentication){
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = ((UserDetails)principal);
            List<GrantedAuthority> authorityList =
                    AuthorityUtils.commaSeparatedStringToAuthorityList(request.getRequestURI());
            return userDetails.getAuthorities().contains(authorityList.get(0));
        }
        return false;
    }
}
