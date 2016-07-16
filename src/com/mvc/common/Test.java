package com.mvc.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.mvc.entity.Project;
import com.mvc.entity.User;
import com.mvc.service.ProjectService;
import com.mvc.service.ProjectServiceImpl;

public class Test {
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		long s= 1386665666777L;
		System.out.println(new Date(s));
	}
}
