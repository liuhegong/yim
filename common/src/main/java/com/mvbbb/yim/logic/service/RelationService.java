package com.mvbbb.yim.logic.service;

import com.mvbbb.yim.common.entity.Group;
import com.mvbbb.yim.common.entity.User;
import com.mvbbb.yim.common.vo.GroupVO;

import java.util.List;

public interface RelationService {

    List<User> listFriend(String userId);

    void addFriend(String userId,String friendId);

    void deleteFriend(String userId,String friendId);

    List<Group> listGroup(String userId);

    GroupVO joinGroup(String userId, String groupId);

    GroupVO createGroup(String userId,String groupName,List<String> members);

    void quitGroup(String userId,String groupId);

    GroupVO kickoutGroupMember(String groupId,String userId);

    GroupVO addGroupMember(String groupId,String userId);

    GroupVO getGroupInfo(String groupId);

    void dissolutionGroup(String groupId);

    List<Group> listAllGroup();


}
