package com.mvc.service;

import java.util.List;

import com.mvc.entity.Rtp;

public interface RtpService {
	public List<Object> getRtpsByContractIdAndProjectName(String contractId, String projectName);
	public List<Object> getRtpsByProjectNameAndReportId(String projectName, String reportId);
	public List<Object> getRtpsByProjectNameAndPropertyId(String projectName, String propertyId);
	public Rtp getTopDateRtpByProjectNameAndPropertyId(String projectName, String propertyId);
	public void save(Rtp rtp);
	public void delete(Rtp rtp);
}
