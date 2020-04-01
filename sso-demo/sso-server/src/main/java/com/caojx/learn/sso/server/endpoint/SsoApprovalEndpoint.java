package com.caojx.learn.sso.server.endpoint;

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.endpoint.WhitelabelApprovalEndpoint;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

/**
 * 完全copy代码 {@link WhitelabelApprovalEndpoint} 之后做小部分修改
 *
 * 隐藏确认授权页面  <body style="display:none;">
 * 自动提交确认授权表单         builder.append("<script>document.getElementById(\"confirmationForm\").submit()</script>");
 *
 * @author caojx created on 2020/3/22 5:43 下午
 */
@RestController
@SessionAttributes({"authorizationRequest"})
public class SsoApprovalEndpoint {

    public SsoApprovalEndpoint() {
    }


    /**
     * 需要确认授权的时候，会使用该方法返回html展示确认授权页面
     *
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping({"/oauth/confirm_access"})
    public ModelAndView getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        final String approvalContent = this.createTemplate(model, request);
        if (request.getAttribute("_csrf") != null) {
            model.put("_csrf", request.getAttribute("_csrf"));
        }

        View approvalView = new View() {
            public String getContentType() {
                return "text/html";
            }

            public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setContentType(this.getContentType());
                response.getWriter().append(approvalContent);
            }
        };
        return new ModelAndView(approvalView, model);
    }

    protected String createTemplate(Map<String, Object> model, HttpServletRequest request) {
        AuthorizationRequest authorizationRequest = (AuthorizationRequest)model.get("authorizationRequest");
        String clientId = authorizationRequest.getClientId();
        StringBuilder builder = new StringBuilder();
        /*
        * <body style="display:none;"> 隐藏 确认授权页
        * */
        builder.append("<html><body style=\"display:none;\"></div><h1>OAuth Approval</h1>");
        builder.append("<p>Do you authorize \"").append(HtmlUtils.htmlEscape(clientId));
        builder.append("\" to access your protected resources?</p>");
        builder.append("<form id=\"confirmationForm\" name=\"confirmationForm\" action=\"");
        String requestPath = ServletUriComponentsBuilder.fromContextPath(request).build().getPath();
        if (requestPath == null) {
            requestPath = "";
        }

        builder.append(requestPath).append("/oauth/authorize\" method=\"post\">");
        builder.append("<input name=\"user_oauth_approval\" value=\"true\" type=\"hidden\"/>");
        String csrfTemplate = null;
        CsrfToken csrfToken = (CsrfToken)((CsrfToken)(model.containsKey("_csrf") ? model.get("_csrf") : request.getAttribute("_csrf")));
        if (csrfToken != null) {
            csrfTemplate = "<input type=\"hidden\" name=\"" + HtmlUtils.htmlEscape(csrfToken.getParameterName()) + "\" value=\"" + HtmlUtils.htmlEscape(csrfToken.getToken()) + "\" />";
        }

        if (csrfTemplate != null) {
            builder.append(csrfTemplate);
        }

        String authorizeInputTemplate = "<label><input name=\"authorize\" value=\"Authorize\" type=\"submit\"/></label></form>";
        if (!model.containsKey("scopes") && request.getAttribute("scopes") == null) {
            builder.append(authorizeInputTemplate);
            builder.append("<form id=\"denialForm\" name=\"denialForm\" action=\"");
            builder.append(requestPath).append("/oauth/authorize\" method=\"post\">");
            builder.append("<input name=\"user_oauth_approval\" value=\"false\" type=\"hidden\"/>");
            if (csrfTemplate != null) {
                builder.append(csrfTemplate);
            }

            builder.append("<label><input name=\"deny\" value=\"Deny\" type=\"submit\"/></label></form>");
        } else {
            builder.append(this.createScopes(model, request));
            builder.append(authorizeInputTemplate);
        }

        /*自动提交确认授权*/
        builder.append("<script>document.getElementById(\"confirmationForm\").submit()</script>");

        builder.append("</body></html>");
        return builder.toString();
    }

    private CharSequence createScopes(Map<String, Object> model, HttpServletRequest request) {
        StringBuilder builder = new StringBuilder("<ul>");
        Map<String, String> scopes = (Map)((Map)(model.containsKey("scopes") ? model.get("scopes") : request.getAttribute("scopes")));
        Iterator var5 = scopes.keySet().iterator();

        while(var5.hasNext()) {
            String scope = (String)var5.next();
            String approved = "true".equals(scopes.get(scope)) ? " checked" : "";
            String denied = !"true".equals(scopes.get(scope)) ? " checked" : "";
            scope = HtmlUtils.htmlEscape(scope);
            builder.append("<li><div class=\"form-group\">");
            builder.append(scope).append(": <input type=\"radio\" name=\"");
            builder.append(scope).append("\" value=\"true\"").append(approved).append(">Approve</input> ");
            builder.append("<input type=\"radio\" name=\"").append(scope).append("\" value=\"false\"");
            builder.append(denied).append(">Deny</input></div></li>");
        }

        builder.append("</ul>");
        return builder.toString();
    }
}
