package com.thankyou.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.thankyou.struts.form.LoginForm;
 
public class LoginAction extends Action {
 
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
     
        String target = null;
        LoginForm loginForm = (com.thankyou.struts.form.LoginForm)form;
             
        if(loginForm.getUserName().equals("admin")
                && loginForm.getPassword().equals("admin123")) {
            target = "success";
            request.setAttribute("message", loginForm.getPassword());
        }
        else {
            target = "failure";
        }
         
        return mapping.findForward(target);
    }
}