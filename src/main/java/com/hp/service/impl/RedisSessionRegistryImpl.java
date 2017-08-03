package com.hp.service.impl;

import com.hp.service.RedisService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.util.Assert;
import org.springframework.util.SerializationUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by qw on 2017/8/3.
 */
public class RedisSessionRegistryImpl  implements SessionRegistry,
        ApplicationListener<SessionDestroyedEvent> {

    @Autowired
    private RedisService redisService;

    // ~ Instance fields
    // ================================================================================================

    protected final Log logger = LogFactory.getLog(RedisSessionRegistryImpl.class);

    /** <principal:Object,SessionIdSet> */
    /**
     * key:UserDetails实现类
     * value:sessionId的集合
     */
    private final ConcurrentMap<Object, Set<String>> principals = new ConcurrentHashMap<>();

    /** <sessionId:Object,SessionInformation> */
    /**
     * key:sessionId
     * value:SessionInformation
     *      principal:UserDetails实现类
     *      sessionId:
     *      lastRequest:最后登录时间
     *      expired:是否过期
     */
    private final Map<String, SessionInformation> sessionIds = new ConcurrentHashMap<>();

    private byte[] sessionId_key = "session_id_map".getBytes();
    private byte[] principal_key = "principal_map".getBytes();

    // ~ Methods
    // ========================================================================================================

    //获取所有的用户
    public List<Object> getAllPrincipals() {
        return new ArrayList<>(principals.keySet());
    }

    /**
     * 获取所有的session
     * @param principal
     * @param includeExpiredSessions
     * @return
     */
    public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
        //final Set<String> sessionsUsedByPrincipal = principals.get(principal);
        byte[] bytes = redisService.hgetAll(sessionId_key).get(SerializationUtils.serialize(principal));
        final Set<String> sessionsUsedByPrincipal = (Set<String>) SerializationUtils.deserialize(bytes);

        if (sessionsUsedByPrincipal == null) {
            return Collections.emptyList();
        }

        List<SessionInformation> list = new ArrayList<>(
                sessionsUsedByPrincipal.size());

        for (String sessionId : sessionsUsedByPrincipal) {
            SessionInformation sessionInformation = getSessionInformation(sessionId);

            if (sessionInformation == null) {
                continue;
            }

            if (includeExpiredSessions || !sessionInformation.isExpired()) {
                list.add(sessionInformation);
            }
        }

        return list;
    }

    /**
     * 根据sessionId获取用户信息
     * @param sessionId
     * @return
     */
    public SessionInformation getSessionInformation(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        List<byte[]> hmget = redisService.hmget(sessionId_key, sessionId.getBytes());
        Object deserialize = SerializationUtils.deserialize(hmget.get(0));
        if (deserialize instanceof  SessionInformation) {
            return (SessionInformation) deserialize;
        } else {
            return null;
        }
        // return sessionIds.get(sessionId);
    }

    public void onApplicationEvent(SessionDestroyedEvent event) {
        String sessionId = event.getId();
        removeSessionInformation(sessionId);
    }

    public void refreshLastRequest(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");

        SessionInformation info = getSessionInformation(sessionId);

        if (info != null) {
            info.refreshLastRequest();
        }
    }

    /**
     * 注册一个新session
     * @param sessionId
     * @param principal
     */
    public void registerNewSession(String sessionId, Object principal) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");
        Assert.notNull(principal, "Principal required as per interface contract");

        if (logger.isDebugEnabled()) {
            logger.debug("Registering session " + sessionId + ", for principal "
                    + principal);
        }

        if (getSessionInformation(sessionId) != null) {
            removeSessionInformation(sessionId);
        }

        //sessionIds.put(sessionId, new SessionInformation(principal, sessionId, new Date()));
        redisService.hset(sessionId_key, sessionId.getBytes(), SerializationUtils.serialize(new SessionInformation(principal, sessionId, new Date())));

        //Set<String> sessionsUsedByPrincipal = principals.get(principal);
        byte[] bytes = redisService.hgetAll(sessionId_key).get(SerializationUtils.serialize(principal));
        Set<String> sessionsUsedByPrincipal = (Set<String>) SerializationUtils.deserialize(bytes);


        if (sessionsUsedByPrincipal == null) {
            sessionsUsedByPrincipal = new CopyOnWriteArraySet<>();
            //Set<String> prevSessionsUsedByPrincipal = principals.putIfAbsent(principal, sessionsUsedByPrincipal);
            //redisService.hset(principal_key, SerializationUtils.serialize(principal), SerializationUtils.serialize(sessionsUsedByPrincipal));
//            if (prevSessionsUsedByPrincipal != null) {
//                sessionsUsedByPrincipal = prevSessionsUsedByPrincipal;
//            }
        }

        sessionsUsedByPrincipal.add(sessionId);
        redisService.hset(principal_key, SerializationUtils.serialize(principal), SerializationUtils.serialize(sessionsUsedByPrincipal));

        if (logger.isTraceEnabled()) {
            logger.trace("Sessions used by '" + principal + "' : "
                    + sessionsUsedByPrincipal);
        }
    }


    public void removeSessionInformation(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");

        SessionInformation info = getSessionInformation(sessionId);

        if (info == null) {
            return;
        }

        if (logger.isTraceEnabled()) {
            logger.debug("Removing session " + sessionId
                    + " from set of registered sessions");
        }

        //sessionIds.remove(sessionId);
        redisService.hdel(sessionId_key, sessionId.getBytes());

        //Set<String> sessionsUsedByPrincipal = principals.get(info.getPrincipal());
        byte[] bytes = redisService.hgetAll(sessionId_key).get(SerializationUtils.serialize(info.getPrincipal()));
        Set<String> sessionsUsedByPrincipal = (Set<String>) SerializationUtils.deserialize(bytes);


        if (sessionsUsedByPrincipal == null) {
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Removing session " + sessionId
                    + " from principal's set of registered sessions");
        }

        sessionsUsedByPrincipal.remove(sessionId);

        if (sessionsUsedByPrincipal.isEmpty()) {
            // No need to keep object in principals Map anymore
            if (logger.isDebugEnabled()) {
                logger.debug("Removing principal " + info.getPrincipal()
                        + " from registry");
            }
//            principals.remove(info.getPrincipal());
            redisService.hdel(principal_key, SerializationUtils.serialize(info.getPrincipal()));
        }

        if (logger.isTraceEnabled()) {
            logger.trace("Sessions used by '" + info.getPrincipal() + "' : "
                    + sessionsUsedByPrincipal);
        }
    }

}

