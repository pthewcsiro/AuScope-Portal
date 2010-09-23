package org.auscope.portal.server.web.view;

import java.net.URL;
import java.util.Arrays;

import org.auscope.portal.csw.CSWGeographicBoundingBox;
import org.auscope.portal.csw.CSWGeographicElement;
import org.auscope.portal.csw.CSWOnlineResource;
import org.auscope.portal.csw.CSWRecord;
import org.auscope.portal.csw.CSWOnlineResource.OnlineResourceType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.springframework.ui.ModelMap;

public class TestViewCSWRecordFactory {
	private Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    private CSWRecord mockCSWRecord = context.mock(CSWRecord.class);
    private CSWOnlineResource mockOnlineRes = context.mock(CSWOnlineResource.class);
    private CSWGeographicBoundingBox mockBbox = context.mock(CSWGeographicBoundingBox.class);
    
    @Test
    public void testToView() throws Exception {
    	ViewCSWRecordFactory factory = new ViewCSWRecordFactory();
    	
    	final String serviceName = "sn";
    	final String contactOrg = "co";
    	final String fileId = "asb";
    	final String recordInfoUrl = "http://bob.xom";
    	final String dataAbstract = "assda";
    	
    	final URL orUrl = new URL("http://hah.com");
    	final String orName = "ascom";
    	final String orDesc = "desc";
    	final OnlineResourceType orType = OnlineResourceType.WFS;
    	
    	final double bboxNorth = 10;
    	final double bboxSouth = 5;
    	final double bboxEast = 7;
    	final double bboxWest = 6;
    	
    	final ModelMap expectation = new ModelMap();
    	final ModelMap onlineResExpectation = new ModelMap();
    	final ModelMap geoExpectation = new ModelMap();
    	
    	expectation.put("serviceName", serviceName);
    	expectation.put("contactOrganisation", contactOrg);
    	expectation.put("fileIdentifier", fileId);
    	expectation.put("recordInfoUrl", recordInfoUrl);
    	expectation.put("dataIdentificationAbstract", dataAbstract);
    	expectation.put("onlineResources", Arrays.asList(onlineResExpectation));
    	expectation.put("geographicElements", Arrays.asList(geoExpectation));
    	
    	onlineResExpectation.put("url", orUrl.toString());
    	onlineResExpectation.put("name", orName);
    	onlineResExpectation.put("description", orDesc);
    	onlineResExpectation.put("onlineResourceType", orType.name());
    	
    	geoExpectation.put("type", "bbox");
    	geoExpectation.put("eastBoundLongitude", bboxEast);
    	geoExpectation.put("westBoundLongitude", bboxWest);
    	geoExpectation.put("northBoundLatitude", bboxNorth);
    	geoExpectation.put("southBoundLatitude", bboxSouth);
    	
    	context.checking(new Expectations() {{
    		oneOf(mockCSWRecord).getServiceName();will(returnValue(serviceName));
    		oneOf(mockCSWRecord).getContactOrganisation();will(returnValue(contactOrg));
    		oneOf(mockCSWRecord).getFileIdentifier();will(returnValue(fileId));
    		oneOf(mockCSWRecord).getRecordInfoUrl();will(returnValue(recordInfoUrl));
    		oneOf(mockCSWRecord).getDataIdentificationAbstract();will(returnValue(dataAbstract));
    		oneOf(mockCSWRecord).getOnlineResources();will(returnValue(new CSWOnlineResource[] {mockOnlineRes}));
    		oneOf(mockCSWRecord).getCSWGeographicElements();will(returnValue(new CSWGeographicElement[] {mockBbox}));
    		
    		oneOf(mockBbox).getEastBoundLongitude();will(returnValue(bboxEast));
    		oneOf(mockBbox).getWestBoundLongitude();will(returnValue(bboxWest));
    		oneOf(mockBbox).getNorthBoundLatitude();will(returnValue(bboxNorth));
    		oneOf(mockBbox).getSouthBoundLatitude();will(returnValue(bboxSouth));
    		
    		oneOf(mockOnlineRes).getDescription();will(returnValue(orDesc));
    		oneOf(mockOnlineRes).getName();will(returnValue(orName));
    		oneOf(mockOnlineRes).getType();will(returnValue(orType));
    		oneOf(mockOnlineRes).getLinkage();will(returnValue(orUrl));
        }});
    	
    	ModelMap result = factory.toView(mockCSWRecord);
    	
    	TestViewUtility.assertModelMapsEqual(expectation,result);
    }
}