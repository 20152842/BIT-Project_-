package lastcoder.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthHandlerInterceptor implements HandlerInterceptor {
	
//	preHandle() - 리턴값이 false면 컨트롤러는 HandlerInterceptor를 실행하지 않는다.
//	postHandle() - 컨트롤러가 익셉션 없이 실행되었을 때 그 후에 이 메서드가 실행된다.
//	afterCompletion() - 뷰가 클라이언트(브라우저)에 Response를 던진 후에 실행된다. 익셉션이 발생하면 익셉션을 전송한다.
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
//	           HandlerMethod hm = (HandlerMethod) handler;
//	            User sessionUser = (User) request.getSession().getAttribute("USER");
//	            if (hm.hasMethodAnnotation(LoginRequired.class) && sessionUser == null) {
//	                throw new AuthenticationException(request.getRequestURI());
//	            }
//	            if(hm.hasMethodAnnotation(AdminOnly.class) && sessionUser.getAuthority() != "ADMIN") {
//	                throw new AuthorizationException();
//	            }
			
			
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			
			HttpSession session = request.getSession(false);
			
			
			
	        if (session != null) {
	            Object authInfo = session.getAttribute("authInfo");
	            if (authInfo != null) {
	                return true;
				}
	        }
	        response.sendRedirect(request.getContextPath() + "/login");
		}

        return false;
	}

}