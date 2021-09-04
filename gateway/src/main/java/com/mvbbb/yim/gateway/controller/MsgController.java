package com.mvbbb.yim.gateway.controller;

import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.http.request.GenericRequest;
import com.mvbbb.yim.common.protoc.http.request.HistoryRequest;
import com.mvbbb.yim.common.protoc.http.response.GenericResponse;
import com.mvbbb.yim.common.protoc.http.response.PullOfflineMsgResponse;
import com.mvbbb.yim.logic.service.MsgService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MsgController {

    @DubboReference(check = false)
    MsgService msgService;

    @RequestMapping(path = "/message/history",method = RequestMethod.GET)
    public GenericResponse<List<MsgData>> getHistoryMsg(@RequestBody GenericRequest<HistoryRequest> request){
        HistoryRequest historyRequest = request.getData();
        List<MsgData> historyMsg = msgService.getHistoryMsg(request.getUserId(),
                historyRequest.getSessionId(),
                historyRequest.getSessionType(),
                historyRequest.getFrom(),
                historyRequest.getLimit());
        return GenericResponse.success(historyMsg);
    }

    @RequestMapping(path = "/message/offline", method = RequestMethod.GET)
    public GenericResponse<PullOfflineMsgResponse> getOfflineMsg(@RequestBody GenericRequest<Object> request){
        String userId = request.getUserId();
        PullOfflineMsgResponse offlineMsg = msgService.getOfflineMsg(userId);
        return GenericResponse.success(offlineMsg);
    }

//    @RequestMapping(path = "/message/recent",method = RequestMethod.GET)
//    public GenericResponse<> getRecentMsg(GenericRequest<Object> request){
//        String userId = request.getUserId();
//
//    }

}