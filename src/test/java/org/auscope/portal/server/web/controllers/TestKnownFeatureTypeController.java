package org.auscope.portal.server.web.controllers;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.auscope.portal.csw.CSWRecord;
import org.auscope.portal.server.web.KnownFeatureTypeDefinition;
import org.auscope.portal.server.web.view.ViewKnownFeatureTypeDefinitionFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

public class TestKnownFeatureTypeController {
	private Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
    
    private KnownFeatureTypeDefinition mockDefn1 = context.mock(KnownFeatureTypeDefinition.class, "defn1");
    private KnownFeatureTypeDefinition mockDefn2 = context.mock(KnownFeatureTypeDefinition.class, "defn2");
	private ViewKnownFeatureTypeDefinitionFactory mockViewFactory = context.mock(ViewKnownFeatureTypeDefinitionFactory.class);
	private HttpServletRequest mockHttpRequest = context.mock(HttpServletRequest.class);
    private HttpServletResponse mockHttpResponse = context.mock(HttpServletResponse.class);
	
    @Before
    public void setup() throws Exception {
    	
    }
    
    @Test
    public void testGet_Success() throws Exception {
    	KnownFeatureTypeController kftController = new KnownFeatureTypeController(Arrays.asList(mockDefn1, mockDefn2), mockViewFactory);
    	final StringWriter actualJSONResponse = new StringWriter();
    	final ModelMap record1 = new ModelMap();
    	final ModelMap record2 = new ModelMap();
    	
    	record1.put("rec1", "val1");
    	record2.put("rec2", "val2");
    	
    	context.checking(new Expectations() {{
    		oneOf(mockViewFactory).toView(mockDefn1);will(returnValue(record1));
    		oneOf(mockViewFactory).toView(mockDefn2);will(returnValue(record2));
    		
    		//check that the correct response is getting output
            oneOf (mockHttpResponse).setContentType(with(any(String.class)));
            oneOf (mockHttpResponse).getWriter(); will(returnValue(new PrintWriter(actualJSONResponse)));
        }});
    	
    	//Run the method, get our response rendered as a JSONObject
    	ModelAndView mav = kftController.getKnownFeatureTypes();
    	mav.getView().render(mav.getModel(), mockHttpRequest, mockHttpResponse);
    	JSONObject jsonObj = JSONObject.fromObject(actualJSONResponse.toString());
    	
    	Assert.assertEquals(true, jsonObj.getBoolean("success"));
    	JSONArray records = jsonObj.getJSONArray("records");
    	Assert.assertNotNull(records);
    	Assert.assertEquals(2, records.size());
    	
    	JSONObject jsonRec1 = records.getJSONObject(0);
    	JSONObject jsonRec2 = records.getJSONObject(1);
    	
    	Assert.assertEquals("val1", jsonRec1.get("rec1"));
    	Assert.assertEquals("val2", jsonRec2.get("rec2"));
    }
}