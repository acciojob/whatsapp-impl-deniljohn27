package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////




    //1
    public String createUser(String name, String mobile) throws Exception {
        //If the mobile number exists in database, throw "User already exists" exception
        //Otherwise, create the user and return "SUCCESS"


        if(!userMobile.contains(mobile)){
            User user=new User(name,mobile);
            userMobile.add(mobile);
            return "SUCCESS";
        }
        return "User already exists";
    }
    //2

    public Group createGroup(List<User> users){
        // The list contains at least 2 users where the first user is the admin. A group has exactly one admin.
        // If there are only 2 users, the group is a personal chat and the group name should be kept as the name of the second user(other than admin)
        // If there are 2+ users, the name of group should be "Group count". For example, the name of first group would be "Group 1", second would be "Group 2" and so on.
        // Note that a personal chat is not considered a group and the count is not updated for personal chats.
        // If group is successfully created, return group.

        //For example: Consider userList1 = {Alex, Bob, Charlie}, userList2 = {Dan, Evan}, userList3 = {Felix, Graham, Hugh}.
        //If createGroup is called for these userLists in the same order, their group names would be "Group 1", "Evan", and "Group 2" respectively.

        if(users.size()==2){
            Group group=new Group(users.get(1).getName(),users.size());
            adminMap.put(group,users.get(0));
            groupUserMap.put(group,users);
            return group;
        }
        customGroupCount+=1;
        Group group=new Group("Group "+customGroupCount,users.size());
        adminMap.put(group,users.get(0));
        groupUserMap.put(group,users);
        return group;
    }
    //3

    public int createMessage(String content){
        // The 'i^th' created message has message id 'i'.
        // Return the message id.
        messageId+=1;

        Message message=new Message(messageId,content);
        return messageId;
    }
    //4

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "You are not allowed to send message" if the sender is not a member of the group
        //If the message is sent successfully, return the final number of messages in that group.

        if(!adminMap.containsKey(group)){
            System.out.println("Group does not exist");
            return 0;
        }
        else if(!groupUserMap.get(group).contains(sender)){
            System.out.println("You are not allowed to send message");
            return 0;
        }
        senderMap.put(message,sender);
        if(groupMessageMap.containsKey(group)){
            List<Message> temp=groupMessageMap.get(group);
            temp.add(message);
            groupMessageMap.put(group,temp);
        }else{
            List<Message> temp=new ArrayList<>();
            temp.add(message);
            groupMessageMap.put(group,temp);
        }
        return groupMessageMap.get(group).size();
    }
    //5

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        //Throw "User is not a participant" if the user is not a part of the group
        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.
        if(!adminMap.containsKey(group)){
            return "Group does not exist";
        }
        else if(!adminMap.get(group).equals(approver)){
            return "Approver does not have rights";
        }
        else if(!groupUserMap.get(group).contains(user)){
            return "User is not a participant";
        }

        adminMap.put(group,user);

        return"SUCCESS" ;
    }
    //6

//    public int removeUser(User user) throws Exception{
//        //This is a bonus problem and does not contains any marks
//        //A user belongs to exactly one group
//        //If user is not found in any group, throw "User not found" exception
//        //If user is found in a group and it is the admin, throw "Cannot remove admin" exception
//        //If user is not the admin, remove the user from the group, remove all its messages from all the databases, and update relevant attributes accordingly.
//        //If user is removed successfully, return (the updated number of users in the group + the updated number of messages in group + the updated number of overall messages)
//
//        return ;
//    }
    //7

//    public String findMessage(Date start, Date end, int K) throws Exception{
//        //This is a bonus problem and does not contains any marks
//        // Find the Kth latest message between start and end (excluding start and end)
//        // If the number of messages between given time is less than K, throw "K is greater than the number of messages" exception
//
//        return ;
//    }




}
