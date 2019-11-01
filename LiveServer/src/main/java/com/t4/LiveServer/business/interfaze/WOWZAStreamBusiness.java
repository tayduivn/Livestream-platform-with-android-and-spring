package com.t4.LiveServer.business.interfaze;

import com.t4.LiveServer.model.wowza.OutputStreamTarget;
import com.t4.LiveServer.model.wowza.StreamOutput;
import com.t4.LiveServer.model.wowza.StreamTarget;

import java.util.List;

public interface WOWZAStreamBusiness {
    String create();
    String fetchAll();
    String fetchOne(String id);
    String update();
    String delete(String id);
    String start(String id);
    String stop(String id);
    String reset(String id);
    String regenerate(String id);
    String fetchThumbnail(String id);
    String fetchState(String id);
    String fetchMetrics(String id);
    String fetchVersions();
    StreamTarget createCustomStreamTarget(StreamTarget entry);
    String fetchAllCustomStreamTargets();
    String fetchCustomStreamTarget(String id);
    String updateCustomStreamTarget(String id);
    String deleteCustomStreamTarget(String id);
    String regenerateCodeForAnyStreamTarget(String id);

    String getDefaultTransCoder(String id);
    List<StreamOutput> fetchAllOutputOfATransCoder(String id);
}