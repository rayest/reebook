package cn.reebook.foundation.common;

import cn.reebook.foundation.hateoas.TLink;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * Created by xubt on 5/26/16.
 */
public class RestResource extends ResourceSupport {
    protected Object domainObject;
    protected JSONArray resourcesJSON;

    public JSONObject getResource() {
        JSONObject resourceJSON = new JSONObject();
        JSONObject links = new JSONObject();
        for (Link link : super.getLinks()) {
            links.put(link.getRel(), ((TLink) link).toJSON());
        }
        JSONObject domainJSON = JSONObject.parseObject(JSONObject.toJSONString(domainObject));
        if (domainJSON != null) {
            resourceJSON.putAll(domainJSON);
        }
        resourceJSON.put("_links", links);
        return resourceJSON;
    }

    @Override
    public String toString() {
        if (resourcesJSON != null) {
            return resourcesJSON.toJSONString();
        }
        return getResource().toString();
    }

    public void buildDataObject(String key, Object value) {
        JSONArray domainResources;
        Object domainResourcesValue = value;
        if (domainResourcesValue instanceof List) {
            domainResources = new JSONArray();
            for (int i = 0; i < ((List) domainResourcesValue).size(); i++) {
                domainResources.add(((List) domainResourcesValue).get(i));
            }
            domainResourcesValue = domainResources;
        }
        JSONObject dataObject = new JSONObject();
        dataObject.put(key, domainResourcesValue);
        this.domainObject = dataObject;
    }
}
