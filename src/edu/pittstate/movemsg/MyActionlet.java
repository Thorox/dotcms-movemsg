package edu.pittstate.movemsg;

import java.util.ArrayList;
import java.util.HashMap;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.util.UtilMethods;
import com.dotmarketing.util.InodeUtils;
import com.dotmarketing.portlets.workflows.actionlet.WorkFlowActionlet;
import com.dotmarketing.portlets.workflows.model.WorkflowActionClassParameter;
import com.dotmarketing.portlets.workflows.model.WorkflowActionFailureException;
import com.dotmarketing.portlets.workflows.model.WorkflowActionletParameter;
import com.dotmarketing.portlets.workflows.model.WorkflowProcessor;
import com.dotmarketing.portlets.categories.model.Category;
import com.dotmarketing.portlets.categories.business.CategoryAPI;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.contentlet.business.ContentletAPI;
import com.dotmarketing.portlets.structure.model.Field;
import com.dotmarketing.portlets.structure.model.Field.FieldType;
import com.dotmarketing.portlets.structure.model.Structure;
import com.dotmarketing.cache.FieldsCache;
import com.dotmarketing.cache.StructureCache;
import com.dotmarketing.util.Logger;
import java.util.List;
import java.util.Map;
import com.dotmarketing.business.UserAPI;

public class MyActionlet extends WorkFlowActionlet {

    private static final long serialVersionUID = 1L;

    @Override
    public List<WorkflowActionletParameter> getParameters () {
        return null;
    }

    @Override
    public String getName () {
        return "Move MyCampus Message";
    }

    @Override
    public String getHowTo () {
        return "For Use By CMS Administrators Only";
    }

    @Override
    public void executeAction ( WorkflowProcessor processor, Map<String, WorkflowActionClassParameter> params ) throws WorkflowActionFailureException {
        boolean clean = false;
        boolean live  = false;

	try {
		Structure s = StructureCache.getStructureByVelocityVarName("MyCampusInfo");
		if (s == null || s.getName() == null) {
			Logger.error(this,"Could Not Find MyCampusInfo");
		}
		Contentlet old = processor.getContentlet();
		Contentlet c = new Contentlet();
		c.setStructureInode(s.getInode());
		ContentletAPI conAPI = APILocator.getContentletAPI();
		CategoryAPI catAPI = APILocator.getCategoryAPI();
		UserAPI userAPI = APILocator.getUserAPI();
	
		for (Field field : FieldsCache.getFieldsByStructureInode(s.getInode())) {
			if (field.getVelocityVarName().equals("title")) {
				conAPI.setContentletProperty(c, field, old.getStringProperty("messageTitle"));
			}
			if (field.getVelocityVarName().equals("message")) {
				conAPI.setContentletProperty(c, field, old.getStringProperty("message"));
			}
                        if (field.getVelocityVarName().equals("messageType")) {
                                conAPI.setContentletProperty(c, field, old.getStringProperty("messageType"));
                        }
			if (field.getVelocityVarName().equals("messageSubType")) {
				conAPI.setContentletProperty(c, field, old.getStringProperty("messageSubType"));
			}
                        if (field.getVelocityVarName().equals("audience")) {
                                conAPI.setContentletProperty(c, field, old.getStringProperty("audience"));
                        }
                        if (field.getVelocityVarName().equals("publish")) {   
                                conAPI.setContentletProperty(c, field, old.getDateProperty("publish"));
                        }
                        if (field.getVelocityVarName().equals("expire")) {
                                conAPI.setContentletProperty(c, field, old.getDateProperty("expire"));
                        }
		}
		conAPI.checkin(c, userAPI.getSystemUser(), true, null);
	} catch (Exception e) {
		throw new WorkflowActionFailureException(e.getMessage());
	}
}
}
 
