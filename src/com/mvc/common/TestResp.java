package com.mvc.common;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mvc.entity.Project;
import com.mvc.entity.User;
import com.mvc.entity.Utp;
import com.mvc.entity.UserLoginResult;

public class TestResp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String json = "{\"projects\":[{\"id\":1,\"name\":\"project1\",\"map\":\"111\",\"creator\":\"test1\",\"date\":\"111\",\"machinecost\":\"1111\",\"peoplecost\":\"1111\",\"materialcost\":\"1111\",\"othercost\":\"1111\",\"valuation\":\"111\",\"allcost\":\"4444\",\"contractId\":\"1\"},{\"id\":2,\"name\":\"project2\",\"map\":\"222\",\"creator\":\"test1\",\"date\":\"2\",\"machinecost\":\"2\",\"peoplecost\":\"2\",\"materialcost\":\"2\",\"othercost\":\"2\",\"valuation\":\"21\",\"allcost\":\"8\",\"contractId\":\"1\"}],\"utps\":[{\"id\":1,\"username\":\"test1\",\"projectname\":\"project1\",\"permission\":\"1\"},{\"id\":2,\"username\":\"test1\",\"projectname\":\"project2\",\"permission\":\"1\"}],\"code\":\"00000\",\"user\":{\"id\":1,\"name\":\"test1\",\"turename\":\"测试帐号\",\"pwd\":\"1234\",\"phone\":\"15820034791\",\"date\":\"\",\"remark\":\"111\",\"authority\":\"1\"},\"msg\":\"登陆成功\"}";
				TestResp tr = new TestResp();
		tr.readResp(json);
	}

	public void readResp(String json){//解析resp返回信息:resp->(data->(list(list)->utp,project),user),code,msg
		Gson gson = new Gson();
		UserLoginResult results = gson.fromJson(json, UserLoginResult.class);
		User user = results.getUser();
		List<Utp> utps = results.getUtps();
		System.out.println(utps.get(0).getUserName());
//		System.out.println(user.getName());
		}

	
}