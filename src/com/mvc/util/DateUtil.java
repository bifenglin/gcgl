package com.mvc.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

@Service
public class DateUtil {

    public String dayStartDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return String.valueOf(c.getTime().getTime());
    }
    
    public String dayEndDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return String.valueOf(c.getTime().getTime());
    }
    
    public long getYesterdayEndTime(){  
	    	Calendar yesterdayEnd=Calendar.getInstance();
	    	yesterdayEnd.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
	    	yesterdayEnd.add(Calendar.DATE,-1);
	    	yesterdayEnd.set(Calendar.HOUR_OF_DAY, 23);
	    	yesterdayEnd.set(Calendar.MINUTE, 59);  
	    	yesterdayEnd.set(Calendar.SECOND, 59);  
	    	yesterdayEnd.set(Calendar.MILLISECOND, 999);
	    	Date time=yesterdayEnd.getTime();
    	    return yesterdayEnd.getTime().getTime();
    	    
    	} 
    	 public  long getYesterdayStartTime(){
	    		 
	    	Calendar yesterdayStart=Calendar.getInstance();
	    	yesterdayStart.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
	    	yesterdayStart.add(Calendar.DATE,-1);
	    	yesterdayStart.set(Calendar.HOUR_OF_DAY, 0);
	    	yesterdayStart.set(Calendar.MINUTE, 0);  
	    	yesterdayStart.set(Calendar.SECOND, 0);  
	    	yesterdayStart.set(Calendar.MILLISECOND, 0);
	    	Date time=yesterdayStart.getTime();
    	    return yesterdayStart.getTime().getTime();
    	    
    	} 
}
