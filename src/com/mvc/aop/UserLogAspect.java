package com.mvc.aop;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mvc.entity.User;
import com.mvc.entity.UserLog;
import com.mvc.service.UserLogService;

@Component
@Aspect
public class UserLogAspect implements ThrowsAdvice {
	
	@Autowired
	@Qualifier("UserLogServiceImpl")
	UserLogService userLogService;
	
	public UserLogAspect() {
		// TODO Auto-generated constructor stub
		System.out.println("userLogAspect init");
	}

//	@Before(value = "execution(* com.mvc.controller.*.* *(..))")
//	public void doBefore(JoinPoint jp) {
//		// JoinPoint取值示例
//		Object[] obj = jp.getArgs();
//		for (int i = 0; i < obj.length; i++) {
//			System.out.println("pram" + i + ":" + obj[i]);
//		}
//		System.out.println("doBefore");
//	}

	// 后置通知
	@After(value = "execution(* com.mvc.controller.*.login*(..))")
	public void doAfter(JoinPoint jp) {
		Object[] obj = jp.getArgs();
		Map map = new HashMap();
		HttpServletRequest request = (HttpServletRequest) obj[2];
		UserLog userLog = new UserLog();
		userLog.setIp(request.getRemoteAddr());
		
		String temp = (String) obj[0];
		if (temp.charAt(0) == '{') {
			Gson gson = new Gson();
			map = gson.fromJson(obj[0].toString(), new TypeToken<Map<String, String>>(){}.getType());
			userLog.setDevice("手机");
			userLog.setUserName(map.get("name").toString());
		} else {
			userLog.setDevice("网页");
			userLog.setUserName(obj[0].toString());
		}
		userLogService.saveUserLog(userLog);
//		for (int i = 0; i < obj.length; i++) {
//			System.out.println("pram" + i + ":" + obj[i]);
//		}
		
	}

}
